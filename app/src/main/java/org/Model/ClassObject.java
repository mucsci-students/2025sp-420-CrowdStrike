package org.Model;
import java.util.ArrayList;
import java.util.LinkedHashMap;

//Creates a Class object
public class ClassObject implements ClassObjectInterface {

	// Name of a class
	private String name;

	// Stores all attributes belonging to a class
	private LinkedHashMap<String, ArrayList<AttributeInterface>> attrMap;
	
	/**
	 * Constructs a class object
	 * @param name		| The name to be given to the class object
	 */
	public ClassObject(String name) {
		this.name = name;
		attrMap = new LinkedHashMap<>();
		// Need to create the Maps to be paired with each key
		ArrayList<AttributeInterface> fieldList = new ArrayList<>();
		ArrayList<AttributeInterface> methodList = new ArrayList<>();
		// Add Maps to the map paired with keys based on the type
		// Should probably add functions in ClassObject to get these lists
		attrMap.put("Field", fieldList);
		attrMap.put("Method", methodList);
	}
	
	/**
	 * Gets the name of a class object
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Gets the HashMap containing lists of fields and methods
	 * @return HashMap containing ArrayLists of fields and methods
	 */
	public LinkedHashMap<String, ArrayList<AttributeInterface>> getAttrMap() {
		return attrMap;
	}
	
	/**
	 * Gets the ArrayList containing fields
	 * @return ArrayList containing fields
	 */
	public ArrayList<AttributeInterface> getFieldList() {
		return attrMap.get("Field");
	}
	
	/**
	 * Gets the ArrayList containing methods
	 * @return ArrayList containing methods
	 */
	public ArrayList<AttributeInterface> getMethodList() {
		return attrMap.get("Method");
	}
	
	@Override
	public void addAttribute(AttributeInterface attr) {
		attrMap.get(attr.getType()).add(attr);
	}
	
	@Override
	public void removeAttribute(AttributeInterface attr) {
		attrMap.get(attr.getType()).remove(attr);
	}
	
	@Override
	public Field fetchField(String fieldName) throws Exception {
		int index = 0;
        // Iterate through the array of classes
        while (index < attrMap.get("Field").size()) {
            // Check if the current class' name equals the given className
            if (fieldName.equals(attrMap.get("Field").get(index).getName())) {
                // If yes, then it exists and return true
                return (Field) attrMap.get("Field").get(index);
            }
            index++;
        }
        // Class with className did not exist, return false
        throw new Exception ("Field " + fieldName + " does not exist in " + name);
	}

	/**
	 * Used to return a boolean when needed for checking is a field exists
	 * @param fieldName		| The name of the field being checked
	 * @return True is a field with the name exists, false otherwise
	 */
	public boolean fieldNameUsed (String fieldName) {
		try {
			fetchField(fieldName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public Method fetchMethod(String methodName, int paramArity) throws Exception{
		int index = 0;
        // Iterate through the array of classes
        while (index < attrMap.get("Method").size()) {
            // Check if the current method's name equals the given methodName
            if (methodName.equals(attrMap.get("Method").get(index).getName())) {
            	// If yes, check if paramArity == # of param's current method has
            	// Need to cast as a method to access paramList
                Method activeMethod = (Method) attrMap.get("Method").get(index);
                if (activeMethod.getParamList().size() == paramArity) {
                	return activeMethod;
                }
            }
            index++;
        }
        // Class with className did not exist, return false
        throw new Exception ("Method " + methodName + " with parameter arity " + paramArity + " does not exist in " + name);
	}

	/**
	 * Used to return a boolean when needed for checking is a method exists
	 * @param methodName	| Name of method being checked
	 * @param paramArity	| Number of expected parameters
	 * @return True if method with name and arity exists, false otherwise
	 */
	public boolean methodExists(String methodName, int paramArity) {
		try {
			fetchMethod(methodName, paramArity);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public ArrayList<Method> fetchMethodByName(String methodName) {
		ArrayList<Method> sameNameList = new ArrayList<>();
		Method activeMethod;
		for (int i = 0; i < attrMap.get("Method").size(); i++) {
			if (methodName.equals(attrMap.get("Method").get(i).getName())) {
				activeMethod = (Method) attrMap.get("Method").get(i);
				sameNameList.add(activeMethod);
			}
		}
		return sameNameList;
	}
}

