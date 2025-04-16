package org.Model;

import org.Controller.UMLEditor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MementoTest {

    UMLModel testModel = new UMLModel();
    UMLEditor testEditor = new UMLEditor(testModel);
    ClassObject class1;
    UMLMemento memento = new UMLMemento();

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
    public void testSaveState() {
        memento.saveState(testModel);
    }

    @Test
    public void testUndoState() {}

    @Test
    public void failTestUndoState() {
        // should throw exception
        // memento.undoState();
    }

    @Test
    public void testRedoState() {}

    @Test
    public void failTestRedoState() {}
}
