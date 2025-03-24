package org.Model;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.security.PublicKey;
import java.util.ArrayList;

public class ClassObjectTest {
    ClassObject c;

    @BeforeEach
    public void init(){
	c = new ClassObject("test");
    }

    @Test
    public void name(){
	assertEquals("test",c.getName());

	c.setName("test2");
	assertEquals("test2",c.getName());
    }

    @Test
    public void getLists(){
	assertNotEquals(c.getAttrMap(),null);
	assertNotEquals(c.getFieldList(),null);
	assertNotEquals(c.getMethodList(),null);
    }

    @Test
    public void attrAddDel(){
	Method m = new Method("test", new ArrayList<>());
	Field f = new Field("test");
	
	c.addAttribute(m);
	assertEquals(c.getMethodList().contains(m),true);
	c.removeAttribute(m);
	assertEquals(c.getMethodList().contains(m),false);

	c.addAttribute(f);
	assertEquals(c.getFieldList().contains(f),true);
	c.removeAttribute(f);
	assertEquals(c.getFieldList().contains(f),false);
    }

    @Test
    public void used(){
	Method m = new Method("test", new ArrayList<>());
	Field f = new Field("test");
	c.addAttribute(m);
	c.addAttribute(f);

	assertEquals(c.fieldNameUsed("test"),true);
	assertEquals(c.fieldNameUsed("test123"),false);

	assertEquals(c.methodExists("test",0),true);
	assertEquals(c.methodExists("test",12),false);
	assertEquals(c.methodExists("test123",0),false);
    }

    @Test
    public void fetch(){
	Method m = new Method("test", new ArrayList<>());
	Field f = new Field("test");
	c.addAttribute(m);
	c.addAttribute(f);
	c.addAttribute(new Method("foo", new ArrayList<>()));

	assertDoesNotThrow(() -> c.fetchField("test"));
	assertThrows(java.lang.Exception.class,() -> c.fetchField("none"));

	assertDoesNotThrow(() -> c.fetchMethod("test",0));
	assertThrows(java.lang.Exception.class, () -> c.fetchMethod("test",1));
	assertThrows(java.lang.Exception.class, () -> c.fetchMethod("test123",0));


	assertEquals(c.fetchMethodByName("test").contains(m),true);

	
    }
    
}
