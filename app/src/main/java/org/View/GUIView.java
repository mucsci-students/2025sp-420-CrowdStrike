package org.View;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

@Deprecated
public class GUIView extends JFrame {

    private JButton addClassButton;
    private JButton renameClassButton;
    private JButton deleteClassButton;

    private JToggleButton addRelationshipButton;
    private JButton deleteRelationshipButton;
    private JButton editRelationshipButton;

    // Field Management Buttons
    private JButton addFieldButton;
    private JButton deleteFieldButton;
    private JButton editFieldButton;

    // Method Management Buttons
    private JButton addMethodButton;
    private JButton deleteMethodButton;
    private JButton editMethodButton;

    // Parameter Management Buttosn
    private JButton addParameterButton;
    private JButton deleteParameterButton;
    private JButton changeParameterButton;

    // Undo/Redo buttons
    private JButton undoButton;
    private JButton redoButton;

    //Save/Load buttons
    private JButton saveButton;
    private JButton loadButton;

    //Help and Exit Buttons
    private JButton exitButton;
    private JButton helpButton;

    private CustomDrawingPanel drawingPanel;

    public GUIView() {
        setTitle("UML CLASS DIAGRAM");
        setExtendedState(JFrame.MAXIMIZED_BOTH);//Open in full-screen mode
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initToolPanel();
        initDrawingPanel();
    }

    private void initToolPanel() {

        //TOOL PANEL
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));

        addClassButton = new JButton("Add Class");
        renameClassButton = new JButton("Rename Class");
        deleteClassButton = new JButton("Delete Class");
        addRelationshipButton = new JToggleButton("Add Relationship");
        deleteRelationshipButton = new JButton("Delete Relationship");
        editRelationshipButton = new JButton("Edit Relationship");

        // Field Controls
        addFieldButton = new JButton("Add Field");
        deleteFieldButton = new JButton("Delete Field");
        editFieldButton = new JButton("Edit Field");

        // Method Controls
        addMethodButton = new JButton("Add Method");
        deleteMethodButton = new JButton("Delete Method");
        editMethodButton = new JButton("Edit Method");

        // Parameter Controls
        addParameterButton = new JButton("Add Parameter");
        deleteParameterButton = new JButton("Delete Parameter");
        changeParameterButton = new JButton("Change Parameter");

        // Undo/Redo Controls
        undoButton = new JButton("Undo");
        redoButton = new JButton("Redo");

        //Save/Load Controls 
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");

        //Help/Exit Controls
        exitButton = new JButton("Exit");
        helpButton = new JButton("Help");

        toolPanel.add(addClassButton);
        toolPanel.add(renameClassButton);
        toolPanel.add(deleteClassButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(addRelationshipButton);
        toolPanel.add(editRelationshipButton);
        toolPanel.add(deleteRelationshipButton);
        toolPanel.add(new JSeparator()); // UI Divider
        toolPanel.add(addFieldButton);
        toolPanel.add(deleteFieldButton);
        toolPanel.add(editFieldButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(addMethodButton);
        toolPanel.add(deleteMethodButton);
        toolPanel.add(editMethodButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(addParameterButton);
        toolPanel.add(deleteParameterButton);
        toolPanel.add(changeParameterButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(undoButton);
        toolPanel.add(redoButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(saveButton);
        toolPanel.add(loadButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(exitButton);
        toolPanel.add(helpButton);

        add(toolPanel, BorderLayout.WEST);
    }

    /**
     * Creates and returns a help panel with multiple cards explaining how the
     * various controls in the tool panel work.
     */
    public JPanel helpPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        String[] helpSteps = {
            "<html><h2>Overview</h2><p>This UML Diagram Editor lets you create, edit, and manage class diagrams. Use the tool panel on the left to add classes, define relationships, and manage class members such as fields, methods, and parameters.</p></html>",
            "<html><h2>Class Controls</h2><p><b>Add Class:</b> Create a new class box on the canvas.<br><b>Rename Class:</b> Modify the name of a selected class.<br><b>Delete Class:</b> Remove a class from your diagram.</p></html>",
            "<html><h2>Relationship Controls</h2><p><b>Add Relationship:</b> Enable relationship mode to connect classes.<br><b>Edit Relationship:</b> Change the source, destination, or type of an existing relationship.<br><b>Delete Relationship:</b> Remove a selected relationship from the diagram.</p></html>",
            "<html><h2>Field Controls</h2><p><b>Add Field:</b> Insert a new attribute into the selected class.<br><b>Delete Field:</b> Remove an attribute from the class.<br><b>Rename Field:</b> Change the name of an attribute.</p></html>",
            "<html><h2>Method Controls</h2><p><b>Add Method:</b> Add a new operation to the selected class.<br><b>Delete Method:</b> Remove an existing method.<br><b>Rename Method:</b> Modify the name of a method.</p></html>",
            "<html><h2>Parameter Controls</h2><p><b>Add Parameter:</b> Add a parameter to a method.<br><b>Delete Parameter:</b> Remove a method parameter.<br><b>Change Parameter:</b> Edit the details of a parameter.</p></html>",
            "<html><h2>File & Other Controls</h2><p><b>Save:</b> Save your current diagram to a file.<br><b>Load:</b> Open a saved diagram.<br><b>Help:</b> Open this help guide.<br><b>Exit:</b> Close the application (with an option to save changes).</p></html>"
        };

        // Create a card for each help step.
        for (int i = 0; i < helpSteps.length; i++) {
            JPanel stepPanel = new JPanel(new BorderLayout());
            stepPanel.setSize(740, 740);;
            // Center the help text in each card.
            JLabel label = new JLabel(helpSteps[i]);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            stepPanel.add(label, BorderLayout.CENTER);
            cardPanel.add(stepPanel, String.valueOf(i));
        }

        AtomicInteger currentIndex = new AtomicInteger(0);

        // Navigation buttons for previous and next steps.
        JButton prevButton = new JButton("<- Previous");
        JButton nextButton = new JButton("Next ->");

        prevButton.addActionListener(e -> {
            if (currentIndex.get() > 0) {
                currentIndex.decrementAndGet();
                cardLayout.show(cardPanel, String.valueOf(currentIndex.get()));
            }
        });

        nextButton.addActionListener(e -> {
            if (currentIndex.get() < helpSteps.length - 1) {
                currentIndex.incrementAndGet();
                cardLayout.show(cardPanel, String.valueOf(currentIndex.get()));
            }
        });

        JPanel navPanel = new JPanel();
        navPanel.add(prevButton);
        navPanel.add(nextButton);

        panel.add(cardPanel, BorderLayout.CENTER);
        panel.add(navPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void initDrawingPanel() {
        drawingPanel = new CustomDrawingPanel();
        drawingPanel.setLayout(null);
        drawingPanel.setBackground(new Color(240, 240, 240));
        add(drawingPanel, BorderLayout.CENTER);
    }

    public JButton getUndoButton(){return undoButton;}
    public JButton getRedoButton(){return redoButton;}

    public JButton getAddClassButton() {
        return addClassButton;
    }

    public JButton getRenameClassButton() {
        return renameClassButton;
    }

    public JButton getDeleteClassButton() {
        return deleteClassButton;
    }

    public JToggleButton getAddRelationshipButton() {
        return addRelationshipButton;
    }

    public JButton getDeleteRelationshipButton() {
        return deleteRelationshipButton;
    }

    public JButton getEditRelationshipButton() {
        return editRelationshipButton;
    }

    public JButton getAddFieldButton() {
        return addFieldButton;
    }

    public JButton getDeleteFieldButton() {
        return deleteFieldButton;
    }

    public JButton getEditFieldButton() {
        return editFieldButton;
    }

    public JButton getAddMethodButton() {
        return addMethodButton;
    }

    public JButton getDeleteMethodButton() {
        return deleteMethodButton;
    }

    public JButton getEditMethodButton() {
        return editMethodButton;
    }

    public JButton getAddParamButton() {
        return addParameterButton;
    }

    public JButton getDeleteParamButton() {
        return deleteParameterButton;
    }

    public JButton getChangeParamButton() {
        return changeParameterButton;
    }

    public JButton getSaveButton() {
        return saveButton;
    }

    public JButton getLoadButton() {
        return loadButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    public CustomDrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    /**
     * Function to display any error messages that occur
     *
     * @param message | The message to be displayed
     */
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showGUI() {
        setVisible(true);
    }
}
