package org.View.GUICmp;

import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.Controller.UMLEditor;
import org.Model.ClassObject;
import org.Model.Relationship;

public class EditRelationshipsInput extends JFrame {
	private Boolean ok;

	public EditRelationshipsInput(UMLEditor edit) {
		super();
		setTitle("Edit Relationship");

		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		add(p);

		JPanel sel = new JPanel();
		p.add(sel);
		ArrayList<Relationship> rs = edit.getModel().getRelationshipList();
		ok = rs.size() >= 1;
		JComboBox<Relationship> rbox = new JComboBox<>(rs.toArray(new Relationship[0]));
		JButton bttn = new JButton("set");

		sel.add(rbox);
		sel.add(bttn);

		JPanel edt = new JPanel();
		ArrayList<ClassObject> cs = edit.getModel().getClassList();
		JComboBox<ClassObject> src = new JComboBox<>(cs.toArray(new ClassObject[0]));
		JComboBox<ClassObject> dst = new JComboBox<>(cs.toArray(new ClassObject[0]));
		JComboBox<Relationship.Type> typ = new JComboBox<>();
		typ.addItem(Relationship.Type.AGGREGATION);
		typ.addItem(Relationship.Type.COMPOSITION);
		typ.addItem(Relationship.Type.INHERITANCE);
		typ.addItem(Relationship.Type.REALIZATION);
		edt.add(src);
		edt.add(typ);
		edt.add(dst);
		p.add(edt);

		bttn.addActionListener(e -> {
			Relationship tmpr = (Relationship) rbox.getSelectedItem();
			src.setSelectedItem(tmpr.getSource());
			typ.setSelectedItem(tmpr.getType());
			dst.setSelectedItem(tmpr.getDestination());
		});

		JPanel control = new JPanel();
		JButton tmp = new JButton("Update");
		tmp.addActionListener(e -> {
			ClassObject csrc, cdst;
			Relationship.Type t;
			Relationship r;

			r = (Relationship) rbox.getSelectedItem();
			csrc = (ClassObject) src.getSelectedItem();
			cdst = (ClassObject) dst.getSelectedItem();
			t = (Relationship.Type) typ.getSelectedItem();

			try {
				edit.updateRelationship(csrc.getName(), cdst.getName(), t, r);
			} catch (Exception err) {
				JOptionPane.showMessageDialog(null, err.getMessage());
			}

			rbox.revalidate();
			rbox.repaint();
		});
		control.add(tmp);

		tmp = new JButton("Remove");
		tmp.addActionListener(e -> {
			Relationship tmpr = (Relationship) rbox.getSelectedItem();
			rbox.removeItem(tmpr);
			edit.deleteRelationship(tmpr.getSource().getName(), tmpr.getDestination().getName());
			rbox.revalidate();
			rbox.repaint();
			pack();
		});
		control.add(tmp);

		tmp = new JButton("Exit");
		tmp.addActionListener(e -> {
			setVisible(false);
			dispose();
		});
		control.add(tmp);
		p.add(control);

		revalidate();
		repaint();
		pack();
	}

	public Boolean ok() {
		return ok;
	}
}
