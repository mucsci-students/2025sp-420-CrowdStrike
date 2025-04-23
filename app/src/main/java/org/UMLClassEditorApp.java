package org;

import org.View.CLView;
import org.View.GUIView;
import org.View.GUIWindow;
import org.Controller.CLController;
import org.Controller.GUIController;
import org.Controller.UMLEditor;
import org.Model.UMLModel;
/**
 * UMLClassEditorApp.java
 * 
 * The main application that users run to build the project
 * Instantiates a model and controller
 * (In later sprints, will ask the user for a view choice)
 * Instantiates the view and runs the init() method on the view controller
 */
class UMLClassEditorApp {

    public static void main(String[] args) {
        UMLModel model = new UMLModel();
        UMLEditor editor = new UMLEditor(model);
        if(args.length != 0 && args[0].equals("--cli")) {
	    System.setProperty("java.awt.headless", "false");
            CLView view = new CLView();
            CLController controller = new CLController(model, editor, view);
            controller.init();
        } else {
	    GUIWindow window = new GUIWindow(model,editor);
            GUIView view = new GUIView();
            GUIController controller = new GUIController(model, editor, view);
            controller.initController();
	    window.setVisible(true);
        }
        
    }
}
