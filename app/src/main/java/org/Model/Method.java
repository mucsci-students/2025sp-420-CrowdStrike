package org.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Method implements AttributeInterface {

	// Data Fields
	private String name;
	private String type;
	private ArrayList<Parameter> paramList;

	/**
	 * Constructor to create a new method
	 * 
	 * @param name      | The name of the method
	 * @param paramList | List of parameters the method contains
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
	 * 
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
	 * Concatenates the parameter list moreParams to the end of paramList
	 * 
	 * @param moreParams | List of params being added
	 */
	public void addParameters(LinkedHashMap<String, String> moreParamNames) {
        ArrayList<Parameter> moreParams= new ArrayList<>();
        Parameter param;
        for(Map.Entry<String,String> obj : moreParamNames.entrySet()){
            param = new Parameter(obj.getKey(), obj.getValue());
            paramList.add(param);
        }
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
	 * 
	 * @param param | The parameter to be removed
	 */
	public void removeParameter(Parameter param) {
		paramList.remove(param);
	}

	/**
	 * Checks if this method is equal to the one being passed in
	 * 
	 * @param compareMethod | The method this is being compared to
	 * @return True if this is equal to compareMethod, false otherwise
	 */
	public boolean equals(Method compareMethod) {
		if (this.name.equals(compareMethod.getName()) && this.paramList.size() == compareMethod.getParamList().size()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Fetches the specific parameter from the list of params
	 * 
	 * @param paramName | The given name that will be searched for
	 * @throws Exception
	 */
	public Parameter fetchParameter(String paramName) throws Exception{
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
		throw new Exception (paramName + " does not exist in method " + name);
	}
	
	/**
	 * checks if a parameter is Used
	 * 
	 * @param paramName | String name of a parameter to see if it exists
	 */
	public boolean paramUsed(String paramName) {
		try {
			fetchParameter(paramName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	 /**
     * Adds a new parameter with the given name to the method.
     * @param paramName The name of the new parameter.
     * @throws IllegalArgumentException if the parameter name is null, empty, or already exists.
     */
    public void addParameter(String paramName, String type) {
        if (paramName == null || paramName.trim().isEmpty()) {
            throw new IllegalArgumentException("Parameter name cannot be empty.");
        }
        // Check for duplicate parameter names.
        for (Parameter p : paramList) {
            if (p.getName().equals(paramName)) {
                throw new IllegalArgumentException("Parameter already exists: " + paramName);
            }
        }
        paramList.add(new Parameter(paramName, type));
    }

    /**
     * Removes the parameter with the specified name from the method.
     * @param paramName The name of the parameter to remove.
     * @throws IllegalArgumentException if the parameter is not found.
     */
    public void removeParameter(String paramName) {
        Parameter toRemove = null;
        for (Parameter p : paramList) {
            if (p.getName().equals(paramName)) {
                toRemove = p;
                break;
            }
        }
        if (toRemove != null) {
            paramList.remove(toRemove);
        } else {
            throw new IllegalArgumentException("Parameter not found: " + paramName);
        }
    }

    /**
     * Updates the name of an existing parameter.
     * @param oldParamName The current name of the parameter.
     * @param newParamName The new name for the parameter.
     * @throws IllegalArgumentException if the new name is invalid or the parameter is not found.
     */
    public void updateParameter(String oldParamName, String newParamName, String newParamType) {
        if (newParamName == null || newParamName.trim().isEmpty()) {
            throw new IllegalArgumentException("New parameter name cannot be empty.");
        }
        for (Parameter p : paramList) {
            if (p.getName().equals(oldParamName)) {
                p.setName(newParamName);
				p.setType(newParamType);
                return;
            }
        }
        throw new IllegalArgumentException("Parameter not found: " + oldParamName);
    }


}