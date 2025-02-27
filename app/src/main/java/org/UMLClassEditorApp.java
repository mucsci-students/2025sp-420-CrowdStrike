package org;
import org.View.CLView;
import org.Controller.CLController;
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
        CLView view = new CLView();
        CLController controller = new CLController(model, editor, view);
        controller.init();
    }
}
