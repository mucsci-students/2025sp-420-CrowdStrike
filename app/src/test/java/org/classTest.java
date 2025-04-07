package org;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Controller.UMLEditor;

public class classTest {
	UMLModel testModel = new UMLModel();
	UMLEditor testEditor = new UMLEditor(testModel);
	ClassObject class1;
	ClassObject class2;
	
	public void populateClass() {
		try {
			testEditor.addClass("class1");
			class1 = testModel.fetchClass("class1");
			testEditor.addClass("2class");
			class2 = testModel.fetchClass("2class");
		} catch (Exception e) {
		}
	}

	public void getName() {
		assertEquals(class1.getName(),"class1" , "should be equal to 'class1' ");
	}
	
	public void getNameinvalid() {
		try {
			String name = class2.getName();
		}catch(Exception e) {
			fail("Invalid class name");
		}
	}
	
	public void setName(String newName) {
		class1.setName("newTestName");
		assertEquals(class1.getName(), "newTestName" , "Should return 'newTestName' ");
	}
	
	public void setNameInvalideName(String newName) {
		class1.setName("1class");
		
		try {
			String name = class1.getName();
		}catch(Exception e) {
			fail("Invalid class name");
		}
	}
}
