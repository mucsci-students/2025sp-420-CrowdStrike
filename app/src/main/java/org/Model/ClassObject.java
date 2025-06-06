package org.Model;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

//Creates a Class object
public class ClassObject implements ClassObjectInterface {

	// Name of a class
	private String name;
	
	// Position of class when displayed in GUI
	private Point position;

	// Stores all attributes belonging to a class
	private LinkedHashMap<String, ArrayList<AttributeInterface>> attrMap;
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/**
	 * Constructs a class object
	 * @param name		| The name to be given to the class object
	 */
	public ClassObject(String name) {
		this.name = name;
		attrMap = new LinkedHashMap<>();
		this.position = new Point(-1,-1);
		// Need to create the Maps to be paired with each key
		ArrayList<AttributeInterface> fieldList = new ArrayList<>();
		ArrayList<AttributeInterface> methodList = new ArrayList<>();
		// Add Maps to the map paired with keys based on the type
		// Should probably add functions in ClassObject to get these lists
		attrMap.put("Field", fieldList);
		attrMap.put("Method", methodList);
	}

    public ClassObject(ClassObject o){
	this.name = o.name;
	this.position = o.position;
	attrMap = new LinkedHashMap<>();
	ArrayList<AttributeInterface> fieldList = new ArrayList<>();
	ArrayList<AttributeInterface> methodList = new ArrayList<>();

	attrMap.put("Field", fieldList);
	attrMap.put("Method", methodList);

	for(AttributeInterface f: o.attrMap.get("Field")){
	    Field fm = (Field) f;
	    attrMap.get("Field").add(new Field(f.getName(),fm.getVarType()));
	}

	for(AttributeInterface m: o.attrMap.get("Method")){
	    Method mo = (Method) m;
	    attrMap.get("Method").add(new Method(m.getName(), mo.getReturnType(), mo.getParamList()));
	}
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

	
	public Point getPosition(){
		return this.position;
	}

	public void setPosition(Point p){
		this.position = p;
	}

	public void setPosition(int xPos, int yPos){
		this.position.move(xPos, yPos);
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
		ArrayList<AttributeInterface> n = attrMap.get(attr.getType());
		ArrayList<AttributeInterface> o = new ArrayList<>(n);
		n.add(attr);

		pcs.firePropertyChange("Updated"+attr.getType()+"s", o, n);
	}
	
	@Override
	public void removeAttribute(AttributeInterface attr) {
		ArrayList<AttributeInterface> n = attrMap.get(attr.getType());
		ArrayList<AttributeInterface> o = new ArrayList<>(n);
		n.remove(attr);

		pcs.firePropertyChange("Updated"+attr.getType()+"s", o, n);
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

	/*
	 * Used to handle string to paramTypes generated by GUI and return dessired method
	 */
	public Method fetchMethod(String methodName, String paramTypes) throws Exception {
		String[] types = paramTypes.split(",");
		for (AttributeInterface attr : attrMap.get("Method")) {
			Method activeMethod = (Method) attr;
			if (activeMethod.getName().equals(methodName)) {
				// Current method has matching name
				// Check if activeMethod has the same number of params as the method being searched for
				if (activeMethod.getParamList().size() == 0 || activeMethod.getParamList().size() != types.length) {
					continue;
				}
				boolean match = true;
				for (int i = 0; i < types.length; i++) {
					if (!types[i].trim().equalsIgnoreCase(activeMethod.getParamList().get(i).getType())) {
						match = false;
						break;
					}
				}
				if (match) {
					return activeMethod;
				}
			}
		}
		// Class with className did not exist, return false
        throw new Exception ("Method " + methodName + " does not exist in " + name);
	}

	/**
	 * Overload fetchMethod to find a method with no params
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public Method fetchMethod(String methodName) throws Exception {
		int index = 0;
		while (index < attrMap.get("Method").size()) {
			Method activeMethod = (Method) attrMap.get("Method").get(index);
			if (methodName.equals(activeMethod.getName()) && activeMethod.getParamList().size() == 0) {
				return activeMethod;
			}
			index++;
		}
		throw new Exception ("Method " + methodName + " does not exist in " + name);
	}

	public Method fetchMethod(String methodName, LinkedHashMap<String, String> paramMap) throws Exception {
		for (AttributeInterface attr : attrMap.get("Method")) {
			Method activeMethod = (Method) attr;
			if (activeMethod.getName().equals(methodName)) {
				// Current method has matching name
				// Check if activeMethod has the same number of params as the method being searched for
				if (activeMethod.getParamList().size() == 0 || activeMethod.getParamList().size() != paramMap.size()) {
					continue;
				}
				boolean match = true;
				int index = 0;
				for (String type : paramMap.values()) {
					if (!type.trim().equalsIgnoreCase(activeMethod.getParamList().get(index).getType())) {
						match = false;
						break;
					}
					index++;
				}
				if (match) {
					return activeMethod;
				}
			}
		}
		// Class with className did not exist, return false
        throw new Exception ("Method " + methodName + " does not exist in " + name);
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

	public boolean methodExists(String methodName, ArrayList<Parameter> paramList) {
		// Create string of paramTypes seperated by commas
		String types = "";
		for (Parameter param : paramList) {
			types += param.getType();
		}
		try {
			fetchMethod(methodName, types);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean methodExists(String methodName, LinkedHashMap<String, String> params) {
		// Create string of paramTypes seperated by commas
		String types = "";
		for (String type : params.values()) {
			types += type + ",";
		}
		try {
			fetchMethod(methodName, types);
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

	/**
	 * Subscribe to changes to the model made by the edditor.
	 *
	 * @param listener the object trying to subscribe
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

    @Override
    public String toString(){
	return name;
    }
}
