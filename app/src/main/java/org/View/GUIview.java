package view;

import javax.swing.*;
import java.awt.*;

public class GUIView extends JFrame {
    private JButton addClassButton;
    private JButton renameClassButton;
    private JButton deleteClassButton;

    

    private JToggleButton addRelationshipButton;
    private JButton deleteRelationshipButton;
    

    // Field Management Buttons
    private JButton addFieldButton;
    private JButton deleteFieldButton;
    private JButton renameFieldButton;

    // Method Management Buttons
    private JButton addMethodButton;
    private JButton deleteMethodButton;
    private JButton renameMethodButton;

    private CustomDrawingPanel drawingPanel;

    public GUIView() {
        setTitle("UML Editor");
        setSize(1000, 600);
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

        // Field Controls
        addFieldButton = new JButton("Add Field");
        deleteFieldButton = new JButton("Delete Field");
        renameFieldButton = new JButton("Rename Field");

        // Method Controls
        addMethodButton = new JButton("Add Method");
        deleteMethodButton = new JButton("Delete Method");
        renameMethodButton = new JButton("Rename Method");

        


        toolPanel.add(addClassButton);
        toolPanel.add(renameClassButton);
        toolPanel.add(deleteClassButton);
        toolPanel.add(addRelationshipButton);
        toolPanel.add(deleteRelationshipButton);
        toolPanel.add(new JSeparator()); // UI Divider
        toolPanel.add(addFieldButton);
        toolPanel.add(deleteFieldButton);
        toolPanel.add(renameFieldButton);
        toolPanel.add(new JSeparator());
        toolPanel.add(addMethodButton);
        toolPanel.add(deleteMethodButton);
        toolPanel.add(renameMethodButton);

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

    public JButton getAddFieldButton() { return addFieldButton; }
    public JButton getDeleteFieldButton() { return deleteFieldButton; }
    public JButton getRenameFieldButton() { return renameFieldButton; }
    public JButton getAddMethodButton() { return addMethodButton; }
    public JButton getDeleteMethodButton() { return deleteMethodButton; }
    public JButton getRenameMethodButton() { return renameMethodButton; }

    public CustomDrawingPanel getDrawingPanel() {
        return drawingPanel;
    }



    public void showGUI() {
        setVisible(true);
    }
}