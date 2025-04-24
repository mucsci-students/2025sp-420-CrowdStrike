package org.View.GUICmp;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.Controller.UMLEditor;
import org.Model.ClassObject;

public class EditClassInput extends JFrame {
	public EditClassInput(UMLEditor edit) {
		super();
		setTitle("Edit class");

		JPanel p, sel, name, fields, methods, control;

		p = new JPanel();
		name = new NameFrame();
		fields = new FieldFrame();
		methods = new MethodFrame();
		control = new JPanel();
		sel = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		ClassObject a[] = edit.getModel().getClassList().toArray(new ClassObject[0]);
		JComboBox<ClassObject> cl = new JComboBox<>(a);
		JButton set = new JButton("set");

		sel.add(cl);
		sel.add(set);

		JButton tmp = new JButton();
		tmp.setText("Update Class");
		tmp.addActionListener(e -> {
		});			NameFrame n = (NameFrame) name;
			FieldFrame f = (FieldFrame) fields;
			MethodFrame m = (MethodFrame) methods;
			edit.addClass(n.getData(), f.getData(), m.getData());

			f.reset();
			n.reset();
			m.reset();
			pack();
			control.add(tmp);

		tmp = new JButton();
		tmp.setText("Exit");
		tmp.addActionListener(e -> {
			setVisible(false);
			dispose();
		});
		control.add(tmp);

		p.add(sel);
		p.add(name);
		p.add(fields);
		p.add(methods);
		p.add(control);
		add(p);

		revalidate();
		repaint();
		pack();
	}
}
