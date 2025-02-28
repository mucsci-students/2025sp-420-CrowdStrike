package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomDrawingPanel extends JPanel {
    private static class Relationship {
        Point p1, p2;
        String type;

        Relationship(Point p1, Point p2, String type) {
            this.p1 = p1;
            this.p2 = p2;
            this.type = type;
        }
    }

    private List<Relationship> relationships = new ArrayList<>();

    public void addRelationship(Point p1, Point p2, String type) {
        relationships.add(new Relationship(p1, p2, type));
        repaint();
    }

    public void removeRelationship(Point p1, Point p2) {
        relationships.removeIf(rel -> rel.p1.equals(p1) && rel.p2.equals(p2));
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Relationship rel : relationships) {
            g.setColor(Color.BLACK);
            g.drawLine(rel.p1.x, rel.p1.y, rel.p2.x, rel.p2.y);

            // Draw Relationship Type Label
            int midX = (rel.p1.x + rel.p2.x) / 2;
            int midY = (rel.p1.y + rel.p2.y) / 2;
            g.drawString(rel.type, midX + 5, midY - 5);
        }
    }
}