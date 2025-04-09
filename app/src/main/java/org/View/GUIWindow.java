package org.View;

import org.Model.UMLModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIWindow extends JFrame {
	private UMLModel m;

	public GUIWindow(UMLModel m) {
		this.m = m;
		setTitle("UML CLASS DIAGRAM");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);

		JButton up = new JButton("Update");
		up.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("update");
			}
		});

		JPanel input = new JPanel();
		JPanel todo = new JPanel();
		input.add(up);

		JSplitPane s = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, input, todo);
		getContentPane().add(s);
	}
}
