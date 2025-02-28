package view;

import javax.swing.*;
import java.awt.*;

public class GUIView extends JFrame {
    private JButton addClassButton;
    private JButton renameClassButton;
    private JButton deleteClassButton;
    private JToggleButton addRelationshipButton;
    private JButton deleteRelationshipButton;
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
        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.Y_AXIS));

        addClassButton = new JButton("Add Class");
        renameClassButton = new JButton("Rename Class");
        deleteClassButton = new JButton("Delete Class");
        addRelationshipButton = new JToggleButton("Add Relationship");
        deleteRelationshipButton = new JButton("Delete Relationship");

        toolPanel.add(addClassButton);
        toolPanel.add(renameClassButton);
        toolPanel.add(deleteClassButton);
        toolPanel.add(addRelationshipButton);
        toolPanel.add(deleteRelationshipButton);

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

    public CustomDrawingPanel getDrawingPanel() {
        return drawingPanel;
    }

    public void showGUI() {
        setVisible(true);
    }
}