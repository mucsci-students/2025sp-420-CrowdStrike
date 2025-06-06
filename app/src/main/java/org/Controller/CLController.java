package org.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

import org.FileManager;
import org.Model.ClassObject;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.Relationship.Type;
import org.Model.UMLModel;
import org.UMLToJsonAdapter;
import org.View.CLView;
import org.View.GUICmp.UMLDiagram;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.HelpCommand;
import picocli.CommandLine.Parameters;

// Checks validity of action then calls function in
// editor to carry out change
@Command(name = "", mixinStandardHelpOptions = true, subcommands = {HelpCommand.class}, version = "CLController 1.0",
		description = "CLI Controller for UML Editor")
public class CLController {

	private UMLModel model;

	private UMLEditor editor;

	private CLView view;

	private UMLCompleter completer;

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
	private void CL_listClassInfo(@Parameters(paramLabel = "className", description = "The class being listed") String className) {
		try {
			activeClass = model.fetchClass(className);
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
	private void CL_addClass(@Parameters(paramLabel = "className", description = "The class being added") String className) {
		try {
			model.isValidClassName(className);
			editor.addClass(className);
			view.show("Class " + className + " succesfully added");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class info from user and displays if class was deleted
	 */
	@Command(name = "deleteclass", aliases = ("dc"), description = "Delete a class")
	private void CL_deleteClass(@Parameters(paramLabel = "className", description = "The class being deleted") String className) {
		try {
			editor.deleteClass(className);
			view.show("Class " + className + " successfully deleted");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class info from user and displays if class was renamed
	 */
	@Command(name = "renameclass", aliases = ("rc"), description = "Rename a class")
	private void CL_renameClass(@Parameters(paramLabel = "className", description = "The class being renamed") String className,
								@Parameters(paramLabel = "newName", description = "New name for class") String newName) {
		try {
			activeClass = model.fetchClass(className);
			model.isValidClassName(newName);
			editor.renameClass(activeClass, newName);
			view.show("Class " + className + " renamed to " + newName);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets relationship info from user and returns if action succeeded or failed
	 */
	@Command(name = "addrelationship", aliases = ("ar"), description = "Add relationship between classes")
	private void CL_addRelationship(@Parameters(paramLabel = "source", description = "The source class' name") String source,
									@Parameters(paramLabel = "dest", description = "The destination class' name") String dest,
									@Parameters(paramLabel = "relType", description = "The type of the relationship") String relType) {
		try {
			model.fetchClass(source);
			model.fetchClass(dest);
			if (!model.relationshipExist(source, dest)) {
				// Relationship does not already exist
				Type type = null;
				switch(relType.toLowerCase()) {
					case "aggregation":
					case "a":
						type = Type.AGGREGATION;
						break;
					case "composition":
					case "c":
						type = Type.COMPOSITION;
						break;
					case "inheritance":
					case "i":
						type = Type.INHERITANCE;
						break;
					case "realization":
					case "r":
						type = Type.REALIZATION;
						break;
					default:
						view.show("Invalid type given, relationship could not be created");
						return;
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
	private void CL_editRelationship(@Parameters(paramLabel = "source", description = "The source class' name") String source,
									 @Parameters(paramLabel = "dest", description = "The destination class' name") String dest,
									 @Parameters(paramLabel = "editField", description = "The field being edited by the user") String editField,
									 @Parameters(paramLabel = "newValue", description = "The value being updated into the field being changed") String newValue) {
		try {
			model.fetchClass(source);
			model.fetchClass(dest);
			if (model.relationshipExist(source, dest)) {
				switch(editField.toLowerCase()) {
					case "source":
						try {
							model.fetchClass(newValue);
							// Need to check if Relationship w/ new source and current dest
							// already exists and abort if yes
							if (model.relationshipExist(newValue, dest)) {
								view.show("Relationship between " + newValue + " and " + dest + " already exists");
								return;
							}
						} catch (Exception e) {
							view.show(e.getMessage());
							return;
						}
						break;
					case "destination":
						try {
							model.fetchClass(newValue);
							// Need to check if Relationship w/ current source and new dest
							// already exists and abort if yes
							if (model.relationshipExist(source, newValue)) {
								view.show("Relationship between " + source + " and " + newValue + " already exists");
								return;
							}
						} catch (Exception e) {
							view.show(e.getMessage());
							return;
						}
						break;
					case "type":
						switch(newValue.toLowerCase()) {
							case "aggregation":
							case "a":
								newValue = "AGGREGATION";
								break;
							case "composition":
							case "c":
								newValue = "COMPOSITION";
								break;
							case "inheritance":
							case "i":
								newValue = "INHERITANCE";
								break;
							case "realization":
							case "r":
								newValue = "REALIZATION";
								break;
							default:
								view.show("Invalid type given, relationship could not be edited");
								return;
						}
						break;
					default:
						view.show("We do not support changing the " + editField
								+ " of a relationship right now");
						return;
				}

				try {
					if (newValue != null) {
						editor.editRelationship(source, dest, editField, newValue);
						view.show("Relationship successfully updated");
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
	private void CL_deleteRelationship(@Parameters(paramLabel = "source", description = "The source class' name") String source,
									   @Parameters(paramLabel = "dest", description = "The destination class' name") String dest) {
		try {
			model.fetchClass(source);
			model.fetchClass(dest);
			model.fetchRelationship(source, dest);
			editor.deleteRelationship(source, dest);
			view.show("Relationship between " + source + " and " + dest + " successfully deleted");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and field info from user and returns if action succeeded or not
	 */
	@Command(name = "addfield", aliases = ("af"), description = "Adds a field")
	private void CL_addField(@Parameters(paramLabel = "className", description = "The class the field is being added to") String className,
							 @Parameters(paramLabel = "fieldName", description = "The field being added") String fieldName,
							 @Parameters(paramLabel = "fieldType", description = "The type of the field") String fieldType) {
		try {
			activeClass = model.fetchClass(className);
			if (fieldName.isEmpty() || fieldType.isEmpty()) {
				view.show("Fields must have a name and type");
			}
			editor.addField(activeClass, fieldName, fieldType);
			view.show("Field " + fieldName + " successfully added to class " + className);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and field info from user and returns if deletion succeeded
	 */
	@Command(name = "deletefield", aliases = ("df"), description = "Delete a field")
	private void CL_deleteField(@Parameters(paramLabel = "className", description = "The class containing the field") String className,
								@Parameters(paramLabel = "fieldName", description = "The field being deleted") String fieldName) {
		try {
			activeClass = model.fetchClass(className);
			editor.deleteField(activeClass, fieldName);
			view.show("Field " + fieldName + " successfully deleted from class " + className);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/**
	 * Gets class and field info from user and allows them to change its name/type
	 */
	@Command(name = "editfield", aliases = ("ef"), description = "Edits a field")
	private void CL_editField(@Parameters(paramLabel = "className", description = "The class containing the field") String className,
							  @Parameters(paramLabel = "fieldName", description = "The field being changed") String fieldName,
							  @Parameters(paramLabel = "editField", description = "The part of the field being edited") String editField,
							  @Parameters(paramLabel = "newValue", description = "The value being updated in the field being changed") String newValue) {
		try {
			activeClass = model.fetchClass(className);
			Field activeField = activeClass.fetchField(fieldName);
			switch(editField.toLowerCase()) {
				case "name":
					editor.renameField(activeClass, activeField, newValue);
					view.show("Field " + fieldName + " renamed to " + newValue);
					break;
				case "type":
					editor.changeFieldType(activeField, newValue);
					view.show("Type successfully changed to " + newValue);
					break;
				default:
					view.show("We do not support changing the " + editField
							+ " of a field right now");
					return;
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
	private void CL_addMethod(@Parameters(paramLabel = "className", description = "The class the method is being added to") String className,
							  @Parameters(paramLabel = "methodName", description = "The name of the method being added") String methodName,
							  @Parameters(paramLabel = "paramList", arity = "0..1", description = "The parameters of the method") String paramList,
							  @Parameters(paramLabel = "returnType", arity = "0..1", description = "The return type of the method") String returnType) {
		try {
			activeClass = model.fetchClass(className);
			// Format for paramList --> (name1: type1,name2: type2,etc)
			// If user did not 
			if (paramList == null) {
				paramList = "()";
			}
			LinkedHashMap<String, String> paramMap = parseParamList(paramList);

			if (returnType == null) {
				returnType = "void";
			}
			
			editor.addMethod(activeClass, methodName, returnType, paramMap);

			if (paramMap.size() == 0) {
				view.show("Method " + methodName + "() successfully added to class " + className);
			} else {
				int i = 0;
				String message = "Method " + methodName + "(";
				for (String str : paramMap.keySet()) {
					message += str;
					if (i < paramMap.size() - 1) {
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
	private void CL_deleteMethod(@Parameters(paramLabel = "className", description = "The class containing the method") String className,
								 @Parameters(paramLabel = "methodSig", description = "The method name and param types") String methodSig) {
		try {
			activeClass = model.fetchClass(className);
			Method delMethod = parseMethod(activeClass, methodSig);
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
	private void CL_editMethod(@Parameters(paramLabel = "className", description = "The class containing the method") String className,
							   @Parameters(paramLabel = "methodSig", description = "The method name and param types") String methodSig,
							   @Parameters(paramLabel = "editField", description = "The part of the method being edited") String editField,
							   @Parameters(paramLabel = "newValue", description = "The value being updated in the method") String newValue) {
		try {
			activeClass = model.fetchClass(className);
			Method activeMethod = parseMethod(activeClass, methodSig);
			switch(editField.toLowerCase()) {
				case "name":
					String oldName = activeMethod.getName();
					editor.renameMethod(activeClass, activeMethod, newValue);
					view.show("Method " + oldName + " renamed to " + newValue);
					break;
				case "type":
					editor.changeMethodType(activeMethod, newValue);
					view.show("Return type successfully changed to " + newValue);
					break;
				default:
					view.show("We do not support changing the " + editField
							+ " of a method right now");
					return;
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
	private void CL_addParam(@Parameters(paramLabel = "className", description = "The class containing the method") String className,
							 @Parameters(paramLabel = "methodSig", description = "The method having params added") String methodSig,
							 @Parameters(paramLabel = "paramList", description = "The parameters being added") String paramList) {
		try {
			activeClass = model.fetchClass(className);
			Method activeMethod = parseMethod(activeClass, methodSig);
			LinkedHashMap<String, String> paramMap = parseParamList(activeMethod, paramList);
			editor.addParam(paramMap, activeMethod);
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
	private void CL_removeParam(@Parameters(paramLabel = "className", description = "The class containing the method") String className,
								@Parameters(paramLabel = "methodSig", description = "The method having a param deleted") String methodSig,
								@Parameters(paramLabel = "paramName", description = "The parameter to be removed") String paramName) {
		try {
			activeClass = model.fetchClass(className);
			Method activeMethod = parseMethod(activeClass, methodSig);
			Parameter param = activeMethod.fetchParameter(paramName);
			editor.removeParam(activeMethod, param);
			view.show("Success! Parameter " + paramName + " has been removed.");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/*
	 * Removes all parameters from the given method
	 */
	@Command(name = "removeallparameters", aliases = ("rap"), description = "Removes all parameters")
	private void CL_removeAllParam(@Parameters(paramLabel = "className", description = "The class containing the method") String className,
								   @Parameters(paramLabel = "methodSig", description = "The method having params removed") String methodSig) {
		try {
			activeClass = model.fetchClass(className);
			Method activeMethod = parseMethod(activeClass, methodSig);
			editor.removeAllParams(activeMethod);
			view.show("All parameters were removed");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/*
	 * Replaces the specefied param in the given method with the list of params given
	 */
	@Command(name = "changeparameter", aliases = ("cp"), description = "Replaces one param")
	private void CL_changeParam(@Parameters(paramLabel = "className", description = "The class containing the method") String className,
								@Parameters(paramLabel = "methodSig", description = "The method having a param changed") String methodSig,
								@Parameters(paramLabel = "paramName", description = "The parameter to be changed") String paramName,
								@Parameters(paramLabel = "paramList", description = "The new parameters") String paramList) {
		try {
			activeClass = model.fetchClass(className);
			Method activeMethod = parseMethod(activeClass, methodSig);
			Parameter param = activeMethod.fetchParameter(paramName);
			LinkedHashMap<String, String> paramMap = parseParamList(activeMethod, param, paramList);
			editor.changeParameter(activeMethod, param, paramMap);
			view.show("Parameter " + param.getName() + " was replaced with new parameter list.");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	/*
	 * Replaces all parameters in the given method with the new list
	 */
	@Command(name = "changeallparameters", aliases = ("cap"), description = "Replaces all params")
	private void CL_changeParam(@Parameters(paramLabel = "className", description = "The class containing the method") String className,
								@Parameters(paramLabel = "methodSig", description = "The method having a param changed") String methodSig,
								@Parameters(paramLabel = "paramList", description = "The new parameters") String paramList) {
		try {
			activeClass = model.fetchClass(className);
			Method activeMethod = parseMethod(activeClass, methodSig);
			LinkedHashMap<String, String> paramMap = parseParamList(paramList);
			editor.changeAllParams(activeMethod, paramMap);
			view.show("All parameters were replaced.");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	@Command(name = "undo", description = "undo change")
	private void CL_undo(){
		try {
		editor.undo();
		this.model = editor.getModel();
		completer.setModel(editor.getModel());
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
		completer.setModel(editor.getModel());
		view.show("The last undone action was redone.");
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	@Command(name = "save", description = "Saves model")
	private void save(@Parameters(paramLabel = "path", description = "Location to save UML") String path) {
		UMLToJsonAdapter adapter = new UMLToJsonAdapter();
		try {
			FileManager file = new FileManager();
			file.save(adapter, model, path);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

    	@Command(name = "saveimg", description = "Saves model image")
	private void saveing(@Parameters(paramLabel = "path", description = "Location to save UML image") String path) {
		UMLDiagram d = new UMLDiagram(editor);
		d.doLayout();
		d.save(path);
	}

	@Command(name = "load", description = "Loads a saved model")
	private void load(@Parameters(paramLabel = "path", description = "Location of saved UML to load") String path) {
		UMLToJsonAdapter adapter = new UMLToJsonAdapter();
		try {
			FileManager file = new FileManager();
			model = file.load(adapter ,path);
			editor = new UMLEditor(model);
			completer.setModel(model);
		} catch (Exception e) {
			view.show(e.getMessage());
		}
	}

	private Method parseMethod(ClassObject cls, String methodSig) throws Exception {
		String mthdName = "";
        String parameters = "";
        // Find the index of the paren that splits name and params
        int parenIndex = methodSig.indexOf("(");
        if (parenIndex != -1) {
            // Create Strings of method name and paramTypes seperated by commas
            mthdName = methodSig.substring(0, parenIndex);
            parameters = methodSig.substring(parenIndex + 1, methodSig.lastIndexOf(")"));
        } else {
            // Method has no parameters so call fetch on methodSig
            return cls.fetchMethod(methodSig);
        }
		return cls.fetchMethod(mthdName, parameters);
	}

	/**
	 * Used when adding methods or replacing all params, just skips any params with the same name
	 * in the list being made
	 */
	private LinkedHashMap<String, String> parseParamList(String lst) {
		// Format for paramList --> (name1: type1,name2: type2,etc)
		LinkedHashMap<String, String> paramList = new LinkedHashMap<>();
		if (lst.equals("()")) {
			return paramList;
		}
		// Create an array of Strings with "name1: type1" at each index
		//String[] paramArr = lst.substring(1, lst.lastIndexOf(")")).split(",");
		String[] paramArr = lst.substring(1, lst.length() - 1).split(",");
		for (int i = 0; i < paramArr.length; i++) {
			String[] param = paramArr[i].split(":");
			/*
			 * Add a check to avoid duplicate params
			 */
			if (paramList.keySet().contains(param[0].trim())) {
				continue;
			}
			paramList.put(param[0].trim(), param[1].trim());
		}
		return paramList;
	}

	/**
	 * Used when adding params to an existsing method
	 */
	private LinkedHashMap<String, String> parseParamList(Method mthd, String lst) {
		// Format for paramList --> (name1: type1,name2: type2,etc)
		LinkedHashMap<String, String> paramList = new LinkedHashMap<>();
		if (lst.equals("()")) {
			return paramList;
		}
		// Create an array of Strings with "name1: type1" at each index
		//String[] paramArr = lst.substring(1, lst.lastIndexOf(")")).split(",");
		String[] paramArr = lst.substring(1, lst.length() - 1).split(",");
		for (int i = 0; i < paramArr.length; i++) {
			String[] param = paramArr[i].split(":");
			// Makes sure duplicate params are skipped(in current list and method list)
			if (paramList.keySet().contains(param[0].trim()) || mthd.paramUsed(param[0].trim())) {
				continue;
			}
			paramList.put(param[0].trim(), param[1].trim());
		}
		return paramList;
	}

	/**
	 * Used when changing one parameter, so that it can be added back in if needed
	 */
	private LinkedHashMap<String, String> parseParamList(Method mthd, Parameter changeParam, String lst) {
		// Format for paramList --> (name1: type1,name2: type2,etc)
		LinkedHashMap<String, String> paramList = new LinkedHashMap<>();
		if (lst.equals("()")) {
			return paramList;
		}
		// Create an array of Strings with "name1: type1" at each index
		//String[] paramArr = lst.substring(1, lst.lastIndexOf(")")).split(",");
		String[] paramArr = lst.substring(1, lst.length() - 1).split(",");
		boolean paramReadded = false;
		for (int i = 0; i < paramArr.length; i++) {
			String[] param = paramArr[i].split(":");
			// Allow the param being changed to be readded once
			if (param[0].equals(changeParam.getName()) && !paramReadded) {
				paramList.put(param[0].trim(), param[1].trim());
				paramReadded = true;
				continue;
			}
			// Makes sure duplicate params are skipped(in current list and method list)
			if (paramList.keySet().contains(param[0].trim()) || mthd.paramUsed(param[0].trim())) {
				continue;
			}
			paramList.put(param[0].trim(), param[1].trim());
		}
		return paramList;
	}

	private List<String> tokenizeCommands(String commands) {
		List<String> cmds = new ArrayList<>();
		StringBuilder current = new StringBuilder();
		boolean inParens = false;
		for (int i = 0; i < commands.length(); i++) {
			char c = input.charAt(i);

			if (c == '(') {
				inParens = true;
				current.append(c);
			} else if (c == ')') {
				current.append(c);
				inParens = false;
			} else if (Character.isWhitespace(c)) {
				if (inParens) {
					current.append(c);
				} else {
					if (current.length() > 0) {
						cmds.add(current.toString());
						current.setLength(0);
					}
				}
			} else {
				current.append(c);
			}
		}

		if (current.length() > 0) {
			cmds.add(current.toString());
		}

		return cmds;
	}

	/**
	 * Initializes the controller that allows user to interact with UML software
	 */
	public void init() {
		try {
			Terminal terminal = TerminalBuilder.builder().system(true).build();
			CommandLine cmd = new CommandLine(this);
			completer = new UMLCompleter(cmd.getCommandSpec(), model);
			LineReader reader = LineReaderBuilder.builder().terminal(terminal).completer(completer).build();

			while ((input = reader.readLine(basePrompt)) != null) {
				if (input.trim().equalsIgnoreCase("exit") || input.trim().equalsIgnoreCase("q")) {
					break;
				}

				try {
					if (input.trim().equalsIgnoreCase("help")) {
						input = "-h";
					}
					List<String> lst = tokenizeCommands(input);
					String[] cmds = new String[lst.size()];
					for (int i = 0; i < lst.size(); i++) {
						cmds[i] = lst.get(i);
					}
					cmd.execute(cmds);
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
