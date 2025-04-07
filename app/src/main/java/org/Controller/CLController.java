package org.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

import org.FileManager;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.Relationship.Type;
import org.Model.UMLModel;
import org.View.CLView;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.shell.jline3.PicocliJLineCompleter;

// Checks validity of action then calls function in
// editor to carry out change
@Command(name = "CLController", mixinStandardHelpOptions = true, version = "CLController 1.0",
		description = "CLI Controller for UML Editor")
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
	private final String basePrompt = "Please type your command(Commands for list of commands): ";

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
	 * Prints help list
	 */
	@Command(name = "commands", aliases = {"com"}, description = "Prints help list")
	private void CL_Help() {
		view.showHelp();
		//view.show(basePrompt);
	}

	/**
	 * Checks if any classes exist before printing them
	 */
	@Command(name = "listclasses", aliases = ("lcs"), description = "List all classes")
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
	@Command(name = "listclass", aliases = ("lc"), description = "List info for one class")
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
	@Command(name = "listrelationships", aliases = ("lr"), description = "List all relationships")
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
	@Command(name = "addclass", aliases = ("ac"), description = "Add a new class")
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
	@Command(name = "delete class", aliases = ("dc"), description = "Delete a class")
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
	@Command(name = "renameclass", aliases = ("rc"), description = "Rename a class")
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
	@Command(name = "addrelationship", aliases = ("ar"), description = "Add relationship between classes")
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
	@Command(name = "editrelationship", aliases = ("er"), description = "Edits a relationship")
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
	@Command(name = "deleterelationship", aliases = ("dr"), description = "Deletes a relationship")
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
	@Command(name = "addfield", aliases = ("af"), description = "Adds a field")
	private void CL_addField() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to add a field to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show("What do you want to name the field?");
			input = sc.nextLine();
			if (input.isEmpty()) {
				view.show("Field name must not be blank");
				return;
			}
			view.show("What do you want the type of the field to be?");
			String fieldType = sc.nextLine();
			editor.addField(activeClass, input, fieldType);
			view.show("Field " + input + " successfully added to class " + className);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and field info from user and returns if deletion succeeded
	 */
	@Command(name = "deletefield", aliases = ("df"), description = "Delete a field")
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
	 * Gets class and field info from user and allows them to change its name/type
	 */
	@Command(name = "editfield", aliases = ("ef"), description = "Edits a field")
	private void CL_editField() {
		try {
			view.show(model.listClassNames());
			view.show("What class do you want to rename a field from?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listFields(activeClass));
			view.show("What is the name of the field you want to rename?");
			input = sc.nextLine();
			Field editField = activeClass.fetchField(input);
			view.show("What part of " + input + " do you want to edit? (Name or Type)");
			input = sc.nextLine().replaceAll("\\s", "");
			while(true) {
				if (input.equalsIgnoreCase("name")) {
					// Edit name
					view.show("What do you want to rename the field to?");
					String newName = sc.nextLine();
					editor.renameField(activeClass, editField, newName);
					view.show("Field " + input + " renamed to " + newName);
					break;
				} else if (input.equalsIgnoreCase("type")) {
					// Edit type
					view.show("What do you want the field's new type to be?");
					String newType = sc.nextLine();
					editor.changeFieldType(editField, newType);
					view.show("Type successfully changed to " + newType);
					break;
				}
				view.show("Input did not match a changable value \nPlease try again");
				input = sc.nextLine().replaceAll("\\s", "");
			}
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class, method, and parameters from user and returns if action succeeded
	 * or not
	 */
	@Command(name = "addmethod", aliases = ("am"), description = "Adds a method")
	private void CL_addMethod() {
		try {
			view.show(model.listClassNames());
			view.show("What class would you like to add a method to?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show("What do you want to name the method?");
			input = sc.nextLine();
			view.show("What would you like the method's return type to be?");
			String retType = sc.nextLine().replaceAll("\\s", "");
			if (retType.isEmpty()) {
				retType = "void";
			}
			LinkedHashMap<String, String> paramList = new LinkedHashMap<>();
			view.show("Type the name of a parameter you'd like to add to this new method (enter to skip)");
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

			
			editor.addMethod(activeClass, input, retType, paramList);

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
	@Command(name = "deletemethod", aliases = ("dm"), description = "Deletes a method")
	private void CL_deleteMethod() {
		try {
			view.show(model.listClassNames());
			view.show("What class do you want to rename a method from?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("Enter the number of the method you want to delete");
			int methodNum = -1;
			boolean validNum = false;
			while (!validNum) {
				if (sc.hasNextInt()) {
					methodNum = sc.nextInt() - 1;
					if (!(0 <= methodNum && methodNum < activeClass.getMethodList().size())) {
						view.show("Number must be associated with a method");
						continue;
					}
					validNum = true;
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}
            Method delMethod = (Method) activeClass.getMethodList().get(methodNum);
			editor.deleteMethod(activeClass, delMethod);
			view.show("Method " + delMethod.getName() + " successfully deleted");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and method info from user and allows the to change name/return type
	 */
	@Command(name = "editmethod", aliases = {"em"}, description = "Edits a method")
	private void CL_editMethod() {
		try {
			view.show(model.listClassNames());
			view.show("What class do you want to rename a method from?");
			String className = sc.nextLine();
			activeClass = model.fetchClass(className);
			view.show(model.listMethods(activeClass));
			view.show("Enter the number of the method you want to rename");
			int methodNum = -1;
			boolean validNum = false;
			while (!validNum) {
				if (sc.hasNextInt()) {
					methodNum = sc.nextInt() - 1;
					if (!(0 <= methodNum && methodNum < activeClass.getMethodList().size())) {
						view.show("Number must be associated with a method");
						continue;
					}
					validNum = true;
				} else {
					view.show("Invalid input. Please enter a positive number");
				}
				// Clear invalid input from buffer
				sc.nextLine();
			}
            Method editMethod = (Method) activeClass.getMethodList().get(methodNum);
			view.show("What part of " + editMethod.getName() + " do you want to change? (Name or Type)");
			input = sc.nextLine().replaceAll("\\s", "");
			while(true) {
				if (input.equalsIgnoreCase("name")) {
					// Edit name
					String oldName = editMethod.getName();
					view.show("What do you want to rename the method to?");
					String newName = sc.nextLine();
					editor.renameMethod(activeClass, editMethod, newName);
					view.show("Method " + oldName + " renamed to " + newName);
					break;
				} else if (input.equalsIgnoreCase("type")) {
					// Edit type
					view.show("What do you want the method's new return type to be?");
					String newType = sc.nextLine();
					if (newType.isEmpty()) {
						newType = "void";
					}
					editor.changeMethodType(editMethod, newType);
					view.show("Type successfully changed to " + newType);
					break;
				}
				view.show("Input did not match a changable value \nPlease try again");
				input = sc.nextLine().replaceAll("\\s", "");
			}
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/*
	 * Gets a class, a method, and the names of parameters that a user would like to
	 * add
	 * to the list of parameters attached to the method.
	 */
	@Command(name = "addparameter", aliases = ("ap"), description = "Adds a parameter")
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
						view.show("Please type the name of the next parameter (enter to stop): ");
						continue;
					}
					if (editor.nameAlrAdded(input, parameterList)) {
						view.show("This parameter has already been added.");
						view.show("Please type the name of the next parameter (enter to stop):");
						continue;
					}
					view.show("Enter the parameter's type");
					typeInput = sc.nextLine().replaceAll("\\s", "");
					if(typeInput.equals("")) {
						view.show("Parameters must have a type");
						view.show("Please type the name of the next parameter:");
						continue;					
					}
					parameterList.put(input, typeInput);
				}
				view.show("Please type the name of the next parameter (enter to stop):");
			}
			editor.addParam(parameterList, activeMethod);
			view.show("The parameter(s) were added to " + activeMethod.getName());
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
	@Command(name = "removeparameter", aliases = ("rp"), description = "Removes a parameter")
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

	@Command(name = "removeallparameters", aliases = ("rap"), description = "Removes all parameters")
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
	@Command(name = "changeparameter", aliases = ("cp"), description = "Replaces one or all params")
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
			view.show("Type the name of a parameter you'd like to add to the new list. (enter to stop):");
			while (loop) {
				// Loops for adding
				paramName = sc.nextLine().replaceAll("\\s", "");
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
					view.show("Enter the parameter's type");
					type = sc.nextLine().replaceAll("\\s", "");
					if(type.equals("")) {
						view.show("Parameters must have a type");
						view.show("Please type the name of the next parameter:");
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

	@Command(name = "undo", description = "undo change")
	private void CL_undo(){
		try {
		editor.undo();
		this.model = editor.getModel();
		view.show("The last action was undone.");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
		
	}

    	@Command(name = "redo", description = "redo change")
	private void CL_redo(){
		try {
		editor.redo();
		this.model = editor.getModel();
		view.show("The last undone action was redone.");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	@Command(name = "save", description = "Saves model")
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

	@Command(name = "load", description = "Loads a saved model")
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
		try {
			Terminal terminal = TerminalBuilder.builder().system(true).build();
			CommandLine cmd = new CommandLine(this);
			Completer completer = new PicocliJLineCompleter(cmd.getCommandSpec());
			LineReader reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).build();

			while ((input = reader.readLine(basePrompt)) != null) {
				if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("q")) {
					break;
				}

				try {
					cmd.execute(input.replaceAll("\\s", ""));
				} catch (CommandLine.ParameterException ex) {
					System.err.println(ex.getMessage());
					ex.getCommandLine().usage(System.err);
				} catch (Exception ex) {
					System.err.println("An unexpected error occurred: " + ex.getMessage());
					ex.printStackTrace();
				}
				view.show("");
			}
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}
}
