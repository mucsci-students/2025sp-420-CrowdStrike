package org.View;

import org.Controller.UMLEditor;
import org.Model.UMLModel;
import org.View.GUICmp.AddMenu;
import org.View.GUICmp.EditMenu;
import org.View.GUICmp.FileMenu;
import org.View.GUICmp.UMLDiagram;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

public class GUIWindow extends JFrame {
    //TODO add rick role
    public GUIWindow(UMLModel m, UMLEditor e) {
		UMLDiagram d = new UMLDiagram(e);

		setTitle("UML CLASS DIAGRAM");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);

		JMenuBar bar = new JMenuBar();
		bar.add(new FileMenu(e,d));
		bar.add(new AddMenu(e));
		bar.add(new EditMenu(e));

		JScrollPane sp = new JScrollPane();
		sp.setViewportView(d);

		JSplitPane s = new JSplitPane(JSplitPane.VERTICAL_SPLIT, bar, sp);
		getContentPane().add(s);
	}
}
