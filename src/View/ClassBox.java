package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ClassBox extends JPanel {
    private JTextField classNameField;
    private DefaultListModel<String> fieldsListModel;
    private DefaultListModel<String> methodsListModel;
    private JList<String> fieldsList;
    private JList<String> methodsList;
    
    public ClassBox(String className) {
        // Use a border layout to divide the panel.
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // --- Class Name Compartment ---
        classNameField = new JTextField(className);
        classNameField.setHorizontalAlignment(JTextField.CENTER);
        classNameField.setEditable(true);
        // Add a bottom border to separate the name from the next compartment.
        classNameField.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        add(classNameField, BorderLayout.NORTH);
        
        // --- Fields and Methods Compartments ---
        JPanel compartmentsPanel = new JPanel(new GridLayout(2, 1, 0, 0));
        
        // Fields compartment:
        fieldsListModel = new DefaultListModel<>();
        fieldsList = new JList<>(fieldsListModel);
        JScrollPane fieldsScroll = new JScrollPane(fieldsList);
        fieldsScroll.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        compartmentsPanel.add(fieldsScroll);
        
        // Methods compartment:
        methodsListModel = new DefaultListModel<>();
        methodsList = new JList<>(methodsListModel);
        JScrollPane methodsScroll = new JScrollPane(methodsList);
        compartmentsPanel.add(methodsScroll);
        
        add(compartmentsPanel, BorderLayout.CENTER);
        
        // Set a preferred size for display purposes.
        setPreferredSize(new Dimension(150, 100));
    }
    
    public void addField(String field) {
        fieldsListModel.addElement(field);
    }
    
    public void addMethod(String method) {
        methodsListModel.addElement(method);
    }
    
    public String getClassName() {
        return classNameField.getText();
    }
    
}
