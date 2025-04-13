package org.View.GUICmp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import org.Model.Field;
import org.Model.Method;
import org.Model.ClassObject;
import org.Model.AttributeInterface;

public class UMLClass extends JPanel {
	private int mouseX, mouseY;

	public UMLClass(ClassObject c) {
		super();

		setLocation(c.getPosition());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				getParent().revalidate();
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int newX = getX() + e.getX() - mouseX;
				int newY = getY() + e.getY() - mouseY;
				newX = Math.max(10, newX);
				newY = Math.max(10, newY);

				setLocation(newX, newY);
				c.setPosition(getLocation());
			}
		});

		rebuild(c);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension pref = super.getPreferredSize();

		int width = Math.max(pref.width, 100);
		int height = Math.max(pref.height, 100);

		return new Dimension(width, height);
	}

	public void rebuild(ClassObject c) {
		JPanel fields, methods;
		JLabel nl, fl, ml;
		Dimension maxWidth;
		Font boldFont = new Font(getFont().getName(), Font.BOLD, getFont().getSize());

		methods = new JPanel();
		fields = new JPanel();

		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
		methods.setLayout(new BoxLayout(methods, BoxLayout.Y_AXIS));

		setBorder(new CompoundBorder(BorderFactory.createTitledBorder(c.getName()),
				new EmptyBorder(5, 5, 5, 5)));
		fields.setBorder(BorderFactory.createTitledBorder("Fields"));
		methods.setBorder(BorderFactory.createTitledBorder("Methods"));

		for (AttributeInterface i : c.getFieldList()) {
			Field f = (Field) i;// I hate this
			fields.add(new JLabel(f.toString()));
		}

		for (AttributeInterface i : c.getMethodList()) {
			Method m = (Method) i;// I hate this
			methods.add(new JLabel(m.toString()));
		}

		add(fields);
		add(Box.createVerticalStrut(3));
		add(methods);

		maxWidth = new Dimension(Short.MAX_VALUE, fields.getPreferredSize().height);
		fields.setMaximumSize(maxWidth);

		maxWidth = new Dimension(Short.MAX_VALUE, methods.getPreferredSize().height);
		methods.setMaximumSize(maxWidth);

		revalidate();
		setSize(getPreferredSize());
	}
}
