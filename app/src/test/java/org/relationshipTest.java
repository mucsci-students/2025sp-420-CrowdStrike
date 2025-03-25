package org;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Controller.UMLEditor;

public class relationshipTest {
	UMLModel testModel = new UMLModel();
	UMLEditor testEditor = new UMLEditor(testModel);
	ClassObject class1;
	ClassObject class2;
	Relationship rel;
	
	@BeforeEach
	public void populateClasses() {
		try {
			testEditor.addClass("class1");
    		testEditor.addClass("class2");
			class1 = testModel.fetchClass("class1");
			class2 = testModel.fetchClass("class2");
			testEditor.addRelationship("class1", "class2", Type.REALIZATION);
    		rel = testModel.fetchRelationship("class1", "class2");
		} catch (Exception e) {
		}
	}

	@AfterEach
	public void cleanUp() {
		try {
			testEditor.deleteClass("class1");
			testEditor.deleteClass("class2");
		} catch (Exception e) {
		}
	}
	
	@Test
    public void getSource(){
		
		assertEquals(rel.getSource(), class1, "source should equal to 'class1' ");
    }
    
	@Test
    public void getDestination(){
    	assertEquals(rel.getDestination(), class2, "destination should be equal to 'class2' ");
    	
    }
    
	@Test
    public void getID(){
    	int class1Hash = class1.hashCode();
    	int class2Hash = class2.hashCode();
    	
    	assertEquals(rel.getID(),class1Hash + class2Hash ,"Should return the same value");
    }

}
