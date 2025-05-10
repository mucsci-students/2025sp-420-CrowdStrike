package org.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ParameterTest {
	Parameter p;

	@Test
	public void constructor() {
		p = new Parameter("test", "String");
		assertEquals(p.getName(), "test");
	}

	@Test
	public void rename() {
		p = new Parameter("test", "int");
		p.setName("test2");
		assertEquals(p.getName(), "test2");
	}

	@Test
	public void callSetType() {
		p = new Parameter("test", "String");
		assertEquals("String", p.setType());
	}
}
