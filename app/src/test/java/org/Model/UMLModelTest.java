package org.Model;

import java.util.ArrayList;

import org.Model.Relationship.Type;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

		b.addAttribute(new Field("zap"));
		b.addAttribute(new Field("zoop"));
		b.addAttribute(new Method("m", p));
		b.addAttribute(new Method("m", new ArrayList<>()));

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
		assertTrue(m.listClassInfo(f).contains("foo"));
		assertTrue(m.listClassInfo(b).contains("bar"));
		assertTrue(m.listClassInfo(null).equals(""));
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
			b.addAttribute(new Field(i.toString()));
		assertDoesNotThrow(() -> m.listFields(b));
	}

	@Test
	public void listMethods() {
		assertThrows(java.lang.Exception.class, () -> m.listMethods(f));
		assertDoesNotThrow(() -> m.listMethods(b));

		/* cover new line after 6 fields branch */
		for (Integer i = 0; i <= 12; i++)
			b.addAttribute(new Method(i.toString(), new ArrayList<>()));
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
}
