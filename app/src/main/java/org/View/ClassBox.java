package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;


public class ClassBox extends JPanel {
    private JTextField classNameField;
    private DefaultListModel<String> fieldsListModel;
    private DefaultListModel<String> methodsListModel;
    private JList<String> fieldsList;
    private JList<String> methodsList;

    public ClassBox(String className) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.BLACK, 2));
        //Class Name Field
        classNameField = new JTextField(className);
        classNameField.setHorizontalAlignment(JTextField.CENTER);
        classNameField.setEditable(false);
        //name of the class will appear in the top part of the box
        add(classNameField, BorderLayout.NORTH);

        //Field & Methods Panel
        // fields
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        fieldsListModel = new DefaultListModel<>();
        fieldsList = new JList<>(fieldsListModel);
        JScrollPane fieldsScroll = new JScrollPane(fieldsList);
        fieldsScroll.setBorder(BorderFactory.createTitledBorder("fields"));
        contentPanel.add(fieldsScroll);


        // Methods
        methodsListModel = new DefaultListModel<>();
        methodsList = new JList<>(methodsListModel);
        JScrollPane methodsScroll = new JScrollPane(methodsList);
        methodsScroll.setBorder(BorderFactory.createTitledBorder("Methods"));
        contentPanel.add(methodsScroll);

        add(contentPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(250, 500));
    }

    public String getClassName() {
        return classNameField.getText();
    }

    public void setClassName(String newName) {
        classNameField.setText(newName);
    }

    public void addField(String field) {
        fieldsListModel.addElement(field);
    }

    public void removeField(String field) {
        fieldsListModel.removeElement(field);
    }

    public void renameField(String oldName, String newName) {
        int index = fieldsListModel.indexOf(oldName);
        if (index != -1) {
            fieldsListModel.set(index, newName);
        }
    }

    public void addMethod(String method) {
        methodsListModel.addElement(method);
    }

    public void removeMethod(String method) {
        methodsListModel.removeElement(method);
    }

    public void renameMethod(String oldName, String newName) {
        int index = methodsListModel.indexOf(oldName);
        if (index != -1) {
            methodsListModel.set(index, newName);
        }
    }

    // tel
    public List<String> getFields() {
        List<String> fields = new ArrayList<>();
        Enumeration<String> elements = fieldsListModel.elements();
        while (elements.hasMoreElements()) {//
            fields.add(elements.nextElement());
        }
        return fields;
    }

    //Correct way to return methods from DefaultListModel
    public List<String> getMethods() {
        List<String> methods = new ArrayList<>();
        Enumeration<String> elements = methodsListModel.elements();
        while (elements.hasMoreElements()) {
            methods.add(elements.nextElement());
        }
        return methods;
    }


    public Point getCenter() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }
}
