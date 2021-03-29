package controller;

import view.UserInputWindow;
import controller.Calculations.Units;

/**
 * Sets up and controls the functions of the UserInputWindow
 */
public class InputController {

    private final UserInputWindow inputWindow;
    private final MainController mainController;
    private double width;
    private double depth;
    private double diameter;

    public InputController(MainController mainController){
        this.mainController = mainController;
        width = 0.0;
        depth = 0.0;
        diameter = 0.0;

        inputWindow = new UserInputWindow();

        inputWindow.getCancelButton().addActionListener(e -> inputWindow.setVisible(false));
        inputWindow.getOkButton().addActionListener(e -> inputWindow.setVisible(false)); //Pull input values and do stuff with them

        inputWindow.getCircularButton().addActionListener(e -> {
            inputWindow.getCircularInputPanel().setVisible(true);
            inputWindow.getRectangularInputPanel().setVisible(false);
            inputWindow.getDiameterInputField().setText("0.0");
        });

        inputWindow.getRectangularButton().addActionListener(e -> {
            inputWindow.getCircularInputPanel().setVisible(false);
            inputWindow.getRectangularInputPanel().setVisible(true);
            inputWindow.getDepthInputField().setText("0.0");
            inputWindow.getWidthInputField().setText("0.0");
        });

        inputWindow.getUnitSelectionBox().addActionListener(e -> {
           onUnitSystemChange();
        });
    }

    /*
     * Resets all the text fields back to their initial values
     * Resets all stored input values to 0.0
     */
    protected void clear(){
        inputWindow.getWidthInputField().setText("0.0");
        inputWindow.getDepthInputField().setText("0.0");
        inputWindow.getDiameterInputField().setText("0.0");

        width = 0.0;
        depth = 0.0;
        diameter = 0.0;
    }

    /*
     * Converts the gauge length to the appropriate units and updates all labels
     */
    protected void onUnitSystemChange(){
        //convert the values to the correct unit system
        double convertedValue;
        if (inputWindow.getUnitSelectionBox().getSelectedItem().equals("English")) {
            convertedValue = Calculations.convertLength(inputWindow.getCurrentUnitSystem(), Units.ENGLISH, getGaugeLengthInput());
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
            convertedValue = Calculations.convertLength(inputWindow.getCurrentUnitSystem(), Units.METRIC, getGaugeLengthInput());
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

    /**
     * Returns if the user has input values for the appropriate cross section type
     * @return if values have been input
     */
    public boolean haveInputs(){
        if (isRectangularSelected()){
            return getWidthInput() != 0.0 && getDepthInput() != 0.0;
        }else{
            return getDiameterInput() != 0.0;
        }
    }

    /**
     * Checks if the values input from the user are the same as the previous time the data was run
     * @return if the input values are the same
     */
    public boolean areInputsFromPreviousRun(){
        if(isRectangularSelected()){
            return width == getWidthInput() && depth == getDepthInput();
        }else{
            return diameter == getDiameterInput();
        }
    }

    /**
     * Stores the input values from the input window
     */
    public void pullInputValues(){
        width = getWidthInput();
        depth = getDepthInput();
        diameter = getDiameterInput();
    }

    /*
     * Gets the width that was input from the user
     */
    private double getWidthInput(){
        return Double.parseDouble(inputWindow.getWidthInputField().getText().trim());
    }

    /*
     * Gets the depth that was input from the user
     */
    private double getDepthInput(){
        return Double.parseDouble(inputWindow.getDepthInputField().getText().trim());
    }

    /*
     * Gets the diameter that was input from the user
     */
    private double getDiameterInput(){
        return Double.parseDouble(inputWindow.getDiameterInputField().getText().trim());
    }

    /*
     * Gets the gauge length that was input from the user (more likely this is still just the default value)
     */
    private double getGaugeLengthInput(){
        return Double.parseDouble(inputWindow.getGaugeLengthInputField().getText().trim());
    }

    //getters and setters

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public UserInputWindow getInputWindow(){
        return inputWindow;
    }
}
