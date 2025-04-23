package org.View.GUICmp;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.Controller.UMLEditor;
import org.Model.ClassObject;

public class EditClassInput extends JFrame{
    public EditClassInput(UMLEditor edit){
	super();
	setTitle("Edit class");

	JPanel sel = new JPanel();

	ClassObject a[] = edit.getModel().getClassList().toArray(new ClassObject[0]);
	JComboBox<ClassObject> cl = new JComboBox<>(a);
	JButton set = new JButton("set");

	sel.add(cl);
	sel.add(set);
	add(sel);

	revalidate();
	repaint();
	pack();
    }
}
