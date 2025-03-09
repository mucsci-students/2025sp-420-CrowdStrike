package org.View;

import javax.swing.*;
import java.awt.*;
import org.View.CustomDrawingPanel;

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

        add(toolPanel, BorderLayout.WEST);
    }

    private void initDrawingPanel() {
        drawingPanel = new CustomDrawingPanel();
        drawingPanel.setLayout(null);
        drawingPanel.setBackground(Color.WHITE);
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

    public CustomDrawingPanel getDrawingPanel() {
        return drawingPanel;
    }



    public void showGUI() {
        setVisible(true);
    }
}