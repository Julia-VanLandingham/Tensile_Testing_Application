package controller;

import view.UserInputWindow;
import controller.Calculations.Units;

/**
 * Sets up and controls the functions of the UserInputWindow
 */
public class InputController {

    private UserInputWindow inputWindow;
    private MainController mainController;
    private double width;
    private double depth;
    private double diameter;

    public InputController(MainController mainController){
        this.mainController = mainController;
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

        inputWindow.getUnitSelectionBox().addActionListener(e -> {
           onUnitSystemChange();

        });
    }

    /*
     * Resets all the text fields back to their initial values (nothing or default values)
     */
    public void clear(){
        inputWindow.getWidthInputField().setText("");
        inputWindow.getDepthInputField().setText("");
        inputWindow.getDiameterInputField().setText("");
    }

    public void onUnitSystemChange(){
        //convert the values to the correct unit system
        double convertedValue;
        if (inputWindow.getUnitSelectionBox().getSelectedItem().equals("English")) {
            convertedValue = Calculations.convertLength(inputWindow.getCurrentUnitSystem(), Units.ENGLISH, inputWindow.getGaugeLengthInput());
            inputWindow.setCurrentUnitSystem(Units.ENGLISH);

            //update the graph labels
            mainController.getMainWindow().getChart().getXYPlot().getDomainAxis().setLabel("Strain (in/in)");
            mainController.getMainWindow().getChart().getXYPlot().getRangeAxis().setLabel("Stress (KSI)");

            //update the units displayed on input fields
            inputWindow.getGaugeLengthLabel().setText("Gauge Length (in): ");
            inputWindow.getDepthLabel().setText("Depth (in): ");
            inputWindow.getDiameterLabel().setText("Diameter (in): ");
            inputWindow.getWidthLabel().setText("Width (in): ");
        }else {
            convertedValue = Calculations.convertLength(inputWindow.getCurrentUnitSystem(), Units.METRIC, inputWindow.getGaugeLengthInput());
            inputWindow.setCurrentUnitSystem(Units.METRIC);

            //update the graph labels
            mainController.getMainWindow().getChart().getXYPlot().getDomainAxis().setLabel("Strain (mm/mm)");
            mainController.getMainWindow().getChart().getXYPlot().getRangeAxis().setLabel("Stress (MPa)");

            //update the units displayed on input fields
            inputWindow.getGaugeLengthLabel().setText("Gauge Length (mm): ");
            inputWindow.getDepthLabel().setText("Depth (mm): ");
            inputWindow.getDiameterLabel().setText("Diameter (mm): ");
            inputWindow.getWidthLabel().setText("Width (mm): ");
        }
        inputWindow.getGaugeLengthInputField().setText(String.format("%.10f", convertedValue));
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
