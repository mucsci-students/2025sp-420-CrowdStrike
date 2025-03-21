package org;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.Model.UMLModel;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Controller.UMLEditor;

public class relationshipTest {
	UMLModel testModel = new UMLModel();
	UMLEditor testEditor = new UMLEditor(testModel);
	
	public void populateClasses() {
		testEditor.addClass("class1");
    	testEditor.addClass("class2");
	}
	
    public void getSource(){
    	populateClasses();
    	testEditor.addRelationship("class1", "class2", Type.REALIZATION);
    	Relationship rel = testModel.relationshipExist("class1", "class2");
		
		assertEquals(rel.getSource(), testModel.fetchClass("class1"), "source should equal to 'class1' ");
    }
    
    public void getDestination(){
    	populateClasses();
    	testEditor.addRelationship("class1", "class2", Type.REALIZATION);
    	Relationship rel = testModel.relationshipExist("class1", "class2");
	
    	
    	assertEquals(rel.getDestination(), testModel.fetchClass("class2"), "destination should be equal to 'class2' ");
    	
    }
    
    public void getID(){
    	populateClasses();
    	testEditor.addRelationship("class1", "class2", Type.REALIZATION);
    	Relationship rel = testModel.relationshipExist("class1", "class2");
    	int class1Hash = testModel.fetchClass("class1").hashCode();
    	int class2Hash = testModel.fetchClass("class2").hashCode();
    	
    	assertEquals(rel.getID(),class1Hash + class2Hash ,"Should return the same value");
    }

}
