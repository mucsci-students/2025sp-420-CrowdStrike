package org.Model;

import org.Controller.UMLEditor;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MementoTest{

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
        assertDoesNotThrow(() -> {
            memento.saveState(testModel);
        });
    }

    @Test
    public void testUndoState() {
        assertDoesNotThrow(() -> {
            memento.saveState(testModel); // Save initial state
            memento.saveState(testModel); // Save second state
            UMLModel previous = memento.undoState();
            assertNotNull(previous);
        });
    }

    @Test
    public void failTestUndoState() {
        assertThrows(Exception.class, () -> {
            memento.undoState(); // Should fail, nothing to undo
        });
    }

    @Test
    public void testRedoState() {
        assertDoesNotThrow(() -> {
            memento.saveState(testModel); // Save initial state
            memento.saveState(testModel); // Save second state
            memento.undoState();          // Undo once
            UMLModel redo = memento.redoState(); // Redo
            assertNotNull(redo);
        });
    }

    @Test
    public void failTestRedoState() {
        Exception exception = assertThrows(Exception.class, () -> {
            memento.redoState(); // Should fail, nothing to redo
        });
        assertNotNull(exception); // Ensure the exception is not null
    }

    @Test
    public void testUndoStateWhenOnlyOneState() {
        assertDoesNotThrow(() -> {
            memento.saveState(testModel); // Only save one state
        });
        Exception exception = assertThrows(Exception.class, () -> {
            memento.undoState(); // Should throw because undoHistory.size() == 1
        });
        assertNotNull(exception); // Ensure the exception is not null
    }
}