package org.View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.QuadCurve2D;


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

    // Removes every relationship taking no parameters
    public void removeAllRelationships(){
        relationships.clear();
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

    /**
     * Draws the arrowhead for a line between two points
     *
     * @param g the graphics context
     * @param p1 the starting point of the line segment
     * @param p2 the end point at which the arrowhead is drawn
     */
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
                g.setColor(new Color(0, 128, 0));
            } else if (rel.type.equals("Inheritance")) {
                g.setColor(Color.BLUE);
            } else if (rel.type.equals("Realization")) {
                g.setColor(new Color(255, 140, 0));
            } else {
                g.setColor(Color.BLACK);
            }

        if(rel.source != rel.destination){
            // Compute the edge intersection points for source and destination
            Point sourceCenter = rel.source.getCenter();
            Point destinationCenter = rel.destination.getCenter();
            Point p1 = getClosestHit(rel.source, destinationCenter);
            Point p2 = getClosestHit(rel.destination, sourceCenter);
            //if a box that isn't source or destination contains p1 or p2, then that variable should be shifted along its hitbox
            

            Graphics2D g2 = (Graphics2D) g; // used to draw curves instead of lines

            double collisionBend = 0;
            //check if reverse exists from b to a
            if(getReverseRelationship(rel)!=null){
                collisionBend+=100;
            }

            //look for any classes in the way
            
            for (Component comp : getComponents()) {
                if (comp instanceof ClassBox){
                    ClassBox box = (ClassBox) comp;
                    if (!box.equals(rel.source) && !box.equals(rel.destination)){


                        double baseOffset = box.getHeight();
                        // Determine reverse order so that each of a reverse pair goes to opposite sides
                        int revOrder = 1;
                        if (getReverseRelationship(rel) != null && relationships.indexOf(rel) > relationships.indexOf(getReverseRelationship(rel))) {
                            revOrder = -1;
                        }
                        if (lineIntersectsBox(p1, p2, box)) {
                            Point center = box.getCenter();
                            double cross = (p2.x - p1.x)*(center.y - p1.y) - (p2.y - p1.y)*(center.x - p1.x);
                            double side = (cross >= 0 ? -1 : 1);

                            collisionBend = side * baseOffset * revOrder;

                            Point bent = getBendPoint(p1, p2, collisionBend);
                            QuadCurve2D curve = new QuadCurve2D.Double(p1.x, p1.y, bent.x, bent.y, p2.x, p2.y);

                            if (curveIntersectsBox(curve, box)) {
                                // Try the other concavity
                                collisionBend = -collisionBend;
                                bent = getBendPoint(p1, p2, collisionBend);
                                curve.setCurve(p1.x, p1.y, bent.x, bent.y, p2.x, p2.y);

                                if (curveIntersectsBox(curve, box)) {
                                    // Both concavities collide so we widen the offset
                                    collisionBend = side * baseOffset * 1.5;
                                    collisionBend *= revOrder;
                                }
                            }
                        }
                    }
                }
            }

            // If collisionBend is non-zero, use a quadratic curve with the detour control point, otherwise use a straight line
            if (collisionBend != 0) {
                Point bent = getBendPoint(p1, p2, collisionBend);
                //QuadCurve2D curve = new QuadCurve2D.Double(p1.x+5, p1.y+5, bent.x, bent.y, p2.x-5, p2.y-5);
                QuadCurve2D curve = new QuadCurve2D.Double(p1.x, p1.y, bent.x, bent.y, p2.x, p2.y);
                
                g2.draw(curve);
                arrowhead(g2, bent, p2);
                int midX = (p1.x + p2.x) / 2;
                double midY = (curve.getCtrlY()+p1.y+p2.y)/3;
                g.drawString(rel.type, midX - (metrics.stringWidth(rel.type) / 2), (int)
                (midY-10));
            } 
            else {
                // Draw a straight line when no collisions are detected
                g.drawLine(p1.x, p1.y, p2.x, p2.y);
                arrowhead(g2, p1, p2);
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g.drawString(rel.type, midX - (metrics.stringWidth(rel.type) / 2), midY - 10);
            }
            
	    } 
        else {
        // Drawing self relationships
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

    /**
     * Determines if a reverse relationship (i.e. destination-to-source) exists for the given relationship.
     *
     * @param rel the relationship to test
     * @return the reverse relationship if found; null otherwise.
     */
    private Relationship getReverseRelationship(Relationship rel) {
        for (Relationship other : relationships) {
            if (other.source.equals(rel.destination) && other.destination.equals(rel.source)) {
                return other;
            }
        }
        return null;
    }

    /**
     * Returns a rectangle representing the bounds of a ClassBox.
     * It assumes the origin (x, y) is at the top-left corner of the ClassBox.
     *
     * @param box the ClassBox
     * @return the bounding Rectangle of the box
     */
    private Rectangle getBoxBounds(ClassBox box) {
        Point center = box.getCenter();
        int halfWidth = box.getWidth() / 2;
        int halfHeight = box.getHeight() / 2;
        return new Rectangle(center.x - halfWidth, center.y - halfHeight, box.getWidth(), box.getHeight());
    }

    /**
     * Returns a rectangle representing the zone around a classbox according to some padding value
     * It assumes the origin (x, y) is at the top-left corner of the ClassBox.
     *
     * @param box the ClassBox
     * @return the bounding Rectangle of the box
     */
    private Rectangle getBoxZone(ClassBox box) {
        Point center = box.getCenter();
        int halfWidth = box.getWidth() / 2;
        int halfHeight = box.getHeight() / 2;
        int padding = 10;
        return new Rectangle(center.x - halfWidth, center.y - halfHeight, box.getWidth() + padding, box.getHeight() + padding);
    }

    /**
     * Checks if a straight line from p1 to p2 collides (intersects) with the given box.
     *
     * @param p1 start point of the line
     * @param p2 end point of the line
     * @param box the ClassBox to test against
     * @return true if the line collides with the box; false otherwise.
     */
    private boolean lineIntersectsBox(Point p1, Point p2, ClassBox box) {
        Rectangle bounds = getBoxBounds(box);
        // Create a line from p1 to p2
        return bounds.intersectsLine(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Checks if a curved line from p1 to p2 collides (intersects) with the given box.
     *
     * @param curve the quadratic curve representing a relationship
     * @param box the ClassBox to test against
     * @return true if the curve collides with the box; false otherwise.
     */
    private boolean curveIntersectsBox(QuadCurve2D curve, ClassBox box) {
        Rectangle bounds = getBoxBounds(box);
        return curve.intersects(bounds);
    }

    /**
     * Computes the position of the new vertex that the arrow will bend to include
     *
     * @param p1 source point
     * @param p2 destination point
     * @param bendingOffset the magnitude of the perpendicular offset to apply
     * @return the computed position
     */
    private Point getBendPoint(Point p1, Point p2, double bendingOffset) {
        // Compute midpoint of the line
        int midX = (p1.x + p2.x) / 2;
        int midY = (p1.y + p2.y) / 2;

        // Compute the vector from p1 to p2
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double length = Math.hypot(dx, dy);
        if (length == 0) {
            return new Point(midX, midY); // degenerate case
        }
        // Compute a perpendicular unit vector (rotate (dx,dy) 90Â°)
        double pdx = -dy / length;
        double pdy = dx / length;

        // Apply the bending offset to the midpoint
        int bentX = (int) (midX + bendingOffset * pdx);
        int bentY = (int) (midY + bendingOffset * pdy);
        return new Point(bentX, bentY);
    }
}
