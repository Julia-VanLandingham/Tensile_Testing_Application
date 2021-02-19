package view;

import javax.swing.*;

public class UserInputWindow extends JFrame {

    private JPanel rectangularInputPanel;
    private JTextField widthInputField;
    private JTextField depthInputField;

    public UserInputWindow(){
        setTitle("Measurements Input");

        setupRectangularInputPanel();
        add(rectangularInputPanel);

        pack();
        setVisible(true);
    }

    private void setupRectangularInputPanel(){
        rectangularInputPanel = new JPanel();
        rectangularInputPanel.setLayout(new BoxLayout(rectangularInputPanel, BoxLayout.X_AXIS));

        JLabel widthLabel = new JLabel("Width: ");
        JLabel depthLabel = new JLabel("Depth: ");

        widthInputField = new JTextField();
        depthInputField = new JTextField();

        rectangularInputPanel.add(widthLabel);
        rectangularInputPanel.add(widthInputField);
        rectangularInputPanel.add(Box.createHorizontalGlue());
        rectangularInputPanel.add(Box.createHorizontalStrut(10));
        rectangularInputPanel.add(depthLabel);
        rectangularInputPanel.add(depthInputField);
    }



    private void clear(){


    }
    public static void main(String[] args){
        new UserInputWindow();
    }

}

