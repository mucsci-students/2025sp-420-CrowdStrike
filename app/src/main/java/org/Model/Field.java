package org.Model;
public class Field implements AttributeInterface {

	// Date Fields
	private String name;
	private String varType;
	private String type;
	
	/**
	 * Constructor to create a new field
	 * @param name	| the name of the field
	 */
	public Field(String name, String varType) {
		this.name = name;
		this.varType = varType;
		type = "Field";
	}
	
	@Override
	public String getName() {
		return name;
	}

	public String getVarType() {
		return varType;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void renameAttribute(String newName) {
		this.name = newName;
	}

	@Override
	public String toString() {
		return name+":"+varType;
	}

	public void setVarType(String newType) {
		this.varType = newType;
	}
}
