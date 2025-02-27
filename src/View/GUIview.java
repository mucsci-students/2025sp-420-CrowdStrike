package view;

import javax.swing.*;
import java.awt.*;

public class GUIView extends JFrame {
    // UI Components
    private JToggleButton addClassButton;
    private JButton renameClassButton;
    private JButton deleteClassButton;
    private JToggleButton addRelationshipButton;  // Changed from arrowToolToggle
    private JButton deleteRelationshipButton;
    private JButton addAttributeButton;
    private JButton deleteAttributeButton;
    private JButton renameAttributeButton;
    private CustomDrawingPanel drawingPanel;

    public GUIView() {
        // Basic Frame Setup
        setTitle("UML Editor");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize UI Components
        initToolPanel(); // Left tool panel
        initDrawingPanel(); // Center drawing area
    }

    // ==================== TOOL PANEL (LEFT SIDE) ==================== //
    private void initToolPanel() {
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));

        // Initialize buttons
        addClassButton = new JToggleButton("Add Class");
        renameClassButton = new JButton("Rename Class");
        deleteClassButton = new JButton("Delete Class");
        addRelationshipButton = new JToggleButton("Add Relationship"); // Updated name
        deleteRelationshipButton = new JButton("Delete Relationship");
        addAttributeButton = new JButton("Add Attribute");
        deleteAttributeButton = new JButton("Delete Attribute");
        renameAttributeButton = new JButton("Rename Attribute");

        // Add components to tool panel
        toolPanel.add(addClassButton);
        toolPanel.add(renameClassButton);
        toolPanel.add(deleteClassButton);
        toolPanel.add(addRelationshipButton); // Updated button reference
        toolPanel.add(deleteRelationshipButton);
        toolPanel.add(addAttributeButton);
        toolPanel.add(deleteAttributeButton);
        toolPanel.add(renameAttributeButton);

        add(toolPanel, BorderLayout.WEST);
    }

    // ==================== DRAWING PANEL (CENTER) ==================== //
    private void initDrawingPanel() {
        drawingPanel = new CustomDrawingPanel();
        drawingPanel.setLayout(null); // Absolute positioning for class boxes
        drawingPanel.setBackground(Color.WHITE);
        add(drawingPanel, BorderLayout.CENTER);
    }

    // ==================== GETTERS (For Controller Access) ==================== //
    public JToggleButton getAddClassButton() {
        return addClassButton;
    }

    public JButton getRenameClassButton() {
        return renameClassButton;
    }

    public JButton getDeleteClassButton() {
        return deleteClassButton;
    }

    public JToggleButton getAddRelationshipButton() { // Updated getter
        return addRelationshipButton;
    }

    public JButton getDeleteRelationshipButton() {
        return deleteRelationshipButton;
    }

    public JButton getAddAttributeButton() {
        return addAttributeButton;
    }

    public JButton getDeleteAttributeButton() {
        return deleteAttributeButton;
    }

    public JButton getRenameAttributeButton() {
        return renameAttributeButton;
    }

    public CustomDrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    // Display GUI
    public void showGUI() {
        setVisible(true);
    }
}