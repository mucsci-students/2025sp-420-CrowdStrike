

public class UMLEditor {
	
	// The model that is being edited
	private UMLModel model;
	// Holds the class actively being worked on
	private ClassObject activeClass;
	
	/**
	 * Constructs an instance of the UMLEditor
	 * @param model		| The list of existing classes and relationships
	 */
	public UMLEditor(UMLModel model) {
		this.model = model;
		activeClass = null;
	}
	
	/**
	 * Set the activeClass to null
	 */
	private void resetActiveClass() {
		activeClass = null;
	}
	
	/**
	 * Adds a new class to the list of classes
	 * @param newClass		| ClassObject to be added to classList
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean addClass(String newClassName) {

		if(isValidClassName(newClassName)==0){
			if (model.fetchClass(newClassName) != null) {
				// Class already exists
				return false;
			} else {
				// Class does not exist
				ClassObject newClass = new ClassObject(newClassName);
				model.getClassList().add(newClass);
				return true;
			}
		}
		else{
			// Class name is not valid
			return false;
		}
	}
	
	/**
	 * Deletes specified class from classList
	 * @param className		| Name of class to be deleted
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean deleteClass(String className) {
		activeClass = model.fetchClass(className);
		if (activeClass != null) {
			// Class exists
			model.getClassList().remove(activeClass);
			resetActiveClass();
			return true;
		}
		return false;
	}
	
	/**
	 * Renames a class
	 * @param className		| Name of class to be renamed
	 * @param newName		| New name to give the class
	 * @return Number indicating status of operation
	 * 			return 		|	status
	 * 			0			|	success
	 * 			1			|	class not found
	 * 			2			|	newName already in use
	 * 			3			|	newName not valid	
	 */
	public int renameClass(String className, String newName) {
		if(isValidClassName(newName)!=0){
			return 3;
		}
		activeClass = model.fetchClass(newName);
		if (activeClass != null) {
			// Class with newName already exists
			resetActiveClass();
			return 2;
		}
		activeClass = model.fetchClass(className);
		if (activeClass == null) {
			// Class being renamed does not exist
			return 1;
		}
		// Class exists and newName is valid
		activeClass.setName(newName);
		resetActiveClass();
		return 0;
	}
	
	/**
	 * Adds relationship to relationshipList
	 * Updates relationshipLength if newRel's name is new longest name
	 * @param newRel	| Relationship to be added to relationshipList
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean addRelationship(String name, String source, String dest) {
		ClassObject src = model.fetchClass(source);
		if (src != null) {
			// Source class does exist
			ClassObject dst = model.fetchClass(dest);
			if (dst != null) {
				// Destination class does exist
				// Check if relationship already exists
				if (model.relationshipExist(source, dest) == null) {
					// Relationship does not exist
					// Create new Relationship
					Relationship newRel = new Relationship(name, src, dst);
					// Update longest relationship name if needed
					if (newRel.getName().length() > model.getRelationshipLength()) {
						model.setRelationshipLength(newRel.getName().length());
					}
					model.getRelationshipList().add(newRel);
					return true;
				}
			}
		}
		// Adding relationship failed
		return false;
	}
	
	/**
	 * Deletes the indicated relationship from relationshipList
	 * @param delRel	| Relationship to be removed from relationshipList
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean deleteRelationship(String source, String dest) {
		// Check if relationship exists
		Relationship relExist = model.relationshipExist(source, dest);
		if (relExist != null) {
			// Relationship does exist
			// Check if relationship being deleted has the longest name
			if (relExist.getName().length() == model.getRelationshipLength()) {
				// Relationship has the longest name
				// Remove it from the list and update relationshipLength
				model.getRelationshipList().remove(relExist);
				model.updateLongest();
			} else {
				// Relationship does not have the longest name
				// Remove relationship from the list
				model.getRelationshipList().remove(relExist);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Adds attribute to the indicated class
	 * @param className		| Name of class to add attribute to
	 * @param newAttr		| Attribute to be added
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean addAttribute(String className, String attrName) {
		// Use the class name to fetch the ClassObject from classList
		activeClass = model.fetchClass(className);
		if (activeClass != null) {
			// Class exists
			if (activeClass.fetchAttribute(attrName) == null) {
				// Attribute does not exist
				Attribute newAttr = new Attribute(attrName);
				activeClass.addAttribute(newAttr);
				resetActiveClass();
				return true;
			}
		}
		resetActiveClass();
		return false;
	}
	
	/**
	 * Deletes attribute from the indicated class
	 * @param className		| Name of class to delete attribute from
	 * @param delAttr		| Attribute to be removed
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean deleteAttribute(String className, String attrName) {
		// Use the class name to fetch the ClassObject from classList
		activeClass = model.fetchClass(className);
		if (activeClass != null) {
			// Class exists
			Attribute attr = activeClass.fetchAttribute(attrName);
			if (attr != null) {
				// Attribute exists in class
				activeClass.removeAttribute(attr);
				resetActiveClass();
				return true;
			}
		}
		resetActiveClass();
		return false;
	}
	
	/**
	 * Renames the indicated attribute in the indicated class
	 * @param className		| Name of class attribute is in
	 * @param curName		| Current name of the attribute to be renamed
	 * @param newName		| New name to give attribute
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean renameAttribute(String className, String curName, String newName) {
		// Use the class name to fetch the ClassObject from classList
		activeClass = model.fetchClass(className);
		if (activeClass != null) {
			// Class exists
			Attribute attr = activeClass.fetchAttribute(curName);
			if (attr != null) {
				// Attribute exists
				attr.renameAttribute(newName);
				resetActiveClass();
				return true;
			}
		}
		resetActiveClass();
		return false;
	}

	/**
	 * Validates whether the provided string could be a valid Java class name
	 * 
	 * A string is valid as a class name assuming:
	 * 			1. It isn't blank or null
	 * 			2. It begins with a letter, underscore, or dollar sign
	 * 			3. It contains only letters, numbers, underscores, and/or dollar signs
	 * 			4. It is not one of the reserved keywords in Java
	 * 
	 * @param className | The class name to be validated
	 * @return 0 on success, 1-3 on fail
	 */
	public static int isValidClassName(String className) {
		// Check if the className is null or an empty string.
		if (className == null || className.isEmpty()) {
			return 1;
		}
			
		// Verify that the first character is valid: in Java, this can be a letter, underscore, or dollar sign
        for (int i = 0; i < className.length(); i++) {
            if (!Character.isLetter(className.charAt(i)) && className.charAt(i) != '_' && className.charAt(i) != '$') {
                return 2;
            }
        }
		
		// Verify that the characters are valid: in Java, this can be a letter, number, underscore, or dollar sign
        for (int i = 0; i < className.length(); i++) {
            if (!Character.isLetterOrDigit(className.charAt(i)) && className.charAt(i) != '_' && className.charAt(i) != '$') {
                return 3;
            }
        }
		
		// String array of all the keywords in java (can't be used as a class name)
		// THIS LIST WAS AI-GENERATED AND HAS NOT BEEN PERSONALLY VERIFIED FOR CORRECTNESS OR COMPLETION	
		String[] reservedWords = {
			"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
			"continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float",
			"for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
			"new", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
			"super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void",
			"volatile", "while", "null", "true", "false"
		};
		
		// Check the name against the list of reserved words
		for (String keyword:reservedWords) {
			if (className.equalsIgnoreCase(keyword)) {
				return 4;
			}
		}
		
		// The className passed all checks and will be declared valid
		return 0;
	}
}