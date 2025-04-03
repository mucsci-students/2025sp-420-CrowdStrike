package org.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.AffineTransform;

public class CustomDrawingPanel extends JPanel {
    // Relationship now stores references to ClassBox objects
    private static class Relationship {
        private ClassBox source;
        private ClassBox destination;
        private String type;

        Relationship(ClassBox source, ClassBox destination, String type) {
            this.source = source;
            this.destination = destination;
            this.type = type;
        }
    }

    // List of relationships
    private List<Relationship> relationships = new ArrayList<>();

    // Adds a relationship using ClassBox references
    public void addRelationship(ClassBox source, ClassBox destination, String type) {
        relationships.add(new Relationship(source, destination, type));
        repaint();
    }

    // Removes a relationship using ClassBox references
    public void removeRelationship(ClassBox source, ClassBox destination) {
        relationships.removeIf(rel -> rel.source.equals(source) && rel.destination.equals(destination));
        repaint();
    }

    /**
     * Computes the closest edge point of the classbox
     *
     * @param box the ClassBox in question
     * @param target the point toward which the line is drawn (the other box)
     * @return the point on the box's boundary
     */
    private Point getClosestHit(ClassBox box, Point target) {
        Point center = box.getCenter();
        double dx = target.x - center.x;
        double dy = target.y - center.y;
        double halfWidth = box.getWidth() / 2.0;
        double halfHeight = box.getHeight() / 2.0;

        if (dx == 0 && dy == 0) {
            return center; // if both centers coincide, return center
        }

        // Determine the scaling factor to reach the boundary
        double scaleX = dx != 0 ? halfWidth / Math.abs(dx) : Double.POSITIVE_INFINITY;
        double scaleY = dy != 0 ? halfHeight / Math.abs(dy) : Double.POSITIVE_INFINITY;
        double scale = Math.min(scaleX, scaleY);

        int newX = (int) (center.x + dx * scale);
        int newY = (int) (center.y + dy * scale);
        return new Point(newX, newY);
    }

    private void arrowhead(Graphics2D g,Point p1, Point p2){
	int size = 10;
	Polygon arrowHead = new Polygon ();
	arrowHead.addPoint (size, 0);
        arrowHead.addPoint (-size, -size);
        arrowHead.addPoint (-size, size);

        AffineTransform tx = AffineTransform.getTranslateInstance (p2.x, p2.y);
        tx.rotate (Math.atan2 (p2.y - p1.y, p2.x - p1.x));

        g.fill(tx.createTransformedShape (arrowHead));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
	FontMetrics metrics = g.getFontMetrics();

        for (Relationship rel : relationships) {
            // Set color based on relationship type
            if (rel.type.equals("Aggregation")) {
                g.setColor(Color.RED);
            } else if (rel.type.equals("Composition")) {
                g.setColor(Color.GREEN);
            } else if (rel.type.equals("Inheritance")) {
                g.setColor(Color.BLUE);
            } else if (rel.type.equals("Realization")) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.BLACK);
            }

	    if(rel.source != rel.destination){
		// Calculate the intersection points at the boundaries of the ClassBoxes
		Point sourceCenter = rel.source.getCenter();
		Point destinationCenter = rel.destination.getCenter();
		Point p1 = getClosestHit(rel.source, destinationCenter);
		Point p2 = getClosestHit(rel.destination, sourceCenter);

		// Draw the main line between the edges of the boxes
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
		arrowhead((Graphics2D)g,p1,p2);

		Point a = new Point(p2.y - p1.y, p2.x - p1.x);

		// Draw the relationship type label near the midpoint of the line
		int midX = (p1.x + p2.x) / 2;
		int midY = (p1.y + p2.y) / 2;
		g.drawString(rel.type,midX-(metrics.stringWidth(rel.type)/2),midY);
	    } else {
		Point center = rel.source.getCenter();
		int w = rel.source.getWidth();
		center.y -= 12;// nuge to change closest hit to top
		Point top = getClosestHit(rel.source,center);

		g.drawString(rel.type,top.x-(metrics.stringWidth(rel.type)/2),top.y);
		g.drawArc(top.x-(w/2),top.y-50,w-4,150,0,180);

		top.x = top.x+(w/2)-10;
		top.y -= 10;
		arrowhead((Graphics2D)g,top,new Point(top.x+2,top.y+5));
	    }
        }
    }
}
