package org.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.Controller.GUIController;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Field;

public class ClassBox extends JLayeredPane {
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
    
    // Transparent overlay panel to capture drag events
    private JPanel dragOverlay;
    
    // Components we need to update later
    private JScrollPane fieldsScrollPane;
    private JScrollPane methodsScrollPane;
    private JLabel classLabel;
    
    // Container for the visible content (class label, fields, and methods)
    private JPanel contentHolder;
    
    // Borders for selection indication
    private static final Border UNSELECTED_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.BLACK, 2),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );
    private static final Border SELECTED_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.RED, 3),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );

    public ClassBox(ClassObject classObject, GUIController controller) {
        this.classObject = classObject;
        this.controller = controller;
        
        // Create the content holder panel with BorderLayout.
        // This panel contains the class label at the top and the fields/methods panels below.
        contentHolder = new JPanel(new BorderLayout());
        contentHolder.setOpaque(false);
        contentHolder.setBorder(UNSELECTED_BORDER);
        
        // Top panel for the class label.
        // Using HTML without fixed-width constraints lets the label report its natural size.
        classLabel = new JLabel("<html>Class: " + classObject.getName() + "</html>");
        classLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        classLabel.setForeground(Color.BLACK);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        topPanel.setOpaque(false);
        topPanel.add(classLabel);
        contentHolder.add(topPanel, BorderLayout.NORTH);
        

        // Panel for fields and methods lists
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Setup fields list inside a scroll pane (scrolling disabled so it expands)
        fieldModel = new DefaultListModel<>();
        fieldsList = new JList<>(fieldModel);
        fieldsList.setFocusable(false);
        fieldsList.setBorder(BorderFactory.createTitledBorder("Fields"));
        fieldsScrollPane = new JScrollPane(fieldsList);
        fieldsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        contentPanel.add(fieldsScrollPane);
        for (AttributeInterface f : classObject.getFieldList()) {
            Field fld = (Field) f;
            fieldModel.addElement(fld.getName() + ": " + fld.getVarType());
        }
        updateFieldsScrollPaneSize();
        
        // Setup methods list inside a scroll pane (scrolling disabled so it expands)
        methodModel = new DefaultListModel<>();
        methodsList = new JList<>(methodModel);
        methodsList.setFocusable(false);
        methodsList.setBorder(BorderFactory.createTitledBorder("Methods"));
        methodsScrollPane = new JScrollPane(methodsList);
        methodsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        contentPanel.add(methodsScrollPane);
        for (AttributeInterface m : classObject.getMethodList()) {
            methodModel.addElement(displayMethod((Method) m));
        }
        updateMethodsScrollPaneSize();
        
        contentHolder.add(contentPanel, BorderLayout.CENTER);
        
        // Create a transparent overlay panel to capture drag events
        dragOverlay = new JPanel();
        dragOverlay.setOpaque(false);
        
        // Common mouse adapter for both selection and starting a drag
        MouseAdapter commonMouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();
                myX = ClassBox.this.getX();
                myY = ClassBox.this.getY();
                controller.selectClassBox(ClassBox.this);
                e.consume();
            }
        };
        MouseMotionListener dragListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int deltaX = e.getXOnScreen() - screenX;
                int deltaY = e.getYOnScreen() - screenY;
                ClassBox.this.setLocation(myX + deltaX, myY + deltaY);
                getParent().repaint();
                e.consume();
            }
            @Override
            public void mouseMoved(MouseEvent e) { }
        };
        dragOverlay.addMouseListener(commonMouseAdapter);
        dragOverlay.addMouseMotionListener(dragListener);
        
        // Add the content holder at the default layer and the overlay above it.
        add(contentHolder, JLayeredPane.DEFAULT_LAYER);
        add(dragOverlay, JLayeredPane.PALETTE_LAYER);
    }
    
    // Override doLayout so that both the content and overlay always fill the panel.
    @Override
    public void doLayout() {
        Dimension pref = getPreferredSize();
        // Set bounds for this ClassBox based on the preferred size
        setBounds(getX(), getY(), pref.width, pref.height);
        contentHolder.setBounds(0, 0, pref.width, pref.height);
        dragOverlay.setBounds(0, 0, pref.width, pref.height);
    }
    
    @Override
    public Dimension getPreferredSize() {
        int widthPadding = 35;
        int heightPadding = Math.max(fieldsList.getModel().getSize(), methodsList.getModel().getSize()) * 15 + 35;
        Dimension classLabelSize = classLabel.getPreferredSize();
        Dimension fieldsSize = fieldsScrollPane.getPreferredSize();
        Dimension methodsSize = methodsScrollPane.getPreferredSize();
        

        int totalWidth = Math.max(classLabelSize.width, Math.max(fieldsSize.width, methodsSize.width)) + widthPadding;
        int totalHeight = classLabelSize.height + fieldsSize.height + methodsSize.height + heightPadding;

        Dimension dynamicPref = new Dimension(totalWidth, totalHeight);
        Dimension defaultSize = new Dimension(175, 175);
        //Dimension defaultSize = new Dimension(175, 50);

        int finalWidth = Math.max(dynamicPref.width, defaultSize.width);
        int finalHeight = Math.max(dynamicPref.height, defaultSize.height);
        //int finalHeight = dynamicPref.height + defaultSize.height;

        return new Dimension(finalWidth, finalHeight);
    }
    
    // Adjust the fields scroll pane size based on its JList
    private void updateFieldsScrollPaneSize() {
        Dimension listPref = fieldsList.getPreferredSize();
        fieldsScrollPane.setPreferredSize(new Dimension(listPref.width + 35, listPref.height + 35));
    }
    
    // Adjust the methods scroll pane size based on its JList
    private void updateMethodsScrollPaneSize() {
        Dimension listPref = methodsList.getPreferredSize();
        methodsScrollPane.setPreferredSize(new Dimension(listPref.width + 35, listPref.height + 35));
    }
    
    public void updateMethodDisplay(Method m) {
        for (int i = 0; i < methodModel.getSize(); i++) {
            String display = methodModel.get(i);
            if (display.startsWith(m.getName() + "(")) {
                methodModel.set(i, displayMethod(m));
                updateMethodsScrollPaneSize();
                revalidate();
                repaint();
                break;
            }
        }
    }
    
    public void setSelected(boolean selected) {
        contentHolder.setBorder(selected ? SELECTED_BORDER : UNSELECTED_BORDER);
        revalidate();
        repaint();
    }
    
    public String getClassName() {
        return classObject.getName();
    }
    
    public void setClassName(String newName) {
        controller.getEditor().renameClass(classObject, newName);
        // Update the label; using HTML without a fixed-width lets it expand to fit the full name.
        classLabel.setText("<html>Class: " + classObject.getName() + "</html>");
        revalidate();
        repaint();
    }
    
    public void addField(String fieldName, String fieldType) {
        try {
            controller.getEditor().addField(classObject, fieldName, fieldType);
            fieldModel.addElement(fieldName + ": " + fieldType);
            updateFieldsScrollPaneSize();
            revalidate();
            repaint();
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
    
    public void removeField(Field field) {
        try {
            controller.getEditor().deleteField(classObject, field.getName());
            fieldModel.removeElement(field.getName() + ": " + field.getVarType());
            updateFieldsScrollPaneSize();
            revalidate();
            repaint();
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
            if (index != -1) {
                fieldModel.set(index, newName);
            }
            updateFieldsScrollPaneSize();
            revalidate();
            repaint();
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }

    public void editField(String fs, String newName, String newType) {
        try {
            if (classObject.fieldNameUsed(newName)) {
                displayErrorMessage("Field with name " + newName + " already exists");
                return;
            }
            Field fld = classObject.fetchField(fs);
            int index = fieldModel.indexOf(fs + ": " + fld.getVarType());
            controller.getEditor().renameField(classObject, fld, newName);
            controller.getEditor().changeFieldType(fld, newType);
            if (index != -1) {
                String newDisplay = newName + ": " + newType;
                fieldModel.set(index, newDisplay);
            }
            updateFieldsScrollPaneSize();
            revalidate();
            repaint();
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
    
    public void addMethod(String method, String methodType, LinkedHashMap<String, String> params) {
        try {
            controller.getEditor().addMethod(classObject, method, methodType, params);
            Method mthd = classObject.fetchMethod2(method, params);
            methodModel.addElement(displayMethod(mthd));
            updateMethodsScrollPaneSize();
            revalidate();
            repaint();
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
    
    public void removeMethod(String method) {
        try {
            String[] parts = method.split(":"); // parts = {name, arity}
            Method mthd = classObject.fetchMethod(parts[0], Integer.parseInt(parts[1]));
            controller.getEditor().deleteMethod(classObject, mthd);
            methodModel.removeElement(displayMethod(mthd));
            updateMethodsScrollPaneSize();
            revalidate();
            repaint();
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
    
    public void renameMethod(String method, String newName, String newType) {
        try {
            String[] parts = method.split(":"); // parts = {name, arity}
            Method mthd = classObject.fetchMethod(parts[0], Integer.parseInt(parts[1]));
            int index = methodModel.indexOf(displayMethod(mthd));
            //UMLeditor code
            if (newType.isEmpty()) {
                newType = "void";
            }
            controller.getEditor().renameMethod(classObject, mthd, newName);
            controller.getEditor().changeMethodType(mthd, newType);
            Method renamedMethod = classObject.fetchMethod(newName, Integer.parseInt(parts[1]));
            if (index != -1) {
                methodModel.set(index, displayMethod(renamedMethod));
            }
            //size is updated
            updateMethodsScrollPaneSize();
            revalidate();
            repaint();
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
        }
    }
    
    public ClassObject getObjectFromBox() {
        return this.classObject;
    }
    
    public Point getCenter() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }
    
    public String displayMethod(Method m) {
        if (m.getParamList().isEmpty()) {
            return m.getName() + "() -> " + m.getReturnType();
        }
        StringBuilder sig = new StringBuilder(m.getName() + "(");
        for (int i = 0; i < m.getParamList().size(); i++) {
            sig.append(m.getParamList().get(i).getName() + ": " + m.getParamList().get(i).getType());
            if (i < m.getParamList().size() - 1) {
                sig.append(", ");
            }
        }
        sig.append(") -> " + m.getReturnType());
        return sig.toString();
    }
    
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}