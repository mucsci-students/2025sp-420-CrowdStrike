package org.View.GUICmp;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.Controller.UMLEditor;
import org.Model.ClassObject;

public class EditClassInput extends JFrame {
	Boolean ok;

	public EditClassInput(UMLEditor edit) {
		super();
		setTitle("Edit class");

		JPanel p = new JPanel();
		NamePanel name = new NamePanel();
		FieldPanel fields = new FieldPanel();
		MethodPanel methods = new MethodPanel();
		JPanel control = new JPanel();
		JPanel sel = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		ClassObject a[] = edit.getModel().getClassList().toArray(new ClassObject[0]);
		JComboBox<ClassObject> cl = new JComboBox<>(a);
		ok = a.length >= 1;
		JButton tmp = new JButton();
		tmp.setText("Set");
		tmp.addActionListener(e -> {
			ClassObject co = (ClassObject) cl.getSelectedItem();
			name.set(co);
			fields.set(co);
			methods.set(co);
		});

		sel.add(cl);
		sel.add(tmp);

		tmp = new JButton();
		tmp.setText("Update Class");
		tmp.addActionListener(e -> {
			ClassObject co = (ClassObject) cl.getSelectedItem();

			edit.updateClass(co, name.getData(), fields.getData(), methods.getData());

			fields.reset();
			name.reset();
			methods.reset();
			pack();
		});
		control.add(tmp);

		tmp = new JButton();
		tmp.setText("Remove");
		tmp.addActionListener(e -> {
			ClassObject co = (ClassObject) cl.getSelectedItem();
			try {
				edit.deleteClass(co.getName());
				cl.removeItem(co);
				cl.revalidate();
				cl.repaint();
				fields.reset();
				name.reset();
				methods.reset();
				pack();
			} catch (Exception err) {
				JOptionPane.showMessageDialog(null, err.getMessage());
			}
		});
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

	public Boolean ok() {
		return ok;
	}
}
