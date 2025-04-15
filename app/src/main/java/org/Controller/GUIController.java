package org.Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.FileManager;
import org.Model.AttributeInterface;
import org.Model.ClassObject;
import org.Model.Method;
import org.Model.Parameter;
import org.Model.Relationship;
import org.Model.Relationship.Type;
import org.Model.UMLModel;
import org.View.ClassBox;
import org.View.GUIView;

/**
 * GUIController acts as the central controller for the UML Diagram Editor. It
 * handles user interactions, updates the model via the editor, and refreshes
 * the view accordingly.
 */
@Deprecated
public class GUIController {

    // Model representing the UML diagram structure.
    private UMLModel model;
    // Editor used to modify the UMLModel.
    private UMLEditor editor;
    // Keeps track of the currently active class object.
    ClassObject activeClass = null;

    // The view component responsible for rendering the GUI.
    private final GUIView view;
    // List of graphical representations of the UML classes.
    private List<ClassBox> classBoxes = new ArrayList<>();
    // List of relationships between the class boxes.
    private List<GUIRelationship> relationships = new ArrayList<>();
    // Keeps track of the currently selected class box.
    private ClassBox selectedClassBox = null;
    //private boolean addRelationshipMode = false;
    // These fields could be used for relationship selection (currently not fully implemented).
    
    

    /**
     * Constructor for GUIController. Initializes the controller with the given
     * UML model, editor, and view.
     *
     * @param model The UML model representing the diagram.
     * @param editor The editor used to modify the UML model.
     * @param view The view which renders the diagram.
     */
    public GUIController(UMLModel model, UMLEditor editor, GUIView view) {
        this.view = view;
        this.model = model;
        this.editor = editor;
    }

    // ==================== INITIALIZATION ==================== //
    /**
     * Checks if buttons were pressed. Initializes the controller's event
     * handlers and displays the GUI.
     */
    public void initController() {
        initButtonActions();
        view.showGUI();
    }

    // ==================== EVENT HANDLERS ==================== //
    /**
     * assigns actions to buttons in the GUI. This method registers all button
     * action listeners to their respective handlers.
     */
    private void initButtonActions() {
        view.getAddClassButton().addActionListener(e -> promptForClassName());
        view.getDeleteClassButton().addActionListener(e -> deleteSelectedClass());
        view.getRenameClassButton().addActionListener(e -> renameSelectedClass());


        view.getAddRelationshipButton().addActionListener(e -> createRelationshipDialog());


        view.getEditRelationshipButton().addActionListener(e -> editRelationship());

        view.getDeleteRelationshipButton().addActionListener(e -> deleteRelationship());

        view.getAddFieldButton().addActionListener(e -> addFieldToClass());
        view.getDeleteFieldButton().addActionListener(e -> deleteFieldFromClass());
        view.getEditFieldButton().addActionListener(e -> editFieldInClass());

        view.getAddMethodButton().addActionListener(e -> addMethodToClass());
        view.getDeleteMethodButton().addActionListener(e -> deleteMethodFromClass());
        view.getRenameMethodButton().addActionListener(e -> renameMethodInClass());

        view.getAddParamButton().addActionListener(e -> addParameterToMethod());
        view.getDeleteParamButton().addActionListener(e -> deleteParameterFromMethod());
        view.getChangeParamButton().addActionListener(e -> changeParameterInMethod());

        view.getUndoButton().addActionListener(e -> undo());
        view.getRedoButton().addActionListener(e -> redo());

        view.getSaveButton().addActionListener(e -> saveDiagram());
        view.getLoadButton().addActionListener(e -> loadDiagram());

        view.getExitButton().addActionListener(e -> exitDiagram());
        view.getHelpButton().addActionListener(e -> {
            JDialog helpDialog = new JDialog(view, "Help", true);
            helpDialog.getContentPane().add(view.helpPanel());
            helpDialog.setSize(500, 400);
            helpDialog.setLocationRelativeTo(view);
            helpDialog.setVisible(true);
            });
    }

    // ==================== ADD CLASS ==================== //
    /**
     * Prompts user for a class name and creates a new class if valid.
     */
    private void promptForClassName() {
        String className = JOptionPane.showInputDialog(view, "Enter Class Name:", "New Class", JOptionPane.PLAIN_MESSAGE);

        //if (model.isValidClassName(className)!=0) return; replace with switch for more specific error messages
        try {
            // Validate the class name using the model's validation method.
            model.isValidClassName(className);
            className = className.trim();

            // Add the class to the model using the editor.
            editor.addClass(className);
            // Retrieve the newly created class object.
            activeClass = model.fetchClass(className);
            // Add a graphical representation (ClassBox) of the class to the view.
            addClass(activeClass);
        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Renames the currently selected class.
     */
    private void renameSelectedClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "No class selected", "Error", JOptionPane.ERROR_MESSAGE);
            /*
             * Switch the command to display errors to the one below
             * view.displayErrorMessage("No class selected");
             */
            return;
        }

        // Prompt user to enter a new class name, pre-filled with the current name.
        String newClassName = JOptionPane.showInputDialog(view, "Enter New ClassName:", selectedClassBox.getClassName());

        if (newClassName == null || newClassName.trim().isEmpty()) {
            return;
        }

        newClassName = newClassName.trim();

        // Ensure no other class box already uses the new name (case-insensitive check).
        for (ClassBox box : classBoxes) {
            if (box != selectedClassBox && box.getClassName().equalsIgnoreCase(newClassName)) {
                JOptionPane.showMessageDialog(view, "Class name already exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Update the ClassBox's name.
        selectedClassBox.setClassName(newClassName);

        // Optionally update the model (code commented out for future improvement).
        //ClassObject classObject = model.fetchClass(selectedClassBox.getClassObject().getName());
        //if(classObject != null){
        //  classObject.setName(newClassName);
        //}
        //TODO
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
    }

    /**
     * Creates and positions a new class box in the diagram.
     *
     * @param className The name of the new class
     */
    private void addClass(ClassObject newClass) {
        // Create a new ClassBox for the given ClassObject.
        ClassBox classBox = new ClassBox(newClass, this);
        Point position = newClass.getPosition();
        
        //System.out.println("position of " + newClass.getName() + ": (" + position.x + ", " + position.y + ")\n"); // Debug
        if(position.x == -1 || position.y == -1){
            position = getNextGridPosition();
        }

        classBox.setBounds(position.x, position.y, 150, 200);

        // Set appearance properties.
        classBox.setOpaque(true);
        classBox.setBackground(Color.WHITE);

        // Add a mouse listener to detect clicks inside the box.
        configureClassBoxMouseListener(classBox);

        // Add the class box to the drawing panel and store it in the list.
        view.getDrawingPanel().add(classBox);
        classBoxes.add(classBox);
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
    }

    /**
     * Adds a mouse listener to a ClassBox. Currently, the listener consumes
     * mouse events; it can be expanded to handle selections.
     *
     * @param classBox The ClassBox to configure.
     */
    private void configureClassBoxMouseListener(ClassBox classBox) {
        classBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Uncomment below to enable class box selection.
                //selectClassBox(classBox);

                // Consume the event so that it doesn't propagate further.
                e.consume();
            }
        });
    }

    // ==================== DELETE CLASS (ON CLICK + BUTTON) ==================== //
    /**
     * Deletes the currently selected class box.
     *
     */
    private void deleteSelectedClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "No class selected!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm deletion with the user.
        int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete this class?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Remove the class box from the drawing panel.
        view.getDrawingPanel().remove(selectedClassBox);
        try {
            // Delete the class from the model using the editor.
            editor.deleteClass(selectedClassBox.getClassName());
        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
            return;
        }

        // Remove the class box from the list and clean up any related relationships.
        classBoxes.remove(selectedClassBox);
        removeRelationships(selectedClassBox);

        selectedClassBox = null;
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
    }

    //Code for handle relationship selection
    /**
     * Converts a string representing a relationship type into its corresponding
     * enum value.
     *
     * @param t The relationship type as a string.
     * @return The corresponding enum value for the relationship type.
     */
    private Type relationshipTypeToEnum(String t) {
        Type relationshipType = null;
        switch (t) {
            case "Aggregation":
                relationshipType = Type.AGGREGATION;
                break;
            case "Composition":
                relationshipType = Type.COMPOSITION;
                break;
            case "Inheritance":
                relationshipType = Type.INHERITANCE;
                break;
            case "Realization":
                relationshipType = Type.REALIZATION;
                break;

        }
        return relationshipType;
    }

    /**
     * Presents a dialog with drop-downs to select the source class, destination
     * class, and relationship type. Creates the relationship if valid.
     */
    private void createRelationshipDialog() {
        // Ensure there is at least one class to form a relationship.
        if (classBoxes.size() < 1) {
            JOptionPane.showMessageDialog(view, "At least one class is required to add a relationship.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a panel with a grid layout to hold the selection components.
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        // Source selection dropdown.
        panel.add(new JLabel("Source:"));
        JComboBox<String> sourceCombo = new JComboBox<>();
        for (ClassBox cb : classBoxes) {
            sourceCombo.addItem(cb.getClassName());
        }
        panel.add(sourceCombo);

        // Destination selection dropdown.
        panel.add(new JLabel("Destination:"));
        JComboBox<String> destinationCombo = new JComboBox<>();
        for (ClassBox cb : classBoxes) {
            destinationCombo.addItem(cb.getClassName());
        }
        panel.add(destinationCombo);

        // Relationship type selection dropdown.
        panel.add(new JLabel("Relationship Type:"));
        String[] types = {"Aggregation", "Composition", "Inheritance", "Realization"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        panel.add(typeCombo);

        // Display the dialog for relationship creation.
        int result = JOptionPane.showConfirmDialog(view, panel, "Create Relationship",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Retrieve the selections.
            String sourceName = (String) sourceCombo.getSelectedItem();
            String destinationName = (String) destinationCombo.getSelectedItem();
            String relationshipType = (String) typeCombo.getSelectedItem();

            if (model.relationshipExist(sourceName, destinationName)) {
                view.displayErrorMessage("Relationship between " + sourceName + " and " + destinationName + " already exists");
                return;
            }
            

            // Find the corresponding ClassBox objects for the selected source and destination.
            ClassBox sourceBox = null;
            ClassBox destinationBox = null;
            for (ClassBox cb : classBoxes) {
                if (cb.getClassName().equals(sourceName)) {
                    sourceBox = cb;
                }
                if (cb.getClassName().equals(destinationName)) {
                    destinationBox = cb;
                }
            }

            // Update the model via the editor and then update the view.
            try {
                editor.addRelationship(sourceName, destinationName, relationshipTypeToEnum(relationshipType));
            } catch (Exception e) {
                view.displayErrorMessage(e.getMessage());
                return;
            }

            // Create a new GUIRelationship and store it.
            GUIRelationship relationship = new GUIRelationship(sourceBox, destinationBox, relationshipType);
            relationships.add(relationship);
            // Add the relationship to the drawing panel.
            view.getDrawingPanel().addRelationship(sourceBox, destinationBox, relationshipType);
            view.getDrawingPanel().repaint();
        }
    }

    /**
     * Allows editing of an existing relationship between two classes. The user
     * can change the source, destination, or type of the relationship.
     */
    private void editRelationship() {
        // Check if there are any relationships to edit.
        if (relationships.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No relationships to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dropdown for selecting the relationship to edit.
        JComboBox<String> relDropdown = new JComboBox<>();
        for (GUIRelationship rel : relationships) {
            String relString = rel.getSource().getObjectFromBox().getName() + " -> " + rel.getDestination().getObjectFromBox().getName();
            relDropdown.addItem(relString);
        }
        int result = JOptionPane.showConfirmDialog(view, relDropdown, "Select Relationship to Edit", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // Get the selected relationship.
        int selectedIndex = relDropdown.getSelectedIndex();
        if (selectedIndex < 0) {
            return;
        }
        GUIRelationship selectedRel = relationships.get(selectedIndex);

        // Choose which aspect of the relationship to edit: source, destination, or type.
        String[] editOptions = {"Source", "Destination", "Type"};
        JComboBox<String> editOptionDropdown = new JComboBox<>(editOptions);
        result = JOptionPane.showConfirmDialog(view, editOptionDropdown, "Select element to edit", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String editChoice = (String) editOptionDropdown.getSelectedItem();

        // Provide a final selection for the new value based on the element chosen.
        JComboBox<String> newValueDropdown;
        if (editChoice.equals("Type")) {
            String[] types = {"Aggregation", "Composition", "Inheritance", "Realization"};
            newValueDropdown = new JComboBox<>(types);
        } else { // For "Source" or "Destination", list available classes.
            newValueDropdown = new JComboBox<>();
            for (ClassBox cb : classBoxes) {
                newValueDropdown.addItem(cb.getClassName());
            }
        }
        result = JOptionPane.showConfirmDialog(view, newValueDropdown, "Select new " + editChoice, JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String newValue = (String) newValueDropdown.getSelectedItem();

        // Determine the updated relationship details; initialize with current values.
        ClassBox newSource = selectedRel.getSource();
        ClassBox newDestination = selectedRel.getDestination();
        String newType = selectedRel.getType();

        // Update the corresponding field based on the user's choice.
        if (editChoice.equals("Source")) {
            for (ClassBox cb : classBoxes) {
                if (cb.getClassName().equals(newValue)) {
                    // Check if the new relationship already exists.
                    if (model.relationshipExist(newValue, selectedRel.getDestination().getClassName())) {
                        view.displayErrorMessage("Relationship between " + newValue + " and " + selectedRel.getDestination().getClassName() + " already exists");
                        return;
                    }
                    newSource = cb;
                    break;
                }
            }
        } else if (editChoice.equals("Destination")) {
            for (ClassBox cb : classBoxes) {
                if (cb.getClassName().equals(newValue)) {
                    if (model.relationshipExist(selectedRel.getSource().getClassName(), newValue)) {
                        view.displayErrorMessage("Relationship between " + selectedRel.getSource().getClassName() + " and " + newValue + " already exists");
                        return;
                    }
                    newDestination = cb;
                    break;
                }
            }
        } else if (editChoice.equals("Type")) {
            newType = newValue;
        }

        // Remove the existing relationship from model and view.
        relationships.remove(selectedRel);
        view.getDrawingPanel().removeRelationship(selectedRel.getSource(), selectedRel.getDestination());
        editor.deleteRelationship(selectedRel.getSource().getClassName(), selectedRel.getDestination().getClassName());

        // Create and add the updated relationship.
        GUIRelationship updatedRel = new GUIRelationship(newSource, newDestination, newType);
        relationships.add(updatedRel);
        try {
            editor.addRelationship(newSource.getClassName(), newDestination.getClassName(), relationshipTypeToEnum(newType));
        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
            return;
        }

        // Update the view with the new relationship.
        view.getDrawingPanel().addRelationship(newSource, newDestination, newType);
        view.getDrawingPanel().repaint();
    }

    /**
     * Deletes a selected relationship from the model and view.
     */
    private void deleteRelationship() {
        if (relationships.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No relationships to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Create a dropdown for selecting the relationship to delete.
        JComboBox<String> relDropdown = new JComboBox<>();
        for (int index = 0; index < relationships.size(); index++) {
            relDropdown.addItem(relationships.get(index).getSource().getObjectFromBox().getName() + " -> " + relationships.get(index).getDestination().getObjectFromBox().getName());
        }
        int result = JOptionPane.showConfirmDialog(view, relDropdown, "Select Relationship to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Retrieve the selected relationship.
            String selectedRel = (String) relDropdown.getSelectedItem();
            GUIRelationship toRemove = relationships.get(relDropdown.getSelectedIndex());
            if (selectedRel != null) {
                // Remove relationship from both the model and the view.
                relationships.remove(toRemove);
                editor.deleteRelationship(toRemove.getSource().getClassName(), toRemove.getDestination().getClassName());
                view.getDrawingPanel().removeRelationship(toRemove.getSource(), toRemove.getDestination());
                view.getDrawingPanel().repaint();
            } else {
                JOptionPane.showMessageDialog(view, "No relationship in model between those classes!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /**
     * Removes all relationships that are associated with a given class box.
     * This is typically called when a class is deleted.
     *
     * @param classBox The class box whose relationships are to be removed.
     */
    private void removeRelationships(ClassBox classBox) {
        List<GUIRelationship> toRemoveList = new ArrayList<>();
        // Identify relationships that involve the given class box.
        for (GUIRelationship toRemove : relationships) {
            if (toRemove.getSource() == classBox || toRemove.getDestination() == classBox) {
                toRemoveList.add(toRemove);
            }
        }
        // Remove each identified relationship from the model and view.
        for (GUIRelationship toRemove : toRemoveList) {
            relationships.remove(toRemove);
            editor.deleteRelationship(toRemove.getSource().getClassName(), toRemove.getDestination().getClassName());
            view.getDrawingPanel().removeRelationship(toRemove.getSource(), toRemove.getDestination());
        }
        view.getDrawingPanel().repaint();
    }

    //================= FIELD ==============================
    /**
     * Creates a panel to enter field info on
     */
    private JPanel createFieldEntryPanel() {
        // Main panel with vertical layout for method entry.
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Create and add a label and text field for the method name.
        JLabel fieldNameLabel = new JLabel("Enter Field Name:");
        fieldNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField fieldNameField = new JTextField();
        JLabel fieldTypeLabel = new JLabel("Enter Field Type:");
        fieldTypeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField fieldTypeField = new JTextField();
        fieldNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldNameField.getPreferredSize().height));
        fieldTypeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldTypeField.getPreferredSize().height));
        entryPanel.add(fieldNameLabel);
        entryPanel.add(fieldNameField);
        entryPanel.add(fieldTypeLabel);
        entryPanel.add(fieldTypeField);

        // Store important components as client properties for later retrieval.
        entryPanel.putClientProperty("fieldNameField", fieldNameField);
        entryPanel.putClientProperty("fieldTypeField", fieldTypeField);

        return entryPanel;
    }


    /**
     * Opens a dialog to add one or more fields to the selected class. Supports
     * dynamic addition of input fields.
     */
    private void addFieldToClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a main panel for the method entry dialog.
        JPanel mainPanel = new JPanel(new BorderLayout());
        // Create the method entry panel.
        JPanel entryPanel = createFieldEntryPanel();
        mainPanel.add(entryPanel, BorderLayout.CENTER);

        // Create a panel for buttons: "Add another method" and "Done".
        JPanel buttonPanel = new JPanel();
        JButton addAnotherButton = new JButton("Add another field");
        JButton doneButton = new JButton("Done");
        buttonPanel.add(addAnotherButton);
        buttonPanel.add(doneButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create and configure a modal dialog to host the method entry panel.
        JDialog dialog = new JDialog((Frame) null, "Add Fields", true);
        dialog.getContentPane().add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(view);
        dialog.setSize(250, 175);

        dialog.setMinimumSize(new Dimension(250, 175)); // sets a minimum size

        // Action for "Add another method" button: process current input and clear fields.
        addAnotherButton.addActionListener(e -> {
            JTextField fieldNameField = (JTextField) entryPanel.getClientProperty("fieldNameField");
            JTextField fieldTypeField = (JTextField) entryPanel.getClientProperty("fieldTypeField");
            @SuppressWarnings("unchecked")
            String fieldName = fieldNameField.getText().trim();
            String fieldType = fieldTypeField.getText().trim();
            if (!fieldName.isEmpty() && !fieldType.isEmpty()) {
                // Only add the method if it does not already exist.
                if (!activeClass.fieldNameUsed(fieldName) && !fieldType.isEmpty()) {
                    selectedClassBox.addField(fieldName, fieldType);
                }
            }
            // Reset the input fields for the next method.
            fieldNameField.setText("");
            fieldTypeField.setText("");
        });

        // "Done" button processes any remaining input and closes the dialog.
        doneButton.addActionListener(e -> {
            JTextField fieldNameField = (JTextField) entryPanel.getClientProperty("fieldNameField");
            JTextField fieldTypeField = (JTextField) entryPanel.getClientProperty("fieldTypeField");
            @SuppressWarnings("unchecked")
            String fieldName = fieldNameField.getText().trim();
            String fieldType = fieldTypeField.getText().trim();
           
           //Helper function for Error checks
            fieldErrorHelper(fieldName, fieldType, entryPanel);

            if (!fieldName.isEmpty() && !activeClass.fieldNameUsed(fieldName) && !fieldType.isEmpty()) {
                selectedClassBox.addField(fieldName, fieldType);
            }
            dialog.dispose();
            view.getDrawingPanel().repaint();
        });

        // Display the dialog.
        dialog.setVisible(true);
    }

    /**
     * Deletes a selected field from the active class.
     */
    private void deleteFieldFromClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (activeClass.getFieldList().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No fields to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Create a dropdown of current field names.
        JComboBox<String> fieldDropdown = new JComboBox<>();
        for (int index = 0; index < activeClass.getFieldList().size(); index++) {
            fieldDropdown.addItem(activeClass.getFieldList().get(index).getName());
        }
        int result = JOptionPane.showConfirmDialog(view, fieldDropdown, "Select Field to Delete", JOptionPane.OK_CANCEL_OPTION);

        try {
            if (result == JOptionPane.OK_OPTION) {
                String selectedField = (String) fieldDropdown.getSelectedItem();
                if (selectedField != null) {
                    // Remove the field from the class box and model.
                    selectedClassBox.removeField(activeClass.fetchField(selectedField));
                }
            }
        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    /**
     * Renames a selected field in the active class.
     */
    private void editFieldInClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (activeClass.getFieldList().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No fields to edit!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dropdown for selecting the field to rename.
        JComboBox<String> fieldDropdown = new JComboBox<>();
        for (int index = 0; index < activeClass.getFieldList().size(); index++) {
            fieldDropdown.addItem(activeClass.getFieldList().get(index).getName());
        }
        // Text field to enter the new field name.
        JTextField newFieldNameInput = new JTextField();
        JTextField newFieldtypeInput = new JTextField();

        // Create a panel that holds both the selection and the new name input.
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Field to Edit:"));
        panel.add(fieldDropdown);
        panel.add(new JLabel("Enter New Name:"));
        panel.add(newFieldNameInput);
        panel.add(new JLabel("Enter New Type:"));
        panel.add(newFieldtypeInput);

        int result = JOptionPane.showConfirmDialog(view, panel, "Edit Field", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldName = (String) fieldDropdown.getSelectedItem();
            String newName = newFieldNameInput.getText().trim();
            String newType = newFieldtypeInput.getText().trim();
            
            //Helper function for Error checks  
            fieldErrorHelper(newName, newType, panel);

            if (!newName.isEmpty() && !newType.isEmpty()) {
                // Update the field name in the class box.
                //selectedClassBox.renameField(oldName, newName);
                selectedClassBox.editField(oldName, newName, newType);
            }
        }
    }
    //================= METHODS ==============================

    /**
     * Creates a method entry panel with a single method name field, one
     * parameter input field, a live signature preview, and an area showing
     * confirmed parameters. Client properties are stored to allow resetting the
     * panel later.
     *
     * @return The constructed JPanel for method entry.
     */
    private JPanel createMethodEntryPanel() {
        // Main panel with vertical layout for method entry.
        JPanel entryPanel = new JPanel();
        entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
        entryPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Create and add a label and text field for the method name.
        JLabel methodNameLabel = new JLabel("Enter Method Name:");
        methodNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JTextField methodNameField = new JTextField();
        methodNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, methodNameField.getPreferredSize().height));
        entryPanel.add(methodNameLabel);
        entryPanel.add(methodNameField);

        // Create a non-editable text area for the live signature preview.
        JTextArea signaturePreviewLabel = new JTextArea("Method: ");
        signaturePreviewLabel.setEditable(false);
        signaturePreviewLabel.setLineWrap(true);
        signaturePreviewLabel.setWrapStyleWord(true);
        signaturePreviewLabel.setBackground(entryPanel.getBackground()); // match background
        signaturePreviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        entryPanel.add(signaturePreviewLabel);

        // Create a panel for new parameter input with a label, text field, and an add button.
        JPanel newParamPanel = new JPanel();
        newParamPanel.setLayout(new BoxLayout(newParamPanel, BoxLayout.X_AXIS));
        JLabel paramLabel = new JLabel("Parameter: ");
        JTextField newParamField = new JTextField();
        JLabel paramTypeLabel = new JLabel("Type: ");
        JTextField newParamTypeField = new JTextField();
        newParamField.setMaximumSize(new Dimension(Integer.MAX_VALUE, newParamField.getPreferredSize().height));
        newParamTypeField.setMaximumSize(new Dimension(Integer.MAX_VALUE, newParamField.getPreferredSize().height));

        JButton addParamButton = new JButton("Add Parameter");
        newParamPanel.add(paramLabel);
        newParamPanel.add(newParamField);
        newParamPanel.add(paramTypeLabel);
        newParamPanel.add(newParamTypeField);
        newParamPanel.add(addParamButton);
        entryPanel.add(newParamPanel);


        // List to store confirmed parameters for the method.
        LinkedHashMap<String, String> confirmedParams = new LinkedHashMap<>();

        // Helper function to update the signature preview dynamically.
        Runnable updatePreview = () -> {
            String methodName = methodNameField.getText().trim();
            //*** ? ***
            String paramsString = confirmedParams.entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining(", "));
            if (methodName.isEmpty() && paramsString.isEmpty()) {
                signaturePreviewLabel.setText("Method: ");
            } else {
                signaturePreviewLabel.setText(methodName + "(" + paramsString + ")");
            }
        };

        // Add a document listener to update the preview when the method name changes.
        methodNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updatePreview.run();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePreview.run();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePreview.run();
            }
        });

        // When "Add Parameter" is pressed, add the parameter if it is valid and update the preview.
        addParamButton.addActionListener(e -> {
            String param = newParamField.getText().trim();
            String paramType = newParamTypeField.getText().trim();
              
              //Helper function for error checks
              paramErrorHelper(param, paramType, entryPanel);
            
            if (!param.isEmpty() && !confirmedParams.containsKey(param)) {
                confirmedParams.put(param, paramType);
                newParamField.setText(""); // Clear the field for the next parameter.
                newParamTypeField.setText("");
                updatePreview.run();
            } else if (confirmedParams.containsKey(param)) {
                JOptionPane.showMessageDialog(entryPanel, "Parameter already exist!", "Duplicate Parameter", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Store important components as client properties for later retrieval.
        entryPanel.putClientProperty("methodNameField", methodNameField);
        entryPanel.putClientProperty("confirmedParams", confirmedParams);
        entryPanel.putClientProperty("signaturePreviewLabel", signaturePreviewLabel);
        entryPanel.putClientProperty("newParamField", newParamField);

        return entryPanel;
    }

    /**
     * Opens a dialog that allows the user to add one method at a time. When
     * "Add another method" is pressed, the current method is processed and the
     * input fields are cleared for the next method. Pressing "Done" processes
     * any remaining input and closes the dialog.
     */
    private void addMethodToClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a main panel for the method entry dialog.
        JPanel mainPanel = new JPanel(new BorderLayout());
        // Create the method entry panel.
        JPanel entryPanel = createMethodEntryPanel();
        mainPanel.add(entryPanel, BorderLayout.CENTER);

        // Create a panel for buttons: "Add another method" and "Done".
        JPanel buttonPanel = new JPanel();
        JButton addAnotherButton = new JButton("Add another method");
        JButton doneButton = new JButton("Done");
        buttonPanel.add(addAnotherButton);
        buttonPanel.add(doneButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Create and configure a modal dialog to host the method entry panel.
        JDialog dialog = new JDialog((Frame) null, "Add Methods", true);
        dialog.getContentPane().add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(view);
        dialog.setSize(500, 300); // sets a fixed size

        dialog.setMinimumSize(new Dimension(500, 300)); // sets a minimum size

        // Action for "Add another method" button: process current input and clear fields.
        addAnotherButton.addActionListener(e -> {
            JTextField methodNameField = (JTextField) entryPanel.getClientProperty("methodNameField");
            @SuppressWarnings("unchecked")
            LinkedHashMap<String, String> confirmedParams = (LinkedHashMap<String,String >) entryPanel.getClientProperty("confirmedParams");
            String methodName = methodNameField.getText().trim();
            if (!methodName.isEmpty()) {
                // Only add the method if it does not already exist.
                if (!activeClass.methodExists(methodName, confirmedParams.size())) {
                    selectedClassBox.addMethod(methodName, confirmedParams);
                }
            }
            // Reset the input fields for the next method.
            methodNameField.setText("");
            confirmedParams.clear();

            JLabel signaturePreviewLabel = (JLabel) entryPanel.getClientProperty("signaturePreviewLabel");
            signaturePreviewLabel.setText("Method: ");
        });

        // "Done" button processes any remaining input and closes the dialog.
        doneButton.addActionListener(e -> {
            JTextField methodNameField = (JTextField) entryPanel.getClientProperty("methodNameField");
            @SuppressWarnings("unchecked")
            LinkedHashMap<String, String> confirmedParams = (LinkedHashMap<String, String>) entryPanel.getClientProperty("confirmedParams");
            String methodName = methodNameField.getText().trim();
            if (!methodName.isEmpty() && !activeClass.methodExists(methodName, confirmedParams.size())) {
                selectedClassBox.addMethod(methodName, confirmedParams);
            }
            dialog.dispose();
            view.getDrawingPanel().repaint();
        });

        // Display the dialog.
        dialog.setVisible(true);
    }

    /**
     * Deletes a method from the selected class.
     */
    private void deleteMethodFromClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve the list of methods in the active class.
        ArrayList<AttributeInterface> methods = activeClass.getMethodList();
        if (methods.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dropdown to select the method to delete.
        JComboBox<String> methodDropdown = new JComboBox<>();
        Map<String, String> displayString = new HashMap<>();

        // Populate the dropdown with method display strings.
        for (int index = 0; index < activeClass.getMethodList().size(); index++) {
            Method m = (Method) activeClass.getMethodList().get(index);
            String str = activeClass.getMethodList().get(index).getName() + ":" + m.getParamList().size();
            String strdisplay = selectedClassBox.displayMethod(m);
            methodDropdown.addItem(strdisplay);
            displayString.put(strdisplay, str);
        }

        int result = JOptionPane.showConfirmDialog(view, methodDropdown, "Select Method to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Get the selected method's unique string identifier.
            String displaySelectedMethod = (String) methodDropdown.getSelectedItem();
            String selectedMethod = displayString.get(displaySelectedMethod);

            if (selectedMethod != null) {
                // Remove the method from the class box and update the view.
                selectedClassBox.removeMethod(selectedMethod);
            }
        }
    }

    /**
     * Renames a selected method in the active class.
     */
    private void renameMethodInClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<AttributeInterface> methods = activeClass.getMethodList();
        if (methods.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods to rename!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create a dropdown to select the method to rename.
        JComboBox<String> methodDropdown = new JComboBox<>();
        Map<String, String> displayString = new HashMap<>();

        // Populate the dropdown with display strings for each method.
        for (int index = 0; index < activeClass.getMethodList().size(); index++) {
            Method m = (Method) activeClass.getMethodList().get(index);

            String str = activeClass.getMethodList().get(index).getName() + ":" + m.getParamList().size();
            String strdisplay = selectedClassBox.displayMethod(m);
            methodDropdown.addItem(strdisplay);
            displayString.put(strdisplay, str);
        }

        // Create a text field for entering the new method name.
        JTextField newMethodNameInput = new JTextField();

        // Build a panel that includes both the method selection and new name input.
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Method to Rename:"));
        panel.add(methodDropdown);
        panel.add(new JLabel("Enter New Name:"));
        panel.add(newMethodNameInput);

        int result = JOptionPane.showConfirmDialog(view, panel, "Rename Method", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String displayChoice = (String) methodDropdown.getSelectedItem();
            String oldName = displayString.get(displayChoice);
            String newName = newMethodNameInput.getText().trim();

            if (!newName.isEmpty() && oldName != null) {
                // Update the method name in the class box.
                selectedClassBox.renameMethod(oldName, newName);
            }
        }
    }

    // ======================= PARAMETERS =============================== //
    /**
     * Prompts the user to select a method from the active class and then enter
     * a new parameter name. The new parameter is added to the selected method.
     */
    private void addParameterToMethod() {
        if (activeClass == null) {
            JOptionPane.showMessageDialog(view, "Select a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve the list of methods.
        ArrayList<AttributeInterface> methodList = activeClass.getMethodList();
        if (methodList.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods available in the selected class!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Let the user select the method via a dropdown.
        JComboBox<String> methodDropdown = new JComboBox<>();
        for (AttributeInterface ai : methodList) {
            Method m = (Method) ai;
            methodDropdown.addItem(m.getName());
        }
        int result = JOptionPane.showConfirmDialog(view, methodDropdown, "Select Method", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // Identify the selected method.
        String selectedMethodName = (String) methodDropdown.getSelectedItem();
        Method selectedMethod = null;
        for (AttributeInterface ai : methodList) {
            Method m = (Method) ai;
            if (m.getName().equals(selectedMethodName)) {
                selectedMethod = m;
                break;
            }
        }
        if (selectedMethod == null) {
            return;
        }

        // Prompt for the new parameter name.
        JTextField paramField = new JTextField();
        result = JOptionPane.showConfirmDialog(view, paramField, "Enter New Parameter Name", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String newParamName = paramField.getText().trim();
        //Error check
        if (newParamName.isEmpty()) {
            //newParamType = "void";
            JOptionPane.showMessageDialog(view, "Parameter must have a name!", "Name Error", JOptionPane.WARNING_MESSAGE);
            return;
            }

        //Prompt for the parameter type
        JTextField paramTypeField = new JTextField();
        result = JOptionPane.showConfirmDialog(view, paramTypeField, "Enter New Parameter Type", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        String newParamType = paramTypeField.getText().trim();
        //Error checks
        if (newParamType.isEmpty()) {
            //newParamType = "void";
            JOptionPane.showMessageDialog(view, "Parameter must have a type!", "Type Error", JOptionPane.WARNING_MESSAGE);
            return;
        }


        
        // ****** Dont Forget this Section ******
        
        // Update the selected method with the new parameter.
        selectedMethod.addParameter(newParamName, newParamType);
        // Optionally, update the model via editor.

        // Refresh the method display.
        selectedClassBox.updateMethodDisplay(selectedMethod);

        selectedClassBox.revalidate();
        selectedClassBox.repaint();
        view.getDrawingPanel().repaint();
    }

    /**
     * Prompts the user to select a method and then one of its parameters for
     * deletion.
     */
    private void deleteParameterFromMethod() {
        if (activeClass == null) {
            JOptionPane.showMessageDialog(view, "Select a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<AttributeInterface> methodList = activeClass.getMethodList();
        if (methodList.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods available in the selected class!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Allow the user to select a method.
        JComboBox<String> methodDropdown = new JComboBox<>();
        for (AttributeInterface ai : methodList) {
            Method m = (Method) ai;
            methodDropdown.addItem(m.getName());
        }
        int result = JOptionPane.showConfirmDialog(view, methodDropdown, "Select Method", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // Identify the selected method.
        String selectedMethodName = (String) methodDropdown.getSelectedItem();
        Method selectedMethod = null;
        for (AttributeInterface ai : methodList) {
            Method m = (Method) ai;
            if (m.getName().equals(selectedMethodName)) {
                selectedMethod = m;
                break;
            }
        }
        if (selectedMethod == null) {
            return;
        }

        // Check if the method has any parameters.
        if (selectedMethod.getParamList().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No parameters available in the selected method!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Let the user select the parameter to delete.
        JComboBox<String> paramDropdown = new JComboBox<>();
        for (Parameter param : selectedMethod.getParamList()) {
            paramDropdown.addItem(param.getName());
        }
        result = JOptionPane.showConfirmDialog(view, paramDropdown, "Select Parameter to Delete", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String selectedParamName = (String) paramDropdown.getSelectedItem();
        // Remove the selected parameter from the method.
        selectedMethod.removeParameter(selectedParamName);

        // Update the method display.
        selectedClassBox.updateMethodDisplay(selectedMethod);
        selectedClassBox.revalidate();
        selectedClassBox.repaint();
        view.getDrawingPanel().repaint();
    }

    /**
     * Prompts the user to select a method and then one of its parameters, and
     * finally allows entry of a new parameter name to update the selected
     * parameter.
     */
    private void changeParameterInMethod() {
        if (activeClass == null) {
            JOptionPane.showMessageDialog(view, "Select a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<AttributeInterface> methodList = activeClass.getMethodList();
        if (methodList.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods available in the selected class!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Let the user select the method.
        JComboBox<String> methodDropdown = new JComboBox<>();
        for (AttributeInterface ai : methodList) {
            Method m = (Method) ai;
            methodDropdown.addItem(m.getName());
        }
        int result = JOptionPane.showConfirmDialog(view, methodDropdown, "Select Method", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        // Identify the selected method.
        String selectedMethodName = (String) methodDropdown.getSelectedItem();
        Method selectedMethod = null;
        for (AttributeInterface ai : methodList) {
            Method m = (Method) ai;
            if (m.getName().equals(selectedMethodName)) {
                selectedMethod = m;
                break;
            }
        }
        if (selectedMethod == null) {
            return;
        }

        // Ensure the method has parameters to edit.
        if (selectedMethod.getParamList().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No parameters available in the selected method!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Let the user select the parameter to change.
        JComboBox<String> paramDropdown = new JComboBox<>();
        for (Parameter param : selectedMethod.getParamList()) {
            paramDropdown.addItem(param.getName());
        }
        result = JOptionPane.showConfirmDialog(view, paramDropdown, "Select Parameter to Edit", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String oldParamName = (String) paramDropdown.getSelectedItem();
        

        //***** 

        // Prompt for the new parameter name, pre-filled with the current name.
        JTextField paramField = new JTextField(oldParamName);
        result = JOptionPane.showConfirmDialog(view, paramField, "Enter New Parameter Name", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        //creates a variable to store the new parameter name
        String newParamName = paramField.getText().trim();
        if (newParamName.isEmpty()) {
            //newParamType = "void";
            JOptionPane.showMessageDialog(view, "Parameter must have a name!", "Name Error", JOptionPane.WARNING_MESSAGE);
            return;
            }
        // Prompt for the  Parameter type
        JTextField paramTypeField = new JTextField();
        result = JOptionPane.showConfirmDialog(view, paramTypeField, "Enter the new Parameter's Type", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }
        //creates a variable to store the new parameter type
        String newParamType = paramTypeField.getText().trim();
        if (newParamType.isEmpty()) {
            //newParamType = "void";
            JOptionPane.showMessageDialog(view, "Parameter must have a type!", "Type Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Update the parameter in the selected method with the new parameter name and type.
        selectedMethod.updateParameter(oldParamName, newParamName, newParamType);

        // Refresh the method display.
        selectedClassBox.updateMethodDisplay(selectedMethod);
        selectedClassBox.revalidate();
        selectedClassBox.repaint();
        view.getDrawingPanel().repaint();
    }

    //
    // ==================== GRID POSITIONING ==================== //
    /**
     * Determines the next position for a new class box based on a grid layout.
     * The position is calculated to evenly space boxes and center them in the
     * panel.
     *
     * @return A Point object representing the next available x and y
     * coordinates.
     */
    private Point getNextGridPosition() {

        int numBoxes = classBoxes.size();
        int boxWidth = 150;
        int boxHeight = 150;
        int padding = 20;
        int panelWidth = view.getDrawingPanel().getWidth();
        int panelHeight = view.getDrawingPanel().getHeight();

        // Calculate the maximum number of columns based on panel width.
        int maxCols = (panelWidth - padding) / (boxWidth + padding);
        if (maxCols == 0) {
            maxCols = 1;
        }

        // Determine the grid column and row for the new box.
        int gridCol = numBoxes % maxCols;
        int gridRow = numBoxes / maxCols;

        // Calculate the total width of the grid and the starting x position for centering.
        int totalGridWidth = (maxCols * (boxWidth + padding)) - padding;
        int startX = (panelWidth - totalGridWidth) / 2;

        // Calculate the total grid height and starting y position for centering.
        int totalGridHeight = ((numBoxes / maxCols + 1) * (boxHeight - padding)) - padding;
        int startY = (panelHeight - totalGridHeight) / 2;

        int x = startX + gridCol * (boxWidth + padding);
        int y = startY + gridRow * (boxHeight + padding);

        return new Point(x, y);

    }

    // ==================== CLASS SELECTION ==================== //
    /**
     * Sets the provided class box as the currently selected box. Updates its
     * border to visually indicate selection.
     *
     * @param classBox The class box that has been selected.
     */
    public void selectClassBox(ClassBox classBox) {
        // If a class was already selected, change its border back to a default color.
        if (selectedClassBox != null) {
            selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        }

        // Update the selected class box and highlight it.
        selectedClassBox = classBox;
        selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        // Set the active class to the one represented by the selected box.
        activeClass = classBox.getObjectFromBox();
    }

    // ==================== SAVE/LOAD MANAGEMENT ==================== //
    /**
     * Prompts the user for a file path and saves the current diagram to that
     * location.
     */
    private void saveDiagram() {
        String path = JOptionPane.showInputDialog(view, "Where would you like to save:");
        try {
            FileManager fileManager = new FileManager();
            // Save the model to the specified file path.
            fileManager.save(path.trim(), model);
            JOptionPane.showMessageDialog(view, "Diagram saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * This method is called whenever view.getLoadButton is pressed.
     * Saves the model
     */
    private void loadDiagram() {
        String path = JOptionPane.showInputDialog(view, "Where would you like to load from:");
        try {
            FileManager fileManager = new FileManager();
		for(ClassBox cb: classBoxes){
		    view.getDrawingPanel().remove(cb);
		    removeRelationships(cb);
		}

		view.getDrawingPanel().revalidate();
		view.getDrawingPanel().repaint();

                model = fileManager.load(path.trim());
		editor = new UMLEditor(model);
		refreshClassBoxes(model);
		JOptionPane.showMessageDialog(view, "Diagram loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	    }
	    catch (Exception e) {
		    JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
    }



    /**
     * Updates the classbox and relationship according to the model
     */
    private void refreshClassBoxes(UMLModel model){
    // Remove classes and relationships from the controller
	classBoxes.clear();
	relationships.clear();
    // And also remove them from the view
	view.getDrawingPanel().removeAll();
    view.getDrawingPanel().removeAllRelationships();
	for(ClassObject c:model.getClassList())
	    addClass(c);

	for(Relationship r: model.getRelationshipList()){
	    ClassBox s = null;
	    ClassBox d = null;
	    String sn,dn;
	    sn = r.getSource().getName();
	    dn = r.getDestination().getName();
	    for(ClassBox b: classBoxes){
		s = b.getClassName().equals(sn) ? b : s;
		d = b.getClassName().equals(dn) ? b : d;
	    }
	    String type = r.getTypeString();
	    relationships.add(new GUIRelationship(s, d, type));
	    view.getDrawingPanel().addRelationship(s, d, type);
	    view.getDrawingPanel().revalidate();
	    view.getDrawingPanel().repaint();
	}
    }

    /**
     * Getter for the UMLEditor.
     *
     * @return The UMLEditor instance used by this controller.
     */
    public UMLEditor getEditor() {
        return this.editor;
    }

    public void undo(){
        try{
        editor.undo();
	    this.model = editor.getModel();
	    refreshClassBoxes(model);
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
        } catch(Exception e){
        JOptionPane.showMessageDialog(view, "Nothing to undo!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
        }
    }

    public void redo(){
        try{		
        editor.redo();
	    this.model = editor.getModel();
	    refreshClassBoxes(model);
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
        } catch(Exception e){
        JOptionPane.showMessageDialog(view, "Nothing to redo!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
        }
    }


    // ==================== HELP & EXIT ==================== //
    /**
     * Exits the application after optionally saving the current diagram.
     */
    private void exitDiagram() {
        int choice = JOptionPane.showConfirmDialog(
                null, // Parent component (null centers the dialog)
                "Do you want to save before exiting?", // Message
                "Confirm Exit",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            saveDiagram();
            System.exit(0);  // Exits the application
        } else if (choice == JOptionPane.NO_OPTION) {
            System.exit(0);
        }
    }

    // ==================== RELATIONSHIP CLASS ==================== //
    /**
     * Defines a relationship between two class boxes.
     */
    private static class GUIRelationship {

        private final ClassBox source;
        private final ClassBox destination;
        private final String type;

        /**
         * Constructs a GUIRelationship with the specified source, destination,
         * and relationship type.
         *
         * @param source The source class box.
         * @param destination The destination class box.
         * @param type The type of relationship (e.g., Aggregation,
         * Composition).
         */
        public GUIRelationship(ClassBox source, ClassBox destination, String type) {
            this.source = source;
            this.destination = destination;
            this.type = type;
        }

        /**
         * Getter for the source class box.
         *
         * @return The source ClassBox.
         */
        public ClassBox getSource() {
            return source;
        }

        /**
         * Getter for the destination class box.
         *
         * @return The destination ClassBox.
         */
        public ClassBox getDestination() {
            return destination;
        }

        /**
         * Getter for the relationship type.
         *
         * @return The relationship type as a String.
         */
        public String getType() {
            return type;
        }
    }

    private void paramErrorHelper(String param, String paramType, JPanel entryPanel){
        if(param.isEmpty() && paramType.isEmpty()){
            JOptionPane.showMessageDialog(entryPanel, "Parameters cannot be empty", "State Error", JOptionPane.WARNING_MESSAGE);
            return;
            }
            if (param.isEmpty()) {
            // Parameter must have a name
            JOptionPane.showMessageDialog(entryPanel, "Parameter must have a name!", "Name Error", JOptionPane.WARNING_MESSAGE);
            return;
            }
            if (paramType.isEmpty()){
                // Paramter must have a type
                JOptionPane.showMessageDialog(entryPanel, "Parameter must have a type!", "Type Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
    }
    private void fieldErrorHelper(String fieldName, String fieldType, JPanel entryPanel){
        if(fieldName.isEmpty() && fieldType.isEmpty()) {
                JOptionPane.showMessageDialog(entryPanel, "Field cannot be empty!", "State Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(fieldName.isEmpty()){           
                JOptionPane.showMessageDialog(entryPanel, "Field must have a name!", "Name Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if(fieldType.isEmpty()){           
                JOptionPane.showMessageDialog(entryPanel, "Field must have a type!", "Type Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
    }
}
