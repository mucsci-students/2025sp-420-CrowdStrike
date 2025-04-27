package org.Controller;

import org.jline.reader.LineReader;
import org.jline.reader.Completer;
import org.jline.reader.Candidate;
import org.jline.reader.ParsedLine;
import picocli.AutoComplete;
import picocli.CommandLine.Model.CommandSpec;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.lang.CharSequence;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.AttributeInterface;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;

import java.util.LinkedHashMap;

/**
 * Implements a custom completer that supports the completion of 
 * picocli defined commands as well as classes, fields, methods,
 * and parameters found in the model
 */
public class UMLCompleter implements Completer {

    private final CommandSpec spec;

    private UMLModel model;

    private final ArrayList<String> classCommands = new ArrayList<>(Arrays.asList("listclass", "lc", "deleteclass", "dc", "renameclass", "rc",
                                                                    "addrelationship", "ar", "editrelationship", "er", "deleterelationship", "dr",
                                                                    "addfield", "af", "deletefield", "df", "editfield", "ef", "addmethod", "am",
                                                                    "deletemethod", "dm", "editmethod", "em", "addparameter", "ap", "removeparameter",
                                                                    "rp", "removeallparameters", "rap", "changeparameter", "cp", "changeallparameters", "cap"));

    private final ArrayList<String> relCommands = new ArrayList<>(Arrays.asList("addrelationship", "ar", "editrelationship", "er", "deleterelationship", "dr"));

    private final ArrayList<String> fieldCommands = new ArrayList<>(Arrays.asList("deletefield", "df", "editfield", "ef"));

    private final ArrayList<String> methodCommands = new ArrayList<>(Arrays.asList("deletemethod", "dm", "editmethod", "em", "addparameter", "ap", "removeparameter",
                                                                    "rp", "removeallparameters", "rap", "changeparameter", "cp", "changeallparameters", "cap"));

    private final ArrayList<String> paramCommands = new ArrayList<>(Arrays.asList("removeparameter", "rp", "changeparameter", "cp"));

    /**
     * Constructs a new completer for the given command spec
     * @param spec  | The command specefication to generate completions
     *                for (must not be null)
     */
    public UMLCompleter(CommandSpec spec, UMLModel model) {
        if (spec == null) {
            throw new NullPointerException("spec");
        }
        this.spec = spec;
        this.model = model;
    }

    /**
     * Function used to update the model stored in the completer
     * Used when load, undo, or redo is called
     * @param The new model to use for tab-completion
     */
    public void setModel(UMLModel model) {
        this.model = model;
    }

    /**
     * Handles tab-completion of commands
     * @param words         | Array of Strings containing what has already been typed
     * @param line          | The parsed line being worked on (used to track cursor location)
     * @param candidates    | Struct that contains the list of completion candidates
     */
    private void commandCompletion(String[] words, ParsedLine line, List<Candidate> candidates) {
        List<CharSequence> cs = new ArrayList<CharSequence>();
        if (words[0].trim().equalsIgnoreCase("help")) {
            for (int i = 0; i < classCommands.size(); i++) {
                candidates.add(new Candidate(classCommands.get(i)));
            }
            candidates.add(new Candidate("listclasses"));
            candidates.add(new Candidate("lcs"));
            candidates.add(new Candidate("listrelationships"));
            candidates.add(new Candidate("lr"));
            candidates.add(new Candidate("addclass"));
            candidates.add(new Candidate("ac"));
            candidates.add(new Candidate("save"));
            candidates.add(new Candidate("saveimg"));
            candidates.add(new Candidate("load"));
            candidates.add(new Candidate("undo"));
            candidates.add(new Candidate("redo"));
        } else {
            cs.add("help");
            cs.add("exit");
            AutoComplete.complete(spec, words, line.wordIndex(), 0, line.cursor(), cs);
            for (CharSequence c : cs) {
                candidates.add(new Candidate((String) c));
            }
        }
    }

    /**
     * Handles tab-completion of classes
     * @param words          | Array of Strings containing what has already been typed
     * @param line           | The parsed line being worked on
     * @param candidates     | Struct that contains the list of completion candidates
     * @param stringLocation | The location of the String being completed in the array
     */
    private void classCompletion(String[] words, ParsedLine line, List<Candidate> candidates, int stringLocation) {
        if (words.length == stringLocation) {
            // List all classes if user has not started class input yet
            for (ClassObject classObj : model.getClassList()) {
                candidates.add(new Candidate(classObj.getName()));
            }
        } else {
            // Only list classes that match what user has manually typed
            for (ClassObject classObj : model.getClassList()) {
                if (classObj.getName().startsWith(words[stringLocation])) {
                    candidates.add(new Candidate(classObj.getName()));
                }
            }
        }
    }

    /**
     * Handles tab-completion of fields
     * @param words         | Array of Strings containing what has already been typed
     * @param line          | The parsed line being worked on
     * @param candidates    | Struct that contains the list of completion candidates
     */
    private void fieldCompletion(String[] words, ParsedLine line, List<Candidate> candidates) {
        try {
            ClassObject cls = model.fetchClass(words[1]);
            if (words.length == 2) {
                // List all fields in the class
                for (AttributeInterface fld : cls.getFieldList()) {
                    candidates.add(new Candidate(fld.getName()));
                }
            } else {
                for (AttributeInterface fld : cls.getFieldList()) {
                    if (fld.getName().startsWith(words[2])) {
                        candidates.add(new Candidate(fld.getName()));
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    /**
     * Handles tab-completion of methods
     * @param words         | Array of Strings containing what has already been typed
     * @param line          | The parsed line being worked on
     * @param candidates    | Struct that contains the list of completion candidates
     */
    private void methodCompletion(String[] words, ParsedLine line, List<Candidate> candidates) {
        try {
            ClassObject cls = model.fetchClass(words[1]);
            Method mthd;
            if (words.length == 2) {
                // List all methods in the class
                for (AttributeInterface attr : cls.getMethodList()) {
                    mthd = (Method) attr;
                    candidates.add(new Candidate(methodCompleterString(mthd)));
                }
            } else {
                for (AttributeInterface attr : cls.getMethodList()) {
                    mthd = (Method) attr;
                    if (mthd.getName().startsWith(words[2])) {
                        candidates.add(new Candidate(methodCompleterString(mthd)));
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    /**
	 * Creates a String to be displayed when using tab-completion
	 * @return
	 */
	public String methodCompleterString(Method m) {
        if (m.getParamList().size() == 0) {
            return m.getName();
        }
        Parameter param = m.getParamList().get(0);
		String str = m.getName() + "(" + param.getType();
        for (int i = 1; i < m.getParamList().size(); i++) {
            param = m.getParamList().get(i);
            str += "," + param.getType();
        }
        return str + ")";
	}

    /**
     * Handles tab-completion of parameters
     * @param words         | Array of Strings containing what has already been typed
     * @param line          | The parsed line being worked on
     * @param candidates    | Struct that contains the list of completion candidates
     */
    private void paramCompletion(String[] words, ParsedLine line, List<Candidate> candidates) {
        try {
            ClassObject cls = model.fetchClass(words[1]);
            //TODO
            // Getting the method will be a pain name(type1,type2,type3,...) format
            // Try to use the fetchMethod function you made for the GUI
            // Need to convert into name type1,type2,type3,... format and pass in as 2 params
            String mthdName = "";
            String parameters = "";
            // Find the index of the paren that splits name and params
            int parenIndex = words[2].indexOf("(");
            if (parenIndex != -1) {
                // Create Strings of method name and paramTypes seperated by commas
                mthdName = words[2].substring(0, parenIndex);
                parameters = words[2].substring(parenIndex + 1, words[2].lastIndexOf(")"));
            } else {
                // Method has no parameters so paramCompletion cannot be done
                return;
            }

            Method mthd = null;
            if (parameters.isEmpty()) {
                mthd = cls.fetchMethod(mthdName);
            } else {
                mthd = cls.fetchMethod(mthdName, parameters);
            }
            
            /*
             * Same deal as the rest of the functions, worst part will be accessing the method
             */
            if (words.length == 3) {
                for (Parameter param : mthd.getParamList()) {
                    candidates.add(new Candidate(param.getName()));
                }
            } else {
                for (Parameter param : mthd.getParamList()) {
                    if (param.getName().startsWith(words[3])) {
                        candidates.add(new Candidate(param.getName()));
                    }
                }
            }
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        // Get the current input and splits it into an array
        String[] words = new String[line.words().size()];
        words = line.words().toArray(words);

        // Determine what completion function to run based on location in words and
        // the command that has been entered

        // If no input or working on first inputautocomplete commands
        if (words.length == 1 || line.line().isEmpty() || words[0].trim().equalsIgnoreCase("help")) {
            // Completion function for commands
            commandCompletion(words, line, candidates);
        } else if (words.length == 2 && isValidClassCommand(words[0])) {
            // Completion function for class names
            classCompletion(words, line, candidates, 1);
        } else if (words.length == 3 && isValidRelCommand(words[0])) {
            // Completion function for class names
            classCompletion(words, line, candidates, 2);
        } else if (words.length == 3 && isValidFieldCommand(words[0])) {
            // Completion function for field names
            fieldCompletion(words, line, candidates);
        } else if (words.length == 3 && isValidMethodCommand(words[0])) {
            // Completion function for methods (include name and param lists)
            methodCompletion(words, line, candidates);
        } else if (words.length == 4 && isValidParamCommand(words[0])) {
            paramCompletion(words, line, candidates);
        } else {
            // Do nothing
        }
    }

    /**
     * Checks if the command entered needs class selection
     * @param command   The command entered by the user
     * @return True if command takes in a class, false otherwise
     */
    private boolean isValidClassCommand(String command) {
        return classCommands.contains(command);
    }

    /**
     * Checks if the command entered needs a second class selection
     * @param command   The command entered by the user
     * @return True if commands takes in a second class, false otherwise
     */
    private boolean isValidRelCommand(String command) {
        return relCommands.contains(command);
    }

    /**
     * Checks if the command entered needs field selection
     * @param command   The command entered by the user
     * @return True if command takes in a field, false otherwise
     */
    private boolean isValidFieldCommand(String command) {
        return fieldCommands.contains(command);
    }

    /**
     * Checks if the command entered needs method selection
     * @param command   The command entered by the user
     * @return True if command takes in a method, false otherwise
     */
    private boolean isValidMethodCommand(String command) {
        return methodCommands.contains(command);
    }

    /**
     * Checks if the command entered needs parameter selection
     * @param command   The command entered by the user
     * @return True if command takes in a parameter, false otherwise
     */
    private boolean isValidParamCommand(String command) {
        return paramCommands.contains(command);
    }
}