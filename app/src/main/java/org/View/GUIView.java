package org.View;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

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
    private JButton renameFieldButton;

    // Method Management Buttons
    private JButton addMethodButton;
    private JButton deleteMethodButton;
    private JButton renameMethodButton;

    // Parameter Management Buttosn
    private JButton addParameterButton;
    private JButton deleteParameterButton;
    private JButton changeParameterButton;

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
        renameFieldButton = new JButton("Rename Field");

        // Method Controls
        addMethodButton = new JButton("Add Method");
        deleteMethodButton = new JButton("Delete Method");
        renameMethodButton = new JButton("Rename Method");

        // Parameter Controls
        addParameterButton = new JButton("Add Parameter");
        deleteParameterButton = new JButton("Delete Parameter");
        changeParameterButton = new JButton("Change Parameter");

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
        toolPanel.add(renameFieldButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(addMethodButton);
        toolPanel.add(deleteMethodButton);
        toolPanel.add(renameMethodButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(addParameterButton);
        toolPanel.add(deleteParameterButton);
        toolPanel.add(changeParameterButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(saveButton);
        toolPanel.add(loadButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(exitButton);
        toolPanel.add(helpButton);

        add(toolPanel, BorderLayout.WEST);
    }


    private void helpPanel(){
        
        //Help Panel
        JPanel panel = new JPanel(new BorderLayout());
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);

        //Define help steps as HTML string for formatting

        String[] helpSteps = {
            "<html><h2>Step 1:</h2><p>Clicj 'Add Class' to insert Class"
        };

        for(int i =0; i < helpSteps.length; i++){
            JPanel stepPanel = new JPanel();
            stepPanel.add(new JLabel(helpSteps[i]));
            cardPanel.add(stepPanel, String.valueOf(i));
        }

        AtomicInteger currentIndex = new AtomicInteger(0);

        //Navigation buttons for previous and Next
        JButton prevButtons = new JButton("<- Previous");
        JButton nextButtons = new JButton("Next ->");

        prevButtons.addActionListener(e -> {
            if(currentIndex.get() > 0){
                currentIndex.decrementAndGet();
                cardLayout.show(cardPanel, String.valueOf(currentIndex.get()));
            }
        });

        nextButtons.addActionListener(e -> {
            if(currentIndex.get()< helpSteps.length -1){
                currentIndex.incrementAndGet();
                cardLayout.show(cardPanel, String.valueOf(currentIndex));
            }
        });

        JPanel navPanel = new JPanel();
        navPanel.add(prevButtons);
        navPanel.add(nextButtons);

        add(cardPanel, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);

    }


    private void initDrawingPanel() {
        drawingPanel = new CustomDrawingPanel();
        drawingPanel.setLayout(null);
        drawingPanel.setBackground(new Color(240, 240, 240));
        add(drawingPanel, BorderLayout.CENTER);
    }

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
    public JButton getEditRelationshipButton() { return editRelationshipButton; }
    public JButton getAddFieldButton() { return addFieldButton; }
    public JButton getDeleteFieldButton() { return deleteFieldButton; }
    public JButton getRenameFieldButton() { return renameFieldButton; }
    public JButton getAddMethodButton() { return addMethodButton; }
    public JButton getDeleteMethodButton() { return deleteMethodButton; }
    public JButton getRenameMethodButton() { return renameMethodButton; }
    public JButton getAddParamButton() { return addParameterButton; }
    public JButton getDeleteParamButton() { return deleteParameterButton; }
    public JButton getChangeParamButton() { return changeParameterButton; }
    public JButton getSaveButton(){return saveButton;}
    public JButton getLoadButton(){return loadButton;}
    public JButton getExitButton(){return exitButton;}
    public JButton getHelpButton(){return helpButton;}

    public CustomDrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    /**
     * Function to display any error messages that occur
     * @param message   | The message to be displayed
     */
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message,"Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showGUI() {
        setVisible(true);
    }
}