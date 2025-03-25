package org.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.Controller.GUIController;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Method;


public class ClassBox extends JPanel {
    private JTextField classNameField;
    private DefaultListModel<String> fieldModel;
    private JList<String> fieldsList;
    private DefaultListModel<String> methodModel;
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

    public ClassBox(ClassObject classObject, GUIController controller) {

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

        this.classObject = classObject;
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(UNSELECTED_BORDER);
        setOpaque(true);

        //Selection button(will be on the top left corner)
        JButton selectButton = new JButton("Class: ");
        selectButton.setPreferredSize(new Dimension(50,20));
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
        classNameField = new JTextField(classObject.getName());
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
        fieldModel = new DefaultListModel<>();
        fieldsList = new JList<>(fieldModel);
        fieldsList.setFocusable(false);
        fieldsList.setBorder(BorderFactory.createTitledBorder("Fields"));
        contentPanel.add(new JScrollPane(fieldsList));
	for(AttributeInterface f: classObject.getFieldList())
	     fieldModel.addElement(f.getName());
        


        // Methods
        methodModel = new DefaultListModel<>();
        methodsList = new JList<>(methodModel);
        methodsList.setFocusable(false);
        methodsList.setBorder(BorderFactory.createTitledBorder("Methods"));
        contentPanel.add(new JScrollPane(methodsList));
	for(AttributeInterface m: classObject.getMethodList())
	    methodModel.addElement(displayMethod((Method)m));

        add(contentPanel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(250, 150));

    }

    public void updateMethodDisplay(Method m) {
        for (int i = 0; i < methodModel.getSize(); i++) {
            String display = methodModel.get(i);
            
            if (display.startsWith(m.getName() + "(")) {
                methodModel.set(i, displayMethod(m));
                break;
            }
        }
    }

    public void setSelected(boolean selected){
        if(selected){
            setBorder(SELECTED_BORDER);
        }else{
            setBorder(UNSELECTED_BORDER);
        }
    }
    

    public String getClassName() {
        return classObject.getName();
    }

    public void setClassName(String newName) {
        controller.getEditor().renameClass(classObject, newName);
        classNameField.setText(newName);
    }

    public void addField(String field) {
        try {
            controller.getEditor().addField(classObject, field);
            fieldModel.addElement(field);
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    public void removeField(AttributeInterface field) {
        try {
            controller.getEditor().deleteField(classObject, field.getName());
            fieldModel.removeElement(field.getName());
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    public void renameField(String fs, String newName) {
        try {
            if (classObject.fieldNameUsed(newName)) {
                displayErrorMessage("Field with name " + newName + " already exists");
                return;
            }
            AttributeInterface field = classObject.fetchField(fs);
            controller.getEditor().renameField(classObject, field, newName);
            int index = fieldModel.indexOf(fs);
            if(index != -1){
                fieldModel.set(index, newName);
            }
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    public void addMethod(String method, LinkedHashMap<String, String> params) {
        try {
            controller.getEditor().addMethod(classObject, method, params);
            Method mthd = classObject.fetchMethod(method, params.size());
            methodModel.addElement(displayMethod(mthd));
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    public void removeMethod(String method) {
        try {
            String [] parts = method.split(":"); //parts={String name, String arity}
            Method mthd = classObject.fetchMethod(parts[0], Integer.parseInt(parts[1]));
            controller.getEditor().deleteMethod(classObject, mthd.getName(), mthd.getParamList().size());
        
            methodModel.removeElement(displayMethod(mthd));
            //int index = methodModel.indexOf(displayMethod(mthd));
            //methodModel.set(index, "Removed");
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    public void renameMethod(String method, String newName) {
        try {
            String [] parts = method.split(":"); //parts={String name, String arity}
            Method mthd = classObject.fetchMethod(parts[0], Integer.parseInt(parts[1]));
            int index = methodModel.indexOf(displayMethod(mthd));
            controller.getEditor().renameMethod(classObject, mthd, newName);
            //int index = methodModel.indexOf(displayMethod(classObject.fetchMethod(parts[0], Integer.parseInt(parts[1]))));
            Method renamedMethod = classObject.fetchMethod(newName, Integer.parseInt(parts[1]));
            if(index != -1){
                //methodModel.set(index, displayMethod(classObject.fetchMethod(newName, Integer.parseInt(parts[1]))));
                methodModel.set(index, displayMethod(renamedMethod));
            }
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    public ClassObject getObjectFromBox(){
        return this.classObject;
    }

    public Point getCenter() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public String displayMethod(Method m){
        /*
        StringBuilder signature = new StringBuilder(methodName);
            signature.append("(");
            if (!params.isEmpty()) {
                signature.append(String.join(", ", params));
            }
            signature.append(")");
        return signature;
        */
       if (m.getParamList().size() == 0) {
        return m.getName() + "()";
       }
        String sig = m.getName() + "(";
        for (int i = 0; i < m.getParamList().size()-1; i++) {
            sig += m.getParamList().get(i).getName() + ", ";
        }
        return sig + m.getParamList().get(m.getParamList().size()-1).getName() + ")";
    }

    /**
     * Function to display any error messages that occur
     * @param message   | The message to be displayed
     */
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,"Error", JOptionPane.ERROR_MESSAGE);
    }
}