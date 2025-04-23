package org.View.GUICmp;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import javax.swing.UIManager;

public class HintTextArea extends JTextArea {
	private boolean showingHint;
    private String hint;

	public HintTextArea(String hint) {
		showingHint = true;
		this.hint = hint;

		setText(hint);
		setForeground(Color.GRAY);

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (showingHint) {
					setText("");
					setForeground(UIManager.getColor("TextField.foreground"));
					showingHint = false;
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (getText().isEmpty()) {
					setText(hint);
					setForeground(Color.GRAY);
					showingHint = true;
				}
			}
		});
	}

	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}

    public void reset(){
	setText(hint);
	setForeground(Color.GRAY);
	showingHint=true;
    }
}
