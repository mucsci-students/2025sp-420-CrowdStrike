package org.Controller;

import java.util.ArrayList;
import java.util.Scanner;

import org.FileManager;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Field;
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listRelationships());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}

		view.show("Enter the source class");
		String source = sc.nextLine();
		if (model.fetchClass(source) == null) {
			view.show("Error: Inputted class " + source + " does not exist! Aborting.");
			return;
		}

		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("Enter the destination class");
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
		while (!(typeint.equals("1") || typeint.equals("2") || typeint.equals("3") || typeint.equals("4"))) {
			view.show(
					"Enter 1-4 to set the type of relationship (1. Aggregation | 2. Composition | 3. Inheritance | 4. Realization)");
			typeint = sc.nextLine();
			if (typeint.equals("1")) {
				type = Type.AGGREGATION;
			} else if (typeint.equals("2")) {
				type = Type.COMPOSITION;
			} else if (typeint.equals("3")) {
				type = Type.INHERITANCE;
			} else if (typeint.equals("4")) {
				type = Type.REALIZATION;
			} else if (typeint.equalsIgnoreCase("cancel")) {
				return;
			} else {
				view.show("Invalid input! Try again (or 'cancel' the creation).");
			}
		}

		if (type != null && editor.addRelationship(source, dest, type)) {
			view.show(type + " Relationship successfully created from " + source + " to " + dest);
		} else {
			view.show("Relationship could not be created");
		}
	}

	/**
	 * Enables the editing of existing relationships from the CLI editor
	 * Loops until the
	 */
	private void CL_editRelationship() {
		try {
			view.show(model.listRelationships());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		// Because we are listing relationships listing classes after feels redundant
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What is the source of the relationship are you changing?");
		String source = sc.nextLine();
		if (model.fetchClass(source) == null) {
			view.show("Error: Inputted class " + source + " does not exist! Aborting.");
			return;
		}
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What is the destination of the relationship are you changing?");
		String dest = sc.nextLine();
		if (model.fetchClass(dest) == null) {
			view.show("Error: Inputted class " + dest + " does not exist! Aborting.");
			return;
		}
		if (model.relationshipExist(source, dest) != null) {
			view.show("What property of the relationship are you changing?\n");
			view.show("You can edit the 'source', 'destination', or 'type' of this relationship.");
			String field = sc.nextLine().toLowerCase();
			String value = null;
			switch (field) {
				case "source":
					try {
						view.show(model.listClassNames());
					} catch (Exception e) {
						view.show(e.getMessage());
						return;
					}
					view.show("Which class do you want to name as the new source?");
					value = sc.nextLine();
					if (model.fetchClass(value) != null) {
						view.show("Relationship's source successfully set to " + value);
					} else {
						view.show("Error: No class named " + value + "! Aborting.");
						return;
					}
					break;

				case "destination":
					try {
						view.show(model.listClassNames());
					} catch (Exception e) {
						view.show(e.getMessage());
						return;
					}
					view.show("What class do you want to name as the new destination?");
					value = sc.nextLine();
					if (model.fetchClass(value) != null) {
						view.show("Relationship's destination successfully set to " + value);
					} else {
						view.show("Error: No class named " + value + "! Aborting.");
						return;
					}
					break;

				case "type":
					String typeint = "0";
					while (!(typeint.equals("1") || typeint.equals("2") || typeint.equals("3")
							|| typeint.equals("4"))) {
						view.show(
								"Enter 1-4 to set the type of relationship (1. Aggregation | 2. Composition | 3. Inheritance | 4. Realization)");
						typeint = sc.nextLine();
						if (typeint.equals("1")) {
							value = "AGGREGATION";
						} else if (typeint.equals("2")) {
							value = "COMPOSITION";
						} else if (typeint.equals("3")) {
							value = "INHERITANCE";
						} else if (typeint.equals("4")) {
							value = "REALIZATION";
						} else if (typeint.equalsIgnoreCase("cancel")) {
							view.show("Operation canceled by user. Aborting.");
							return;
						} else {
							view.show("Invalid input! Try again (or 'cancel' the update).");
						}
					}
					view.show("Relationship's type successfully set to " + value);
					break;
				default:
					view.show("Unfortunately, we don't support changing the " + field
							+ " of a relationship right now. Aborting.");
					break;
			}
			if (value != null)
				editor.editRelationship(source, dest, field, value);
		} else {
			view.show("Error: Relationship between " + source + " and " + dest + " does not exist! Aborting.");
		}
	}

	/**
	 * Gets relationship info from user and returns if it was deleted
	 */
	private void CL_deleteRelationship() {
		try {
			view.show(model.listRelationships());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What is the source of the relationship are you deleting?");
		String source = sc.nextLine();
		if (model.fetchClass(source) == null) {
			view.show("Error: Inputted class " + source + " does not exist! Aborting.");
			return;
		}
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What is the destination of the relationship are you deleting?");
		String dest = sc.nextLine();
		if (model.fetchClass(dest) == null) {
			view.show("Error: Inputted class " + dest + " does not exist! Aborting.");
			return;
		}
		if (model.relationshipExist(source, dest) == null) {
			view.show("Error: Relationship between " + source + " and " + dest + " does not exist! Aborting.");
			return;
		}
		if (editor.deleteRelationship(source, dest)) {
			view.show("Relationship between " + source + " and " + dest + " deleted.");
		} else {
			view.show("Relationship between " + source + " and " + dest + " could not be deleted.");
		}
	}

	/**
	 * Gets class and field info from user and returns if action succeeded or not
	 */
	private void CL_addField() {
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What class would you like to add a field to?");
		String className = sc.nextLine();
		// Error throw will be added to fetchClass later
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class does not exist
			view.show("Class " + className + " does not exist");
			return;
		}
		view.show("What do you want to name the field?");
		input = sc.nextLine();
		// Duplication check will be done in addField method later
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listFields(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listFields(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		ArrayList<String> paramList = new ArrayList<>();
		view.show("Type the name of a parameter you'd like to add to this new method (enter to skip)");
		String paramName = sc.nextLine().replaceAll("\\s", "");

		boolean empty_input = paramName.equalsIgnoreCase("");
		while (!empty_input) {
			if (paramName.equalsIgnoreCase("stop") || paramName.equals("")) {
				empty_input = true;
				break;
			} else {
				boolean exist = false;
				for (int i = 0; i < paramList.size(); i++) {
					if (paramName.equals(paramList.get(i))) {
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
					paramList.add(paramName);
					view.show("Parameter " + paramName + " added to method " + input);
				}
			}
			view.show("What would you like to name the next parameter?");
			paramName = sc.nextLine().replaceAll("\\s", "");
		}

		if (activeClass.fetchMethod(input, paramList.size()) != null) {
			// Method with same name and # of parameters already exists
			view.show("Method with name " + input + " and parameter arity " + paramList.size() + " already exists");
		} else {
			// Method with same name and # of parameters does not exist
			editor.addMethod(activeClass, input, paramList);
			if (paramList.size() == 0) {
				view.show("Method " + input + "() successfully added to class " + className);
			} else {
				String message = "Method " + input + "(";
				for (int i = 0; i < paramList.size() - 1; i++) {
					message += paramList.get(i) + ", ";
				}
				message += paramList.get(paramList.size() - 1) + ")";
				message += " successfully added to class " + className;
				view.show(message);
			}
		}
	}

	/**
	 * Gets class and method info from user and returns if deletion succeeded
	 */
	private void CL_deleteMethod() {
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listMethods(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What is the name of the method you want to delete?");
		input = sc.nextLine();
		try {
			view.show(model.listMethodArities(activeClass, input));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		int paramArity = -1;
		view.show("How many parameters does " + input + " have?");
		boolean validArity = false;
		while (!validArity) {
			if (sc.hasNextInt()) {
				paramArity = sc.nextInt();
				if (paramArity < 0) {
					view.show("Parameter arity must be non-negative.");
					return;
				}
				// Consume newLine char left by nextInt
				sc.nextLine();
				validArity = true;
			} else {
				// Clear invalid input from buffer
				sc.nextLine();
				view.show("Invalid input. Please enter a number:");
				paramArity = sc.nextInt();
			}
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
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listMethods(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What is the name of the method you want to rename?");
		input = sc.nextLine();
		try {
			view.show(model.listMethodArities(activeClass, input));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		int paramArity = -1;
		view.show("How many parameters does " + input + " have?");
		if (sc.hasNextInt()) {
			paramArity = sc.nextInt();
			if (paramArity < 0) {
				view.show("Parameter arity must be non-negative");
				// Consume newLine char left by nextInt
				sc.nextLine();
				return;
			}
			// Consume newLine char left by nextInt
			sc.nextLine();
		} else {
			view.show("Invalid input. Please enter a number");
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

	/*
	 * Gets a class, a method, and the names of parameters that a user would like to
	 * add
	 * to the list of parameters attached to the method.
	 */
	private void CL_addParam() {
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show(model.listMethods(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("Please type the name of the method you'd like to add parameters to:");
		String methodName = sc.nextLine();
		try {
			view.show(model.listMethodArities(activeClass, methodName));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
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
			view.show("Invalid input. Please enter a number");
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
		view.show(
				"Type the name of a parameter you'd like to add to the list. Type 'stop' or press enter to stop adding parameters:");
		while (loop) {
			// Loops for adding
			input = sc.nextLine().replaceAll("\\s", "");
			if (input.equalsIgnoreCase("stop") || input.equals("")) {
				loop = false;
			} else {
				if (activeMethod.paramUsed(input)) {
					view.show("This parameter is already in the method.");
					view.show("Please type the name of the next parameter:");
					continue;
				}
				if (editor.nameAlrAdded(input, parameterList)) {
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

	/*
	 * Gets the class, method, and either all of the name of a parameter that the
	 * user
	 * would like to remove. If all every parameter is deleted, otherwise its just
	 * the named parameter.
	 */
	private void CL_removeParam() {
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What class does the method you want to remove the parameter from belong to?");
		String className = sc.nextLine();
		ClassObject activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// class doesn't exist
			view.show("The class " + className + " does not exist.");
			return;
		}
		try {
			view.show(model.listMethods(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("Please type the name of the method you'd like to remove parameter(s) from: ");
		String methodName = sc.nextLine();
		int paramArity = -1;
		view.show("How many parameters does " + methodName + " have?");
		boolean validArity = false;
		while (!validArity) {
			validArity = sc.hasNextInt();
			if (validArity) {
				paramArity = sc.nextInt();
				if (paramArity < 0) {
					view.show("Parameter arity must be non-negative.");
					return;
				}
				// Consume newLine char left by nextInt
				sc.nextLine();
			} else {

				// Clear invalid input from buffer
				sc.nextLine();
				view.show("Invalid input. Please enter a number:");
			}
		}
		Method attr = activeClass.fetchMethod(methodName, paramArity);

		if (attr == null) {
			view.show("Error: Method " + methodName + " does not exist! Aborting. ");
			return;
		}
		// The method exists
		Method activeMethod = (Method) attr;
		ArrayList<Parameter> paramList = activeMethod.getParamList();

		view.show("Type the name of a parameter you'd like to remove from this method (enter to stop)");
		String paramName = sc.nextLine().replaceAll("\\s", "");

		boolean empty_input = paramName.equalsIgnoreCase("");
		while (!empty_input) {
			if (paramName.equalsIgnoreCase("stop") || paramName.equals("")) {
				empty_input = true;
				break;
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
					Parameter param = activeMethod.fetchParameter(paramName);
					editor.removeParam(activeMethod, param);
					view.show("Success! Parameter " + paramName + " has been removed.");
				} else {
					view.show("Error: Parameter " + paramName + " does not exist in method " + input);
				}
			}
			view.show("What parameter would you like to remove next?");
			paramName = sc.nextLine().replaceAll("\\s", "");
		}

	}

	private void CL_removeAllParam() {
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What class does the method you want to remove the parameter from belong to?");
		String className = sc.nextLine();
		ClassObject activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// class doesn't exist
			view.show("The class " + className + " does not exist.");
			return;
		}
		// The class exists
		try {
			view.show(model.listMethods(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("Please type the name of the method you'd like to remove parameter(s) from: ");
		String methodName = sc.nextLine();
		int paramArity = -1;
		view.show("How many parameters does " + methodName + " have?");
		boolean validArity = false;
		while (!validArity) {
			if (sc.hasNextInt()) {
				paramArity = sc.nextInt();
				if (paramArity < 0) {
					view.show("Parameter arity must be non-negative.");
					return;
				}
				// Consume newLine char left by nextInt
				sc.nextLine();
				validArity = true;
			} else {

				// Clear invalid input from buffer
				sc.nextLine();
				view.show("Invalid input. Please enter a number:");
				paramArity = sc.nextInt();
			}
		}
		Method attr = activeClass.fetchMethod(methodName, paramArity);

		if (attr == null) {
			view.show("method not here");
			return;
		}
		// The method exists
		Method activeMethod = (Method) attr;
		editor.removeAllParams(activeMethod);
		view.show("All parameters were removed");
	}

	/*
	 * Gets the class, and method that the parameters belongs to and asks the user
	 * if they'd like to change
	 * one or all of the parameters. If its all parameters it replaces everything
	 * after index 0 with a new list.
	 * if its one parameter it replaces everything after the parameter to be changed
	 * with a new list of parameters
	 * containing all of the new parameters as well as the old parameters at their
	 * locations prior to the change.
	 */
	private void CL_changeParam() {
		try {
			view.show(model.listClassNames());
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("What class does the method you want to add the parameter to belong to?");
		String className = sc.nextLine();
		ClassObject activeClass = model.fetchClass(className);
		if (activeClass == null) {
			view.show("Class not here :(");
			return;
		}
		try {
			view.show(model.listMethods(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
		view.show("Please type the name of the method you'd like to add parameters to:");
		String methodName = sc.nextLine();
		try {
			view.show(model.listMethodArities(activeClass, methodName));
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
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
			view.show("Invalid input. Please enter a number");
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
		if (!input.equalsIgnoreCase("all") && oldParam == null) {
			view.show("You did not type 'All' or the name of an existing parameter.");
			view.show("Function not executed.");
			return;

		}
		ArrayList<String> parameterList = new ArrayList<>();
		boolean loop = true;
		boolean changeParamReadded = false;
		String paramName = "";
		view.show(
				"Type the name of a parameter you'd like to add to the new list. Type 'stop' to stop adding parameters:");
		while (loop) {
			// Loops for adding
			paramName = sc.nextLine().replaceAll("\\s", "");
			if (paramName.equalsIgnoreCase("stop")) {
				loop = false;
			} else {
				if (!input.equalsIgnoreCase("all")) {
					// Only need to check if new paramName is in method if all parameters are not
					// being replaced
					if (activeMethod.paramUsed(paramName)) {
						if (!changeParamReadded && paramName.equals(input)) {
							parameterList.add(paramName);
							view.show("Please type the name of the next parameter:");
							changeParamReadded = true;
							continue;
						}
						view.show("This parameter is already in the method.");
						view.show("Please type the name of the next parameter:");
						continue;
					}
				}
				if (editor.nameAlrAdded(paramName, parameterList)) {
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

	private void save() {
		view.show("Where would you like to save:");
		String path = sc.nextLine();
		try {
			FileManager file = new FileManager();
			file.save(path, model);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	private void load() {
		view.show("Where would you like to load from:");
		String path = sc.nextLine();
		try {
			FileManager file = new FileManager();
			model = file.load(path);
			editor = new UMLEditor(model);
		} catch (Exception e) {
			view.show(e.getMessage());
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
				case "addparameter":
				case "ap":
					CL_addParam();
					break;
				case "removeparameter":
				case "rp":
					CL_removeParam();
					break;
				case "removeallparameters":
				case "rap":
					CL_removeAllParam();
					break;
				case "changeparameter":
				case "cp":
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
