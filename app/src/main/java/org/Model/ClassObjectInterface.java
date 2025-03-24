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
	public void addAttribute(AttributeInterface attr);

	/**
	 * Removes a specific attribute from the list of attributes
	 * @param attr		| The attribute that will be removed from the attribute list 
	 */
	public void removeAttribute(AttributeInterface attr);

	/**
	 * Fetches the specific field from the list of fields
	 * @param attrName		| The given name that will be searched for
	 */
	public Field fetchField(String fieldName) throws Exception;

	/**
	 * Fetches the specific method from the list of methods
	 * @param attrName		| The given name that will be searched for
	 * @param paramArity	| The number of params the method should have
	 */
	public Method fetchMethod(String methodName, int paramArity) throws Exception;
}
