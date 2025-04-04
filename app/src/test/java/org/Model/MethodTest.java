package org.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;
import org.Controller.UMLEditor;

public class MethodTest {

    UMLModel testModel = new UMLModel();
    UMLEditor testEditor = new UMLEditor(testModel);
    ClassObject class1;
    LinkedHashMap<String, String> emptyConstructMap;
    Method mthd;

    @BeforeEach
    public void populateClasses() {
        try {
            testEditor.addClass("class1");
            class1 = testModel.fetchClass("class1");
            emptyConstructMap = new LinkedHashMap<>();
            testEditor.addMethod(class1, "method1", emptyConstructMap);
            mthd = class1.fetchMethod("method1", 0);
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
        assertEquals(mthd.getName(), "method1", "mthd name should be 'method1'");
    }

    @Test
    public void testGetType() {
        assertEquals(mthd.getType(), "Method", "mthd type should be 'Method'");
    }

    @Test
    public void testGetParamList() {
        ArrayList<Parameter> paramList = new ArrayList<>();
        assertEquals(mthd.getParamList(), paramList, "mthd's paramList should equal paramList");
    }

    @Test
    public void testRenameMethod() {
        try {
            testEditor.renameMethod(class1, mthd, "newName");
            assertEquals(mthd.getName(), "newName", "mthd name should be 'newName'");
        } catch (Exception e) {
        }
        
        
    }

    @Test
    public void testAddParametersOne() {
        String paramName = "param1";
        String paramType = "int";
        LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
        newMap.put(paramName, paramType);
        mthd.addParameters(newMap);
        assertEquals(mthd.getParamList().size(), 1, "mthd should have a list containing 'param1'");
    }

    @Test
    public void testAddParameters() {
        LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
        newMap.put("param1", "int");
        newMap.put("param2", "String");
        newMap.put("param3", "boolean");
        mthd.addParameters(newMap);
        assertEquals(mthd.getParamList().size(), 3, "mthd should have a list containing ['param1', 'param2', 'param3']");
    }

    @Test
    public void testRemoveAllParameter() {
        LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
        newMap.put("param1", "int");
        newMap.put("param2", "String");
        newMap.put("param3", "boolean");
        mthd.addParameters(newMap);
        mthd.removeAllParameters();
        assertEquals(mthd.getParamList().size(), 0, "mthd should have an empty list");
    }

    @Test
    public void testRemoveParameter() {
        try {
            LinkedHashMap<String, String> newMap = new LinkedHashMap<>();
            newMap.put("param1", "int");
            newMap.put("param2", "String");
            newMap.put("param3", "boolean");
            mthd.addParameters(newMap);
            Parameter param = mthd.fetchParameter("param2");
            mthd.removeParameter(param);
            assertEquals(mthd.getParamList().size(), 2, "mthd should have a list containing ['param1', 'param3']");
        } catch (Exception e) {
        }
        
    }

    @Test
    public void testEqualsDifNames() {
        try {
            testEditor.addMethod(class1, "method1", emptyConstructMap);
            testEditor.addMethod(class1, "method2", emptyConstructMap);
            Method mthd1 = class1.fetchMethod("method1", 0);
            Method mthd2 = class1.fetchMethod("method2", 0);
            assertEquals(mthd1.equals(mthd2), false, "mthd1 should not equal mthd2 because they have different names");
        } catch (Exception e) {
        }
    }

    @Test
    public void testEqualsDifLists() {
        try {
            testEditor.addMethod(class1, "method1", emptyConstructMap);
            LinkedHashMap<String, String> paramMap2 = new LinkedHashMap<>();
            paramMap2.put("param1", "int");
            paramMap2.put("param2", "String");
            paramMap2.put("param3", "boolean");
            testEditor.addMethod(class1, "method1", paramMap2);
            Method mthd1 = class1.fetchMethod("method1", 0);
            Method mthd2 = class1.fetchMethod("method1", 3);
            assertEquals(mthd1.equals(mthd2), false, "mthd1 should not equal mthd2 because they have different paramLists");
        } catch (Exception e) {
        }
        
    }

    @Test
    public void testFetchParameter() {
        try {
            LinkedHashMap<String, String> paramMap = new LinkedHashMap<>();
            paramMap.put("param1", "int");
            paramMap.put("param2", "String");
            paramMap.put("param3", "boolean");
            mthd.addParameters(paramMap);
            assertEquals(mthd.fetchParameter("param2").getName(), "param2", "fetchParameter should return param3");
        } catch (Exception e) {
        }
    }
}