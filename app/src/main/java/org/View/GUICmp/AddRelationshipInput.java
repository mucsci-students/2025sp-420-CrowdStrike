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
import org.Model.Relationship;

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

		JComboBox<String> src, dest;
		src = new JComboBox<>(a);
		dest = new JComboBox<>(a);
		select.add(src);
		select.add(new JLabel("->"));
		select.add(dest);

		JComboBox<Relationship.Type> t = new JComboBox<>(
				new Relationship.Type[] { Relationship.Type.AGGREGATION, Relationship.Type.COMPOSITION,
						Relationship.Type.INHERITANCE, Relationship.Type.REALIZATION });
		type.add(t);

		select.setBorder(BorderFactory.createTitledBorder("Select classes"));
		type.setBorder(BorderFactory.createTitledBorder("Relationship type"));

		JButton tmp = new JButton("add relationship");
		tmp.addActionListener(e -> {
			edit.addRelationship((String) src.getSelectedItem(), (String) dest.getSelectedItem(),
					(Relationship.Type) t.getSelectedItem());
		});
		control.add(tmp);

		tmp = new JButton("exit");
		tmp.addActionListener(e -> {
			setVisible(false);
			dispose();
		});
		control.add(tmp);

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
