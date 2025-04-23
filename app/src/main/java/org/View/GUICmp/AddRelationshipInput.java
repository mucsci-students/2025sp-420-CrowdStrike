package org.View.GUICmp;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.Controller.UMLEditor;
import org.Model.ClassObject;

public class AddRelationshipInput extends JFrame {
	private Boolean ok = true;

	public AddRelationshipInput(UMLEditor edit) {
		super();
		setTitle("Add Relationship");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel p, control, select, type;
		p = new JPanel();
		select = new JPanel();
		control = new JPanel();
		type = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		ArrayList<String> l = new ArrayList<>();
		for (ClassObject co : edit.getModel().getClassList())
			l.add(co.getName());
		String[] a = l.toArray(new String[0]);
		if (a.length < 1)
			ok = false;
		System.out.println(a.length < 1);

		select.add(new JComboBox<>(a));
		select.add(new JLabel("->"));
		select.add(new JComboBox<>(a));

		JButton tmp = new JButton("add relationship");
		tmp.addActionListener(e -> {
			// TODO
		});
		control.add(tmp);

		tmp = new JButton("exit");
		tmp.addActionListener(e -> {
			setVisible(false);
			dispose();
		});
		control.add(tmp);

		JComboBox<String> t = new JComboBox<>(
				new String[] { "AGGREGATION", "COMPOSITION", "INHERITANCE", "REALIZATION" });
		type.add(t);

		select.setBorder(BorderFactory.createTitledBorder("Select classes"));
		type.setBorder(BorderFactory.createTitledBorder("Relationship type"));

		p.add(select);
		p.add(type);
		p.add(control);
		add(p);
		pack();
	}

	public Boolean ok() {
		return ok;
	}
}
