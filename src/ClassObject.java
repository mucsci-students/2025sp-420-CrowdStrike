

import java.util.ArrayList;

//Creates a Class object
public class ClassObject implements ClassObjectIntrface {

	private String name;

	private ArrayList<Attribute> attrList;
	
	/**
	 * Constructs a class object
	 * @param name		| The name to be given to the class object
	 */
	public ClassObject(String name) {
		this.name = name;
		attrList = new ArrayList<>();
	}
	
	/**
	 * Gets the name of a class object
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the class object 
	 * @param newName		| The new name to replace and be assigned to the class
	 */
	public void setName(String newName) {
		this.name = newName;
	}
	
	/**
	 * Gets the attribute list that correlates to this class object
	 */
	public ArrayList<Attribute> getAttrList() {
		return attrList;
	}
	
	/**
	 * Adds an attribute to the attribute list that is assigned to this class object
	 * @param attr		| The new attribute that will be added to the attribute list
	 */
	public void addAttribute(Attribute attr) {
		attrList.add(attr);
	}
	
	/**
	 * Removes a specific attribute from the list of attributes
	 * @param attr		| The attribute that will be removed from the attribute list 
	 */
	public void removeAttribute(Attribute attr) {
		attrList.remove(attr);
	}
	
	/**
	 * Fetches the specific attribute from the list of attribute
	 * @param attrName		| The given name that will be searched for
	 */
	public Attribute fetchAttribute(String attrName) {
		int index = 0;
        	// Iterate through the array of classes
        while (index < attrList.size()) {
            // Check if the current class' name equals the given className
            if (attrName.equals(attrList.get(index).getName())) {
                // If yes, then it exists and return true
                return attrList.get(index);
            }
            index++;
        }
        // Class with className did not exist, return false
        return null;
	}
}

