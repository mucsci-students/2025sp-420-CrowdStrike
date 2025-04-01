package org.View;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

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
            
            // Calculate the intersection points at the boundaries of the ClassBoxes
            Point sourceCenter = rel.source.getCenter();
            Point destinationCenter = rel.destination.getCenter();
            Point p1 = getClosestHit(rel.source, destinationCenter);
            Point p2 = getClosestHit(rel.destination, sourceCenter);

            // Draw the main line between the edges of the boxes
            g.drawLine(p1.x, p1.y, p2.x, p2.y);

            // Calculate the angle for the arrow head
            double angle = Math.atan2(p2.y - p1.y, p2.x - p1.x);
            int arrowHeadLength = 10;
            double arrowHeadAngle = Math.toRadians(30);

            // Compute coordinates for the arrow head at the destination edge
            int xArrow1 = (int) (p2.x - arrowHeadLength * Math.cos(angle - arrowHeadAngle));
            int yArrow1 = (int) (p2.y - arrowHeadLength * Math.sin(angle - arrowHeadAngle));
            int xArrow2 = (int) (p2.x - arrowHeadLength * Math.cos(angle + arrowHeadAngle));
            int yArrow2 = (int) (p2.y - arrowHeadLength * Math.sin(angle + arrowHeadAngle));

            // Draw the arrow head lines
            g.drawLine(p2.x, p2.y, xArrow1, yArrow1);
            g.drawLine(p2.x, p2.y, xArrow2, yArrow2);

            // Draw the relationship type label near the midpoint of the line
            int midX = (p1.x + p2.x) / 2;
            int midY = (p1.y + p2.y) / 2;
            g.drawString(rel.type, midX + 5, midY - 5);
        }
    }
}
