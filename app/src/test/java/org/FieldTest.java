package test.java.org;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Field;
import org.Controller.UMLEditor;

public class FieldTest {

    UMLModel testModel = new UMLModel();
    UMLEditor testEditor = new UMLEditor(testModel);
    ClassObject class1;

    public void populateClasses() {
        testEditor.addClass("class1");
        class1 = testModel.fetchClass("class1");
    }

    public void testGetName() {
        populateClasses();
        testEditor.addField(class1, "field1");
        Field fld = class1.fetchField("field1");
        assertEquals(fld.getName(), "field1", "fld name should be 'field1'");
    }

    public void testGetType() {
        populateClasses();
        testEditor.addField(class1, "field1");
        Field fld = class1.fetchField("field1");
        assertEquals(fld.getType(), "Field", "fld type should be 'Field'");
    }

    public void testRenameField() {
        populateClasses();
        testEditor.addField(class1, "field1");
        Field fld = class1.fetchField("field1");
        testEditor.renameAttribute(fld, "newName");
        assertEquals(fld.getName(), "newName", "fld name should be 'newName'");
    }
}