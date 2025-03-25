package org.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

public class ClassObjectTest {
	ClassObject c;

	@BeforeEach
	public void init() {
		c = new ClassObject("test");
	}

	@Test
	public void name() {
		assertEquals("test", c.getName());

		c.setName("test2");
		assertEquals("test2", c.getName());
	}

	@Test
	public void getLists() {
		assertNotNull(c.getAttrMap());
		assertNotNull(c.getFieldList());
		assertNotNull(c.getMethodList());
	}

	@Test
	public void attrAddDel() {
		Method m = new Method("test", new ArrayList<>());
		Field f = new Field("test");

		c.addAttribute(m);
		assertTrue(c.getMethodList().contains(m));
		c.removeAttribute(m);
		assertFalse(c.getMethodList().contains(m));

		c.addAttribute(f);
		assertTrue(c.getFieldList().contains(f));
		c.removeAttribute(f);
		assertFalse(c.getFieldList().contains(f));
	}

	@Test
	public void used() {
		Method m = new Method("test", new ArrayList<>());
		Field f = new Field("test");
		c.addAttribute(m);
		c.addAttribute(f);

		assertTrue(c.fieldNameUsed("test"));
		assertFalse(c.fieldNameUsed("test123"));

		assertTrue(c.methodExists("test", 0));
		assertFalse(c.methodExists("test", 12));
		assertFalse(c.methodExists("test123", 0));
	}

	@Test
	public void fetch() {
		Method m = new Method("test", new ArrayList<>());
		Field f = new Field("test");
		c.addAttribute(m);
		c.addAttribute(f);
		c.addAttribute(new Method("foo", new ArrayList<>()));

		assertDoesNotThrow(() -> c.fetchField("test"));
		assertThrows(java.lang.Exception.class, () -> c.fetchField("none"));

		assertDoesNotThrow(() -> c.fetchMethod("test", 0));
		assertThrows(java.lang.Exception.class, () -> c.fetchMethod("test", 1));
		assertThrows(java.lang.Exception.class, () -> c.fetchMethod("test123", 0));

		assertTrue(c.fetchMethodByName("test").contains(m));
	}

}
