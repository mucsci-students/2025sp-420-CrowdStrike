package view;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ClassBox extends JPanel {
    private JTextField classNameField;

    public ClassBox(String className) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new LineBorder(Color.BLACK, 2));

        classNameField = new JTextField(className);
        classNameField.setHorizontalAlignment(JTextField.CENTER);
        classNameField.setEditable(false);

        add(classNameField, BorderLayout.NORTH);
        setPreferredSize(new Dimension(150, 100));
    }

    public String getClassName() {
        return classNameField.getText();
    }

    public void setClassName(String newName) {
        classNameField.setText(newName);
    }

    public Point getCenter() {
        return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }
}