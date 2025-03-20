package org.Model;
import java.util.ArrayList;
import org.Model.Relationship.Type;


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
     */
    public ClassObject fetchClass(String className) {
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
        // Class with className did not exist, return false
        return null;
    }
    
    /**
     * Checks if a relationships exists
     * @param source	| The source of the relationship
     * @param dest		| Destination of the relationship
     * @param type      | The type of the relationship 
     * @return True if relationship exists, false otherwise
     */
    public Relationship relationshipExist(String source, String dest, Type type) {
    	int index = 0;
    	Relationship curRelationship;
    	// Iterate through the array of relationships
    	while (index < relationshipList.size()) {
    		curRelationship = relationshipList.get(index);
    		// Check if the current relationship is between source and dest
    		if (curRelationship.getSource().getName().equals(source) && 
                curRelationship.getDestination().getName().equals(dest) &&
                curRelationship.getType().equals(type)) {
    			// Relationship between source and dest already exists
    			return curRelationship;
    		}
    		index++;
    	}
    	// Relationship between source and dest does not exist
    	return null;
    }

        /**
     * Checks if a relationships exists
     * @param source	| The source of the relationship
     * @param dest		| Destination of the relationship
     * @return True if relationship exists, false otherwise
     */
    public Relationship relationshipExist(String source, String dest) {
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
    	return null;
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
        for (int i = 0; i < cls.getMethodList().size(); i++) {
        	Method method1 = (Method) cls.getMethodList().get(i);
        	finalString = finalString + "   " + method1.getName() + "\n     Parameters:\n"; 
        	for (int j = 0; j < method1.getParamList().size(); j++) {
        		finalString = finalString + "       " + method1.getParamList().get(j).getName() + "\n";
        	}
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
            	String newSource = curRelationship.getName() + " (" + curRelationship.getTypeString() + ")" + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
            	// Add formatted String to sourceRelatiosnhips
            	sourceRelationships = sourceRelationships + "\n    " + newSource;
            } else if (curRelationship.getDestination().getName().equals(cls.getName())) {
            	// Current class is current relationhip's destination
                // Create a String of the curRelationship and format using relationshipLength
            	String newDest =  curRelationship.getName() + " (" + curRelationship.getTypeString() + ")" + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
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
	 */
    public String listRelationships() {
        int index = 0;
        String relString = "Relationships:";
        // Create a variable to store the current relationship
        // Can do the same in listClasses and listClass
        Relationship curRelationship;
        while (index < relationshipList.size()) {
            curRelationship = relationshipList.get(index);
            relString = relString + "\n  " + curRelationship.getName() + " (" + curRelationship.getTypeString() + ")" + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
            index++;
        }
        return relString;
    }
    
    /**
     * Creates a list of all created class names that user can reference
     * @return List of class names
     */
    public String listClassNames() throws Exception {
    	if (classList.size() == 0) {
    		throw new Exception ("No classes exist");
    	}
    	int countNewLine = 0;
    	int index = 1;
    	String finalString = "Available CLasses:\n- " + classList.get(0).getName();
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
     */
    public String listMethods(ClassObject cls) throws Exception{
    	ArrayList<AttributeInterface> methodList = cls.getMethodList();
    	if (methodList.size() == 0) {
    		throw new Exception("No methods exist in class " + cls.getName());
    	}
    	int countNewLine = 0;
    	int index = 1;
    	String finalString = "Available Methods:\n- " + methodList.get(0).getName();
    	AttributeInterface attr;
    	while (index < methodList.size()) {
    		attr = methodList.get(index);
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
	 * @return 0 on success, 1-3 on fail
	 */
	public int isValidClassName(String className) {
		// Check if the className is null or an empty string.
		if (className == null || className.isEmpty()) {
			return 1;
		}

		// Verify that the first character is valid: this can be a letter or underscore
		if (!Character.isLetter(className.charAt(0)) && className.charAt(0) != '_') {
			return 2;
		}

		// Verify that the characters are alphanumerics, underscores, or dollar signs
		for (int i = 0; i < className.length(); i++) {
			if (!Character.isLetterOrDigit(className.charAt(i)) && className.charAt(i) != '_') {
				return 3;
			}
		}

		// Check if class w/ className already exists
		if (fetchClass(className) != null) {
			return 4;
		}

		// The className passed all checks and will be declared valid
		return 0;
	}
}
