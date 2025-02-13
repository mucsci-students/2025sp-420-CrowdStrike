import java.util.ArrayList;

public class UMLModel {
	
	// List to track all created classes
    private ArrayList<ClassObject> classList;
    // List to track all created relationships
    private ArrayList<Relationship> relationshipList;
    // Int to track the longest relationship name in relationshipList
    // Used to format when listing relationships to ensure they line up
    private int relationshipLength;
    
    /**
     * Creates an empty array to store classes and relationships
     * Sets relationshipLength to 0
     */
    UMLModel() {
    	classList = new ArrayList<>();
    	relationshipList = new ArrayList<>();
    	relationshipLength = 0;
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
     * Returns the length of the longest relationship name
     * @return relationshipLength
     */
    public int getRelationshipLength() {
    	return relationshipLength;
    }
    
    /**
     * Updates relationshipLength
     * @param newLen	| Length of new longest relationship name
     */
    public void setRelationshipLength(int newLen) {
    	relationshipLength = newLen;
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
     * @return True if relationship exists, false otherwise
     */
    public Relationship relationshipExist(String source, String dest) {
    	int index = 0;
    	Relationship curRelationship;
    	// Iterate through the array of relationships
    	while (index < relationshipList.size()) {
    		curRelationship = relationshipList.get(index);
    		// Check if the current relationship is between source and dest
    		if (curRelationship.getSource().getName().equals(source) && curRelationship.getDestination().getName().equals(dest)) {
    			// Relationship between source and dest already exists
    			return curRelationship;
    		}
    		index++;
    	}
    	// Relationship between source and dest does not exist
    	return null;
    }
    
    /**
     * Iterates through relationships to find the longest name
     * Used when deleting relationships
     */
    public void updateLongest() {
    	int len = 0;
    	for (int i = 0; i < relationshipList.size(); i++) {
    		int curLen = relationshipList.get(i).getName().length();
    		if (curLen > len) {
    			len = curLen;
    		}
    	}
    	setRelationshipLength(len);
    }
       
    /**
     * Creates a String listing classes and returns it
     * @return String listing all classes and their info
     */
	public String listClasses() {
        ClassObject curClass;
        Relationship curRelationship;
        int index1 = 0;
        int index2 = 0;
        // Strings to keep relationships where curClass is source or dest together
        String sourceRelationships = "";
        String destRelationships = "";
        // String to be returned that contains list of classes and info
        String finalString = "";
        while (index1 < classList.size()) {
            // Save current class in curClass
            curClass = classList.get(index1);
            // Add Class name to finalString
            finalString = finalString + "Class Name: " + curClass.getName() + "\n  Attributes:\n";
            // Iterate through the attribute list stored in each class
            while (index2 < curClass.getAttrList().size()) {
                // Add all attributes in attrList for curClass to finalString
                finalString = finalString + "   " + curClass.getAttrList().get(index2).getName() + "\n";
                index2++;
            }
            index2 = 0;
            // Iterate through relationship list to find all relationships involving the current class
            while (index2 < relationshipList.size()) {
                curRelationship = relationshipList.get(index2);
                if (curRelationship.getSource().getName().equals(curClass.getName())) {
                    // Current class is current relationship's source
                    // Create a String of the curRelationship and format using relationshipLength
                	String newSource = curRelationship.getName() + " ".repeat(relationshipLength - curRelationship.getName().length()) + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
                	// Add formatted String to sourceRelatiosnhips
                	sourceRelationships = sourceRelationships + "\n    " + newSource;
                } else if (curRelationship.getDestination().getName().equals(curClass.getName())) {
                    // Current class is current relationhip's destination
                    // Create a String of the curRelationship and format using relationshipLength
                	String newDest =  curRelationship.getName() + " ".repeat(relationshipLength - curRelationship.getName().length()) + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
                	// Add formatted String to destRelatiosnhips
                	destRelationships = destRelationships + "\n    " + newDest;
                } else {
                    // Current relationship does not involve the class
                }
                index2++;
            }
            // Add Relationships to finalString
            finalString = finalString + "  Relationships:" + sourceRelationships + destRelationships + "\n";
            // Reset values and increment index1
            sourceRelationships = "";
            destRelationships = "";
            index2 = 0;
            index1++;
        }
        return finalString;
    }

	/**
	 * List the info of a specified class
	 * @param className		| The name of the class to be printed
	 * @return A String containing the list of specified class' info or 
	 * 		   empty string if class does not exist
	 */
	public String listClassInfo(String className) {
        ClassObject printClass = fetchClass(className);
        if (printClass == null) {
        	// Class does not exist
        	return "";
        }
        int index = 0;
        Relationship curRelationship;
        String sourceRelationships = "";
        String destRelationships = "";
        String finalString = "";
        // Add class name to finalString
        finalString = "Class Name: " + printClass.getName() + "\n  Attributes:\n";
        // Iterate through attribute list and print
        while (index < printClass.getAttrList().size()) {
            // Add all attributes in attrList for printClass to finalString
            finalString = finalString + "   " + printClass.getAttrList().get(index).getName() + "\n";
            index++;
        }
        // Reset index
        index = 0;
        while(index < relationshipList.size()) {
            // Set current relationship to curRelationship
            curRelationship = relationshipList.get(index);
            if (curRelationship.getSource().getName().equals(printClass.getName())) {
            	// Current class is current relationship's source
                // Create a String of the curRelationship and format using relationshipLength
            	String newSource = curRelationship.getName() + " ".repeat(relationshipLength - curRelationship.getName().length()) + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
            	// Add formatted String to sourceRelatiosnhips
            	sourceRelationships = sourceRelationships + "\n    " + newSource;
            } else if (curRelationship.getDestination().getName().equals(printClass.getName())) {
            	// Current class is current relationhip's destination
                // Create a String of the curRelationship and format using relationshipLength
            	String newDest =  curRelationship.getName() + " ".repeat(relationshipLength - curRelationship.getName().length()) + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
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
            relString = relString + "\n  " + curRelationship.getName() + " ".repeat(relationshipLength - curRelationship.getName().length()) + ": " + curRelationship.getSource().getName() + " -> " + curRelationship.getDestination().getName();
            index++;
        }
        return relString;
    }
    
    /**
     * Creates a list of all created class names that user can reference
     * @return List of class names
     */
    public String listClassNames() {
    	if (classList.size() == 0) {
    		return "";
    	}
    	int countNewLine = 0;
    	int index = 1;
    	String finalString = "- " + classList.get(0).getName();
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
     * Creates a list of all created attributes in a given class
     * @param className		| Class whose attributes are being listed
     * @return A String of all attributes in the class
     */
    public String listAttributes(String className) {
    	ArrayList<Attribute> attrList = fetchClass(className).getAttrList();
    	if (attrList.size() == 0) {
    		return "";
    	}
    	int countNewLine = 0;
    	int index = 1;
    	String finalString = "- " + attrList.get(0).getName();
    	Attribute attr;
    	while (index < attrList.size()) {
    		attr = attrList.get(index);
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
}
