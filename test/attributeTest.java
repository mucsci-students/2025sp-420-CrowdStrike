import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class attributeTest {

		
	@Test
	public void testGetName() {
		Attribute test = new Attribute("testName");
		assertEquals("testName", test.getName(),"The name should be 'testName'");
	}
		
	@Test
	public void testRenameName() {
		Attribute test = new Attribute("testing");
		test.renameAttribute("newName");
		assertEquals("newName", test.getName(),"The name should be changed to 'newName'");
	}
}
