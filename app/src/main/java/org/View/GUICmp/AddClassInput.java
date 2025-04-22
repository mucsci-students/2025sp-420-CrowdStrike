package org.View.GUICmp;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AddClassInput extends JFrame {

	public AddClassInput() {
		super();
		setTitle("Add Class");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel p, name, fields, methods, control;
		p = new JPanel();
		name = new NameFrame();
		fields = new FieldFrame();
		methods = new MethodFrame();
		control = new JPanel();

		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

		JButton tmp = new JButton();
		tmp.setText("Add Class");
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

	private class NameFrame extends JPanel {
		public NameFrame() {
			super();
			setBorder(BorderFactory.createTitledBorder("Name"));
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(new HintTextArea("Name"));
		}
	}

	private class FieldFrame extends JPanel {
		public FieldFrame() {
			super();
			setBorder(BorderFactory.createTitledBorder("Fields"));
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

			JPanel grid = new JPanel();
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
				pack();
			});
			control.add(tmp);

			tmp = new JButton("-");
			tmp.addActionListener(e -> {
				GridLayout g = (GridLayout) grid.getLayout();
				if (g.getRows() < 2)
					return;
				g.setRows(g.getRows() - 1);
				int cnt = grid.getComponentCount();
				grid.remove(grid.getComponent(cnt - 1));
				grid.remove(grid.getComponent(cnt - 2));
				grid.revalidate();
				grid.repaint();
				pack();
			});
			control.add(tmp);
			add(control);
		}
	}

	private class MethodFrame extends JPanel {
		public MethodFrame() {
			super();
			setBorder(BorderFactory.createTitledBorder("Methods"));
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			JPanel mar = new JPanel();
			mar.setLayout(new BoxLayout(mar, BoxLayout.Y_AXIS));
			add(mar);

			JPanel control = new JPanel();

			JButton tmp = new JButton("+");
			tmp.addActionListener(e -> {
				addMethod(mar);
				revalidate();
				repaint();
				pack();
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
				pack();
			});
			control.add(tmp);
			add(control);
		}

		private void addMethod(JPanel mar) {
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
	}

	private class ParamFrame extends JPanel {
		public ParamFrame() {
			super();
			setLayout(new FlowLayout(FlowLayout.RIGHT,5,5));
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
			pack();
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
			pack();
		}
	}
}
