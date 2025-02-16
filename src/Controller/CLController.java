
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
	 * Checks if any classes exist before printing them
	 */
	private void CL_listClasses() {
		if (model.getClassList().size() != 0) {
			view.show(model.listClasses());
		} else {
			view.show("No classes currently exist");
		}
	}
	
	
	/**
	 * Gets the class to be listed and tells user if action failed
	 */
	private void CL_listClassInfo() {
		if (listClassNames()) {
			view.show("What class would you like printed?");
			input = sc.nextLine();
			String classInfo = model.listClassInfo(input);
			if (classInfo.equals("")) {
				// Class does not exist
				view.show("Requested class does not exist");
			} else {
				view.show(classInfo);
			}
		} else {
			view.show("No classes currently exist");
		}
		
	}
	
	/**
	 * Checks if any relationships exist before printing them
	 */
	private void CL_listRelationships() {
		if (model.getRelationshipList().size() != 0) {
			view.show(model.listRelationships());
		} else {
			view.show("No relationships currently exist");
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
	
	/**
	 * Gets class info from user and displays if class was deleted
	 */
	private void CL_deleteClass() {
		if (listClassNames()) {
			view.show("What class would you like to delete?");
			input = sc.nextLine();
			if (editor.deleteClass(input)) {
				view.show("Class " + input + " successfully deleted");
			} else {
				view.show("Class " + input + " could not be deleted");
			}
		} else {
			view.show("No classes currently exist");
		}
	}
	
	/**
	 * Gets class info from user and displays if class was renamed
	 */
	private void CL_renameClass() {
		if (listClassNames()) {
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
			} else if (result == 2) {
				// Failed b/c tried to rename to existing name
				view.show("Tried to rename to existing name");
			} else {
				// Failed b/c newName is invalid
				view.show("Name " + newName + " is invalid");
			}
		} else {
			view.show("No classes currently exist");
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
		if (listClassNames()) {
			view.show("Enter the source class");
			String source = sc.nextLine();
			view.show("Enter the destination class");
			String dest = sc.nextLine();
			if (editor.addRelationship(input, source, dest)) {
				view.show("Relationship " + input + " successfully created");
			} else {
				view.show("Relationship " + input + " could not be created");
			}
		} else {
			view.show("No classes currently exist");
		}
		
	}
	
	/**
	 * Gets relationship info from user and returns if it was deleted
	 */
	private void CL_deleteRelationship() {
		if (listClassNames()) {
			view.show("What is the source of the relationship?");
			input = sc.nextLine();
			view.show("What is the destination of the relationship?");
			String dest = sc.nextLine();
			if (editor.deleteRelationship(input, dest)) {
				view.show("Relaionship between " + input + " and " + dest + " deleted");
			} else {
				view.show("Relaionship between " + input + " and " + dest + " could not be deleted");
			}
		} else {
			view.show("No classes currently exist");
		}
		
	}
	
	/**
	 * Gets class and attribute info from user and returns if action succeeded or not
	 */
	private void CL_addAttribute() {
		if (listClassNames()) {
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
		} else {
			view.show("No classes currently exist");
		}
		
	}
	
	/**
	 * Gets class and attribute info from user and returns if deletion succeeded
	 */
	private void CL_deleteAttribute() {
		if (listClassNames()) {
			view.show("What class would you like to delete an attribute from?");
			input = sc.nextLine();
			if (listAttrNames(input)) {
				view.show("What is the name of the attribute you want to delete?");
				String attrName = sc.nextLine();
				if (editor.deleteAttribute(input, attrName)) {
					view.show("Attribute " + attrName + " deleted from " + input);
				} else {
					view.show("Attribute could not be deleted");
				}
			} else {
				view.show("Class has no attributes");
			}
			
		} else {
			view.show("No classes currently exist");
		}
		
	}
	
	/**
	 * Gets class and attribute info and new name from user and returns if
	 * operation succeeded
	 */
	private void CL_renameAttribute() {
		if (listClassNames()) {
			view.show("What class do you want to rename an attribute from?");
			input = sc.nextLine();
			if (listAttrNames(input)) {
				view.show("What attribute do you want to rename?");
				String attrName = sc.nextLine();
				view.show("What do you want to rename the attribute to?");
				String newName = sc.nextLine();
				if (editor.renameAttribute(input, attrName, newName)) {
					view.show("Attribute " + attrName + " renamed to " + newName);
				} else {
					view.show("Attribute " + attrName + " could not be renamed");
				}
			} else {
				view.show("Class has no attributes");
			}
		} else {
			view.show("No classes currently exist");
		}
		
	}
	
	/**
	 * Helper class to return if any classes have been listed
	 * Used for error catching and avoiding redundant code
	 * @return True if classes have been listed, false otherwise
	 */
	private boolean listClassNames() {
		String classNames = model.listClassNames();
		if (!classNames.equals("")) {
			view.show("Available Classes:\n" + classNames);
			return true;
		}
		return false;
	}
	
	/**
	 * Helper class to return if any attributes have been listed
	 * Used for error catching and avoiding redundant code
	 * @param className		| Name of the class being checked
	 * @return True if attributes have been listed, false otherwise
	 */
	private boolean listAttrNames(String className) {
		String attrNames = model.listAttributes(className);
		if (!attrNames.equals("")) {
			view.show("Available Attributes:\n" + attrNames);
			return true;
		}
		return false;
	}

	private boolean save(){
	    view.show("Where would you like to save:");
	    String path = sc.nextLine();
	    try{
		FileManager file = new FileManager();
		file.save(path,model);
		return true;
	    } catch (Exception e){
		return false;
	    }
	}
	private boolean load(){
	    view.show("Where would you like to load from:");
	    String path = sc.nextLine();
	    try{
		FileManager file = new FileManager();
		model = file.load(path);
		return true;
	    } catch (Exception e){
		return false;
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
        		//TODO
        		break;
        	case "save":
		    	save();
        		break;
        	case "load":
        		load();
        		break;
        	case "exit":
        		loop = false;
        		break;
        	case "list classes":
        		CL_listClasses();
        		break;
        	case "list class":
        		CL_listClassInfo();
        		break;
        	case "list relationships":
        		CL_listRelationships();
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
        	case "delete relationship":
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
        	// Skip a line to break up user actions in the command line
        	view.show("");
        }
	}
}
