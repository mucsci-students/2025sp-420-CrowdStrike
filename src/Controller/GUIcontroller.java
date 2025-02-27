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

    // State Variables
    private boolean addClassMode = false;
    private boolean addRelationshipMode = false;
    private JComponent selectedAnchor1 = null;
    private JComponent selectedAnchor2 = null;
    private ClassBox selectedClassBox = null;  // Track the currently selected class box

    public GUIController(GUIView view) {
        this.view = view;
        initController();
    }

    // ==================== INITIALIZATION ==================== //
    private void initController() {
        initMouseListeners();
        initButtonActions();
    }

    // ==================== EVENT HANDLERS ==================== //
    private void initMouseListeners() {
        // Handle class creation only when "Add Class" is toggled ON
        view.getDrawingPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (addClassMode) {
                    addClass(e.getX(), e.getY());
                }
            }
        });
    }

    private void initButtonActions() {
        // TRy Radio Button

        view.getAddClassButton().addActionListener(e -> {
            addClassMode = view.getAddClassButton().isSelected();// gets view to show that the button has been selected 
        });

        view.getAddRelationshipButton().addActionListener(e -> {
            addRelationshipMode = view.getAddRelationshipButton().isSelected();
            if (addRelationshipMode) {
                JOptionPane.showMessageDialog(view, "Click on two anchors to create a relationship.");
            }
        });
    }

    // ==================== CLASS BOX HANDLING ==================== //
    private void addClass(int x, int y) {
        int boxWidth = 150;
        int boxHeight = 100;
        int margin = 10;
        Rectangle newRect = new Rectangle(x, y, boxWidth, boxHeight);

        for (JPanel box : classBoxes) {
            Rectangle existingRect = box.getBounds();
            Rectangle expandedRect = new Rectangle(
                    existingRect.x - margin,
                    existingRect.y - margin,
                    existingRect.width + 2 * margin,
                    existingRect.height + 2 * margin
            );
            if (newRect.intersects(expandedRect)) {
                JOptionPane.showMessageDialog(view.getDrawingPanel(),
                        "Cannot place box here: too close to an existing box.");
                return;
            }

        ClassBox classBox = new ClassBox("ClassName");
        classBox.setBounds(x, y, boxWidth, boxHeight);

        addAnchorsToClass(classBox);

        // Add Mouse Listener to select the class box
        classBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectClassBox(classBox);
                e.consume();
            }
        });

        view.getDrawingPanel().add(classBox);
        classBoxes.add(classBox);
        view.getDrawingPanel().revalidate();
        view.getDrawingPanel().repaint();
    }

    // ==================== CLASS SELECTION HANDLING ==================== //
    private void selectClassBox(ClassBox classBox) {
        // Remove highlight from previous selection
        if (selectedClassBox != null) {
            selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        // Highlight the selected box
        selectedClassBox = classBox;
        selectedClassBox.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        view.getDrawingPanel().repaint();
    }

    // ==================== RELATIONSHIP HANDLING ==================== //
    private void addAnchorsToClass(JPanel classPanel) {
        int anchorSize = 10;

        JPanel topAnchor = createAnchor();
        topAnchor.setBounds((classPanel.getWidth() - anchorSize) / 2, 0, anchorSize, anchorSize);
        classPanel.add(topAnchor);

        JPanel bottomAnchor = createAnchor();
        bottomAnchor.setBounds((classPanel.getWidth() - anchorSize) / 2, classPanel.getHeight() - anchorSize, anchorSize, anchorSize);
        classPanel.add(bottomAnchor);

        JPanel leftAnchor = createAnchor();
        leftAnchor.setBounds(0, (classPanel.getHeight() - anchorSize) / 2, anchorSize, anchorSize);
        classPanel.add(leftAnchor);

        JPanel rightAnchor = createAnchor();
        rightAnchor.setBounds(classPanel.getWidth() - anchorSize, (classPanel.getHeight() - anchorSize) / 2, anchorSize, anchorSize);
        classPanel.add(rightAnchor);
    }

    private JPanel createAnchor() {
        JPanel anchor = new JPanel();
        anchor.setBackground(Color.RED);
        anchor.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        anchor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                anchorClicked((JComponent) e.getSource());
                e.consume();
            }
        });
        return anchor;
    }

    private void anchorClicked(JComponent anchor) {
        if (!addRelationshipMode) {
            return;
        }
        if (selectedAnchor1 == null) {
            selectedAnchor1 = anchor;
        } else {
            selectedAnchor2 = anchor;
            createRelationship(selectedAnchor1, selectedAnchor2);
            selectedAnchor1 = null; // Reset for next relationship
        }
    }

    private void createRelationship(JComponent anchor1, JComponent anchor2) {
        Point p1 = SwingUtilities.convertPoint(anchor1, anchor1.getWidth() / 2, anchor1.getHeight() / 2, view.getDrawingPanel());
        Point p2 = SwingUtilities.convertPoint(anchor2, anchor2.getWidth() / 2, anchor2.getHeight() / 2, view.getDrawingPanel());

        view.getDrawingPanel().addRelationship(p1, p2, "Association");
        resetArrowDrawing();
        view.getAddRelationshipButton().setSelected(false);
    }

    private void resetArrowDrawing() {
        selectedAnchor1 = null;
        selectedAnchor2 = null;
        addRelationshipMode = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUIView view = new GUIView();
            new GUIController(view);
            view.showGUI();
        });
    }
}