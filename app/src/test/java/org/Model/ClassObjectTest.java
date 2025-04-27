package org.Model;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		Method m = new Method("test", "void", new ArrayList<>());
		Field f = new Field("test", "int");

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
		Method m = new Method("test","void", new ArrayList<>());
		Field f = new Field("test", "int");
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
		Method m = new Method("test", "void", new ArrayList<>());
		Field f = new Field("test", "int");
		c.addAttribute(m);
		c.addAttribute(f);
		c.addAttribute(new Method("foo", "int", new ArrayList<>()));

		assertDoesNotThrow(() -> c.fetchField("test"));
		assertThrows(java.lang.Exception.class, () -> c.fetchField("none"));

		assertDoesNotThrow(() -> c.fetchMethod("test", 0));
		assertThrows(java.lang.Exception.class, () -> c.fetchMethod("test", 1));
		assertThrows(java.lang.Exception.class, () -> c.fetchMethod("test123", 0));

		assertTrue(c.fetchMethodByName("test").contains(m));
	}

	@Test
	public void fetchMethodString() {
		Method m = new Method("testMethod", "void", new ArrayList<>());
		c.addAttribute(m);

		assertDoesNotThrow(() -> c.fetchMethod("testMethod"));
		assertThrows(Exception.class, () -> c.fetchMethod("nonExistentMethod"));
	}

	@Test
	public void fetchMethodLinkedHashMap() {
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(new Parameter("param", "String"));
		Method m = new Method("testMethod", "void", params);
		c.addAttribute(m);

		java.util.LinkedHashMap<String, String> correctParams = new java.util.LinkedHashMap<>();
		correctParams.put("param", "String");

		assertDoesNotThrow(() -> c.fetchMethod("testMethod", correctParams));

		java.util.LinkedHashMap<String, String> wrongParams = new java.util.LinkedHashMap<>();
		wrongParams.put("param", "Integer");

		assertThrows(Exception.class, () -> c.fetchMethod("testMethod", wrongParams));
	}

	@Test
	public void methodExistsArrayList() {
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(new Parameter("param", "String"));
		Method m = new Method("testMethod", "void", params);
		c.addAttribute(m);

		assertTrue(c.methodExists("testMethod", params));

		ArrayList<Parameter> wrongParams = new ArrayList<>();
		wrongParams.add(new Parameter("param", "int"));
		assertFalse(c.methodExists("testMethod", wrongParams));
	}

	@Test
	public void methodExistsLinkedHashMap() {
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(new Parameter("param", "String"));
		Method m = new Method("testMethod", "void", params);
		c.addAttribute(m);

		java.util.LinkedHashMap<String, String> correctParams = new java.util.LinkedHashMap<>();
		correctParams.put("param", "String");

		assertTrue(c.methodExists("testMethod", correctParams));

		java.util.LinkedHashMap<String, String> wrongParams = new java.util.LinkedHashMap<>();
		wrongParams.put("param", "int");

		assertFalse(c.methodExists("testMethod", wrongParams));
	}
	@Test
	public void copyConstructor() {
		c.addAttribute(new Field("field", "int"));
		c.addAttribute(new Method("method", "void", new ArrayList<>()));

		ClassObject copy = new ClassObject(c);

		assertEquals(c.getName(), copy.getName());
		assertEquals(c.getAttrMap().size(), copy.getAttrMap().size());
		assertNotNull(copy.getAttrMap().get("Field"));
		assertNotNull(copy.getAttrMap().get("Method"));
	}

}
