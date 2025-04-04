package org.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.FileManager;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.Relationship.Type;
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
			view.show("What class would you like printed?");
			input = sc.nextLine();
			activeClass = model.fetchClass(input);
			view.show(model.listClassInfo(activeClass));
		} catch (Exception e) {
			view.show(e.getMessage());
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
		try {
			view.show("Enter the new class' name: ");
			input = sc.nextLine();
			model.isValidClassName(input);
			editor.addClass(input);
			view.show("Class " + input + " succesfully added");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class info from user and displays if class was deleted
	 */
	private void CL_deleteClass() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to delete?");
			input = sc.nextLine();
			editor.deleteClass(input);
			view.show("Class " + input + " successfully deleted");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class info from user and displays if class was renamed
	 */
	private void CL_renameClass() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to rename?");
			input = sc.nextLine();
			activeClass = model.fetchClass(input);
			view.show("What would you like the new name to be?");
			String newName = sc.nextLine();
			model.isValidClassName(newName);
			editor.renameClass(activeClass, newName);
			view.show("Class " + input + " renamed to " + newName);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets relationship info from user and returns if action succeeded or failed
	 */
	private void CL_addRelationship() {
		try {
			view.show(model.listClassNames());
			view.show("Enter the source class");
			String source = sc.nextLine();
			model.fetchClass(source);
			view.show(model.listClassNames());
			view.show("Enter the destination class");
			String dest = sc.nextLine();
			model.fetchClass(dest);
			if (!model.relationshipExist(source, dest)) {
				// Relationship does not already exist
				String typeint = "0";
				Type type = null;
				while (!(typeint.equals("1") || typeint.equals("2") || typeint.equals("3") || typeint.equals("4"))) {
					view.show("Enter 1-4 to set the type of relationship (1. Aggregation | 2. Composition | 3. Inheritance | 4. Realization)");
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
			} else {
				view.show("Relationship between " + source + " and " + dest + " already exists");
			}
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Enables the editing of existing relationships from the CLI editor
	 * Loops until the
	 */
	private void CL_editRelationship() {
		try {
			view.show(model.listRelationships());
			view.show("What is the source of the relationship are you changing?");
			String source = sc.nextLine();
			model.fetchClass(source);
			view.show(model.listClassNames());
			view.show("What is the destination of the relationship are you changing?");
			String dest = sc.nextLine();
			model.fetchClass(dest);
			if (model.relationshipExist(source, dest)) {
				view.show("What property of the relationship are you changing?\n");
				view.show("You can edit the 'source', 'destination', or 'type' of this relationship.");
				String field = sc.nextLine().toLowerCase();
				String value = null;
				switch (field) {
					case "source":
						try {
							view.show(model.listClassNames());
							view.show("Which class do you want to name as the new source?");
							value = sc.nextLine();
							model.fetchClass(value);
							// Need to check if Relationship w/ new source and current dest
							// already exists and abort if yes
							if (model.relationshipExist(value, dest)) {
								view.show("Relationship between " + value + " and " + dest + " already exists");
								return;
							}
						} catch (Exception e) {
							view.show(e.getMessage());
							return;
						}
						break;
	
					case "destination":
						try {
							view.show(model.listClassNames());
							view.show("What class do you want to name as the new destination?");
							value = sc.nextLine();
							model.fetchClass(value);
							// Need to check if Relationship w/ current source and new dest
							// already exists and abort if yes
							if (model.relationshipExist(source, value)) {
								view.show("Relationship between " + source + " and " + value + " already exists");
								return;
							}
						} catch (Exception e) {
							view.show(e.getMessage());
							return;
						}
						break;
	
					case "type":
						String typeint = "0";
						while (!(typeint.equals("1") || typeint.equals("2") || typeint.equals("3")
								|| typeint.equals("4"))) {
							view.show("Enter 1-4 to set the type of relationship (1. Aggregation | 2. Composition | 3. Inheritance | 4. Realization)");
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
				try {
					if (value != null) {
						editor.editRelationship(source, dest, field, value);
					}
				} catch (Exception e) {
					//Temp Try-Catch block
				}
			} else {
				view.show("Error: Relationship between " + source + " and " + dest + " does not exist! Aborting.");
			}
		} catch (Exception e) {
			view.show(e.getMessage());
			return;
		}
	}

	/**
	 * Gets relationship info from user and returns if it was deleted
	 */
	private void CL_deleteRelationship() {
		try {
			view.show(model.listRelationships());
			view.show("What is the source of the relationship are you deleting?");
			String source = sc.nextLine();
			model.fetchClass(source);
			view.show(model.listClassNames());
			view.show("What is the destination of the relationship are you deleting?");
			String dest = sc.nextLine();
			model.fetchClass(dest);
			model.fetchRelationship(source, dest);
			editor.deleteRelationship(source, dest);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and field info from user and returns if action succeeded or not
	 */
	private void CL_addField() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to add a field to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show("What do you want to name the field?");
			input = sc.nextLine();
			editor.addField(activeClass, input);
			view.show("Field " + input + " successfully added to class " + className);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and field info from user and returns if deletion succeeded
	 */
	private void CL_deleteField() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to delete a field from?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listFields(activeClass));
			view.show("What is the name of the field you want to delete?");
			input = sc.nextLine();
			editor.deleteField(activeClass, input);
			view.show("Field " + input + " successfully deleted from class " + className);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and field info from user as well as what they want to rename it to
	 * Returns whether or not the operation succeeds
	 */
	private void CL_renameField() {
		try {
			view.show(model.listClassNames());
			view.show("What class do you want to rename a field from?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listFields(activeClass));
			view.show("What is the name of the field you want to rename?");
			input = sc.nextLine();
			AttributeInterface renameField = activeClass.fetchField(input);
			view.show("What do you want to rename the field to?");
			String newName = sc.nextLine();
			editor.renameField(activeClass, renameField, newName);
			view.show("Field " + input + " renamed to " + newName);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class, method, and parameters from user and returns if action succeeded
	 * or not
	 */
	private void CL_addMethod() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to add a method to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show("What do you want to name the method?");
			input = sc.nextLine();
			LinkedHashMap<String, String> paramList = new LinkedHashMap<>();
			view.show("Type the name then type of a parameter you'd like to add to this new method (enter to skip)");
			String paramName = sc.nextLine().replaceAll("\\s", "");
			String type;

			boolean empty_input = paramName.equalsIgnoreCase("");
			while (!empty_input) {
				if (paramName.equalsIgnoreCase("stop") || paramName.equals("")) {
					empty_input = true;
					break;
				} else {
					boolean exist = false;
					for (String str : paramList.keySet()) {
						if (paramName.equals(str)) {
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
						view.show("Enter the parameter's type");
						type = sc.nextLine().replaceAll("\\s", "");
						if (type.isEmpty()) {
							// Will restart loop which will immediately come back to this point b/c paramName is still stored
							view.show("Parameters must have a type, please try again");
							continue;
						}
						paramList.put(paramName, type);
						view.show("Parameter " + paramName + " added to method " + input);
					}
				}
				view.show("What would you like to name the next parameter?");
				paramName = sc.nextLine().replaceAll("\\s", "");
			}
			editor.addMethod(activeClass, input, paramList);

			if (paramList.size() == 0) {
				view.show("Method " + input + "() successfully added to class " + className);
			} else {
				int i = 0;
				String message = "Method " + input + "(";
				for (String str : paramList.keySet()) {
					message += str;
					if (i < paramList.size() - 1) {
						message += ", ";
					}
					i++;
				}
				message += ")";
				message += " successfully added to class " + className;
				view.show(message);
			}
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and method info from user and returns if deletion succeeded
	 */
	private void CL_deleteMethod() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to delete a method from?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("What is the name of the method you want to delete?");
			input = sc.nextLine();
			view.show(model.listMethodArities(activeClass, input));
			int paramArity = -1;
			view.show("How many parameters does " + input + " have?");
			boolean validArity = false;
			while (!validArity) {
				if (sc.hasNextInt()) {
					paramArity = sc.nextInt();
					try {
						validArity = model.arityValid(paramArity);
					} catch (Exception e) {
						view.show(e.getMessage());
					}
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}
			editor.deleteMethod(activeClass, input, paramArity);
			view.show("Method " + input + " successfully deleted from class " + className);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and method info from user as well as what they want to rename it
	 * to Returns whether or not the operation succeeds
	 */
	private void CL_renameMethod() {
		try {
			view.show(model.listClassNames());
			view.show("What class do you want to rename a method from?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("What is the name of the method you want to rename?");
			input = sc.nextLine();
			view.show(model.listMethodArities(activeClass, input));
			int paramArity = -1;
			view.show("How many parameters does " + input + " have?");
			boolean validArity = false;
			while (!validArity) {
				if (sc.hasNextInt()) {
					paramArity = sc.nextInt();
					try {
						validArity = model.arityValid(paramArity);
					} catch (Exception e) {
						view.show(e.getMessage());
					}
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}
			Method renameMethod = activeClass.fetchMethod(input, paramArity);
			view.show("What do you want to rename the method to?");
			String newName = sc.nextLine();
			editor.renameMethod(activeClass, renameMethod, newName);
			view.show("Method " + input + " renamed to " + newName);
		} catch (Exception e) {
			view.show(e.getMessage());
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
			view.show("What class does the method you want to add the parameter to belong to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("Please type the name of the method you'd like to add parameters to:");
			String methodName = sc.nextLine();
			view.show(model.listMethodArities(activeClass, methodName));
			int paramArity = -1;
			view.show("How many parameters does " + methodName + " have?");
			boolean validArity = false;
			while (!validArity) {
				if (sc.hasNextInt()) {
					paramArity = sc.nextInt();
					try {
						validArity = model.arityValid(paramArity);
					} catch (Exception e) {
						view.show(e.getMessage());
					}
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}

			Method activeMethod = activeClass.fetchMethod(methodName, paramArity);
			LinkedHashMap<String, String> parameterList = new LinkedHashMap<>();
			boolean loop = true;
			String typeInput = "";
			view.show("Type the name of a parameter you'd like to add (enter to stop)");
			while(loop) {
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
					typeInput = sc.nextLine().replaceAll("\\s", "");
					if(typeInput != "") {
						view.show("This parameter must have a type.");
						view.show("Please type the name of the next parameter:");
						continue;					
					}
					parameterList.put(input, typeInput);
				}
				view.show("Please type the name of the next parameter:");
			}
			editor.addParam(parameterList, activeMethod);
			view.show("The parameter(s) were added");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
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
			view.show("What class does the method you want to remove the parameter from belong to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("Please type the name of the method you'd like to remove parameter(s) from: ");
			String methodName = sc.nextLine();
			view.show(model.listMethodArities(activeClass, methodName));
			int paramArity = -1;
			view.show("How many parameters does " + methodName + " have?");
			boolean validArity = false;
			while (!validArity) {
				if (sc.hasNextInt()) {
					paramArity = sc.nextInt();
					try {
						validArity = model.arityValid(paramArity);
					} catch (Exception e) {
						view.show(e.getMessage());
					}
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}

			Method activeMethod = activeClass.fetchMethod(methodName, paramArity);
			ArrayList<Parameter> paramList = activeMethod.getParamList();
			view.show("Type the name of the parameter you'd like to remove");
			String paramName = sc.nextLine().replaceAll("\\s", "");
			Parameter param = activeMethod.fetchParameter(paramName);
			editor.removeParam(activeMethod, param);
			view.show("Success! Parameter " + paramName + " has been removed.");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	private void CL_removeAllParam() {
		try {
			view.show(model.listClassNames());
			view.show("What class does the method you want to remove the parameter from belong to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("Please type the name of the method you'd like to remove parameter(s) from: ");
			String methodName = sc.nextLine();
			view.show(model.listMethodArities(activeClass, methodName));
			int paramArity = -1;
			view.show("How many parameters does " + methodName + " have?");
			boolean validArity = false;
			while (!validArity) {
				if (sc.hasNextInt()) {
					paramArity = sc.nextInt();
					try {
						validArity = model.arityValid(paramArity);
					} catch (Exception e) {
						view.show(e.getMessage());
					}
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}

			Method activeMethod = activeClass.fetchMethod(methodName, paramArity);
			editor.removeAllParams(activeMethod);
			view.show("All parameters were removed");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/*
	 * Gets the class, and method that the parameters belongs to and asks the user if they'd like to change
	 * one or all of the parameters. If its all parameters it replaces everything after index 0 with a new list.
	 * if its one parameter it replaces everything after the parameter to be changed with a new list of parameters
	 * containing all of the new parameters as well as the old parameters at their locations prior to the change.
	 */
	private void CL_changeParam() {
		try {
			view.show(model.listClassNames());
			view.show("What class does the method you want to add the parameter to belong to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("Please type the name of the method you'd like to add parameters to:");
			String methodName = sc.nextLine();
			view.show(model.listMethodArities(activeClass, methodName));
			int paramArity = -1;
			view.show("How many parameters does " + methodName + " have?");
			boolean validArity = false;
			while (!validArity) {
				if (sc.hasNextInt()) {
					paramArity = sc.nextInt();
					try {
						validArity = model.arityValid(paramArity);
					} catch (Exception e) {
						view.show(e.getMessage());
					}
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}

			Method activeMethod = activeClass.fetchMethod(methodName, paramArity);
			view.show("Type 'All' to replace all of the parameters or type the name of the parameter you'd like to replace:");
			input = sc.nextLine().replaceAll("//s", "");
			Parameter oldParam = null;
			boolean changeAll = false;
			if (input.equalsIgnoreCase("all")) {
				// Changing all parameters
				changeAll = true;
			} else {
				// Changing one parameter
				oldParam = activeMethod.fetchParameter(input);
			}
			LinkedHashMap<String, String> parameterList = new LinkedHashMap<>();
			boolean loop = true;
			boolean changeParamReadded = false;
			String paramName = "";
			String type = "";
			view.show("Type the name then type of a parameter you'd like to add to the new list. (enter to stop):");
			while (loop) {
				// Loops for adding
				paramName = sc.nextLine().replaceAll("\\s", "");
				type = sc.nextLine().replaceAll("\\s", "");
				if (paramName.equalsIgnoreCase("stop") || paramName.equals("")) {
					loop = false;
				} else {
					if (!input.equalsIgnoreCase("all")) {
						// Only need to check if new paramName is in method if all parameters are not
						// being replaced
						if (activeMethod.paramUsed(paramName)) {
							if (!changeParamReadded && paramName.equals(input)) {
								parameterList.put(paramName, type);
								view.show("Please type the name then type of the next parameter:");
								changeParamReadded = true;
								continue;
							}
							view.show("This parameter is already in the method.");
							view.show("Please type the name then type of the next parameter:");
							continue;
						}
					}
					if (editor.nameAlrAdded(paramName, parameterList)) {
						view.show("This parameter has already been added.");
						view.show("Please type the name then type of the next parameter:");
						continue;
					}
					parameterList.put(paramName, type);
				}
				view.show("Please type the name of the next parameter:");
			}
			if (changeAll) {
				editor.changeAllParams(activeMethod, parameterList);
				view.show("All parameters were replaced.");
			} else {
				editor.changeParameter(activeMethod, oldParam, parameterList);
				view.show("Parameter " + oldParam.getName() + " was replaced with new parameter list.");
			}
		} catch (Exception e) {
			view.show(e.getMessage());
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
