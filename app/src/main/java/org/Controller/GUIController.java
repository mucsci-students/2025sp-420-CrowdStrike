package org.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.View.GUIView;
import org.View.ClassBox;

public class GUIController {

    
    private GUIView view;
    private List<ClassBox> classBoxes = new ArrayList<>();
    private List<Relationship> relationships = new ArrayList<>();
    private ClassBox selectedClassBox = null;
    private boolean addRelationshipMode = false;
    private ClassBox selectedDestination = null;
    private ClassBox selectedSource = null;

    
    

    /**
	 * Constructor for GUI
	 * @param view | Contains methods that will paint the Panel of the Diagram
	 */
    public GUIController(GUIView view) {
        this.view = view;
        initController();
    }

    // ==================== INITIALIZATION ==================== //

    /**
	 * Checks if buttons were pressed
	 */
    private void initController() {
        initButtonActions();
    }

    // ==================== EVENT HANDLERS ==================== //

    /**
	 * assigns actions to buttons in the GUI
	 */
    private void initButtonActions() {
        view.getAddClassButton().addActionListener(e -> promptForClassName());
        view.getDeleteClassButton().addActionListener(e -> deleteSelectedClass());
        view.getRenameClassButton().addActionListener(e -> renameSelectedClass());

        view.getAddRelationshipButton().addActionListener(e -> {
            if (classBoxes.size() < 2) {
                JOptionPane.showMessageDialog(view, "At least two classes are required to add a relationship.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            addRelationshipMode = view.getAddRelationshipButton().isSelected();
            if (addRelationshipMode) {
                JOptionPane.showMessageDialog(view, "Click on a source class, then a destination class.");
            } else {
                resetRelationshipSelection();
            }
        });

        view.getDeleteRelationshipButton().addActionListener(e -> deleteRelationship());

        view.getAddFieldButton().addActionListener(e -> addFieldToClass());
        view.getDeleteFieldButton().addActionListener(e -> deleteFieldFromClass());
        view.getRenameFieldButton().addActionListener(e -> renameFieldInClass());

        view.getAddMethodButton().addActionListener(e -> addMethodToClass());
        view.getDeleteMethodButton().addActionListener(e -> deleteMethodFromClass());
        view.getRenameMethodButton().addActionListener(e -> renameMethodInClass());
        
    }

    // ==================== ADD CLASS ==================== //
     /**
     * Prompts user for a class name and creates a new class if valid.
     */
    private void promptForClassName() {
        String className = JOptionPane.showInputDialog(view, "Enter Class Name:", "New Class", JOptionPane.PLAIN_MESSAGE);
        if (className == null || className.trim().isEmpty()) return;

        className = className.trim();

        for (ClassBox box : classBoxes) {
            if (box.getClassName().equalsIgnoreCase(className)) {
                JOptionPane.showMessageDialog(view, "Class name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        addClass(className);
    }


    /**
     * Renames the currently selected class.
     */
    private void renameSelectedClass(){

        //if no class is selected 
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "No class selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        String className = JOptionPane.showInputDialog(view, "Enter Class Name:", "New Class", JOptionPane.PLAIN_MESSAGE);
        if(className == null || className.trim().isEmpty()) return;


        className = className.trim();


        for (ClassBox box : classBoxes) {
            if (box.getClassName().equalsIgnoreCase(className)) {
                JOptionPane.showMessageDialog(view, "Class name already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }


        //TODO

    }

    /**
     * Creates and positions a new class box in the diagram.
     * @param className The name of the new class
     */
    private void addClass(String className) {
        ClassBox classBox = new ClassBox(className);
        Point position = getNextGridPosition();
        classBox.setBounds(position.x, position.y, 150, 100);

        classBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (addRelationshipMode) {
                    handleRelationshipSelection(classBox);
                } else {
                    selectClassBox(classBox);
                }
                e.consume();
            }
        });

        view.getDrawingPanel().add(classBox);
        classBoxes.add(classBox);
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
    }

    // ==================== DELETE CLASS (ON CLICK + BUTTON) ==================== //

     /**
     * Deletes the currently selected class box.
     */
    private void deleteSelectedClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "No class selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this class?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Remove from panel
        view.getDrawingPanel().remove(selectedClassBox);
        classBoxes.remove(selectedClassBox);
        removeRelationships(selectedClassBox);

        selectedClassBox = null;
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
    }
    
    // ==================== RELATIONSHIP HANDLING ==================== //
     /**
     * Handles selecting source and destination for a relationship.
     */
    
    private void handleRelationshipSelection(ClassBox classBox) {
        if (selectedSource == null) {
            selectedSource = classBox;
            selectedSource.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        } else {
            selectedDestination = classBox;
            if (selectedSource == selectedDestination) {
                JOptionPane.showMessageDialog(view, "Cannot connect a class to itself!", "Error", JOptionPane.ERROR_MESSAGE);
                resetRelationshipSelection();
                return;
            }

            createRelationship(selectedSource, selectedDestination);
        }
    }

    /**
     * Creates a new relationship between two selected classes.
     */
    private void createRelationship(ClassBox source, ClassBox destination) {
        String[] types = {"Aggregation", "Composition", "Inheritance", "Realization"};
        String type = (String) JOptionPane.showInputDialog(view, "Select Relationship Type:", "Relationship Type",
                JOptionPane.QUESTION_MESSAGE, null, types, types[0]);

        if (type == null) {
            resetRelationshipSelection();
            return;
        }

        Relationship relationship = new Relationship(source, destination, type);
        relationships.add(relationship);

        view.getDrawingPanel().addRelationship(source.getCenter(), destination.getCenter(), type);
        resetRelationshipSelection();

    }

    /**
     * Deletes the last relationship added.
     */
    private void deleteRelationship() {
        if (relationships.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No relationships to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Relationship toRemove = relationships.get(relationships.size() - 1);
        relationships.remove(toRemove);
        view.getDrawingPanel().removeRelationship(toRemove.getSource().getCenter(), toRemove.getDestination().getCenter());

        view.getDrawingPanel().repaint();
    }

    private void removeRelationships(ClassBox classBox) {
        relationships.removeIf(r -> r.getSource() == classBox || r.getDestination() == classBox);
        view.getDrawingPanel().repaint();
    }

    /**
     * Removes all relationships connected to a deleted class.
     */
    private void resetRelationshipSelection() {
        if (selectedSource != null) selectedSource.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        selectedSource = null;
        selectedDestination = null;
        addRelationshipMode = false;
        view.getAddRelationshipButton().setSelected(false);
    }


    //================= FIELD ==============================

        private void addFieldToClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        List<JTextField> fieldInputs = new ArrayList<>();
        JButton addFieldButton = new JButton("+");

        // Initial Field Input
        JTextField fieldInput = new JTextField();
        fieldInputs.add(fieldInput);
        panel.add(new JLabel("Enter Field Name:"));
        panel.add(fieldInput);
        panel.add(addFieldButton);

        // Add more fields dynamically when clicking "+"
        addFieldButton.addActionListener(e -> {
            JTextField newFieldInput = new JTextField();
            fieldInputs.add(newFieldInput);
            panel.add(new JLabel("Enter Field Name:"));
            panel.add(newFieldInput);
            panel.revalidate();
            panel.repaint();
        });

        int result = JOptionPane.showConfirmDialog(view, panel, "Add Fields", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            for (JTextField input : fieldInputs) {
                String fieldName = input.getText().trim();
                if (!fieldName.isEmpty() && !selectedClassBox.getFields().contains(fieldName)) {
                    selectedClassBox.addField(fieldName);
                }
            }
        }
    }

    private void deleteFieldFromClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> fields = selectedClassBox.getFields();
        if (fields.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No fields to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> fieldDropdown = new JComboBox<>(fields.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(view, fieldDropdown, "Select Field to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedField = (String) fieldDropdown.getSelectedItem();
            if (selectedField != null) {
                selectedClassBox.removeField(selectedField);
            }
        }
    }

    private void renameFieldInClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> fields = selectedClassBox.getFields();
        if (fields.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No fields to rename!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> fieldDropdown = new JComboBox<>(fields.toArray(new String[0]));
        JTextField newFieldNameInput = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Field to Rename:"));
        panel.add(fieldDropdown);
        panel.add(new JLabel("Enter New Name:"));
        panel.add(newFieldNameInput);

        int result = JOptionPane.showConfirmDialog(view, panel, "Rename Field", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldName = (String) fieldDropdown.getSelectedItem();
            String newName = newFieldNameInput.getText().trim();

            if (!newName.isEmpty()) {
                selectedClassBox.renameField(oldName, newName);
            }
        }
    }

    private void addMethodToClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(0, 1));
        List<JTextField> methodInputs = new ArrayList<>();
        JButton addMethodButton = new JButton("+");

        // Initial Method Input
        JTextField methodInput = new JTextField();
        methodInputs.add(methodInput);
        panel.add(new JLabel("Enter Method Name:"));
        panel.add(methodInput);
        panel.add(addMethodButton);

        // Add more methods dynamically when clicking "+"
        addMethodButton.addActionListener(e -> {
            JTextField newMethodInput = new JTextField();
            methodInputs.add(newMethodInput);
            panel.add(new JLabel("Enter Method Name:"));
            panel.add(newMethodInput);
            panel.revalidate();
            panel.repaint();
        });

        int result = JOptionPane.showConfirmDialog(view, panel, "Add Methods", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            for (JTextField input : methodInputs) {
                String methodName = input.getText().trim();
                if (!methodName.isEmpty() && !selectedClassBox.getMethods().contains(methodName)) {
                    selectedClassBox.addMethod(methodName);
                }
            }
        }
    }

    private void deleteMethodFromClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> methods = selectedClassBox.getMethods();
        if (methods.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> methodDropdown = new JComboBox<>(methods.toArray(new String[0]));
        int result = JOptionPane.showConfirmDialog(view, methodDropdown, "Select Method to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedMethod = (String) methodDropdown.getSelectedItem();
            if (selectedMethod != null) {
                selectedClassBox.removeMethod(selectedMethod);
            }
        }
    }

    private void renameMethodInClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> methods = selectedClassBox.getMethods();
        if (methods.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods to rename!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> methodDropdown = new JComboBox<>(methods.toArray(new String[0]));
        JTextField newMethodNameInput = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Method to Rename:"));
        panel.add(methodDropdown);
        panel.add(new JLabel("Enter New Name:"));
        panel.add(newMethodNameInput);

        int result = JOptionPane.showConfirmDialog(view, panel, "Rename Method", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldName = (String) methodDropdown.getSelectedItem();
            String newName = newMethodNameInput.getText().trim();

            if (!newName.isEmpty()) {
                selectedClassBox.renameMethod(oldName, newName);
            }
        }
    }





    //

    // ==================== GRID POSITIONING ==================== //
    /**
     * Determines the next position for a new class box based on a grid layout.
     * NOTE:
     */
    private Point getNextGridPosition() {


        int numBoxes = classBoxes.size();
        int boxWidth = 150;
        int boxHeight = 150;
        int padding = 20;
        int panelWidth = view.getDrawingPanel().getWidth();
        int panelHeight = view.getDrawingPanel().getHeight();

        int maxCols = (panelWidth - padding) / (boxWidth + padding);
        if (maxCols == 0){maxCols = 1;}

        int gridCol = numBoxes % maxCols;
        int gridRow = numBoxes/ maxCols;

        //get x and y to center the grid

        int totalGridWidth = (maxCols *(boxWidth + padding)) - padding; // 

        int startX = (panelWidth - totalGridWidth) / 2; //so its placed in the center

        int totalGridHeight = ((numBoxes / maxCols + 1) * (boxHeight - padding)) - padding;
        
        int startY = (panelHeight - totalGridHeight) / 2;

        int x = startX + gridCol * (boxWidth + padding);
        int y = startY + gridRow * (boxHeight + padding);

        return new Point(x, y);

    }

    // ==================== CLASS SELECTION ==================== //

    private void selectClassBox(ClassBox classBox) {
        if (selectedClassBox != null) {
            selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        selectedClassBox = classBox;
        selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    }

    





    // ==================== RELATIONSHIP CLASS ==================== //
    /**
     * Defines a relationship between two class boxes.
     */
    private static class Relationship {
        private final ClassBox source;
        private final ClassBox destination;
        private final String type;

        public Relationship(ClassBox source, ClassBox destination, String type) {
            this.source = source;
            this.destination = destination;
            this.type = type;
        }

        public ClassBox getSource() { return source; }
        public ClassBox getDestination() { return destination; }
        public String getType() { return type; }
    }

    

    public GUIController() {
        SwingUtilities.invokeLater(() -> {
            GUIView view = new GUIView();
            new GUIController(view);
            view.showGUI();
        });
    }
}
