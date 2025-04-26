package org.View.GUICmp;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.Controller.UMLEditor;

public class AddClassInput extends JFrame {

	public AddClassInput(UMLEditor edit) {
		super();
		setTitle("Add Class");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel p, name, fields, methods, control;
		p = new JPanel();
		name = new NamePanel();
		fields = new FieldPanel();
		methods = new MethodPanel();
		control = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		JButton tmp = new JButton();
		tmp.setText("Add Class");
		tmp.addActionListener(e -> {
			NamePanel n = (NamePanel) name;
			FieldPanel f = (FieldPanel) fields;
			MethodPanel m = (MethodPanel) methods;
			edit.addClass(n.getData(), f.getData(), m.getData());

			f.reset();
			n.reset();
			m.reset();
			pack();
		});
		control.add(tmp);

		tmp = new JButton();
		tmp.setText("Exit");
		tmp.addActionListener(e -> {
			setVisible(false);
			dispose();
		});
		control.add(tmp);

		p.add(name);
		p.add(fields);
		p.add(methods);
		p.add(control);
		add(p);
		pack();
	}

}
