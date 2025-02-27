package org.Model;
public interface ClassObjectInterface {
	
	/**
	 * Gets the attribute list that correlates to this class object
	 */
	public void setName(String newName);
	
	/**
	 * Adds an attribute to the attribute list that is assigned to this class object
	 * @param attr		| The new attribute that will be added to the attribute list
	 */
	public void addAttribute(Attribute attr);

	/**
	 * Removes a specific attribute from the list of attributes
	 * @param attr		| The attribute that will be removed from the attribute list 
	 */
	public void removeAttribute(Attribute attr);

	/**
	 * Fetches the specific attribute from the list of attribute
	 * @param attrName		| The given name that will be searched for
	 */
	public Attribute fetchAttribute(String attrName);
}
