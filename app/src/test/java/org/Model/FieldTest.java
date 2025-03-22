package org.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Field;
import org.Controller.UMLEditor;

public class FieldTest {

    UMLModel testModel = new UMLModel();
    UMLEditor testEditor = new UMLEditor(testModel);
    ClassObject class1;

    @BeforeEach
    public void populateClasses() {
        try {
            testEditor.addClass("class1");
            class1 = testModel.fetchClass("class1");
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
        testEditor.addField(class1, "field1");
        Field fld = class1.fetchField("field1");
        assertEquals(fld.getName(), "field1", "fld name should be 'field1'");
    }

    @Test
    public void testGetType() {
        testEditor.addField(class1, "field1");
        Field fld = class1.fetchField("field1");
        assertEquals(fld.getType(), "Field", "fld type should be 'Field'");
    }

    @Test
    public void testRenameField() {
        testEditor.addField(class1, "field1");
        Field fld = class1.fetchField("field1");
        testEditor.renameAttribute(fld, "newName");
        assertEquals(fld.getName(), "newName", "fld name should be 'newName'");
    }
}