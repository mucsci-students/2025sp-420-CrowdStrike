package org.View.GUICmp;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class MethodFrame extends JPanel {
	private JPanel mar;

	public MethodFrame() {
		super();
		setBorder(BorderFactory.createTitledBorder("Methods"));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel mar = new JPanel();
		mar.setLayout(new BoxLayout(mar, BoxLayout.Y_AXIS));
		add(mar);
		this.mar = mar;

		JPanel control = new JPanel();

		JButton tmp = new JButton("+");
		tmp.addActionListener(e -> {
			addMethod();
			revalidate();
			repaint();
			SwingUtilities.getWindowAncestor(this).pack();
		});
		control.add(tmp);

		tmp = new JButton("-");
		tmp.addActionListener(e -> {
			int cnt = mar.getComponentCount();
			if (cnt < 1)
				return;
			mar.remove(mar.getComponent(cnt - 1));
			revalidate();
			repaint();
			SwingUtilities.getWindowAncestor(this).pack();
		});
		control.add(tmp);
		add(control);
	}

	private void addMethod() {
		JPanel method = new JPanel();
		method.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		method.add(new ParamFrame());

		JButton tmp = new JButton("+");
		tmp.addActionListener(e -> {
			ParamFrame f = (ParamFrame) method.getComponent(0);
			f.addBox();
		});
		method.add(tmp);

		tmp = new JButton("-");
		tmp.addActionListener(e -> {
			ParamFrame f = (ParamFrame) method.getComponent(0);
			f.remBox();
		});
		method.add(tmp);
		mar.add(method);
	}

	public ArrayList<ArrayList<String>> getData() {
		ArrayList<ArrayList<String>> r = new ArrayList<>();
		for (Component c : mar.getComponents()) {
			JPanel tmp = (JPanel) c;
			ParamFrame p = (ParamFrame) tmp.getComponent(0);
			r.add(p.getData());
		}
		return r;
	}

	public void reset() {
		mar.removeAll();
	}

	private class ParamFrame extends JPanel {
		public ParamFrame() {
			super();
			setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			add(new HintTextArea("Return type"));
			add(new HintTextArea("Name"));
			add(new JLabel("("));
			add(new JLabel(")"));

		}

		public void addBox() {
			remove(getComponent(getComponentCount() - 1));
			add(new HintTextArea("Type"));
			add(new HintTextArea("Name"));
			add(new JLabel(")"));
			revalidate();
			repaint();
			SwingUtilities.getWindowAncestor(this).pack();
		}

		public void remBox() {
			int cnt = getComponentCount();
			if (cnt <= 4)
				return;
			Component a, b;
			a = getComponent(getComponentCount() - 2);
			b = getComponent(getComponentCount() - 3);
			remove(a);
			remove(b);
			revalidate();
			repaint();
			SwingUtilities.getWindowAncestor(this).pack();
		}

		public ArrayList<String> getData() {
			ArrayList<String> m = new ArrayList<>();
			for (Component c : getComponents()) {
				if (c.getClass() != HintTextArea.class)
					continue;
				HintTextArea h = (HintTextArea) c;
				m.add(h.getText());
			}
			return m;
		}
	}
}
