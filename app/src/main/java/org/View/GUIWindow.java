package org.View;

import org.Model.UMLModel;
import org.View.GUICmp.UMLDiagram;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIWindow extends JFrame {
	public GUIWindow(UMLModel m) {
		UMLDiagram d = new UMLDiagram(m);

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

		JScrollPane sp = new JScrollPane();
		sp.setViewportView(d);

		JSplitPane s = new JSplitPane(JSplitPane.VERTICAL_SPLIT, input, sp);
		getContentPane().add(s);
	}
}
