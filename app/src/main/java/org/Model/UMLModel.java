package org.Model;
import java.util.ArrayList;


public class UMLModel implements UMLModelInterface{
	
	// List to track all created classes
    private ArrayList<ClassObject> classList;
    // List to track all created relationships
    private ArrayList<Relationship> relationshipList;
    
    /**
     * Creates an empty array to store classes and relationships
     */
    public UMLModel() {
    	classList = new ArrayList<>();
    	relationshipList = new ArrayList<>();
    }
    
    /**
     * Returns the classList
     * @return list of created classes
     */
    public ArrayList<ClassObject> getClassList() {
    	return classList;
    }
    
    /**
     * Returns the relationshipList
     * @return list of created relationships
     */
    public ArrayList<Relationship> getRelationshipList() {
    	return relationshipList;
    }
    
    

    /**
     * Gets a ClassObject by the given name
     * @param className		| The name of the class to return
     * @return ClassObject with specified name if it exists
     * 		   returns null if class does not exist
	 * @throws Exception
     */
    public ClassObject fetchClass(String className) throws Exception {
        int index = 0;
        // Iterate through the array of classes
        while (index < classList.size()) {
            // Check if the current class' name equals the given className
            if (className.equals(classList.get(index).getName())) {
                // If yes, then it exists and return true
                return classList.get(index);
            }
            index++;
        }
        throw new Exception("Class " + className + " does not exist");
    }

	/**
	 * Checks if a class name is currently in use
	 * @param className	| The name being checked
	 * @return True if a class by the given name exists, false otherwise
	 */
	public boolean classNameUsed(String className) {
		try {
			fetchClass(className);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

        /**
     * Checks if a relationships exists
     * @param source	| The source of the relationship
     * @param dest		| Destination of the relationship
     * @return True if relationship exists, false otherwise
	 * @throws Exception
     */
    public Relationship fetchRelationship(String source, String dest) throws Exception {
    	int index = 0;
    	Relationship curRelationship;
    	// Iterate through the array of relationships
    	while (index < relationshipList.size()) {
    		curRelationship = relationshipList.get(index);
    		// Check if the current relationship is between source and dest
    		if (curRelationship.getSource().getName().equals(source) && 
                curRelationship.getDestination().getName().equals(dest)) {
    			// Relationship between source and dest already exists
    			return curRelationship;
    		}
    		index++;
    	}
    	// Relationship between source and dest does not exist
    	throw new Exception ("Relationship between " + source + " and " + dest + " does not exist");
    }

	/**
	 * Checks if a relationship exists
	 * @param source	| The name of the soruce class
	 * @param dest		| The name of the destination class
	 * @return True if relationship exists, false otherwise
	 */
	public boolean relationshipExist(String source, String dest) {
		try {
			fetchRelationship(source, dest);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

       
    /**
     * Creates a String listing classes and returns it
     * @return String listing all classes and their info
     */
	public String listClasses() {
		String finalString = "";
		// Iterate through the classList to call printClassInfo on each class to make its String
		for (int i = 0; i < classList.size(); i++) {
			finalString = finalString + listClassInfo(classList.get(i));
		}
		return finalString;
	}

	/**
	 * List the info of a specified class
	 * @param cls		| The class to be printed
	 * @return A String containing the list of specified class' info or 
	 * 		   empty string if class does not exist
	 */
	public String listClassInfo(ClassObject cls) {
		//ClassObject printClass = fetchClass(className);
        if (cls == null) {
        	// Class does not exist
        	return "";
        }
        int index = 0;
        Relationship curRelationship;
        String sourceRelationships = "";
        String destRelationships = "";
        // Add class name to finalString
        String finalString = "Class Name: " + cls.getName() + "\n  Fields:\n";
        // Add Fields to finalString
        for (int i = 0; i < cls.getFieldList().size(); i++) {
        	finalString = finalString + "   " +  cls.getFieldList().get(i).getName() + "\n";
        }
        finalString = finalString + "  Methods:\n";
		for (AttributeInterface mthd : cls.getMethodList()) {
			Method activeMethod = (Method) mthd;
			finalString = finalString + "   " + activeMethod.getName() + listParams(activeMethod) + "\n";
		}

        /*
         * Call the listRelationships function passing in the class to get that part
         * of the string
         */
        while(index < relationshipList.size()) {
            // Set current relationship to curRelationship
            curRelationship = relationshipList.get(index);
            if (curRelationship.getSource().getName().equals(cls.getName())) {
            	// Current class is current relationship's source
                // Create a String of the curRelationship and format using relationshipLength
            	String newSource = " (" + curRelationship.getTypeString() + ")" + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
            	// Add formatted String to sourceRelatiosnhips
            	sourceRelationships = sourceRelationships + "\n    " + newSource;
            } else if (curRelationship.getDestination().getName().equals(cls.getName())) {
            	// Current class is current relationhip's destination
                // Create a String of the curRelationship and format using relationshipLength
            	String newDest =" (" + curRelationship.getTypeString() + ")" + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
            	// Add formatted String to destRelatiosnhips
            	destRelationships = destRelationships + "\n    " + newDest;
            } else {
                // Relationship does not contain class
            }
            index++;
        }
        // Add Relationships to finalString
        finalString = finalString + "  Relationships:" + sourceRelationships + destRelationships + "\n";
        return finalString;
	}
	
	/**
	 * Lists all created relationships
	 * @return A string containing a list of all relationships
	 * @throws Exception
	 */
    public String listRelationships() throws Exception {
		if (relationshipList.size() == 0) {
			throw new Exception ("No relationships currently exist");
		}
        int index = 0;
        String relString = "Relationships:";
        // Create a variable to store the current relationship
        // Can do the same in listClasses and listClass
        Relationship curRelationship;
        while (index < relationshipList.size()) {
            curRelationship = relationshipList.get(index);
            relString = relString + "\n  " + " (" + curRelationship.getTypeString() + ")" + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
            index++;
        }
        return relString;
    }
    
    /**
     * Creates a list of all created class names that user can reference
     * @return List of class names
	 * @throws Exception
     */
    public String listClassNames() throws Exception {
    	if (classList.size() == 0) {
    		throw new Exception ("No classes exist");
    	}
    	int countNewLine = 0;
    	int index = 1;
    	String finalString = "Available Classes:\n- " + classList.get(0).getName();
    	ClassObject curClass;
    	while (index < classList.size()) {
    		curClass = classList.get(index);
    		if (countNewLine >= 5) {
    			// Create a new line after every six names
    			finalString = finalString + "\n- " + curClass.getName();
    			countNewLine = 0;
    		} else {
    			finalString = finalString + "   - " + curClass.getName();
    			countNewLine++;
    		}
    		index++;
    	}
    	return finalString;
    }

     /**
     * Creates a list of all created fields in the given class
     * 
     * @param cls	| Class whose fields are being listed
     * @return A string of all fields in the class
	 * @throws Exception
     */
    public String listFields(ClassObject cls) throws Exception{
    	ArrayList<AttributeInterface> fieldList = cls.getFieldList();
    	if (fieldList.size() == 0) {
    		throw new Exception("No fields exist in class " + cls.getName());
    	}
    	int countNewLine = 0;
    	int index = 1;
    	String finalString = "Available Fields:\n- " + fieldList.get(0).getName();
    	AttributeInterface attr;
    	while (index < fieldList.size()) {
    		attr = fieldList.get(index);
    		if (countNewLine >= 5) {
    			// Create a new line after every six names
    			finalString = finalString + "\n- " + attr.getName();
    			countNewLine = 0;
    		} else {
    			finalString = finalString + "   - " + attr.getName();
    			countNewLine++;
    		}
    		index++;
    	}
    	return finalString;
    }
    
    /**
     * Creates a list of all created methods in the given class
     * 
     * @param cls	| Class whose methods are being listed
     * @return A string containing all methods in the class
	 * @throws Exception
     */
    public String listMethods(ClassObject cls) throws Exception{
    	ArrayList<AttributeInterface> methodList = cls.getMethodList();
    	if (methodList.size() == 0) {
    		throw new Exception("No methods exist in class " + cls.getName());
    	}
    	int countNewLine = 0;
    	int index = 1;
    	String finalString = "Available Methods:\n- " + methodList.get(0).getName() + listParams((Method) methodList.get(0));
    	AttributeInterface attr;
    	while (index < methodList.size()) {
    		attr = methodList.get(index);
    		if (countNewLine >= 5) {
    			// Create a new line after every six names
    			finalString = finalString + "\n- " + attr.getName() + listParams((Method) attr);
    			countNewLine = 0;
    		} else {
    			finalString = finalString + "   - " + attr.getName() + listParams((Method) attr);
    			countNewLine++;
    		}
    		index++;
    	}
    	return finalString;
    }

	private String listParams(Method mthd) {
		String str = "(";
		if (mthd.getParamList().size() > 0) {
			str = str + mthd.getParamList().get(0).getName() + ": " + mthd.getParamList().get(0).getType();
			for (int i = 1; i < mthd.getParamList().size(); i++) {
				str = str + ", " + mthd.getParamList().get(i).getName() + ": " + mthd.getParamList().get(i).getType();
			}
		}
		return str = str + ")";
	}

	/**
	 * Checks if method with methodName exists
	 * If yes, list the arities of any methods with methodName
	 * @param cls			| The class being checked for methods with methodName
	 * @param methodName	| The name of methods being looked for
	 * @return String that lists arities of any methods with the given name
	 * @throws Exception
	 */
	public String listMethodArities(ClassObject cls, String methodName) throws Exception {
		// Get all methods with the same name
		ArrayList<Method> methodList = cls.fetchMethodByName(methodName);
		if (methodList.size() == 0) {
			throw new Exception ("No methods with name " + methodName + " exist in " + cls.getName());
		}
		String finalString = "Available Arities:\n";
		for (Method method : methodList) {
			finalString = finalString + "- " + method.getParamList().size() + "   ";
		}
		return finalString;
	}

    /**
	 * Validates whether the provided string could be a valid Java class name
	 * 
	 * A string is valid as a class name assuming: 
	 * 	1. It isn't blank or null 
	 * 	2. It begins with a letter or underscore
	 * 	3. It contains only letters, numbers, underscores, and/or dollar signs
	 *  4. Class w/ ClassName already exists
	 * 
	 * @param className | The class name to be validated
	 * @throws Exception
	 */
	public void isValidClassName(String className) throws Exception{
		// Check if the className is null or an empty string.
		if (className == null || className.isEmpty()) {
			throw new Exception ("No class name was given");
		}

		// Verify that the first character is valid: this can be a letter or underscore
		if (!Character.isLetter(className.charAt(0)) && className.charAt(0) != '_') {
			throw new Exception ("Name " + className + " is invalid. First character must be a letter or '_'");
		}

		// Verify that the characters are alphanumerics, underscores, or dollar signs
		for (int i = 0; i < className.length(); i++) {
			if (!Character.isLetterOrDigit(className.charAt(i)) && className.charAt(i) != '_') {
				throw new Exception ("Name " + className + " is invalid. Name can only contain alphanumerics, '_', or '$'");
			}
		}

		// Check if class w/ className already exists
		if (classNameUsed(className)) {
			throw new Exception ("Name " + className + " is already used by another class");
		}
	}

	public boolean arityValid(int arity) throws Exception {
		if (arity < 0) {
			throw new Exception ("Arity must be non-negative");
		} else {
			return true;
		}
	}
}
