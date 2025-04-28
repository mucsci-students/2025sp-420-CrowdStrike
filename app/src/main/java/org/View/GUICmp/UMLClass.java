package org.View.GUICmp;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.awt.event.MouseAdapter;

import org.Model.ClassObject;
import org.Model.AttributeInterface;

public class UMLClass extends JPanel implements PropertyChangeListener {
	private int mouseX, mouseY;

	public UMLClass(ClassObject c) {
		super();

		c.addPropertyChangeListener(this);

		setLocation(c.getPosition());
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new CompoundBorder(BorderFactory.createTitledBorder(c.getName()),
				new EmptyBorder(5, 5, 5, 5)));

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

		Border border = getBorder();
		CompoundBorder cb = (CompoundBorder) border;
		TitledBorder tb = (TitledBorder) cb.getOutsideBorder();
		FontMetrics fm = getFontMetrics(tb.getTitleFont());

		int width = Math.max(Math.max(pref.width, fm.stringWidth(tb.getTitle()) + 25), 100);
		int height = Math.max(pref.height, 100);

		return new Dimension(width, height);
	}

	public void rebuild(ClassObject c) {
		JPanel fields, methods;
		Dimension maxWidth;

		fields = new JPanel();
		methods = new JPanel();

		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
		methods.setLayout(new BoxLayout(methods, BoxLayout.Y_AXIS));

		fields.setName("fields");
		methods.setName("methods");

		setBorder(new CompoundBorder(BorderFactory.createTitledBorder(c.getName()),
				new EmptyBorder(5, 5, 5, 5)));
		fields.setBorder(BorderFactory.createTitledBorder("Fields"));
		methods.setBorder(BorderFactory.createTitledBorder("Methods"));

		add(fields);
		add(Box.createVerticalStrut(3));
		add(methods);

		updateSub("fields", c.getFieldList());
		updateSub("methods", c.getMethodList());

		maxWidth = new Dimension(Short.MAX_VALUE, Short.MAX_VALUE);
		fields.setMaximumSize(maxWidth);
		methods.setMaximumSize(maxWidth);

		revalidate();
		setSize(getPreferredSize());
	}

	@Override
	public void propertyChange(java.beans.PropertyChangeEvent evt) {
		Border border = getBorder();
		CompoundBorder cb = (CompoundBorder) border;
		TitledBorder tb = (TitledBorder) cb.getOutsideBorder();
		revalidate();
		repaint();

		switch (evt.getPropertyName()) {
			case "DeleteClass":
				ClassObject o = (ClassObject) evt.getOldValue();
				if (o.getName() == tb.getTitle())
					getParent().remove(this);
				break;
			case "RenameClass":
				if (evt.getOldValue() == tb.getTitle())
					tb.setTitle((String) evt.getNewValue());
				break;
			case "UpdatedFields":
				@SuppressWarnings("unchecked")
				ArrayList<AttributeInterface> a = (ArrayList<AttributeInterface>) evt.getNewValue();
				updateSub("fields", a);
				break;
			case "UpdatedMethods":
				@SuppressWarnings("unchecked")
				ArrayList<AttributeInterface> b = (ArrayList<AttributeInterface>) evt.getNewValue();
				updateSub("methods", b);
				break;

			case "FullUpdateClass":
				fullUpdate(evt, tb);

		}
		revalidate();
		setSize(getPreferredSize());
		repaint();
	}

	public boolean representsClassObject(ClassObject cls){
		Border border = getBorder();
		CompoundBorder cb = (CompoundBorder) border;
		TitledBorder tb = (TitledBorder) cb.getOutsideBorder();
		return tb.getTitle() == cls.getName();
	}

	private void fullUpdate(PropertyChangeEvent evt, TitledBorder tb) {
		ClassObject co = (ClassObject) evt.getOldValue();
		ClassObject cn = (ClassObject) evt.getNewValue();
		if (co.getName() != tb.getTitle())
			return;
		tb.setTitle(cn.getName());
		updateSub("fields", cn.getFieldList());
		updateSub("methods", cn.getMethodList());
	}

	private void updateSub(String name, ArrayList<AttributeInterface> a) {
		for (Component c : getComponents()) {
			if (c.getName() != name)
				continue;
			JPanel p = (JPanel) c;
			assert p != null : true;
			p.removeAll();
			for (AttributeInterface i : a) {
				p.add(new JLabel(i.toString()));
			}
			p.revalidate();
			p.repaint();
		}
	}
}
