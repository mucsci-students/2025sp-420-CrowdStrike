package org.View.GUICmp;

import java.io.File;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;

import javax.swing.JPanel;
import javax.imageio.ImageIO;

import org.Model.UMLModel;
import org.Controller.UMLEditor;
import org.Model.ClassObject;

public class UMLDiagram extends JPanel implements PropertyChangeListener {
	private UMLEditor e;

	public UMLDiagram(UMLEditor e) {
		super(null);
		updatemdl(e.getModel());
		e.addPropertyChangeListener(this);
		this.e = e;
	}

	public void save(String pth) {
		setSize(getPreferredSize());
		BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		printAll(g);
		g.dispose();

		try {
			ImageIO.write(image, "png", new File(pth));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatemdl(UMLModel m) {
		removeAll();
		for (ClassObject c : m.getClassList()) {
			UMLClass uc = new UMLClass(c);
			add(uc);
			e.addPropertyChangeListener(uc);
		}
		setSize(getPreferredSize());
		revalidate();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		int x = 0, y = 0;
		for (Component c : getComponents()) {
			x = Math.max(x, c.getX() + c.getWidth());
			y = Math.max(y, c.getY() + c.getHeight());
		}
		return new Dimension(x + 10, y + 10);
	}

	@Override
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {// TODO finish
			case "AddClass":
				UMLClass c = new UMLClass((ClassObject) evt.getNewValue());
				add(c);
				e.addPropertyChangeListener(c);
				break;
			case "DeleteClass":
				// UMLClass managed this
				break;
			case "AddRelationship":
				break;
			case "DeleteRelationship":
				break;
		}
		revalidate();
		repaint();
	}
}
