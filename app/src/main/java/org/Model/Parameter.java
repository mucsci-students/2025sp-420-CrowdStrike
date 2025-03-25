package org.Model;

public class Parameter {

	private String name;
	private String type;
	
	public Parameter(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public String setType(){
		return type;
	}
	
	public void setName(String newName) {
		name = newName;
	}

	public void setType(String newType){
		type = newType;
	}
}
