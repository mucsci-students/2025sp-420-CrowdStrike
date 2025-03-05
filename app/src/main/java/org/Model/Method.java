package org.Model;

import java.util.ArrayList;

public class Method implements AttributeInterface {

	// Data Fields
	private String name;
	private String type;
	private ArrayList<Parameter> paramList;
	
    /**
	 * Constructor to create a new method
	 * @param name			| The name of the method
	 * @param paramList		| List of parameters the method contains
	 */
	public Method(String name, ArrayList<Parameter> paramList) {
		this.name = name;
		type = "Method";
		this.paramList = paramList;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
    @Override
	public String getType() {
		return type;
	}
	
    /**
	 * Returns the paramList of the method
	 * @return The method's parameter list
	 */
	public ArrayList<Parameter> getParamList() {
		return paramList;
	}

	@Override
	public void renameAttribute(String newName) {
		name = newName;
	}
	
	/**
	 * Concatenates the parameter list moreParams to the end of
	 * paramList
	 * @param moreParams	| List of params being added
	 */
	public void addParameter(ArrayList<Parameter> moreParams) {
		paramList.addAll(moreParams);
	}
	
    /**
	 * Removes all elements of paramList
	 */
	public void removeAllParameters() {
		paramList.clear();
	}
	
    /**
	 * Removes the designated parameter from paramList
	 * @param param		| The parameter to be removed
	 */
	public void removeParameter(Parameter param) {
		paramList.remove(param);
	}
	
    /**
	 * Checks if this method is equal to the one being passed in
	 * @param compareMethod		| The method this is being compared to
	 * @return True if this is equal to compareMethod, false otherwise
	 */
	public boolean equals(Method compareMethod) {
		if (this.name == compareMethod.getName() && this.paramList.size() == compareMethod.getParamList().size()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Fetches the specific parameter from the list of params
	 * @param paramName		| The given name that will be searched for
	 */
	public Parameter fetchParameter(String paramName) {
		int index = 0;
        // Iterate through the array of params
        while (index < paramList.size()) {
            // Check if the current param's name equals the given className
            if (paramName.equals(paramList.get(index).getName())) {
                // If yes, then it exists and return true
                return paramList.get(index);
            }
            index++;
        }
        // Class with className did not exist, return false
        return null;
	}

}