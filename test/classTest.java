import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class classTest {
	UMLModel testModel = new UMLModel();
	UMLEditor testEditor = new UMLEditor(testModel);
	ClassObject class1;
	ClassObject class2;
	
	public void populateClass() {
		testEditor.addClass("class1");
		class1 = testModel.fetchClass("class1");
		testEditor.addClass("2class");
		class2 = testModel.fetchClass("2class");
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
	
	public void getEmptyAttrList() {
		ArrayList <Attribute> attrs = new ArrayList<>();
		assertEquals(class1.getAttrList().equals(attrs), true , "should return an empty list");
	}
	
	public void getPopulatedAttrList() {
		testEditor.addAttribute("class1", "one");
		testEditor.addAttribute("class1", "two");
		testEditor.addAttribute("class1", "six");
		ArrayList <Attribute> attrs = new ArrayList<>();
		attrs.add(class1.fetchAttribute("one"));
		attrs.add(class1.fetchAttribute("two"));
		attrs.add(class1.fetchAttribute("six"));
		
		assertEquals(class1.getAttrList().equals(attrs), true , "should return a list containting ['one','two','six'] ");
	}
	
	public void addAttribute(Attribute attr) {

		assertEquals(testEditor.addAttribute("class1", "one"), true , "should return true since the attribute was added");
	}
	public void addAttributeToNonExistingClass(Attribute attr) {
		assertEquals(testEditor.addAttribute("orange", "one"), false , "should return false since the class doesnt exist");
	}
	
	public void addAddributeAlreadyExists(Attribute attr) {
		testEditor.addAttribute("class1", "one");
		assertEquals(testEditor.addAttribute("class1", "one"), false , "should return false since the attribute already exists ");
	}
	
	public void removeAttribute(Attribute attr) {
		testEditor.addAttribute("class1", "one");
		assertEquals(testEditor.deleteAttribute("class1", "one"), true , "Should return true as the attribute was removed");
	}
	public void removeAttributeClassDoesntExist(Attribute attr) {
		assertEquals(testEditor.deleteAttribute("apples", "one"), false , "Should return false as the class doesnt exist");
	}
	public void removeAttributeDoesntExist(Attribute attr) {
	assertEquals(testEditor.deleteAttribute("class1", "one"), false , "Should return false as the attribute doesnt exist");
	}
	
	public void fetchAttribute(String attrName) {
		testEditor.addAttribute("class1", "one");
		assertEquals(class1.fetchAttribute("one").getName(), "one", "should return one");
	}
	
	public void fetchAttributeDoesntExist(String attrName) {
		assertEquals(class1.fetchAttribute("one"), null , "should return null");

	}

	
}
