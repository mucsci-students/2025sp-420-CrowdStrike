package org.View.GUICmp;

import javax.swing.JPanel;

import java.awt.*;

import java.awt.geom.AffineTransform;
import java.awt.FontMetrics;
import java.beans.PropertyChangeListener;

import javax.swing.text.AsyncBoxView;

import org.Model.ClassObject;
import org.Model.Relationship;
import org.View.GUICmp.UMLClass;


public class RelationshipArrow extends JPanel implements PropertyChangeListener {

    private Relationship rel;

    public RelationshipArrow(Relationship relationship){
        super();
        this.rel = new Relationship(relationship);
	relationship.addPropertyChangeListener(this);
        setVisible(true);
	setBackground(new Color(0, 0, 0, 0));
	setSize(getPreferredSize());
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize(){
	int x,y;
	ClassObject src,dst;
	src = rel.getSource();
	dst = rel.getDestination();
	x = Math.max(src.getPosition().x,dst.getPosition().x);
	y = Math.max(src.getPosition().y,dst.getPosition().y);
	return new Dimension(x,y);
    }

    private UMLClass findUMLClass(ClassObject cls){
        JPanel parent = (JPanel) getParent();
        for (Component child : parent.getComponents()) {
            if (child.getClass() == UMLClass.class) {
                UMLClass uc = (UMLClass) child;
                if(uc.representsClassObject(cls)){
                    return uc;
                }
            }
        }
        return null;
    }

    /**
     * Computes the closest edge point of the classbox
     *
     * @param cls the ClassObject in question
     * @param target the point toward which the line is drawn (the other box)
     * @return the point on the box's boundary
     */
    private Point getClosestHit(ClassObject cls, Point target) {
        Point center = cls.getPosition();
        double dx = target.x - center.x;
        double dy = target.y - center.y;
	double halfWidth = 100 / 2.0;
	double halfHeight = 100 / 2.0;

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
        setSize(getPreferredSize());
        super.paintComponent(g);
        FontMetrics metrics = g.getFontMetrics();
        // Set color based on relationship type
        if (rel.getTypeString().equals("Aggregation")) {
            g.setColor(Color.RED);
        } else if (rel.getTypeString().equals("Composition")) {
            g.setColor(new Color(0, 128, 0));
        } else if (rel.getTypeString().equals("Inheritance")) {
            g.setColor(Color.BLUE);
        } else if (rel.getTypeString().equals("Realization")) {
            g.setColor(new Color(255, 140, 0));
        } else {
            g.setColor(Color.BLACK);
        }
        if(rel.getSource() != rel.getDestination()){
            // Calculate the intersection points at the boundaries of the ClassBoxes
            Point sourceCenter = rel.getSource().getPosition();
            Point destinationCenter = rel.getDestination().getPosition();
            Point p1 = getClosestHit(rel.getSource(), destinationCenter);
            Point p2 = getClosestHit(rel.getDestination(), sourceCenter);

            // Draw the main line between the edges of the boxes
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
            arrowhead((Graphics2D)g,p1,p2);

            Point a = new Point(p2.y - p1.y, p2.x - p1.x);

            // Draw the relationship type label near the midpoint of the line
            int midX = (p1.x + p2.x) / 2;
            int midY = (p1.y + p2.y) / 2;
            g.drawString(rel.getTypeString(),midX-(metrics.stringWidth(rel.getTypeString())/2),midY);
        } 
        else {
            Point center = rel.getSource().getPosition();
            
//int w = rel.getSource().getWidth();
int w = 5;
            center.y -= 12;// nuge to change closest hit to top
            Point top = getClosestHit(rel.getSource(),center);

            g.drawString(rel.getTypeString(),top.x-(metrics.stringWidth(rel.getTypeString())/2),top.y);
            g.drawArc(top.x-(w/2),top.y-50,w-4,150,0,180);

            top.x = top.x+(w/2)-10;
            top.y -= 10;
            arrowhead((Graphics2D)g,top,new Point(top.x+2,top.y+5));
        }
        repaint();
    }

    @Override
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
        
		revalidate();
		repaint();

		switch (evt.getPropertyName()) {
			case "DeleteRelationship":
				break;
			case "EditRelationshipType":
				break;
			case "EditSource":
				break;
			case "EditDestination":
				break;
		}
		revalidate();
		setSize(getPreferredSize());
		repaint();
	}
    
}
