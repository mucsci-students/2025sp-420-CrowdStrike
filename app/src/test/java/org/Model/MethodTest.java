package org.Model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MethodTest {
	Method m;
	ArrayList<Parameter> p;

	@BeforeEach
	public void init() {
		p = new ArrayList<>();
		p.add(new Parameter("bar", "int"));
		p.add(new Parameter("baz", "String"));
		p.add(new Parameter("buz", "boolean"));
		m = new Method("foo", "void", p);
	}

	@Test
	public void geters() {
		assertEquals(m.getName(), "foo");
		assertEquals(m.getType(), "Method");
		assertEquals(m.getParamList(), p);
	}

	@Test
	public void rename() {
		m.renameAttribute("test");
		assertEquals(m.getName(), "test");
	}

	@Test
	public void addParam() {
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.addParameter("bar", "int"));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.addParameter(null, null));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.addParameter("", ""));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.addParameter(" ", " "));

		assertFalse(m.paramUsed("test"));
		m.addParameter("test", "double");
		assertTrue(m.paramUsed("test"));

    LinkedHashMap<String, String> strs = new LinkedHashMap<>();
		for (Integer i = 0; i <= 12; i++)
			strs.put(i.toString(), i.toString());

		assertFalse(m.paramUsed("4"));
		m.addParameters(strs);
		assertTrue(m.paramUsed("4"));
	}

	@Test
	public void removeParam() {
		Parameter h = p.get(1);
		m.removeParameter(h);
		assertFalse(m.getParamList().contains(h));

		h = p.get(1);
		m.removeParameter("buz");
		assertFalse(m.getParamList().contains(h));

		assertThrows(java.lang.IllegalArgumentException.class, () -> m.removeParameter("foo"));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.removeParameter(""));

		m.removeAllParameters();
		assertEquals(m.getParamList(), new ArrayList<>());
	}

	@Test
	public void equals() {
		assertTrue(m.equals(new Method("foo", "void", p)));

		assertFalse(m.equals(new Method("foo2", "void", p)));
		assertFalse(m.equals(new Method("foo", "void", new ArrayList<>())));
	}

	@Test
	public void updateParameter() {
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.updateParameter(null, "zzz", ""));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.updateParameter(" ", "zzz", ""));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.updateParameter("buz", null, ""));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.updateParameter("buz", " ", ""));
		assertThrows(java.lang.IllegalArgumentException.class, () -> m.updateParameter("missing name", "zzz", ""));

		assertDoesNotThrow(() -> m.updateParameter("buz", "zbuz", "zub"));
		assertDoesNotThrow(() -> m.fetchParameter("zbuz"));
	}

}