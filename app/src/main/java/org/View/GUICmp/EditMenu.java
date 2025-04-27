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
			    EditClassInput ec = new EditClassInput(edit);
			    ec.setVisible(true);
			}
		});

		editRelationship.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    EditRelationshipsInput er = new EditRelationshipsInput(edit);
			    er.setVisible(true);
			}
		});

		add(editClass);
		add(editRelationship);
	}
}
