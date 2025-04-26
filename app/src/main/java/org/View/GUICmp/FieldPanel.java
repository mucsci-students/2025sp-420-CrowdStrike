package org.View.GUICmp;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Field;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import java.awt.GridLayout;
import java.util.HashMap;

public class FieldPanel extends JPanel {
	private JPanel grid;

	public FieldPanel() {
		super();
		setBorder(BorderFactory.createTitledBorder("Fields"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		grid = new JPanel();
		grid.setLayout(new GridLayout(0, 2, 5, 5));
		add(grid);

		JPanel control = new JPanel();
		control.setLayout(new BoxLayout(control, BoxLayout.X_AXIS));

		JButton tmp;

		tmp = new JButton("+");
		tmp.addActionListener(e -> {
			addGrid();
		});
		control.add(tmp);

		tmp = new JButton("-");
		tmp.addActionListener(e -> {
			removeGrid();
		});
		control.add(tmp);
		add(control);
	}

	private void addGrid() {
		addGrid(null, null);
	}

	private void addGrid(String name, String type) {
		GridLayout g = (GridLayout) grid.getLayout();
		g.setRows(g.getRows() + 1);

		HintTextArea tmp = new HintTextArea("Type");
		if (type != null)
			tmp.setText(type);
		grid.add(tmp);

		tmp = new HintTextArea("Name");
		if (name != null)
			tmp.setText(name);
		grid.add(tmp);

		grid.revalidate();
		grid.repaint();
		SwingUtilities.getWindowAncestor(this).pack();
	}

	private void removeGrid() {
		GridLayout g = (GridLayout) grid.getLayout();
		if (g.getRows() < 1)
			return;
		g.setRows(g.getRows() - 1);
		int cnt = grid.getComponentCount();
		grid.remove(grid.getComponent(cnt - 1));
		grid.remove(grid.getComponent(cnt - 2));
		grid.revalidate();
		grid.repaint();
		SwingUtilities.getWindowAncestor(this).pack();
	}

	public HashMap<String, String> getData() {
		HashMap<String, String> h = new HashMap<>();
		int cnt = grid.getComponentCount();
		if (cnt < 2)
			return h;
		for (int i = 0; i < cnt; i += 2) {
			String type, name;
			HintTextArea a = (HintTextArea) grid.getComponent(i);
			type = a.getText();
			a = (HintTextArea) grid.getComponent(i + 1);
			name = a.getText();
			h.put(name, type);
		}
		return h;
	}

	public void reset() {
		GridLayout l = (GridLayout) grid.getLayout();
		if (l.getRows() < 1)
			return;
		grid.removeAll();
		l.setRows(0);
	}

	public void set(ClassObject c) {
		for (AttributeInterface i : c.getFieldList()) {
			Field f = (Field) i;
			addGrid(f.getName(), f.getVarType());
		}
	}
}
