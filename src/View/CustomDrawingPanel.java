package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//Find a way to replace points
//GUIview must place the boxclasses not the

public class CustomDrawingPanel extends JPanel {
    private static class Relationship {
        Point p1, p2;
        String type;
        public Relationship(Point p1, Point p2, String type) {
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Relationship rel : relationships) {
            g.setColor(Color.BLACK);
            g.drawLine(rel.p1.x, rel.p1.y, rel.p2.x, rel.p2.y);
            g.drawString(rel.type, (rel.p1.x + rel.p2.x) / 2, (rel.p1.y + rel.p2.y) / 2);
        }
    }
}