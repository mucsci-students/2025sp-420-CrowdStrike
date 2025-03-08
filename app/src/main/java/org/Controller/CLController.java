package org.Controller;

import java.util.ArrayList;
import java.util.Scanner;

import org.FileManager;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.UMLModel;
import org.View.CLView;

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
	 * 
	 * @param model  | Contains lists of classes and relationships and accessors for
	 *               those lists
	 * @param editor | Allows user to edit the lists i the model
	 * @param view   | Determines how the UML system will be displayed
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
		switch (result) {
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
		if ((model.listClassNames().equals(""))) {
			view.show("No classes currently exist");
			return;
		}
		view.show("Would you like to give the relationship a name?(Y for yes)");
		input = sc.nextLine();
		if (input.equalsIgnoreCase("Y")) {
			view.show("Enter the relationship's name");
			input = sc.nextLine();
		} else {
			input = "";
		}
		listClassNames();
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

	/**
	 * Gets relationship info from user and returns if it was deleted
	 */
	private void CL_deleteRelationship() {
		if (!listClassNames()) {
			view.show("No classes currently exist");
			return;
		}
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

	/*Gets a class, a method, and the names of parameters that a user would like to add
	* to the list of parameters attached to the method.
	*/
	private void CL_addParam() {
		if (!listClassNames()) {
			// no class exists
			view.show("No classes currently exist");
			return;
		}
		view.show("What class does the method you want to add the parameter to belong to?");
		String className = sc.nextLine();
		ClassObject activeClass = model.fetchClass(className);

		if (activeClass == null) {
			// class doesn't exist
			view.show("Class not here :(");
			return;
		}

		// The class exists
		view.show("Please type the name of the method you'd like to add parameters to:");
		String methodName = sc.nextLine();
		int paramArity = -1;
		view.show("How many parameters does " + methodName + " have?");
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
		Method attr = activeClass.fetchMethod(methodName, paramArity);
		if (attr == null) {
			view.show("no Method by this name exists");
			return;
		}
		// The method does exist
		Method activeMethod = (Method) attr;
		ArrayList<String> parameterList = new ArrayList<>();
		ArrayList<Parameter> listOfParameters = new ArrayList<>();
		boolean loop = true;
		view.show("Type the name of a parameter you'd like to add to the list. Type 'stop' to stop adding parameters:");
		while (loop) {
			// Loops for adding
			input = sc.nextLine().replaceAll("\\s", "");
			if (input.equalsIgnoreCase("stop")) {
				loop = false;
			} else {
				if(activeMethod.paramUsed(input) ) {
					view.show("This parameter is already in the method.");
					view.show("Please type the name of the next parameter:");
					continue;
				}
				if(editor.nameAlrAdded(input, parameterList)) {
					view.show("This parameter has already been added.");
					view.show("Please type the name of the next parameter:");
					continue;
				}
				
				parameterList.add(input);
			}
			view.show("Please type the name of the next parameter:");
		}
		editor.addParam(parameterList, activeMethod);
		view.show("The parameter(s) were added.");

	}
	/*Gets the class, method, and either all of the name of a parameter that the user
	* would like to remove. If all every parameter is deleted, otherwise its just the named parameter.
	*/
	private void CL_removeParam() {
		if (!listClassNames()) {
			// no classes
			view.show("No classes currently exist");
			return;

		}
		view.show("What class does the method you want to remove the parameter to belong to?");
		String className = sc.nextLine();
		ClassObject activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// class doesn't exist
			view.show("The class " + className + " does not exist.");
			return;
		}
		// The class exists
		view.show("Please type the name of the method you'd like to remove parameter(s) from: ");
		String methodName = sc.nextLine();
		
		int paramArity = -1;
		view.show("How many parameters does " + methodName + " have?");
		if (sc.hasNextInt()) {
			paramArity = sc.nextInt();
			if (paramArity < 0) {
				view.show("Parameter arity must be non-negative.");
				return;
			}
			// Consume newLine char left by nextInt
			sc.nextLine();
		} else {
			view.show("Invalid input. Please enter a number:");
			// Clear invalid input from buffer
			sc.nextLine();
			return;
		}
		Method attr = activeClass.fetchMethod(methodName, paramArity);
		
		if (attr == null) {
			view.show("method not here");
			return;
		}
		// The method exists
		Method activeMethod = (Method) attr;
		view.show("In order to delete all methods type 'all', otherwise type anything to remove one parameter.");
		input = sc.nextLine().replaceAll("\\s", "");
		if (input.equalsIgnoreCase("all")) {
			// all params gone
			editor.removeAllParams(activeMethod);
			view.show("All parameters were removed");
		} else {
			view.show("please type the parameter name you'd like to remove: ");
			input = sc.nextLine();
			Parameter param = activeMethod.fetchParameter(input);
			if(param == null){
				view.show("The current method does not contain the parameter " + input + ".");
				return;
			}
			
			editor.removeParam(activeMethod, param);
			view.show(input + " was removed.");
		}
	}
	/*Gets the class, and method that the parameters belongs to and asks the user if they'd like to change
	* one or all of the parameters. If its all parameters it replaces everything after index 0 with a new list.
	* if its one parameter it replaces everything after the parameter to be changed with a new list of parameters
	* containing all of the new parameters as well as the old parameters at their locations prior to the change. 
	*/
	private void CL_changeParam() {
		if (!listClassNames()) {
			// no classes
			view.show("No classes currently exist.");
			return;
		}
		view.show("What class does the method you want to add the parameter to belong to?");
		String className = sc.nextLine();
		ClassObject activeClass = model.fetchClass(className);
		if (activeClass == null) {
			view.show("Class not here :(");
			return;
		}
		// The class exists
		view.show("Please type the name of the method you'd like to add parameters to:");
		String methodName = sc.nextLine();
		
		int paramArity = -1;
		view.show("How many parameters does " + methodName + " have?");
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
		Method attr = activeClass.fetchMethod(methodName, paramArity);
		if (attr == null) {
			view.show("method not here");
			return;
		}

		// The method exists
		Method activeMethod = (Method) attr;
		view.show(
				"Type 'All' to replace all of the parameters or type the name of the parameter you'd like to replace:");
		input = sc.nextLine().replaceAll("//s", "");
		Parameter oldParam = activeMethod.fetchParameter(input);
		if (!input.equalsIgnoreCase("all") &&  oldParam == null ) {
			view.show("You did not type 'All' or the name of an existing parameter.");
			view.show("Function not executed.");
			return;
			
		}
		ArrayList<String> parameterList = new ArrayList<>();
		boolean loop = true;
		String paramName = "";
		view.show("Type the name of a parameter you'd like to add to the new list. Type 'stop' to stop adding parameters:");
		while (loop) {
			// Loops for adding
			paramName = sc.nextLine().replaceAll("\\s", "");
			if (paramName.equalsIgnoreCase("stop")) {
				loop = false;
			} else {
				if(activeMethod.paramUsed(paramName) ) {
					view.show("This parameter is already in the method.");
					view.show("Please type the name of the next parameter:");
					continue;
				}
				if(editor.nameAlrAdded(paramName, parameterList)) {
					view.show("This parameter has already been added.");
					view.show("Please type the name of the next parameter:");
					continue;
				}
				
				parameterList.add(paramName);
			}
			view.show("Please type the name of the next parameter:");
		}

		if (input.equalsIgnoreCase("all")) {
			editor.changeAllParams(activeMethod, parameterList);
			view.show("All parameters were replaced.");
		} else {
			editor.changeParameter(activeMethod, oldParam, parameterList);
			view.show("parameter " + oldParam.getName() + " was replaced with new parameter list.");
		}
	}

	/**
	 * Helper class to return if any classes have been listed Used for error
	 * catching and avoiding redundant code
	 * 
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


	private boolean save() {
		view.show("Where would you like to save:");
		String path = sc.nextLine();
		try {
			FileManager file = new FileManager();
			file.save(path, model);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean load() {
		view.show("Where would you like to load from:");
		String path = sc.nextLine();
		try {
			FileManager file = new FileManager();
			model = file.load(path);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Initializes the controller that allows user to interact with UML software
	 */
	public void init() {
		// Value that is always true unless exit command is given
		boolean loop = true;

		while (loop) {
			// Print the basePrompt asking for next command
			view.show(basePrompt);
			// Read user input
			if (!sc.hasNextLine())
				continue;
			input = sc.nextLine();

			switch (input.toLowerCase().replaceAll("\\s", "")) {
			case "help":
				view.showHelp();
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
			case "listclasses":
				CL_listClasses();
				break;
			case "listclass":
				CL_listClassInfo();
				break;
			case "listrelationships":
				CL_listRelationships();
				break;
			case "addclass":
				CL_addClass();
				break;
			case "deleteclass":
				CL_deleteClass();
				break;
			case "renameclass":
				CL_renameClass();
				break;
			case "addrelationship":
				CL_addRelationship();
				break;
			case "deleterelationship":
				CL_deleteRelationship();
				break;
			case "addfield":
				CL_addField();
				break;
			case "deletefield":
				CL_deleteField();
				break;
			case "renamefield":
				CL_renameField();
				break;
			case "addmethod":
				CL_addMethod();
				break;
			case "deletemethod":
				CL_deleteMethod();
				break;
			case "renamemethod":
				CL_renameMethod();
				break;
			case "addparameter":
				CL_addParam();
				break;
			case "removeparameter":
				CL_removeParam();
				break;
			case "changeparameter":
				CL_changeParam();
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
