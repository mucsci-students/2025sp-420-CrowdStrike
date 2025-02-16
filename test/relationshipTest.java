import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class relationshipTest {
	ClassObject class1 = new ClassObject("class1");
	ClassObject class2 = new ClassObject("class2");

    public void getSource(){
    	Relationship relTest = new Relationship("testSource", class1, class2);
		ClassObject testSource = relTest.getSource();
		
		assertEquals(testSource, relTest.getSource(), "relTest's source should equal testSource");
    }
    
    public void getDestination(){
    	Relationship relTest = new Relationship("testDest", class1, class2);
    	ClassObject testDestination = relTest.getDestination();
		
    	assertEquals(testDestination, relTest.getDestination(), "relTest's destination should be testDestination");
    	
    }
    
    public void getName(){
    	Relationship relTest = new Relationship("testName", class1, class2);
    	String testName = relTest.getName();
    	
    	assertEquals(testName, relTest.getName(), "relTest.getName()should return 'testName' ");
    	
    }
    
    public void getID(){
    	Relationship relTest = new Relationship("test", class1, class2);
    	int testID = relTest.getID();
    	
    	assertEquals(testID, relTest.getID(), "relTest.getID(), should match testID");
    }
    
    public void setName(String newName) {
    	Relationship relTest = new Relationship("test", class1, class2);
    	relTest.setName("newTestName");
    	
    	assertEquals("newTestName", relTest.getName(), "relTest's name shoult be changed from 'test' to 'newTestName' ");
    	
    }




}
