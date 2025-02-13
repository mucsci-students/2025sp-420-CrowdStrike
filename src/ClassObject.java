

import java.util.ArrayList;

//Creates a Class object
public class ClassObject implements ClassObjectInterface {

	//Name of a class
	private String name;

	//Stores all attributes belonging to a class
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
	
	@Override
	public void setName(String newName) {
		this.name = newName;
	}
	
	/**
	 * Gets the attribute list that correlates to this class object
	 */
	public ArrayList<Attribute> getAttrList() {
		return attrList;
	}
	
	@Override
	public void addAttribute(Attribute attr) {
		attrList.add(attr);
	}
	@Override
	public void removeAttribute(Attribute attr) {
		attrList.remove(attr);
	}
	
	@Override
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

