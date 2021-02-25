package controller;

import view.UserInputWindow;

/**
 * Sets up and controls the functions of the UserInputWindow
 */
public class InputController {

    private UserInputWindow inputWindow;
    private double width;
    private double depth;
    private double diameter;

    public InputController(){
        inputWindow = new UserInputWindow();

        inputWindow.getCancelButton().addActionListener(e -> inputWindow.setVisible(false));
        inputWindow.getOkButton().addActionListener(e -> inputWindow.setVisible(false)); //Pull input values and do stuff with them

        inputWindow.getCircularButton().addActionListener(e -> {
            inputWindow.getCircularInputPanel().setVisible(true);
            inputWindow.getRectangularInputPanel().setVisible(false);
        });

        inputWindow.getRectangularButton().addActionListener(e -> {
            inputWindow.getCircularInputPanel().setVisible(false);
            inputWindow.getRectangularInputPanel().setVisible(true);
        });
    }

    /**
     * Determines if the cross section rectangular radio button is selected,
     * if not the circular is selected
     * @return if cross section is rectangular
     */
    public boolean isRectangularSelected() {
        return inputWindow.getRectangularButton().isSelected();
    }

    public UserInputWindow getInputWindow(){
        return inputWindow;
    }
}
