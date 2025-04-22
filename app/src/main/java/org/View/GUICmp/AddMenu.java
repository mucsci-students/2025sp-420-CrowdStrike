package org.View.GUICmp;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.Controller.UMLEditor;

public class AddMenu extends JMenu {
	public AddMenu(UMLEditor edit) {
		super("Add");
		JMenuItem addClass,addRelationship;
		addClass = new JMenuItem("Add Class");
		addRelationship = new JMenuItem("Add Relationship");

		addClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				AddClassInput ac = new AddClassInput();
				ac.setVisible(true);
			}
		});

		addRelationship.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    //TODO
			}
		});

		add(addClass);
		add(addRelationship);
	}
}
