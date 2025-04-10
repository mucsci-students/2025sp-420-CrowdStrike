package org.View;

import org.Model.UMLModel;
import org.View.GUICmp.UMLDiagram;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIWindow extends JFrame {
	private UMLModel m;
	private UMLDiagram d;// should be in scrole container

	public GUIWindow(UMLModel m) {
		this.m = m;
		d = new UMLDiagram(m);

		setTitle("UML CLASS DIAGRAM");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);

		JButton up = new JButton("Update");
		up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.updatemdl(m);
			}
		});

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.save("test.png");
			}
		});

		JPanel input = new JPanel();
		input.add(up);
		input.add(save);

		JSplitPane s = new JSplitPane(JSplitPane.VERTICAL_SPLIT, input, d);
		getContentPane().add(s);
	}
}
