package org.Model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.Model.Parameter;

public class ParameterTest{
    Parameter p;

    @Test
    public void constructor(){
	p = new Parameter("test");
	assertEquals(p.getName(), "test");
    }

    @Test
    public void rename(){
	p = new Parameter("test");
	p.setName("test2");
	assertEquals(p.getName(), "test2");
    }
}
