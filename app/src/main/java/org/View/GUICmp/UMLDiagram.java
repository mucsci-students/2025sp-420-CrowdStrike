package org.View.GUICmp;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.Model.ClassObject;
import org.Model.UMLModel;
import org.View.ClassBox;

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
			add(new ClassBox(c, null));
		}
		setSize(getPreferredSize());
		revalidate();
		repaint();
	}
}
