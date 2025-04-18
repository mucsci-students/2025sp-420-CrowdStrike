package org.View.GUICmp;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

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
			    try {
				String name;
				//TODO replace with real ui
				Random r = new Random();
				name = "tmp"+new Integer(r.nextInt()).toString();
				//
				edit.getModel().isValidClassName(name);
				edit.addClass(name);
			    } catch (Exception e){
			    }
			}
		});

		addClass.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    //TODO
			}
		});

		add(addClass);
		add(addRelationship);
	}
}
