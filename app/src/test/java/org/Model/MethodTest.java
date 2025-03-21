package org.Model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.Model.UMLModel;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;
import org.Controller.UMLEditor;

public class MethodTest {

    UMLModel testModel = new UMLModel();
    UMLEditor testEditor = new UMLEditor(testModel);
    ClassObject class1;
    ArrayList<String> emptyConstructList;

    @BeforeEach
    public void populateClasses() {
        testEditor.addClass("class1");
        class1 = testModel.fetchClass("class1");
        emptyConstructList = new ArrayList<>();
    }

    @AfterEach
    public void cleanUp() {
        testEditor.deleteClass("class1");
    }

    @Test
    public void testGetName() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        assertEquals(mthd.getName(), "method1", "mthd name should be 'method1'");
    }

    @Test
    public void testGetType() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        assertEquals(mthd.getType(), "Method", "mthd type should be 'Method'");
    }

    @Test
    public void testGetParamList() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        ArrayList<Parameter> paramList = new ArrayList<>();
        assertEquals(mthd.getParamList(), paramList, "mthd's paramList should equal paramList");
    }

    @Test
    public void testRenameMethod() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        testEditor.renameAttribute(mthd, "newName");
        assertEquals(mthd.getName(), "newName", "mthd name should be 'newName'");
    }

    @Test
    public void testAddParametersOne() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        String paramName = "param1";
        ArrayList<String> newList = new ArrayList<>();
        newList.add(paramName);
        mthd.addParameters(newList);
        assertEquals(mthd.getParamList().size(), 1, "mthd should have a list containing 'param1'");
    }

    @Test
    public void testAddParameters() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        String paramName1 = "param1";
        String paramName2 = "param2";
        String paramName3 = "param3";
        ArrayList<String> newList = new ArrayList<>();
        newList.add(paramName1);
        newList.add(paramName2);
        newList.add(paramName3);
        mthd.addParameters(newList);
        assertEquals(mthd.getParamList().size(), 3, "mthd should have a list containing ['param1', 'param2', 'param3']");
    }

    @Test
    public void testRemoveAllParameter() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        String paramName1 = "param1";
        String paramName2 = "param2";
        String paramName3 = "param3";
        ArrayList<String> newList = new ArrayList<>();
        newList.add(paramName1);
        newList.add(paramName2);
        newList.add(paramName3);
        mthd.addParameters(newList);
        mthd.removeAllParameters();
        assertEquals(mthd.getParamList().size(), 0, "mthd should have an empty list");
    }

    @Test
    public void testRemoveParameter() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        String paramName1 = "param1";
        String paramName2 = "param2";
        String paramName3 = "param3";
        ArrayList<String> newList = new ArrayList<>();
        newList.add(paramName1);
        newList.add(paramName2);
        newList.add(paramName3);
        mthd.addParameters(newList);
        Parameter param = mthd.fetchParameter("param2");
        mthd.removeParameter(param);
        assertEquals(mthd.getParamList().size(), 2, "mthd should have a list containing ['param1', 'param3']");
    }

    @Test
    public void testEqualsDifNames() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        testEditor.addMethod(class1, "method2", emptyConstructList);
        Method mthd1 = class1.fetchMethod("method1", 0);
        Method mthd2 = class1.fetchMethod("method2", 0);
        assertEquals(mthd1.equals(mthd2), false, "mthd1 should not equal mthd2 because they have different names");
    }

    @Test
    public void testEqualsDifLists() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        String paramName1 = "param1";
        String paramName2 = "param2";
        String paramName3 = "param3";
        ArrayList<String> paramList2 = new ArrayList<>();
        paramList2.add(paramName1);
        paramList2.add(paramName2);
        paramList2.add(paramName3);
        testEditor.addMethod(class1, "method1", paramList2);
        Method mthd1 = class1.fetchMethod("method1", 0);
        Method mthd2 = class1.fetchMethod("method1", 3);
        assertEquals(mthd1.equals(mthd2), false, "mthd1 should not equal mthd2 because they have different paramLists");
    }

    @Test
    public void testFetchParameter() {
        testEditor.addMethod(class1, "method1", emptyConstructList);
        Method mthd = class1.fetchMethod("method1", 0);
        String paramName1 = "param1";
        String paramName2 = "param2";
        String paramName3 = "param3";
        ArrayList<String> newList = new ArrayList<>();
        newList.add(paramName1);
        newList.add(paramName2);
        newList.add(paramName3);
        mthd.addParameters(newList);
        assertEquals(mthd.fetchParameter("param2").getName(), "param2", "fetchParameter should return param3");
    }
}