package org.View.GUICmp;

import javax.swing.JPanel;

import org.Model.ClassObject;

import javax.swing.BorderFactory;

public class NamePanel extends JPanel {
	private HintTextArea txt = new HintTextArea("Name");

	public NamePanel() {
		super();
		setBorder(BorderFactory.createTitledBorder("Name"));
		add(txt);
		txt.reset();
	}

	public String getData() {
		return txt.getText();
	}

	public void reset() {
		txt.reset();
	}

	public void set(ClassObject c) {
		txt.setText(c.getName());
	}
}
