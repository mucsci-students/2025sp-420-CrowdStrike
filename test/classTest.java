import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class classTest {
	ClassObject classTest = new ClassObject("classTest");

	public void getName() {
		String className = classTest.getName();
		
		assertEquals(className, classTest.getName(), "classTest's name should be equal to 'className' ");
	}
	
	public void setName(String newName) {
		classTest.setName("newClassName");
		
		assertEquals("newClassName", classTest.getName(), "classTest.getName() should return 'newClassName' ");
	}
	
	public void getAttrList() {
		ArrayList<Attribute> testList = classTest.getAttrList();
		
		assertEquals(testList, classTest.getAttrList(), "classTest's attrList should be testList");
	}
	
	public void addAttribute(Attribute attr) {
		classTest.addAttribute(attr);
		
		assertEquals(attr, classTest.getAttrList(), "attr should be the only thing in the list");
	}
	
	public void removeAttribute(Attribute attr) {
		classTest.addAttribute(attr);
		classTest.removeAttribute(attr);
		
		assertEquals("", classTest.getAttrList(), "There should be nothing in the attrList");

	}
	
	public void fetchAttribute(String attrName) {
		Attribute testFetch = classTest.fetchAttribute(attrName);
		
		assertEquals(testFetch, classTest.fetchAttribute(attrName), "testFetch should equal classTest.fetchAttribute(attrName)");
	}
	
}
