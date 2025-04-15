package org.View;

import org.Model.UMLModel;
import org.View.GUICmp.UMLDiagram;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

		JMenuBar bar = new JMenuBar();
		bar.add(getDebug(d, m));

		JScrollPane sp = new JScrollPane();
		sp.setViewportView(d);

		JSplitPane s = new JSplitPane(JSplitPane.VERTICAL_SPLIT, bar, sp);
		getContentPane().add(s);
	}

	private JMenu getDebug(UMLDiagram d, UMLModel m) {
		JMenu debug = new JMenu("debug");
		JMenuItem update = new JMenuItem("Update");
		JMenuItem save = new JMenuItem("Save");

		debug.add(update);
		debug.add(save);

		update.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.updatemdl(m);
			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				d.save("test.png");
			}
		});
		return debug;
	}
}
