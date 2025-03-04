package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import view.GUIView;
import view.ClassBox;

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

     /**
     * Handles selecting source and destination for a relationship.
     */
    // ==================== RELATIONSHIP HANDLING ==================== //
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

    // ==================== GRID POSITIONING ==================== //
    /**
     * Determines the next position for a new class box based on a grid layout.
     */
    private Point getNextGridPosition() {
        int numBoxes = classBoxes.size();
        int maxCols = view.getDrawingPanel().getWidth() / (150 + 20);
        if (maxCols == 0) maxCols = 1;

        int gridCols = numBoxes % maxCols;
        int gridRows = numBoxes / maxCols;

        return new Point(gridCols * (150 + 20), gridRows * (100 + 20));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIView view = new GUIView();
            new GUIController(view);
            view.showGUI();
        });
    }
}