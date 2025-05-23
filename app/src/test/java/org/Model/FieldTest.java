package org.Model;

import org.Controller.UMLEditor;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldTest {

    UMLModel testModel = new UMLModel();
    UMLEditor testEditor = new UMLEditor(testModel);
    ClassObject class1;
    Field fld;

    @BeforeEach
    public void populateClasses() {
        try {
            testEditor.addClass("class1");
            class1 = testModel.fetchClass("class1");
            testEditor.addField(class1, "field1", "int");
            fld = class1.fetchField("field1");
        } catch (Exception e) {
        }
       
    }

    @AfterEach
    public void cleanUp() {
        try {
            testEditor.deleteClass("class1");
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetName() {
        assertEquals(fld.getName(), "field1", "fld name should be 'field1'");
    }

    @Test
    public void testGetVarType() {
        assertEquals(fld.getVarType(), "int", "fld varType should be 'int");
    }

    @Test
    public void testGetType() {
        assertEquals(fld.getType(), "Field", "fld type should be 'Field'");
    }

    @Test
    public void testRenameField() {
        try {
            testEditor.renameField(class1, fld, "newName");
            assertEquals(fld.getName(), "newName", "fld name should be 'newName'");
        } catch (Exception e) {
        }
    }

    @Test
    public void testSetVarType() {
        fld.setVarType("String");
        assertEquals(fld.getVarType(), "String", "fld varType should now be String");
    }

    @Test
    public void testToString() {
        // Expected format: varType + " " + name
        assertEquals("int field1", fld.toString(), "Field toString() should return 'int field1'");

        // Also check after changing the varType and name
        fld.setVarType("String");
        fld.renameAttribute("newField");
        assertEquals("String newField", fld.toString(), "Field toString() should return 'String newField' after changes");
    }



}