package org.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Model.ClassObject;

public class RelationshipTest{
    Relationship r;
    ClassObject s,d;

    @BeforeEach
    public void init(){
	s = new ClassObject("s");
	d = new ClassObject("d");
	r = new Relationship(s,d,Type.AGGREGATION);
    }

    @Test
    public void getClasses(){
	assertEquals(r.getSource(),s,"wrong src");
	assertEquals(r.getDestination(),d,"wrong dest");
    }

    @Test
    public void setClasses(){
	r.setSource(d);
	r.setDestination(s);
	assertEquals(r.getSource(),d,"wrong src");
	assertEquals(r.getDestination(),s,"wrong dest");
    }

    @Test
    public void id(){
	Integer i = null;
	i = r.getID();
	
	assertNotEquals(i,null,"returned null id");
    }

    @Test
    public void type(){
	assertEquals(r.getType(),Type.AGGREGATION);
	assertEquals(r.getTypeString(),"Aggregation");

	r.setType(Type.COMPOSITION);
	assertEquals(r.getType(),Type.COMPOSITION);
	assertEquals(r.getTypeString(),"Composition");

	r.setType(Type.INHERITANCE);
	assertEquals(r.getType(),Type.INHERITANCE);
	assertEquals(r.getTypeString(),"Inheritance");

	r.setType(Type.REALIZATION);
	assertEquals(r.getType(),Type.REALIZATION);
	assertEquals(r.getTypeString(),"Realization");
    }
}
