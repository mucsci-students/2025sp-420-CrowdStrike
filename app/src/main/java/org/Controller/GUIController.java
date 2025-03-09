package org.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import org.View.GUIView;
import org.View.ClassBox;
import org.FileManager;
import org.Model.*;
import org.Model.Relationship.Type;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class GUIController {

    private UMLModel model;
    private UMLEditor editor;
    ClassObject activeClass = null;

    private GUIView view;
    private List<ClassBox> classBoxes = new ArrayList<>();
    private List<GUIRelationship> relationships = new ArrayList<>();
    private ClassBox selectedClassBox = null;
    private boolean addRelationshipMode = false;
    private ClassBox selectedDestination = null;
    private ClassBox selectedSource = null;

    
    

    /**
	 * Constructor for GUI
	 * @param view | Contains methods that will paint the Panel of the Diagram
	 */
    public GUIController(UMLModel model, UMLEditor editor, GUIView view) {
        this.view = view;
        this.model = model;
		this.editor = editor;
    }

    // ==================== INITIALIZATION ==================== //

    /**
	 * Checks if buttons were pressed
	 */
    public void initController() {
        initButtonActions();
        view.showGUI();
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

        view.getEditRelationshipButton().addActionListener(e -> editRelationship());

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

        if (model.isValidClassName(className)!=0) return; //replace with switch for more specific error messages

        className = className.trim();
        
        editor.addClass(className);
        activeClass = model.fetchClass(className);
        addClass(activeClass);
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
    private void addClass(ClassObject newClass) {
        ClassBox classBox = new ClassBox(newClass, this);
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
	editor.deleteClass(selectedClassBox.getClassName());
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

    private Type relationshipTypeToEnum(String t){
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

        GUIRelationship relationship = new GUIRelationship(source, destination, type);
        relationships.add(relationship);
	editor.addRelationship("",selectedSource.getClassName(), selectedDestination.getClassName(),relationshipTypeToEnum(type));

        view.getDrawingPanel().addRelationship(source, destination, type);
        resetRelationshipSelection();

    }

    private void editRelationship() {
    // Check if there are any relationships to edit
    if (relationships.isEmpty()) {
        JOptionPane.showMessageDialog(view, "No relationships to edit!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Select the relationship to edit
    JComboBox<String> relDropdown = new JComboBox<>();
    for (GUIRelationship rel : relationships) {
        String relString = rel.getSource().getObjectFromBox().getName() + " -> " + rel.getDestination().getObjectFromBox().getName();
        relDropdown.addItem(relString);
    }
    int result = JOptionPane.showConfirmDialog(view, relDropdown, "Select Relationship to Edit", JOptionPane.OK_CANCEL_OPTION);
    if (result != JOptionPane.OK_OPTION) return;
    
    int selectedIndex = relDropdown.getSelectedIndex();
    if (selectedIndex < 0) return;
    GUIRelationship selectedRel = relationships.get(selectedIndex);
    
    // Choose which aspect to edit: source, dest, type
    String[] editOptions = {"Source", "Destination", "Type"};
    JComboBox<String> editOptionDropdown = new JComboBox<>(editOptions);
    result = JOptionPane.showConfirmDialog(view, editOptionDropdown, "Select element to edit", JOptionPane.OK_CANCEL_OPTION);
    if (result != JOptionPane.OK_OPTION) return;
    String editChoice = (String) editOptionDropdown.getSelectedItem();
    
    // Provide a final selection for the new value based on the element chosen
    JComboBox<String> newValueDropdown;
    if (editChoice.equals("Type")) {
        String[] types = {"Aggregation", "Composition", "Inheritance", "Realization"};
        newValueDropdown = new JComboBox<>(types);
    } else { // For "Source" or "Destination", list available classes
        newValueDropdown = new JComboBox<>();
        for (ClassBox cb : classBoxes) {
            newValueDropdown.addItem(cb.getClassName());
        }
    }
    result = JOptionPane.showConfirmDialog(view, newValueDropdown, "Select new " + editChoice, JOptionPane.OK_CANCEL_OPTION);
    if (result != JOptionPane.OK_OPTION) return;
    String newValue = (String) newValueDropdown.getSelectedItem();
    
    // Determine the updated relationship details; initialize with current values.
    ClassBox newSource = selectedRel.getSource();
    ClassBox newDestination = selectedRel.getDestination();
    String newType = selectedRel.getType();
    
    if (editChoice.equals("Source")) {
        for (ClassBox cb : classBoxes) {
            if (cb.getClassName().equals(newValue)) {
                newSource = cb;
                break;
            }
        }
    } 
    else if (editChoice.equals("Destination")) {
        for (ClassBox cb : classBoxes) {
            if (cb.getClassName().equals(newValue)) {
                newDestination = cb;
                break;
            }
        }
    } 
    else if (editChoice.equals("Type")) {
        newType = newValue;
    }
    
    // ABOVE: Getting relevant info
    // BELOW: Updating the model and view
    relationships.remove(selectedRel);
    view.getDrawingPanel().removeRelationship(selectedRel.getSource(), selectedRel.getDestination());
    editor.deleteRelationship(selectedRel.getSource().getClassName(), selectedRel.getDestination().getClassName());
    
    // Create an updated relationship
    GUIRelationship updatedRel = new GUIRelationship(newSource, newDestination, newType);
    relationships.add(updatedRel);
    editor.addRelationship("", newSource.getClassName(), newDestination.getClassName(), relationshipTypeToEnum(newType));
    view.getDrawingPanel().addRelationship(newSource, newDestination, newType);
    view.getDrawingPanel().repaint();
    }

    /**
     * Deletes the last relationship added.
     */
    private void deleteRelationship() {
        if (relationships.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No relationships to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JComboBox<String> relDropdown = new JComboBox<>();
        for(int index = 0; index<relationships.size(); index++){
            relDropdown.addItem(relationships.get(index).getSource().getObjectFromBox().getName() + " -> " + relationships.get(index).getDestination().getObjectFromBox().getName());
        }
        int result = JOptionPane.showConfirmDialog(view, relDropdown, "Select Relationship to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedRel = (String) relDropdown.getSelectedItem();
            GUIRelationship toRemove = relationships.get(relDropdown.getSelectedIndex());
            if (selectedRel != null) {
                relationships.remove(toRemove);
	            editor.deleteRelationship(toRemove.getSource().getClassName(),toRemove.getDestination().getClassName());
                view.getDrawingPanel().removeRelationship(toRemove.getSource(), toRemove.getDestination());
                view.getDrawingPanel().repaint();
            }
            else{
                JOptionPane.showMessageDialog(view, "No relationship in model between those classes!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
            }
        }   
    }

    private void removeRelationships(ClassBox classBox) {
        List<GUIRelationship> toRemoveList = new ArrayList<>();
        for (GUIRelationship toRemove : relationships) {
            if (toRemove.getSource() == classBox || toRemove.getDestination() == classBox) {
                toRemoveList.add(toRemove);
            }
        }
        for (GUIRelationship toRemove : toRemoveList) {
            relationships.remove(toRemove);
            editor.deleteRelationship(toRemove.getSource().getClassName(), toRemove.getDestination().getClassName());
            view.getDrawingPanel().removeRelationship(toRemove.getSource(), toRemove.getDestination());
        }
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
                AttributeInterface field = activeClass.fetchField(fieldName);
                if (!fieldName.isEmpty()) {
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

        if (activeClass.getFieldList().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No fields to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JComboBox<String> fieldDropdown = new JComboBox<>();
        for(int index = 0; index<activeClass.getFieldList().size(); index++){
            fieldDropdown.addItem(activeClass.getFieldList().get(index).getName());
        }
        int result = JOptionPane.showConfirmDialog(view, fieldDropdown, "Select Field to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedField = (String) fieldDropdown.getSelectedItem();
            if (selectedField != null) {
                selectedClassBox.removeField(activeClass.fetchField(selectedField));
            }
        }
    }

    private void renameFieldInClass() {
        if (selectedClassBox == null) {
            JOptionPane.showMessageDialog(view, "Click a class first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (activeClass.getFieldList().isEmpty()) {
            JOptionPane.showMessageDialog(view, "No fields to rename!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> fieldDropdown = new JComboBox<>();
        for(int index = 0; index<activeClass.getFieldList().size(); index++){
            fieldDropdown.addItem(activeClass.getFieldList().get(index).getName());
        }
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
            ArrayList<String> params = new ArrayList<>();
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
            
            /*
            // Format the method signature as "methodName(param1, param2, ...)".
            StringBuilder signature = new StringBuilder(methodName);
            signature.append("(");
            if (!params.isEmpty()) {
                signature.append(String.join(", ", params));
            }
            signature.append(")");
            */
            
            // Only add the method if it doesn't already exist.
            AttributeInterface method = activeClass.fetchMethod(methodName, params.size());
                if (!methodName.isEmpty() && !activeClass.getMethodList().contains(method)) {
                    selectedClassBox.addMethod(methodName, params);
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

        ArrayList<AttributeInterface> methods = activeClass.getMethodList();
        if (methods.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No methods to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JComboBox<String> methodDropdown = new JComboBox<>();
            for(int index = 0; index<activeClass.getMethodList().size(); index++){
                Method m = (Method)activeClass.getMethodList().get(index);
                String str = activeClass.getMethodList().get(index).getName() + ":" + m.getParamList().size();
                methodDropdown.addItem(str);
            }


        int result = JOptionPane.showConfirmDialog(view, methodDropdown, "Select Method to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedMethod = (String)methodDropdown.getSelectedItem();
            if (selectedMethod != null) {
                //String parts = selectedMethod.getName() + ":" + selectedMethod.getParamList.size();
                selectedClassBox.removeMethod(selectedMethod);
            }
        }
    }

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

        JComboBox<String> methodDropdown = new JComboBox<>();
            for(int index = 0; index<activeClass.getMethodList().size(); index++){
                Method m = (Method)activeClass.getMethodList().get(index);
                String str = activeClass.getMethodList().get(index).getName() + ":" + m.getParamList().size();
                methodDropdown.addItem(str);
            }
        JTextField newMethodNameInput = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Select Method to Rename:"));
        panel.add(methodDropdown);
        panel.add(new JLabel("Enter New Name:"));
        panel.add(newMethodNameInput);

        int result = JOptionPane.showConfirmDialog(view, panel, "Rename Method", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String oldName = (String)methodDropdown.getSelectedItem();
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
        activeClass = classBox.getObjectFromBox();
    }


    // ==================== SAVE/LOAD MANAGEMENT ==================== //
    private void saveDiagram() {
        String path = JOptionPane.showInputDialog(view, "Where would you like to save:");
            try {
                FileManager fileManager = new FileManager();
                fileManager.save(path.trim(), model);
                JOptionPane.showMessageDialog(view, "Diagram saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    }

    private void loadDiagram() {
        String path = JOptionPane.showInputDialog(view, "Where would you like to load from:");
            try {
                FileManager fileManager = new FileManager();
                model = fileManager.load(path.trim());
                JOptionPane.showMessageDialog(view, "Diagram loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
		    relationships.add(new GUIRelationship(s, d, r.getTypeString()));
		}
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
    }
    

    public UMLEditor getEditor(){
        return this.editor;
    }



    // ==================== RELATIONSHIP CLASS ==================== //
    /**
     * Defines a relationship between two class boxes.
     */
    private static class GUIRelationship {
        private final ClassBox source;
        private final ClassBox destination;
        private final String type;

        public GUIRelationship(ClassBox source, ClassBox destination, String type) {
            this.source = source;
            this.destination = destination;
            this.type = type;
        }

        public ClassBox getSource() { return source; }
        public ClassBox getDestination() { return destination; }
        public String getType() { return type; }
    }

    


}
