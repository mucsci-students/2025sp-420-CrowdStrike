package org.View.GUICmp;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class MethodPanel extends JPanel {
	private JPanel mar;

	public MethodPanel() {
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
		});
		control.add(tmp);

		tmp = new JButton("-");
		tmp.addActionListener(e -> {
			removeMethod();
		});
		control.add(tmp);
		add(control);
	}

	private void removeMethod() {
		int cnt = mar.getComponentCount();
		if (cnt < 1)
			return;
		mar.remove(mar.getComponent(cnt - 1));
		revalidate();
		repaint();
		SwingUtilities.getWindowAncestor(this).pack();
	}

	private void addMethod() {
		addMethod(null, null, null);
	}

	private void addMethod(String name, String ret, ArrayList<Parameter> p) {
		JPanel method = new JPanel();
		method.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		ParamPanel pan;
		pan = new ParamPanel(name, ret);
		if (p != null)
			pan.set(p);
		method.add(pan);

		JButton tmp = new JButton("+");
		tmp.addActionListener(e -> {
			ParamPanel f = (ParamPanel) method.getComponent(0);
			f.addBox();
			SwingUtilities.getWindowAncestor(this).pack();
		});
		method.add(tmp);

		tmp = new JButton("-");
		tmp.addActionListener(e -> {
			ParamPanel f = (ParamPanel) method.getComponent(0);
			f.remBox();
			SwingUtilities.getWindowAncestor(this).pack();
		});
		method.add(tmp);
		mar.add(method);
		revalidate();
		repaint();
		SwingUtilities.getWindowAncestor(this).pack();
	}

	public ArrayList<ArrayList<String>> getData() {
		ArrayList<ArrayList<String>> r = new ArrayList<>();
		for (Component c : mar.getComponents()) {
			JPanel tmp = (JPanel) c;
			ParamPanel p = (ParamPanel) tmp.getComponent(0);
			r.add(p.getData());
		}
		return r;
	}

	public void reset() {
		mar.removeAll();
	}

	public void set(ClassObject c) {
		for (AttributeInterface i : c.getMethodList()) {
			Method m = (Method) i;
			addMethod(m.getName(), m.getReturnType(), m.getParamList());
		}
	}

	private class ParamPanel extends JPanel {
		public ParamPanel(String name, String type) {
			super();
			setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
			HintTextArea tmp;
			tmp = new HintTextArea("Return type");
			if (type != null)
				tmp.setText(type);
			add(tmp);

			tmp = new HintTextArea("Name");
			if (name != null)
				tmp.setText(name);
			add(tmp);
			add(new JLabel("("));
			add(new JLabel(")"));
		}

		public void addBox() {
			addBox(null, null);
		}

		public void addBox(String name, String type) {
			remove(getComponent(getComponentCount() - 1));
			HintTextArea tmp = new HintTextArea("Type");
			if (type != null)
				tmp.setText(type);
			add(tmp);

			tmp = new HintTextArea("Name");
			if (name != null)
				tmp.setText(name);
			add(tmp);
			add(new JLabel(")"));
			revalidate();
			repaint();
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
		}

		public void set(ArrayList<Parameter> plist) {
			for (Parameter p : plist) {
				addBox(p.getName(), p.getType());
			}
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
