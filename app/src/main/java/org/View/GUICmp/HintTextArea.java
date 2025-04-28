package org.View.GUICmp;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import javax.swing.UIManager;

public class HintTextArea extends JTextArea {
	private boolean showingHint;
	private String hint;

	public HintTextArea(String h) {
		showingHint = true;
		hint = h;

		super.setText(hint);
		setForeground(Color.GRAY);

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (showingHint) {
					setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				setHint();
			}
		});
	}

	private void setHint() {
		if (getText().isEmpty()) {
			super.setText(hint);
			setForeground(Color.GRAY);
			showingHint = true;
		}
	}

	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}

	@Override
	public void setText(String s) {
		super.setText(s);
		setForeground(UIManager.getColor("TextField.foreground"));
		showingHint = false;
		repaint();
	}

	public void reset() {
		setText(hint);
		setForeground(Color.GRAY);
		showingHint = true;
	}
}
