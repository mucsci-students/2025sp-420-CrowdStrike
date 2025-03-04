package org.Model;
public class Field implements AttributeInterface {

	// Date Fields
	private String name;
	private String type;
	
	/**
	 * Constructor to create a new field
	 * @param name	| the name of the field
	 */
	public Field(String name) {
		this.name = name;
		type = "Field";
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void renameAttribute(String newName) {
		this.name = newName;
	}
}
