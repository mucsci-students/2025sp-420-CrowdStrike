package org.View.GUICmp;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.Controller.UMLEditor;

public class EditMenu extends JMenu {
	public EditMenu(UMLEditor edit) {
		super("Edit");
		JMenuItem editClass, editRelationship;
		editClass = new JMenuItem("Edit Class");
		editRelationship = new JMenuItem("Edit Relationships");

		editClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
			    EditClassInput ac = new EditClassInput(edit);
			    ac.setVisible(true);
			}
		});

		editRelationship.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});

		add(editClass);
		add(editRelationship);
	}
}
