package org.Controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.UMLMemento;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Model.UMLModel;


public class UMLEditor {
	// The model that is being edited
	private UMLModel model;
	// Holds the class actively being worked on
	private ClassObject activeClass;
	// Memento object keeps track of previous states
	private UMLMemento memento = new UMLMemento();


	/**
	 * Constructs an instance of the UMLEditor
	 * 
	 * @param model | The list of existing classes and relationships
	 */
	public UMLEditor(UMLModel model) {
		this.model = model;
		activeClass = null;
		memento.saveState(this.model);
	}

	/**
	 * Set the activeClass to null
	 */
	private void resetActiveClass() {
		activeClass = null;
	}

	/**
	 * Adds a new class to the list of classes
	 * 
	 * @param newClass | ClassObject to be added to classList
	 */
	public void addClass(String newClassName) {
		ClassObject newClass = new ClassObject(newClassName);
		model.getClassList().add(newClass);
		memento.saveState(this.model);
	}

	/**
	 * Deletes specified class from classList
	 * 
	 * @param className | Name of class to be deleted
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean deleteClass(String className) throws Exception{
		activeClass = model.fetchClass(className);
		if (activeClass != null) {
			// Class exists
			// Check if activeClass is part of any relationships
			// Delete relationship if yes
			Relationship curRelationship;
			for (int index = 0; index < model.getRelationshipList().size(); index++) {
				// Iterate through relationship list to check if activeClass is part of any
				curRelationship = model.getRelationshipList().get(index);
				if (curRelationship.getSource().getName().equals(className)) {
					// Current relationship contains class being deleted
					deleteRelationship(className, curRelationship.getDestination().getName());
					// Decrement index because next class will be at same index
					index--;
				} else if (curRelationship.getDestination().getName().equals(className)) {
					deleteRelationship(curRelationship.getSource().getName(), className);
					// Decrement index because next class will be at same index
					index--;
				} else {
					// Current relationship does not contain class being deleted
				}
			}
			model.getClassList().remove(activeClass);
			resetActiveClass();
			memento.saveState(this.model);
			return true;
		}
		return false;
	}

	/**
	 * Renames a class
	 * 
	 * @param renameClass | The class being renamed
	 * @param newName     | New name to give the class
	 */
	public void renameClass(ClassObject renameClass, String newName) {
		renameClass.setName(newName);
		memento.saveState(this.model);
	}

	/**
	 * Adds relationship to relationshipList Updates relationshipLength if newRel's
	 * name is new longest name
	 * 
	 * @param newRel | Relationship to be added to relationshipList
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean addRelationship(String source, String dest, Type type) {
		try {
			ClassObject sourceClass = model.fetchClass(source);
				// Source class does exist
			ClassObject destClass = model.fetchClass(dest);
					// Destination class does exist
					// Check if relationship already exists
					if (!model.relationshipExist(source, dest)) {
						// Relationship does not already exist
						// Create new Relationship
						Relationship newRel = new Relationship(sourceClass, destClass, type);
						model.getRelationshipList().add(newRel);
						memento.saveState(this.model);
						return true;
					}
			// Adding relationship failed
			
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * Deletes the indicated relationship from relationshipList
	 * 
	 * @param delRel | Relationship to be removed from relationshipList
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean deleteRelationship(String source, String dest) {
		// Check if relationship exists
		try {
			Relationship relExist = model.fetchRelationship(source, dest);
			if (relExist != null) {
				// Relationship does exist
				model.getRelationshipList().remove(relExist);
				memento.saveState(this.model);
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public void editRelationship(String source, String dest, String fieldToUpdate, String newValue) throws Exception{
		try {
			Relationship relExist = model.fetchRelationship(source, dest);
			if (fieldToUpdate.equals("source")){
				if(model.fetchClass(newValue)!=null)
					relExist.setSource(model.fetchClass(newValue));
				//else return 2;
			}
			else if (fieldToUpdate.equals("destination")){
				if(model.fetchClass(newValue)!=null)
					relExist.setDestination(model.fetchClass(newValue));
				//else return 2;
			}
			else if (fieldToUpdate.equals("type")){
				if(newValue.equals("AGGREGATION")){
					relExist.setType(Type.AGGREGATION);
				}
				else if(newValue.equals("COMPOSITION")){
					relExist.setType(Type.COMPOSITION);
				}
				else if(newValue.equals("INHERITANCE")){
					relExist.setType(Type.INHERITANCE);
				}
				else if(newValue.equals("REALIZATION")){
					relExist.setType(Type.REALIZATION);
				}
			}
			memento.saveState(this.model);
		} catch (Exception e) {
		}
	}


	/**
	 * Adds a field to the designated ClassObject
	 * 
	 * @param cls       | The class the method is being added to
	 * @param fieldName | The name of the field
	 * @throws Exception
	 */
	public void addField(ClassObject cls, String fieldName, String fieldType) throws Exception {
		if (cls.fieldNameUsed(fieldName)) {
			throw new Exception ("Field " + fieldName + " is already in " + cls.getName());
		} else if (fieldType.isEmpty()) {
			throw new Exception ("Fields must have a type");
		} else {
			Field fld = new Field(fieldName, fieldType);
			cls.addAttribute(fld);
			memento.saveState(this.model);
		}
	}

	/**
	 * Adds a method to the designated ClassObject
	 * 
	 * @param cls        | The class the method is being added to
	 * @param methodName | The name of the method
	 * @param retType	 | The return type of the method
	 * @param paramList  | The parameter list the method will have
	 * @throws Exception
	 */
	public void addMethod(ClassObject cls, String methodName, String retType, LinkedHashMap<String, String> paramNameList) throws Exception {
		/*
		if (cls.methodExists(methodName, paramNameList.size())) {
			throw new Exception ("Method " + methodName + " with " + paramNameList.size() + " parameters already exists in " + cls.getName());
		} else {
			ArrayList<Parameter> paramList= new ArrayList<>();
			Parameter param;
			for (Map.Entry<String, String> obj : paramNameList.entrySet()) {
				param = new Parameter(obj.getKey(), obj.getValue());
				paramList.add(param);
			}
			Method method = new Method(methodName, retType, paramList);
			cls.addAttribute(method);
			memento.saveState(model);
		}
		*/
		// Check all methods for name and paramTypes
		attrloop:
		for (AttributeInterface attr : cls.getMethodList()) {
			if (!attr.getName().equals(methodName)) {
				// Name does not match method being created, move on to the next
				continue;
			}
			Method activeMethod = (Method) attr;
			if (activeMethod.getParamList().size() != paramNameList.size()) {
				// Number of params does not match, move onto next attr
				continue;
			}
			int index = 0;
			for (String type : paramNameList.values()) {
				if (!type.equals(activeMethod.getParamList().get(index).getType())) {
					// Parameter at given index does not match type in same position
					// Move onto the next attribute
					continue attrloop;
				}
				index++;
			}
			// Method has same name, arity, and types as an existing method
			throw new Exception ("Method " + methodName + " already exists in " + cls.getName());
		}
		// No methods matched the one being created
		ArrayList<Parameter> paramList = new ArrayList<>();
		Parameter param;
		for (Map.Entry<String, String> obj : paramNameList.entrySet()) {
			param = new Parameter(obj.getKey(), obj.getValue());
			paramList.add(param);
		}
		Method method = new Method(methodName, retType, paramList);
		cls.addAttribute(method);
		memento.saveState(model);
	}

	/**
	 * Deletes fields from the designated ClassObject
	 * 
	 * @param cls     	| The class from which the field/method is to be deleted from
	 * @param fieldName | The field/method to be removed
	 * @throws Exception
	 */
	public void deleteField(ClassObject cls, String fieldName) throws Exception {
		if (!cls.fieldNameUsed(fieldName)) {
			throw new Exception ("Class " + cls.getName() + " does not have a field named " + fieldName);
		} else {
			Field delField = cls.fetchField(fieldName);
			cls.removeAttribute(delField);
			memento.saveState(this.model);
		}
	}

	/**
	 * Deletes methods from the designated ClassObject
	 * 
	 * @param cls     	 | The class from which the method is to be deleted from
	 * @param mthd		 | The method being deleted
	 * @throws Exception
	 */

	public void deleteMethod(ClassObject cls, Method mthd) throws Exception {
		cls.removeAttribute(mthd);
		memento.saveState(this.model);
	}

	/**
	 * Renames fields or methods from the designated ClassObject
	 * 
	 * @param cls		 | The class with the field
	 * @param renameAttr | The field being renamed
	 * @param newName    | The new name for the field
	 * @throws Exception
	 */
	public void renameField(ClassObject cls, AttributeInterface renameAttr, String newName) throws Exception {
		if (cls.fieldNameUsed(newName)) {
			throw new Exception (newName + " is currently used by another field in the class");
		} else {
			renameAttr.renameAttribute(newName);
			memento.saveState(this.model);
		}
	}

	public void changeFieldType(Field fld, String newType) throws Exception {
		if (newType.isEmpty()) {
			throw new Exception ("Fields must have a type");
		} else {
			fld.setVarType(newType);
			memento.saveState(this.model);
		}
	}

	/**
	 * Renames fields or methods from the designated ClassObject
	 * 
	 * @param cls		   | The class with the method
	 * @param renameMethod | The method being renamed
	 * @param newName      | The new name for the method
	 * @throws Exception
	 */
	public void renameMethod(ClassObject cls, Method renameMethod, String newName) throws Exception {
		if (renameMethod.getName().equals(newName)) {
			return;
		}
		if (cls.methodExists(newName, renameMethod.getParamList())) {
			throw new Exception (newName + " is currently used by another method in the class");
		} else {
			renameMethod.renameAttribute(newName);
			memento.saveState(this.model);
		}
	}

	public void changeMethodType(Method mthd, String newType) {
		mthd.setReturnType(newType);
		memento.saveState(this.model);
	}

	/**
	 * Adds a parameter to a list of a methods parameter list
	 * 
	 * @param parameterList | The list of parameters being added to the methods parameter list
	 * @param currMethod    | the current Method of which new parameters are being added
	 */
	public void addParam(LinkedHashMap<String,String> parameterMap, Method currMethod) {
		currMethod.addParameters(parameterMap);
		memento.saveState(this.model);
	}

	/**
	 * Removes all the parameters from a given parameter list
	 * 
	 * @param activeMethod | The method whos parameter list is going to be deleted.
	 */
	public void removeAllParams(Method activeMethod) {
		activeMethod.removeAllParameters();
		memento.saveState(this.model);
	}

	/**
	 * removes a singular parameter from a methods list
	 * 
	 * @param activeMethod | the current method that the parameter will be removed from
	 * @param param        | the parameter that will be removed from the parameter list
	 */
	public void removeParam(Method activeMethod, Parameter param) {
		activeMethod.removeParameter(param);
		memento.saveState(this.model);
	}
	/**
	 * Changes all the parameters of a method by replacing it with a new list
	 * 
	 * @param activeMethod  | The method that will have all of its parameters replaced
	 * @param parameterList | The list of new parameters that will replace the old list
	 */
	public void changeAllParams(Method activeMethod, LinkedHashMap<String, String> parameterList) {
		activeMethod.removeAllParameters();
		activeMethod.addParameters(parameterList);
		memento.saveState(this.model);
	}
	/**
	 * Changes a parameter in a method with a new list of parameters
	 * 
	 * @param activeMethod        | The method which will have its parameter list changed
	 * @param oldParam            | The old parameter where the new list will be inserted
	 * @param parameterStringList | The new parameter list set as a string to be added as parameter type
	 */
	public void changeParameter(Method activeMethod, Parameter oldParam, LinkedHashMap<String, String> parameterStringList) {
		int index = activeMethod.getParamList().indexOf(oldParam);
		ArrayList<Parameter> parameterParamList = new ArrayList<>();
        Parameter param;
        for (Map.Entry<String,String> obj : parameterStringList.entrySet()) {
            param = new Parameter(obj.getKey(), obj.getValue());
            parameterParamList.add(param);
        }
		activeMethod.getParamList().addAll(index, parameterParamList);
		activeMethod.getParamList().remove(oldParam);
		memento.saveState(this.model);
	}
	
	public boolean nameAlrAdded(String paramName, LinkedHashMap<String, String> buildParamNameList) {
		for(String name : buildParamNameList.keySet()) {
			if(name.equals(paramName)) {
				return true;
			}
		}
		return false;
	}

	/** Restores a previous state of the model using methods from the memento class
	 * 
	 */
	public void undo() throws Exception {
		// Get previous model or an exception that nothing can be undone
		// Replace the current model with the previous one
		UMLModel prevState = memento.undoState();
		this.model = prevState;
	}

	/** Restores a state of the model prior to an undo operation using methods from the memento class
	 * 
	 */
	public void redo() throws Exception {
		// Get "next" model or an exception that nothing can be redone
		// Replace the current model with the next one
		UMLModel nextState = memento.redoState();
		this.model = nextState;
	}

	public UMLModel getModel()
	{return this.model;}
	
}
