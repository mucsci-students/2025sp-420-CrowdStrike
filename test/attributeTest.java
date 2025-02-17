import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class attributeTest {
	UMLModel testModel = new UMLModel();
	UMLEditor testEditor = new UMLEditor(testModel);
	ClassObject class1;
	
	public void populateClass() {
		testEditor.addClass("class1");
		class1 = testModel.fetchClass("class1");

	}
		
	@Test
	public void testGetName() {
		populateClass();
		testEditor.addAttribute("class1", "testAttr");
		
		assertEquals(class1.fetchAttribute("testAttr").getName(), "testAttr" ,"The name should be 'testAttr' ");
	}
		
	@Test
	public void testRenameName() {
		populateClass();
		testEditor.addAttribute("class1", "testAttr");
		testEditor.renameAttribute("class1", "testAttr", "betterTestAttr");
		
		
		assertEquals(class1.fetchAttribute("betterTestAttr").getName(),"betterTestAttr" ,"The name should be changed to 'betterTestAttr'");
	}
	
	
}
