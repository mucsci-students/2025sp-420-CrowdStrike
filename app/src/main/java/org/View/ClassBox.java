package org.View;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import org.Controller.GUIController;
import org.Model.*;

public class ClassBox extends JPanel {
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

    // Borders
    private static final Border UNSELECTED_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.BLACK, 2),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );
    private static final Border SELECTED_BORDER = BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.RED, 3),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)
    );

    public ClassBox(ClassObject classObject, GUIController controller) {
        // Save references
        this.classObject = classObject;
        this.controller = controller;

        // Create the overlay panel that will capture all mouse events
        dragOverlay = new JPanel();
        dragOverlay.setOpaque(false);
        //dragOverlay.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

        // Common mouse adapter for starting drag and handling clicks (selection)
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
            //@Override
            //public void mouseClicked(MouseEvent e) {
            //    controller.selectClassBox(ClassBox.this);
            //    e.consume();
            //}
        };

        // Drag listener for moving the box
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

        // Attach listeners to the overlay
        dragOverlay.addMouseListener(commonMouseAdapter);
        dragOverlay.addMouseMotionListener(dragListener);

        // Create a panel to hold the original content
        JPanel contentHolder = new JPanel(new BorderLayout());
        contentHolder.setOpaque(false);
        contentHolder.setBorder(UNSELECTED_BORDER);

        // Top panel for the class label
        JLabel classLabel = new JLabel("Class: " + classObject.getName());
        classLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        classLabel.setForeground(Color.BLACK);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        topPanel.setOpaque(false);
        topPanel.add(classLabel);
        contentHolder.add(topPanel, BorderLayout.NORTH);

        // Content panel for fields and methods lists
        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Fields list inside a scroll pane
        fieldModel = new DefaultListModel<>();
        fieldsList = new JList<>(fieldModel);
        fieldsList.setFocusable(false);
        fieldsList.setBorder(BorderFactory.createTitledBorder("Fields"));
        JScrollPane fieldsScrollPane = new JScrollPane(fieldsList);
        contentPanel.add(fieldsScrollPane);
        for (AttributeInterface f : classObject.getFieldList()) {
            fieldModel.addElement(f.getName());
        }

        // Methods list inside a scroll pane
        methodModel = new DefaultListModel<>();
        methodsList = new JList<>(methodModel);
        methodsList.setFocusable(false);
        methodsList.setBorder(BorderFactory.createTitledBorder("Methods"));
        JScrollPane methodsScrollPane = new JScrollPane(methodsList);
        contentPanel.add(methodsScrollPane);
        for (AttributeInterface m : classObject.getMethodList()) {
            methodModel.addElement(displayMethod((Method) m));
        }

        contentHolder.add(contentPanel, BorderLayout.CENTER);
        contentHolder.setPreferredSize(new Dimension(250, 150));

        // Use OverlayLayout so that the drag overlay sits on top of the content
        setLayout(new OverlayLayout(this));
        add(dragOverlay);   // top layer (captures mouse events)
        add(contentHolder); // bottom layer (visible content)

        setPreferredSize(new Dimension(250, 150));
        setOpaque(false);
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

    public void setSelected(boolean selected) {
        // Assuming the contentHolder is the second component added,
        // update its border to show selection
        if (getComponentCount() > 1) {
            Component c = getComponent(1);
            if (c instanceof JComponent) {
                ((JComponent)c).setBorder(selected ? SELECTED_BORDER : UNSELECTED_BORDER);
            }
        }
    }

    public String getClassName() {
        return classObject.getName();
    }

    public void setClassName(String newName) {
        controller.getEditor().renameClass(classObject, newName);
        // Optionally update label text if stored
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

    public void addMethod(String method, ArrayList<String> params) {
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

    public ClassObject getObjectFromBox() {
        return this.classObject;
    }

    public Point getCenter() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    public String displayMethod(Method m) {
        if (m.getParamList().isEmpty()) {
            return m.getName() + "()";
        }
        StringBuilder sig = new StringBuilder(m.getName() + "(");
        for (int i = 0; i < m.getParamList().size(); i++) {
            sig.append(m.getParamList().get(i).getName());
            if (i < m.getParamList().size() - 1) {
                sig.append(", ");
            }
        }
        sig.append(")");
        return sig.toString();
    }

    /**
     * Function to display any error messages that occur
     * @param message   | The message to be displayed
     */
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,"Error", JOptionPane.ERROR_MESSAGE);
    }
}