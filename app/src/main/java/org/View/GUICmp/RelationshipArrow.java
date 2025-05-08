package org.View.GUICmp;

import javax.swing.JPanel;

import java.awt.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.QuadCurve2D;
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
	setOpaque(false);
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
	return new Dimension(x*x,y*y); //may the god of runtime complexity forgive me
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

    private Dimension getClassDimension(ClassObject cls){
        JPanel parent = (JPanel) getParent();
        for (Component child : parent.getComponents()) {
            if (child.getClass() == UMLClass.class) {
                UMLClass uc = (UMLClass) child;
                if(uc.representsClassObject(cls)){
                    return new Dimension(child.getWidth(), child.getHeight());
                }
            }
        }
        return null;
    }

    private Point getClassCenter(ClassObject cls){
        return new Point(cls.getPosition().x + (int)(getClassDimension(cls).getWidth()/2), 
                        cls.getPosition().y + (int)(getClassDimension(cls).getHeight()/2));
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
    

    /**
     * Computes the closest edge point of the classbox
     *
     * @param cls the ClassObject in question
     * @param target the point toward which the line is drawn (the other box)
     * @return the point on the box's boundary
     */
    private Point getClosestHit(ClassObject cls, Point target) {
        Point center = getClassCenter(cls);
        double dx = target.x - center.x;
        double dy = target.y - center.y;
        Dimension classSize = getClassDimension(cls);
        double halfWidth;
	    double halfHeight;
        halfWidth = classSize.getWidth()/2;
        halfHeight = classSize.getHeight()/2;

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
        setSize(getPreferredSize());
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
            Point sourceCenter = getClassCenter(rel.getSource());
            Point destinationCenter = getClassCenter(rel.getDestination());
            Point p1 = getClosestHit(rel.getSource(), destinationCenter);
            Point p2 = getClosestHit(rel.getDestination(), sourceCenter);

            // Draw the main line between the edges of the boxes
            int bendingOffset = -100; 
            //hardcoding this^^ as default behavior: a better fix would change back to 0 and then subtract 100 once reverse rel is detected like in our original implementation


            Point bent = getBendPoint(p1, p2, bendingOffset);
            Graphics2D g2 = (Graphics2D) g; // used to draw curves instead of lines
            QuadCurve2D curve = new QuadCurve2D.Double(p1.x, p1.y, bent.x, bent.y, p2.x, p2.y);
            g2.draw(curve);
            arrowhead((Graphics2D)g,p1,p2);

            Point a = new Point(p2.y - p1.y, p2.x - p1.x);

            // Draw the relationship type label near the bent point of the line
            int midX = (p1.x + p2.x) / 2;
            int midY = (p1.y + p2.y) / 2;
            g.drawString(rel.getTypeString(),midX-(metrics.stringWidth(rel.getTypeString())/2),midY+bendingOffset/(sourceCenter.x<=destinationCenter.x?2:-2));
        } 
        else {
            Point center = getClassCenter(rel.getSource());
            
//int w = rel.getSource().getWidth();
            int w = (int)getClassDimension(rel.getSource()).getWidth();
            center.y -= 12;// nuge to change closest hit to top
            Point top = getClosestHit(rel.getSource(),center);

            g.drawString(rel.getTypeString(),top.x-(metrics.stringWidth(rel.getTypeString())/2),top.y);
            g.drawArc(top.x-(w/2),top.y-50,w-4,150,0,180);

            top.x = top.x+(w/2)-10;
            top.y -= 10;
            arrowhead((Graphics2D)g,top,new Point(top.x+2,top.y+5));
        }
	getParent().revalidate();
	getParent().repaint();
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
