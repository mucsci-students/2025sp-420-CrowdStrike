package org.Model;
public class Attribute implements AttributeInterface {

	// Attribute Name
	private String name;
	
	/**
	 * Constructor to create a new attribute
	 * @param name	| the name of the attribute
	 */
	public Attribute(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void renameAttribute(String newName) {
		// TODO Auto-generated method stub
		this.name = newName;
	}
}
