package org.Controller;
import java.util.Scanner;
import org.Model.UMLModel;
import org.Model.ClassObject;
import org.View.CLView;
import org.FileManager;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Model.AttributeInterface;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;
import java.util.ArrayList;

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
	// Create a global class that can be used and passed into editor
	ClassObject activeClass = null;
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
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class would you like printed?");
		input = sc.nextLine();
		activeClass = model.fetchClass(input);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + input + " does not exist");
			return;
		}
		String classInfo = model.listClassInfo(activeClass);
		if (classInfo.equals("")) {
			// Class does not exist
			view.show("Requested class does not exist");
		} else {
			view.show(classInfo);
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
		int result = model.isValidClassName(input);
		switch(result) {
		case 1:
			view.show("No class name was given");
			break;
		case 2:
			view.show("Name " + input + " is invalid. First character must be a letter or '_'");
			break;
		case 3:
			view.show("Name " + input + " is invalid. Name can only contain alphanumerics, '_', or '$'");
			break;
		case 4:
			view.show("Name " + input + " is already used by another class");
			break;
		case 0:
		default:
			editor.addClass(input);
			view.show("Class " + input + " succesfully added");
			break;
		}
	}
	
	/**
	 * Gets class info from user and displays if class was deleted
	 */
	private void CL_deleteClass() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class would you like to delete?");
		input = sc.nextLine();
		if (editor.deleteClass(input)) {
			view.show("Class " + input + " successfully deleted");
		} else {
			view.show("Class " + input + " could not be deleted");
		}
	}
	
	/**
	 * Gets class info from user and displays if class was renamed
	 */
	private void CL_renameClass() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class would you like to rename?");
		input = sc.nextLine();
		activeClass = model.fetchClass(input);
		if (activeClass != null) {
			// Class to be renamed exists
			view.show("What would you like the new name to be?");
			String newName = sc.nextLine();
			int result = model.isValidClassName(newName);
			switch (result) {
			case 1:
				view.show("No new class name was given");
				break;
			case 2:
				view.show("Name " + newName + " is invalid. First character must be a letter or '_'");
				break;
			case 3:
				view.show("Name " + newName + " is invalid. Name can only contain alphanumerics, '_', or '$'");
				break;
			case 4:
				view.show("Name " + newName + " is already used by another class");
				break;
			case 0:
			default:
				editor.renameClass(activeClass, newName);
				view.show("Class " + input + " renamed to " + newName);
				break;
			}
		} else {
			// Class to be renamed does not exist
			view.show("Class " + input + " does not exist");
		}
	}
	
	/**
	 * Gets relationship info from user and returns if action succeeded or failed
	 */
	private void CL_addRelationship() {
		if(!listClassNames()){
			view.show("Error: No classes currently exist! Aborting.");
			return;
		}

		view.show("Optionally, input a name for the relationship: (Enter to skip)");
		input = sc.nextLine();
		
			view.show("Enter the source class");
			listClassNames();
			String source = sc.nextLine();
			if (model.fetchClass(source) == null) {
       			view.show("Error: Inputted class " + source + " does not exist! Aborting.");
        		return;
    		}
			view.show("Enter the destination class");
			listClassNames();
			String dest = sc.nextLine();
			if (model.fetchClass(dest) == null) {
       			view.show("Error: Inputted class " + dest + " does not exist! Aborting.");
        		return;
    		}
			if (model.relationshipExist(source, dest) != null) {
				view.show("Error: A relationship already exists between " + source + " and " + dest + "! Aborting.");
				return;
			}

			String typeint = "0";
			Type type = null;
			while(!(typeint.equals("1")||typeint.equals("2")||typeint.equals("3")||typeint.equals("4"))){
				view.show("Enter 1-4 to set the type of relationship (1. Aggregation | 2. Composition | 3. Inheritance | 4. Realization)");
				typeint = sc.nextLine(); 
				if (typeint.equals("1")){ type = Type.AGGREGATION;}
				else if (typeint.equals("2")){ type = Type.COMPOSITION;}
				else if (typeint.equals("3")){ type = Type.INHERITANCE;}
				else if (typeint.equals("4")){ type = Type.REALIZATION;}
				else if (typeint.equalsIgnoreCase("cancel")){return;}
				else {view.show("Invalid input! Try again (or 'cancel' the creation).");}
			}

			if (type!=null && editor.addRelationship(input, source, dest, type)) {
				if(input.equals("")){
					view.show("Unnamed " + type + " Relationship successfully created from " + source + " to " + dest);
				}
				else{
					view.show(type + " Relationship " + input + " successfully created from " + source + " to " + dest);
				}
			} else {
				view.show("Relationship " + input + " could not be created");
			}
	}

	/**
	 * Enables the editing of existing relationships from the CLI editor
	 * Loops until the 
	 */
	private void CL_editRelationship(){
		if (!listRelationshipNames()) {
			view.show("Error: No relationships exist to edit! Aborting");
			return;
		}
			view.show("What is the source of the relationship are you changing?");
			listClassNames();
			String source = sc.nextLine();
			if (model.fetchClass(source) == null) {
       			view.show("Error: Inputted class " + source + " does not exist! Aborting.");
        		return;
    		}
			view.show("What is the destination of the relationship are you changing?");
			listClassNames();
			String dest = sc.nextLine();
			if (model.fetchClass(dest) == null) {
       			view.show("Error: Inputted class " + dest + " does not exist! Aborting.");
        		return;
    		}
			if(model.relationshipExist(source, dest)!=null){
				view.show("What property of the relationship are you changing?\n");
				view.show("You can edit the 'name', 'source', 'destination', or 'type' of this relationship.");
				String field = sc.nextLine().toLowerCase();
				String value = null;
					switch(field){
						
						case "name":
							view.show("What do you want to name the relationship?");
							value = sc.nextLine();
							view.show("Relationship successfully renamed " + value);
							break;

						case "source":
							view.show("Which class do you want to name as the new source?");
							listClassNames();
							value = sc.nextLine();
							if (model.fetchClass(value)!=null) 
								{view.show("Relationship's source successfully set to " + value);}
							else {view.show("Error: No class named " + value + "! Aborting."); return;}
							break;

						case "destination":
							view.show("What class do you want to name as the new destination?");
							listClassNames();
							value = sc.nextLine();
							if(model.fetchClass(value)!=null)
								{view.show("Relationship's destination successfully set to " + value);}
							else {view.show("Error: No class named " + value + "! Aborting."); return;}
							break;

						case "type":
							String typeint = "0";
							while(!(typeint.equals("1")||typeint.equals("2")||typeint.equals("3")||typeint.equals("4"))){
								view.show("Enter 1-4 to set the type of relationship (1. Aggregation | 2. Composition | 3. Inheritance | 4. Realization)");
								typeint = sc.nextLine(); 
								if (typeint.equals("1")){ value = "AGGREGATION";}
								else if (typeint.equals("2")){ value = "COMPOSITION";}
								else if (typeint.equals("3")){ value = "INHERITANCE";}
								else if (typeint.equals("4")){ value = "REALIZATION";}
								else if (typeint.equalsIgnoreCase("cancel")){view.show("Operation canceled by user. Aborting.");return;}
								else {view.show("Invalid input! Try again (or 'cancel' the update).");}
							}
							view.show("Relationship's type successfully set to " + value);
							break;


						default:
							//
							view.show("Unfortunately, we don't support changing the " + field + " of a relationship right now. Aborting.");
							break;
					}
				if(value!=null)
					editor.editRelationship(source, dest, field, value);
			}
			else
			{view.show("Error: Relationship between " + source + " and " + dest + " does not exist! Aborting.");}
		}
	
	
	/**
	 * Gets relationship info from user and returns if it was deleted
	 */
	private void CL_deleteRelationship() {
		if (!listRelationshipNames()) {
			view.show("Error: No relationships exist to delete! Aborting");
			return;
		}
		view.show("What is the source of the relationship?");
		input = sc.nextLine();
		view.show("What is the destination of the relationship?");
		String dest = sc.nextLine();
		if(model.relationshipExist(input, dest)!=null){
			view.show("Error: Relationship between " + input + " and " + dest + " does not exist! Aborting.");
			return;
		}
		if (editor.deleteRelationship(input, dest)) {
			view.show("Relationship between " + input + " and " + dest + " deleted.");
		} else {
			view.show("Relationship between " + input + " and " + dest + " could not be deleted.");
		}
	}

	/**
	 * Gets class and field info from user and returns if action succeeded or not
	 */
	private void CL_addField() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class would you like to add a field to?");
		String className = sc.nextLine();
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + className + " does not exist");
			return;
		}
		view.show("What do you want to name the field?");
		input = sc.nextLine();
		if (activeClass.fetchField(input) == null) {
			// Field does not already exist in class
			editor.addField(activeClass, input);
			view.show("Field " + input + " successfully added to class " + className);
		} else {
			view.show("Field with name " + input + " already exists in class " + className);
		}
	}

	/**
	 * Gets class and field info from user and returns if deletion succeeded
	 */
	private void CL_deleteField() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class would you like to delete a field from?");
		String className = sc.nextLine();
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + className + " does not exist");
			return;
		}
		if (!listFieldNames(activeClass)) {
			view.show("Class has no fields");
			return;
		}
		view.show("What is the name of the field you want to delete?");
		input = sc.nextLine();
		AttributeInterface delField = activeClass.fetchField(input);
		if (delField != null) {
			// The field specified exists
			editor.deleteAttribute(activeClass, delField);
			view.show("Field " + input + " successfully deleted from class " + className);
		} else {
			// The field does not exist
			view.show("Class " + className + " does not have a field named " + input);
		}
	}

	/**
	 * Gets class and field info from user as well as what they want to rename it to
	 * Returns whether or not the operation succeeds
	 */
	private void CL_renameField() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class do you want to rename a field from?");
		String className = sc.nextLine();
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + className + " does not exist");
			return;
		}
		if (!listFieldNames(activeClass)) {
			view.show("Class has no fields");
			return;
		}
		view.show("What is the name of the field you want to rename?");
		input = sc.nextLine();
		AttributeInterface renameField = activeClass.fetchField(input);
		if (renameField != null) {
			// The method does exist
			view.show("What do you want to rename the field to?");
			String newName = sc.nextLine();
			if (activeClass.fetchField(newName) == null) {
				// The newName is not in use
				editor.renameAttribute(renameField, newName);
				view.show("Field " + input + " renamed to " + newName);
			} else {
				view.show(newName + " is currently used by another field in " + className);
			}
		} else {
			// The method does not exist
			view.show("Class " + className + " does not have a field named " + input);
		}
	}

	/**
	 * Gets class, method, and parameters from user and returns if action succeeded
	 * or not
	 */
	private void CL_addMethod() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class would you like to add a method to?");
		String className = sc.nextLine();
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + className + " does not exist");
			return;
		}
		// Class exists, get Method name and params
		view.show("What do you want to name the method?");
		input = sc.nextLine();
		ArrayList<Parameter> paramList = new ArrayList<>();
		view.show("Would you like to add parameters to this method? (Y for yes)");
		if (sc.nextLine().replaceAll("\\s", "").equalsIgnoreCase("Y")) {
			boolean loop = true;
			String paramName = "";
			Parameter param;
			view.show("What would you like to name the parameter? Type 'stop' to stop adding parameters");
			while (loop) {
				view.show("What would you like to name the next parameter?");
				paramName = sc.nextLine().replaceAll("\\s", "");
				if (paramName.equalsIgnoreCase("stop")) {
					loop = false;
				} else {
					boolean exist = false;
					for (int i = 0; i < paramList.size(); i++) {
						if (paramName.equals(paramList.get(i).getName())) {
							// Parameter name has already been added
							exist = true;
							break;
						} else {
							// Do nothing
						}
					}
					if (exist) {
						view.show("Parameter " + paramName + " has already been added");
					} else {
						param = new Parameter(paramName);
						paramList.add(param);
						view.show("Parameter " + paramName + " added to method " + input);
					}
				}
			}
		}
		if (activeClass.fetchMethod(input, paramList.size()) != null) {
			// Method with same name and # of parameters already exists
			view.show("Method with name " + input + " and parameter arity " + paramList.size() + " already exists");
		} else {
			// Method with same name and # of parameters does not exist
			editor.addMethod(activeClass, input, paramList);
			view.show("Method " + input + " successfully added to class " + className);
		}
	}

	/**
	 * Gets class and method info from user and returns if deletion succeeded
	 */
	private void CL_deleteMethod() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class would you like to delete a method from?");
		String className = sc.nextLine();
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + className + " does not exist");
			return;
		}
		if (!listMethodNames(activeClass)) {
			view.show("Class has no methods");
			return;
		}
		view.show("What is the name of the method you want to delete?");
		input = sc.nextLine();
		int paramArity = -1;
		view.show("How many parameters does " + input + " have?");
		if (sc.hasNextInt()) {
			paramArity = sc.nextInt();
			if (paramArity < 0) {
				view.show("Parameter arity must be non-negative");
				return;
			}
			// Consume newLine char left by nextInt
			sc.nextLine();
		} else {
			view.show("Invalud input. Please enter a number");
			// Clear invalid input from buffer
			sc.nextLine();
			return;
		}
		Method delMethod = activeClass.fetchMethod(input, paramArity);
		if (delMethod != null) {
			// The method specified exists
			editor.deleteAttribute(activeClass, delMethod);
			view.show("Method " + input + " successfully deleted from class " + className);
		} else {
			// The method does not exist
			view.show("Class " + className + " does not have a method named " + input);
		}
	}

	/**
	 * Gets class and method info from user as well as what they want to rename it
	 * to Returns whether or not the operation succeeds
	 */
	private void CL_renameMethod() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
		view.show("What class do you want to rename a method from?");
		String className = sc.nextLine();
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + className + " does not exist");
			return;
		}
		if (!listMethodNames(activeClass)) {
			view.show("Class has no methods");
			return;
		}
		view.show("What is the name of the method you want to rename?");
		input = sc.nextLine();
		int paramArity = -1;
		view.show("How many parameters does " + input + " have?");
		if (sc.hasNextInt()) {
			paramArity = sc.nextInt();
			if (paramArity < 0) {
				view.show("Parameter arity must be non-negative");
				return;
			}
			// Consume newLine char left by nextInt
			sc.nextLine();
		} else {
			view.show("Invalud input. Please enter a number");
			// Clear invalid input from buffer
			sc.nextLine();
			return;
		}
		Method renameMethod = activeClass.fetchMethod(input, paramArity);
		if (renameMethod != null) {
			// The method does exist
			view.show("What do you want to rename the method to?");
			String newName = sc.nextLine();
			if (activeClass.fetchMethod(newName, paramArity) == null) {
				// The newName is not in use
				editor.renameAttribute(renameMethod, newName);
				view.show("Method " + input + " renamed to " + newName);
			} else {
				view.show(newName + " is currently used by another method in " + className);
			}
		} else {
			// The method does not exist
			view.show("Class " + className + " does not have a method named " + input + " with " + paramArity
					+ " parameters");
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
	 * Helper class to return if any relationships have been created
	 * Used for error catching and avoiding redundant code
	 * @return True if relationships have been listed, false otherwise
	 */
	private boolean listRelationshipNames() {
		String relNames = model.listRelationships();
		if (!relNames.equals("Relationships:")) {
			view.show("Existing Relationships:\n" + relNames);
			return true;
		}
		return false;
	}
	
	/**
	 * Helper function to return if any fields have been listed
	 * 
	 * @param cls | The class whose fields are being listed
	 * @return True if fields have been listed, false otherwise
	 */
	private boolean listFieldNames(ClassObject cls) {
		String fieldNames = model.listFields(cls);
		if (!fieldNames.equals("")) {
			view.show("Available Fields:\n" + fieldNames);
			return true;
		}
		return false;
	}

	/**
	 * Helper function to return if any methods have been listed
	 * 
	 * @param cls | The class whose methods are being listed
	 * @return True if methods have been listed, false otherwise
	 */
	private boolean listMethodNames(ClassObject cls) {
		String methodNames = model.listMethods(cls);
		if (!methodNames.equals("")) {
			view.show("Available Methods:\n" + methodNames);
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
			if(!sc.hasNextLine())
		    	continue;
        	input = sc.nextLine();
        	
        	switch(input.toLowerCase().replaceAll("\\s", "")) {
        	case "help":
        	case "h":
				view.showHelp();
        		break;
        	case "save":
		    	save();
        		break;
        	case "load":
        		load();
        		break;
        	case "exit":
			case "q":
        		loop = false;
        		break;
        	case "listclasses":
			case "lcs":
        		CL_listClasses();
        		break;
        	case "listclass":
			case "lc":
        		CL_listClassInfo();
        		break;
        	case "listrelationships":
			case "lr":
        		CL_listRelationships();
        		break;
        	case "addclass":
        	case "ac":
        		CL_addClass();
        		break;
        	case "deleteclass":
        	case "dc":
        		CL_deleteClass();
        		break;
        	case "renameclass":
        	case "rc":
        		CL_renameClass();
        		break;
        	case "addrelationship":
        	case "ar":
        		CL_addRelationship();
        		break;
        	case "deleterelationship":
        	case "dr":
        		CL_deleteRelationship();
        		break;
			case "editrelationship":
			case "er":
				CL_editRelationship();
				break;
        	case "addfield":
        	case "af":
        		CL_addField();
        		break;
        	case "deletefield":
        	case "df":
        		CL_deleteField();
        		break;
        	case "renamefield":
        	case "rf":
        		CL_renameField();
        		break;
			case "addmethod":
			case "am":
				CL_addMethod();
				break;
			case "deletemethod":
			case "dm":
				CL_deleteMethod();
				break;
			case "renamemethod":
			case "rm":
				CL_renameMethod();
				break;
        	default:
        		view.show("Command not recognized, please try something else");
        		break;
        	}
        	// Reset Variables
        	input = "";
			activeClass = null;
        	// Skip a line to break up user actions in the command line
        	view.show("");
        }
	}
}
