package org.Model;

import java.util.ArrayList;

import org.Model.Relationship.Type;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UMLModelTest {
	private UMLModel m;
	private ClassObject f, b;

	@BeforeEach
	public void init() {
		m = new UMLModel();
		f = new ClassObject("foo");
		b = new ClassObject("bar");
		ArrayList<ClassObject> cl = m.getClassList();
		ArrayList<Relationship> rl = m.getRelationshipList();
		cl.add(f);
		cl.add(b);

		ArrayList<Parameter> p = new ArrayList<>();
		p.add(new Parameter("param1", "String"));
		p.add(new Parameter("param2", "Int"));

		b.addAttribute(new Field("zap", "int"));
		b.addAttribute(new Field("zoop", "String"));
		b.addAttribute(new Method("m", "void", p));
		b.addAttribute(new Method("m", "void", new ArrayList<>()));

		rl.add(new Relationship(f, b, Type.INHERITANCE));

	}

	@Test
	public void arityValid() {
		assertThrows(java.lang.Exception.class, () -> m.arityValid(-1));
		assertDoesNotThrow(() -> m.arityValid(12));
	}

	@Test
	public void fetchClass() {
		assertThrows(java.lang.Exception.class, () -> m.fetchClass("bazz"));
		assertDoesNotThrow(() -> m.fetchClass("foo"));
	}

	@Test
	public void classNameUsed() {
		assertTrue(m.classNameUsed("bar"));
		assertFalse(m.classNameUsed("baz"));
	}

	@Test
	public void relationshipExist() {
		assertTrue(m.relationshipExist("foo", "bar"));

		assertFalse(m.relationshipExist("bizz", "bazz"));
		assertFalse(m.relationshipExist("foo", "bazz"));
	}

	@Test
	public void listClasses() {
		assertTrue(m.listClasses().contains("foo"));
		assertTrue(m.listClasses().contains("bar"));
	}

	@Test
	public void listClassInfo() {
		String infoFoo = m.listClassInfo(f);
		String infoBar = m.listClassInfo(b);

		// Check name appears
		assertTrue(infoFoo.contains("foo"));
		assertTrue(infoBar.contains("bar"));

		// Check that relationship info is somewhere
		assertTrue(infoFoo.contains("Inheritance"));
		assertTrue(infoBar.contains("Inheritance")); 

		assertTrue(infoFoo.contains("bar"));  // source points to bar
		assertTrue(infoBar.contains("foo"));  // destination points from foo

		// Also check null case
		assertEquals("", m.listClassInfo(null));
	}

	@Test
	public void listClassNames() {
		assertDoesNotThrow(() -> m.listClassNames().contains("foo"));
		assertDoesNotThrow(() -> m.listClassNames().contains("bar"));

		/* cover new lines after 6 names branch */
		for (Integer i = 0; i <= 12; i++)
			m.getClassList().add(new ClassObject(i.toString()));

		assertDoesNotThrow(() -> m.listClassNames().contains("11"));

		m.getClassList().clear();
		assertThrows(java.lang.Exception.class, () -> m.listClassNames());
	}

	@Test
	public void listRelationships() {
		assertDoesNotThrow(() -> m.listRelationships());
		m.getRelationshipList().clear();
		assertThrows(java.lang.Exception.class, () -> m.listRelationships());
	}

	@Test
	public void listFields() {
		assertThrows(java.lang.Exception.class, () -> m.listFields(f));
		assertDoesNotThrow(() -> m.listFields(b));

		/* cover new line after 6 fields branch */
		for (Integer i = 0; i <= 12; i++)
			b.addAttribute(new Field(i.toString(), "String"));
		assertDoesNotThrow(() -> m.listFields(b));
	}

	@Test
	public void listMethods() {
		assertThrows(java.lang.Exception.class, () -> m.listMethods(f));
		assertDoesNotThrow(() -> m.listMethods(b));

		/* cover new line after 6 fields branch */
		for (Integer i = 0; i <= 12; i++)
			b.addAttribute(new Method(i.toString(), "int", new ArrayList<>()));
		assertDoesNotThrow(() -> m.listMethods(b));
	}

	@Test
	public void listMethodArities() {
		assertDoesNotThrow(() -> m.listMethodArities(b, "m"));
		assertThrows(java.lang.Exception.class, () -> m.listMethodArities(f, "m"));
	}

	@Test
	public void isValidClassName() {
		assertDoesNotThrow(() -> m.isValidClassName("Bizz"));
		assertDoesNotThrow(() -> m.isValidClassName("_Bizz"));
		assertDoesNotThrow(() -> m.isValidClassName("Bizz_Buzz"));
		assertThrows(java.lang.Exception.class, () -> m.isValidClassName(""));
		assertThrows(java.lang.Exception.class, () -> m.isValidClassName(null));
		assertThrows(java.lang.Exception.class, () -> m.isValidClassName("8Test"));
		assertThrows(java.lang.Exception.class, () -> m.isValidClassName("Test*"));
		assertThrows(java.lang.Exception.class, () -> m.isValidClassName("foo"));
	}

	@Test
	public void deepCopyWithInvalidRelationship() {
		UMLModel badModel = new UMLModel();
		ClassObject a = new ClassObject("A");
		ClassObject b = new ClassObject("B");

		badModel.getClassList().add(a);
		// We intentionally do NOT add "B" to classList to simulate missing class
		badModel.getRelationshipList().add(new Relationship(a, b, Type.INHERITANCE)); // <--- Use INHERITANCE here

		// Check that deepCopy catches the missing class and does NOT crash
		assertDoesNotThrow(() -> {
			UMLModel copied = badModel.deepCopy();
		});
	}


	@Test
	public void listClassInfoDestinationBranch() {
		UMLModel m2 = new UMLModel();
		ClassObject a = new ClassObject("A");
		ClassObject b = new ClassObject("B");
		m2.getClassList().add(a);
		m2.getClassList().add(b);

		// First: b --> a (destination case)
		m2.getRelationshipList().add(new Relationship(b, a, Type.AGGREGATION));
		
		// Second: a --> b (source case)
		m2.getRelationshipList().add(new Relationship(a, b, Type.COMPOSITION));

		String info = m2.listClassInfo(a); // A is source and destination now

		assertTrue(info.contains("A"));   // Class name itself
		assertTrue(info.contains("B"));   // Destination or source name
		assertTrue(info.contains("Aggregation")); // First relationship type
		assertTrue(info.contains("Composition")); // Second relationship type
	}

	@Test
	public void listClassInfoWithUnrelatedRelationship() {
		UMLModel model = new UMLModel();
		ClassObject A = new ClassObject("A");
		ClassObject B = new ClassObject("B");
		ClassObject C = new ClassObject("C");

		model.getClassList().add(A);
		model.getClassList().add(B);
		model.getClassList().add(C);

		// C -> B relationship (A is not involved at all)
		model.getRelationshipList().add(new Relationship(C, B, Type.AGGREGATION));

		String info = model.listClassInfo(A);

		// Should still work and not crash
		assertTrue(info.contains("A")); // Class name is shown
	}

	


}
