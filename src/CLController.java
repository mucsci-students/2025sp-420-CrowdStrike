
import java.util.Scanner;

// Checks validity of action then calls function in
// editor to carry out change
public class CLController {

	private UMLModel model;
	
	private UMLEditor editor;
	
	private CLView view;
	
	// Create scanner to read user input
    private Scanner sc;
    // Create a string to read user input
    private String input = "";
	// Prompt to be displayed at beginning of every loop
	private final String basePrompt = "Please type your command(Help for list of commands): ";
	
	/**
	 * Constructs an instance of the CLController
	 * @param model		| Contains lists of classes and relationships and accessors for those lists
	 * @param editor	| Allows user to edit the lists i the model
	 * @param view		| Determines how the UML system will be displayed
	 */
	public CLController(UMLModel model, UMLEditor editor, CLView view) {
		this.model = model;
		this.editor = editor;
		this.view = view;
		this.sc = new Scanner(System.in);
	}

	
	/**
	 * Gets the class to be listed and tells user if action failed
	 */
	private void CL_listClassInfo() {
		view.show("What class would you like printed?");
		input = sc.nextLine();
		String classInfo = model.listClassInfo(input);
		if (classInfo.equals("")) {
			// Class does not exist
			view.show("Requested class does not exist");
		} else {
			view.show(classInfo);
		}
	}
	
	/**
	 * Gets name of new class from user and displays if it was added successfully
	 */
	private void CL_addClass() {
		view.show("Enter the new class' name: ");
		input = sc.nextLine();
		if (editor.addClass(input)) {
			// editor.addClass succeeded
			view.show("Class " + input + " successfully added");
		} else {
			// editor.addClass failed
			view.show("Class " + input + " could not be added");
		}
	}
	
	private void CL_deleteClass() {
		view.show("What class would you like to delete?");
		input = sc.nextLine();
		if (editor.deleteClass(input)) {
			view.show("Class " + input + " successfully deleted");
		} else {
			view.show("Class " + input + " could not be deleted");
		}
	}
	
	private void CL_renameClass() {
		view.show("What class would you like to rename?");
		input = sc.nextLine();
		view.show("What would you like the new name to be?");
		String newName = sc.nextLine();
		int result = editor.renameClass(input, newName);
		if (result == 0) {
			// Success
			view.show("Class " + input + " renamed to " + newName);
		} else if (result == 1){
			// Failed b/c class does not exist
			view.show("Class " + input + " does not exist");
		} else {
			// Failed b/c tried to rename to existing name
			view.show("Tried to rename to existing name");
		}
	}
	
	/**
	 * Gets relationship info from user and returns if action succeeded or failed
	 */
	private void CL_addRelationship() {
		view.show("Would you like to give the relationship a name?(Y for yes)");
		input = sc.nextLine();
		if (input.equalsIgnoreCase("Y")) {
			view.show("Enter the relationship's name");
			input = sc.nextLine();
		} else {
			input = "";
		}
		view.show("Enter the source class");
		String source = sc.nextLine();
		view.show("Enter the destination class");
		String dest = sc.nextLine();
		if (editor.addRelationship(input, source, dest)) {
			view.show("Relationship " + input + " successfully created");
		} else {
			view.show("Relationship " + input + " could not be created");
		}
	}
	
	private void CL_deleteRelationship() {
		view.show("What is the source of the relationship?");
		input = sc.nextLine();
		view.show("What is the destination of the relationship?");
		String dest = sc.nextLine();
		if (editor.deleteRelationship(input, dest)) {
			view.show("Relaionship between " + input + " and " + dest + " deleted");
		} else {
			view.show("Relaionship between " + input + " and " + dest + " could not be deleted");
		}
	}
	
	/**
	 * Gets class and attribute info from user and returns if action succeeded or not
	 */
	private void CL_addAttribute() {
		view.show("What class would you like to add an attribute to?");
		String className = sc.nextLine();
		view.show("What do you want to name the attribute?");
		input = sc.nextLine();
		if (editor.addAttribute(className, input)) {
			// editor.addAttribute succeeded
			view.show("Attribute " + input + " was successfully added to class " + className);
		} else {
			view.show("Attribute " + input + " could not be created");
		}
	}
	
	/**
	 * Gets class and attribute info from user and returns if deletion succeeded
	 */
	private void CL_deleteAttribute() {
		view.show("What class would you like to delete an attribute from?");
		input = sc.nextLine();
		view.show("What is the name fo the attribute you want to delete?");
		String attrName = sc.nextLine();
		if (editor.deleteAttribute(attrName, attrName)) {
			view.show("Attribute " + attrName + " deleted from " + input);
		} else {
			view.show("Attribute could not be deleted");
		}
	}
	
	/**
	 * Gets class and attribute info and new name from user and returns if
	 * operation succeeded
	 */
	private void CL_renameAttribute() {
		view.show("What class do you want to rename an attribute from?");
		input = sc.nextLine();
		view.show("What attribute do you want to rename?");
		String attrName = sc.nextLine();
		view.show("What do you want to rename the attribute to?");
		String newName = sc.nextLine();
		if (editor.renameAttribute(input, attrName, newName)) {
			view.show("Attribute " + attrName + " renamed to " + newName);
		} else {
			view.show("Attribute " + attrName + " could not be renamed");
		}
	}
	
	/**
	 * Initializes the controller that allows user to interact
	 * with UML software
	 */
	public void init() {
        // Value that is always true unless exit command is given
        boolean loop = true;
        
        while(loop) {
        	// Print the basePrompt asking for next command
        	view.show(basePrompt);
        	// Read user input
        	input = sc.nextLine();
        	
        	switch(input.toLowerCase()) {
        	case "help":
        		showHelp();
        		break;
        	case "save":
        		//TODO
        		break;
        	case "load":
        		//TODO
        		break;
        	case "exit":
        		loop = false;
        		break;
        	case "list classes":
        		// Store this as a String and then use view.show(String) to print
        		view.show(model.listClasses());
        		break;
        	case "list class":
        		// Should you print just class names so user doesn't have to memorize everything?
        		CL_listClassInfo();
        		break;
        	case "list relationships":
        		view.show(model.listRelationships());
        		break;
        	case "add class":
        		//TODO
        		CL_addClass();
        		break;
        	case "delete class":
        		//TODO
        		CL_deleteClass();
        		break;
        	case "rename class":
        		//TODO
        		CL_renameClass();
        		break;
        	case "add relationship":
        		//TODO
        		CL_addRelationship();
        		break;
        	case "delete relatonship":
        		//TODO
        		CL_deleteRelationship();
        		break;
        	case "add attribute":
        		//TODO
        		CL_addAttribute();
        		break;
        	case "delete attribute":
        		//TODO
        		CL_deleteAttribute();
        		break;
        	case "rename attribute":
        		//TODO
        		CL_renameAttribute();
        		break;
        	default:
        		view.show("Command not recognized, please try something else");
        		break;
        	}
        	// Reset Variables
        	input = "";
        }
	}
}
