package org.View;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import org.Controller.GUIController;
import org.Model.ClassObject;


public class ClassBox extends JPanel {
    private JTextField classNameField;
    private DefaultListModel<String> fieldsListModel;
    private DefaultListModel<String> methodsListModel;
    private JList<String> fieldsList;
    private JList<String> methodsList;
    private GUIController controller;
    private ClassObject classObject;
    private volatile int screenX = 0;
    private volatile int screenY = 0;
    private volatile int myX = 0;
    private volatile int myY = 0;

    //Borders
    private static final Border UNSELECTED_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.BLACK, 2),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );

    private static final Border SELECTED_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.RED, 3),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );

    public ClassBox(String className, GUIController controller) {

        addMouseListener(
            new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();
                myX = getX();
                myY = getY();
            }
            @Override
            public void mouseReleased(MouseEvent e) { }
            @Override
            public void mouseEntered(MouseEvent e) { }
            @Override
            public void mouseExited(MouseEvent e) {}
            }
        );

        addMouseMotionListener(
            new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;
                setLocation(myX + deltaX, myY + deltaY);
                // Request the parent container (the drawing panel) to repaint
                getParent().repaint();
            }
            @Override
            public void mouseMoved(MouseEvent e) { }
            }
        );
  
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(UNSELECTED_BORDER);
        setOpaque(true);

        //Selection button(will be on the top left corner)
        JButton selectButton = new JButton("CLICK");
        selectButton.setPreferredSize(new Dimension(20,20));
        selectButton.setFocusable(false);
        selectButton.setBorder(null);
        selectButton.setOpaque(false);
        selectButton.setContentAreaFilled(false);
        selectButton.setToolTipText("Select this class");

        selectButton.addActionListener(e -> {
            controller.selectClassBox(this);
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        topPanel.setOpaque(false);
        topPanel.add(selectButton);

        //Class Name Field
        classNameField = new JTextField(className);
        classNameField.setHorizontalAlignment(JTextField.CENTER);
        classNameField.setEditable(false);
        
        //Prevents text field from capturing mouse clicks
        classNameField.setFocusable(false);
        classNameField.setBorder(null);

        topPanel.add(classNameField);
        add(topPanel, BorderLayout.NORTH);

        //name of the class will appear in the top part of the box
        //add(classNameField, BorderLayout.NORTH);

        //Field & Methods Panel
        // fields
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        //Fields
        fieldsListModel = new DefaultListModel<>();
        fieldsList = new JList<>(fieldsListModel);
        fieldsList.setFocusable(false);
        fieldsList.setBorder(BorderFactory.createTitledBorder("Fields"));
        contentPanel.add(new JScrollPane(fieldsList));
        //JScrollPane fieldsScroll = new JScrollPane(fieldsList);
        //fieldsScroll.setBorder(BorderFactory.createTitledBorder("fields"));
        //contentPanel.add(fieldsScroll);
        


        // Methods
        methodsListModel = new DefaultListModel<>();
        methodsList = new JList<>(methodsListModel);
        methodsList.setFocusable(false);
        methodsList.setBorder(BorderFactory.createTitledBorder("Methods"));
        contentPanel.add(new JScrollPane(methodsList));
        //JScrollPane methodsScroll = new JScrollPane(methodsList);
        //methodsScroll.setBorder(BorderFactory.createTitledBorder("Methods"));
        //contentPanel.add(methodsScroll);

        add(contentPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(250, 150));

    }

    public void setSelected(boolean selected){
        if(selected){
            setBorder(SELECTED_BORDER);
        }else{
            setBorder(UNSELECTED_BORDER);
        }
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
