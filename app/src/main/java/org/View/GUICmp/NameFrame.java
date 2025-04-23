package org.View.GUICmp;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

public class NameFrame extends JPanel {
	private HintTextArea txt = new HintTextArea("Name");

	public NameFrame() {
		super();
		setBorder(BorderFactory.createTitledBorder("Name"));
		add(txt);
	}

	public String getData() {
		return txt.getText();
	}

	public void reset() {
		txt.reset();
	}
}
