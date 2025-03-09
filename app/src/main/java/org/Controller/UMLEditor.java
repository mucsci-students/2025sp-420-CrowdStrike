package org.Controller;
import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Model.AttributeInterface;
import org.Model.Field;
import org.Model.Method;
import org.Model.Parameter;
import java.util.ArrayList;

public class UMLEditor {
	// The model that is being edited
	private UMLModel model;
	// Holds the class actively being worked on
	private ClassObject activeClass;

	/**
	 * Constructs an instance of the UMLEditor
	 * 
	 * @param model | The list of existing classes and relationships
	 */
	public UMLEditor(UMLModel model) {
		this.model = model;
		activeClass = null;
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

	}

	/**
	 * Deletes specified class from classList
	 * 
	 * @param className | Name of class to be deleted
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean deleteClass(String className) {
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
	}

	/**
	 * Adds relationship to relationshipList Updates relationshipLength if newRel's
	 * name is new longest name
	 * 
	 * @param newRel | Relationship to be added to relationshipList
	 * @return True if operation succeeded, false otherwise
	 */
	public boolean addRelationship(String name, String source, String dest, Type type) {
		ClassObject sourceClass = model.fetchClass(source);
		if (source != null) {
			// Source class does exist
			ClassObject destClass = model.fetchClass(dest);
			if (destClass != null) {
				// Destination class does exist
				// Check if relationship already exists
				if (model.relationshipExist(source, dest, type) == null) {
					// Relationship does not already exist
					// Create new Relationship
					Relationship newRel = new Relationship(name, sourceClass, destClass, type);
					model.getRelationshipList().add(newRel);
					return true;
				}
			}
		}
		// Adding relationship failed
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
		Relationship relExist = model.relationshipExist(source, dest);
		if (relExist != null) {
			// Relationship does exist
				model.getRelationshipList().remove(relExist);
			
			return true;
		}
		return false;
	}

	public void editRelationship(String source, String dest, String fieldToUpdate, String newValue){
		Relationship relExist = model.relationshipExist(source, dest);
		if (fieldToUpdate.equals("name")){
			relExist.setName(newValue);
		}
		else if (fieldToUpdate.equals("source")){
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
		else
		{
			//return 1;
		}
		//oreturn 0;
	}


	/**
	 * Adds a field to the designated ClassObject
	 * 
	 * @param cls			| The class the method is being added to
	 * @param fieldName		| The name of the field
	 */
	public void addField(ClassObject cls, String fieldName) {
		Field fld = new Field(fieldName);
		cls.addAttribute(fld);
	}
	
	/**
	 * Adds a method to the designated ClassObject
	 * 
	 * @param cls			| The class the method is being added to
	 * @param methodName	| The name of the method
	 * @param paramList		| The parameter list the method will have
	 */
	public void addMethod(ClassObject cls, String methodName, ArrayList<String> paramNameList) {
		ArrayList<Parameter> paramList= new ArrayList<>();
		Parameter param;
		for (int i = 0; i < paramNameList.size(); i++) {
			param = new Parameter(paramNameList.get(i));
			paramList.add(param);
		}
		Method method = new Method(methodName, paramList);
		cls.addAttribute(method);
	}

	/**
	 * Deletes fields or methods from the designated ClassObject
	 * 
	 * @param cls 		| The class from which the field/method is to be deleted from
	 * @param delAttr   | The field/method to be removed
	 */
	public void deleteAttribute(ClassObject cls, AttributeInterface delAttr) {
		cls.removeAttribute(delAttr);
	}
	
	/**
	 * Renames fields or methods from the designated ClassObject
	 * 
	 * @param renameAttr	| The filed/method being renamed
	 * @param newName		| The new name for the field/method
	 */
	public void renameAttribute(AttributeInterface renameAttr, String newName) {
		renameAttr.renameAttribute(newName);
	}
}
