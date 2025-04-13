package org.View.GUICmp;

import java.io.File;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.imageio.ImageIO;

import org.Model.UMLModel;
import org.Model.ClassObject;

public class UMLDiagram extends JPanel {
	public UMLDiagram(UMLModel m) {
		super(null);
		updatemdl(m);
	}

	public void save(String pth) {
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
			add(new UMLClass(c));
		}
		setSize(getPreferredSize());
		revalidate();
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		int x = 0, y = 0;
		for (Component c : getComponents()) {
			x = Math.max(x, c.getX() + c.getHeight());
			y = Math.max(y, c.getY() + c.getWidth());
		}
		return new Dimension(x + 10, y + 10);
	}
}
