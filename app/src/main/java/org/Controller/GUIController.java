package org.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.View.GUIView;
import org.View.ClassBox;
import org.FileManager;
import org.Model.UMLModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUIController {

    private UMLModel model;
    private UMLEditor editor;
    //ClassObject activeClass = null;

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
        //this.model = model;
		//this.editor = editor;
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

        view.getSaveButton().addActionListener(e -> saveDiagram());
        view.getLoadButton().addActionListener(e -> loadDiagram());
        

        
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
        //editor.addClass(className);
        addClass(className);
    }


    /**
     * Renames the currently selected class.
     */
    private void renameSelectedClass(){
        if(selectedClassBox == null){
            JOptionPane.showMessageDialog(view, "No class selected","Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newClassName = JOptionPane.showInputDialog(view, "Enter New ClassName:", selectedClassBox.getClassName());

        if(newClassName == null || newClassName.trim().isEmpty()){
            return;
        }

        newClassName = newClassName.trim();

        for (ClassBox box: classBoxes){
            if(box != selectedClassBox && box.getClassName().equalsIgnoreCase(newClassName)){
                JOptionPane.showMessageDialog(view, "Class name already exist!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        //Update the ClassBox
        selectedClassBox.setClassName(newClassName);

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
     * @param className The name of the new class
     */
    private void addClass(String className) {
        ClassBox classBox = new ClassBox(className, this);
        Point position = getNextGridPosition();
        classBox.setBounds(position.x, position.y, 150, 200);

        classBox.setOpaque(true);
        classBox.setBackground(Color.WHITE);

        //Add MouseListener to detect clicks anywhere inside the box
        configureClassBoxMouseListener(classBox);

        view.getDrawingPanel().add(classBox);
        classBoxes.add(classBox);
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
    }

    private void configureClassBoxMouseListener(ClassBox classBox){
        classBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //selectClassBox(classBox);
                
                if (addRelationshipMode) {
                    handleRelationshipSelection(classBox);
                } else {
                    selectClassBox(classBox);
                }

                e.consume();
            }
        });
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

        view.getDrawingPanel().addRelationship(source, destination, type);
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
        view.getDrawingPanel().removeRelationship(toRemove.getSource(), toRemove.getDestination());

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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(400,300));//Increase dialog size

        List<JTextField> fieldInputs = new ArrayList<>();
        JButton addFieldButton = new JButton("+");
        
        

        // Initial Field Input
        //JTextField fieldInput = new JTextField();
        //fieldInputs.add(fieldInput);

        JLabel label = new JLabel("Enter Field Name:");
        JTextField fieldInput = new JTextField();
        fieldInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, fieldInput.getPreferredSize().height));//limits height
        fieldInputs.add(fieldInput);


        //panel.add(new JLabel("Enter Field Name:"));
        panel.add(label);
        panel.add(fieldInput);
        panel.add(addFieldButton);

        // Add more fields dynamically when clicking "+"
        addFieldButton.addActionListener(e -> {
            //JTextField newFieldInput = new JTextField();
            //fieldInputs.add(newFieldInput);

            JLabel newLabel = new JLabel("Enter Field Name:");
            JTextField newFieldInput = new JTextField();
            newFieldInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, newFieldInput.getPreferredSize().height));
            fieldInputs.add(newFieldInput);
            panel.add(newLabel);
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
//================= METHODS ==============================
   
   //Helper method for addMethods()
   private JPanel createMethodEntryPanel() {
    JPanel entryPanel = new JPanel();
    entryPanel.setLayout(new BoxLayout(entryPanel, BoxLayout.Y_AXIS));
    entryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
    ));
    
    // Method name input
    JLabel label = new JLabel("Enter Method Name:");
    JTextField methodInput = new JTextField();
    methodInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, methodInput.getPreferredSize().height));
    entryPanel.add(label);
    entryPanel.add(methodInput);
    
    // Parameter inputs section
    JLabel paramLabel = new JLabel("Parameters:");
    entryPanel.add(paramLabel);
    JPanel paramPanel = new JPanel();
    paramPanel.setLayout(new BoxLayout(paramPanel, BoxLayout.Y_AXIS));
    entryPanel.add(paramPanel);
    
    // Button to add parameter fields
    JButton addParamButton = new JButton("Add Parameter");
    addParamButton.addActionListener(e -> {
        JLabel newParamLabel = new JLabel("Parameter:");
        JTextField paramField = new JTextField();
        paramField.setMaximumSize(new Dimension(Integer.MAX_VALUE, paramField.getPreferredSize().height));
        paramPanel.add(newParamLabel);
        paramPanel.add(paramField);
        paramPanel.revalidate();
        paramPanel.repaint();
    });
    entryPanel.add(addParamButton);
    
    // Store the method input field and parameter panel for later retrieval using client properties.
    //this is swing method
    entryPanel.putClientProperty("methodInput", methodInput);
    entryPanel.putClientProperty("paramPanel", paramPanel);
    
    return entryPanel;
}

private void addMethodToClass() {
    if (selectedClassBox == null) {
        JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    // Create the container panel for method entries.
    JPanel methodpanel = new JPanel();
    methodpanel.setLayout(new BoxLayout(methodpanel, BoxLayout.Y_AXIS));
    methodpanel.setPreferredSize(new Dimension(400, 300));
    
    // List to hold each method entry panel.
    List<JPanel> methodEntryPanels = new ArrayList<>();
    
    // Add an initial method entry panel.
    JPanel entry = createMethodEntryPanel();
    methodEntryPanels.add(entry);
    methodpanel.add(entry);
    
    // Button to add additional method entry panels.
    JButton addMethodButton = new JButton("Add another method");
    addMethodButton.addActionListener(e -> {
        JPanel newEntry = createMethodEntryPanel();
        methodEntryPanels.add(newEntry);
        methodpanel.add(newEntry);
        methodpanel.revalidate();
        methodpanel.repaint();
    });
    methodpanel.add(addMethodButton);
    
    int result = JOptionPane.showConfirmDialog(view, methodpanel, "Add Methods", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        // Process each method entry panel.
        for (JPanel entryPanel : methodEntryPanels) {
            // Retrieve the method name from the client property.
            JTextField methodInput = (JTextField) entryPanel.getClientProperty("methodInput");
            if (methodInput == null) continue;
            String methodName = methodInput.getText().trim();
            if (methodName.isEmpty()) continue;
            
            // Retrieve the parameter panel and extract all parameter names.
            JPanel paramPanel = (JPanel) entryPanel.getClientProperty("paramPanel");
            List<String> params = new ArrayList<>();
            if (paramPanel != null) {
                for (Component comp : paramPanel.getComponents()) {
                    if (comp instanceof JTextField) {
                        String param = ((JTextField) comp).getText().trim();
                        if (!param.isEmpty()) {
                            params.add(param);
                        }
                    }
                }
            }
            
            // Format the method signature as "methodName(param1, param2, ...)".
            StringBuilder signature = new StringBuilder(methodName);
            signature.append("(");
            if (!params.isEmpty()) {
                signature.append(String.join(", ", params));
            }
            signature.append(")");
            
            // Only add the method if it doesn't already exist.
            if (!selectedClassBox.getMethods().contains(signature.toString())) {
                selectedClassBox.addMethod(signature.toString());
            }
        }
        view.getDrawingPanel().repaint();
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

    public void selectClassBox(ClassBox classBox) {
        if (selectedClassBox != null) {
            selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        selectedClassBox = classBox;
        selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    }


    // ==================== SAVE/LOAD MANAGEMENT ==================== //
    private void saveDiagram() {
        String path = JOptionPane.showInputDialog(view, "Where would you like to save:");
    
        if (path != null && !path.trim().isEmpty()) { // Ensure path is not null or empty
            try {
                FileManager fileManager = new FileManager();
                fileManager.save(path.trim(), model);
                JOptionPane.showMessageDialog(view, "Diagram saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Failed to save diagram!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Invalid file path!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDiagram() {
        String path = JOptionPane.showInputDialog(view, "Where would you like to load from:");

        if (path != null && !path.trim().isEmpty()) { // Ensure path is not null or empty
            try {
                FileManager fileManager = new FileManager();
                model = fileManager.load(path.trim());
                JOptionPane.showMessageDialog(view, "Diagram loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Failed to load diagram!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(view, "Invalid file path!", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
