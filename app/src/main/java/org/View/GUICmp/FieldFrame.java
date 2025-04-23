package org.View.GUICmp;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.util.HashMap;

public class FieldFrame extends JPanel {
	private JPanel grid;

	public FieldFrame() {
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
			GridLayout g = (GridLayout) grid.getLayout();
			g.setRows(g.getRows() + 1);
			grid.add(new HintTextArea("Type"));
			grid.add(new HintTextArea("Name"));
			grid.revalidate();
			grid.repaint();
		});
		control.add(tmp);

		tmp = new JButton("-");
		tmp.addActionListener(e -> {
			GridLayout g = (GridLayout) grid.getLayout();
			if (g.getRows() < 1)
				return;
			g.setRows(g.getRows() - 1);
			int cnt = grid.getComponentCount();
			grid.remove(grid.getComponent(cnt - 1));
			grid.remove(grid.getComponent(cnt - 2));
			grid.revalidate();
			grid.repaint();
		});
		control.add(tmp);
		add(control);
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
}
