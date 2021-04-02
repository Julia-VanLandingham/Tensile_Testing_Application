package controller;

import view.SettingsView;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import controller.Calculations.Units;

/**
 * Sets up and controls the functions of the SettingsView
 */
public class SettingsController {
    private static final String CONFIG_FILE = "settings.cfg"; //file settings are stored in

    private final SettingsView settingsWindow;
    private final InputController inputController;
    private final MainController mainController;

    public SettingsController(InputController inputController, MainController mainController){
        this.inputController = inputController;
        this.mainController = mainController;
        Scanner input = null;

        try{
           input = new Scanner(new File(CONFIG_FILE));
        } catch (FileNotFoundException e) {
        }

        settingsWindow = new SettingsView(input);
        initializeInputWindowValues();

        settingsWindow.getSaveButton().addActionListener(e -> settingsWindow.setVisible(false));
        if(input != null){
            input.close();
        }

        //stores settings in a file and updates the values in the input window
        settingsWindow.getSaveButton().addActionListener(e -> {
            try {
                double value = settingsWindow.getDefaultGaugeLength();
                updateUnitsSystem();

                PrintWriter out = new PrintWriter(new FileOutputStream(CONFIG_FILE));
                out.println(settingsWindow.getDefaultUnitSelectionBox().getSelectedItem());
                out.println(value);
                out.close();
            } catch (FileNotFoundException exception) {
            }
            catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(settingsWindow,"Default Gauge Length is not a properly formatted number."," Bad Gauge Length",JOptionPane.ERROR_MESSAGE);
            }
        });

        settingsWindow.getDefaultUnitSelectionBox().addActionListener(e -> onUnitSystemChanged());
    }

    /*
     * Update and convert gauge length and all labels appropriately
     */
    private void onUnitSystemChanged(){
        //convert the values to the correct unit system
        double convertedValue;
        if (settingsWindow.getDefaultUnitSelectionBox().getSelectedItem().equals("English")) {
            convertedValue = Calculations.convertLength(settingsWindow.getCurrentUnitSystem(), Units.ENGLISH, settingsWindow.getDefaultGaugeLength());
            settingsWindow.setCurrentUnitSystem(Units.ENGLISH);

            //update the graph labels
            mainController.getMainWindow().getChart().getXYPlot().getDomainAxis().setLabel("Strain (in/in)");
            mainController.getMainWindow().getChart().getXYPlot().getRangeAxis().setLabel("Stress (KSI)");

            //update the units displayed on input fields
            settingsWindow.getGaugeLengthLabel().setText("Gauge Length (in): ");
        }else{
            convertedValue = Calculations.convertLength(settingsWindow.getCurrentUnitSystem(), Units.METRIC, settingsWindow.getDefaultGaugeLength());
            settingsWindow.setCurrentUnitSystem(Units.METRIC);

            //update the graph labels
            mainController.getMainWindow().getChart().getXYPlot().getDomainAxis().setLabel("Strain (mm/mm)");
            mainController.getMainWindow().getChart().getXYPlot().getRangeAxis().setLabel("Stress (MPa)");

            //update the units displayed on input fields
            settingsWindow.getGaugeLengthLabel().setText("Gauge Length (mm): ");
        }

        settingsWindow.getDefaultGaugeLengthField().setText(String.format("%.10f", convertedValue));
    }

    /*
     * Update the unit system and convert the gauge length appropriately
     */
    protected void updateUnitsSystem(){
        double value = settingsWindow.getDefaultGaugeLength();
        inputController.getInputWindow().getGaugeLengthInputField().setText(String.valueOf(value)); //settings gauge length to input gauge length
        if (settingsWindow.getDefaultUnits().equals("English")){
            inputController.getInputWindow().setCurrentUnitSystem(Units.ENGLISH);
        }else{
            inputController.getInputWindow().setCurrentUnitSystem(Units.METRIC);
        }
        inputController.getInputWindow().getUnitSelectionBox().setSelectedItem(settingsWindow.getDefaultUnits());
    }

    /*
     * Initialize all the values and labels of the input window from the saved settings
     */
    private void initializeInputWindowValues(){
        //do this the first time to populate all the values in the input window from the saved settings
        try {
            //we have to set this dummy value first so we can set the selected item of the combo box
            //the real value is set below
            inputController.getInputWindow().getGaugeLengthInputField().setText(String.valueOf(1.0));

            if (settingsWindow.getDefaultUnits().equals("English")){
                inputController.getInputWindow().setCurrentUnitSystem(Units.ENGLISH);
                mainController.getMainWindow().getChart().getXYPlot().getDomainAxis().setLabel("Strain (in/in)");
                mainController.getMainWindow().getChart().getXYPlot().getRangeAxis().setLabel("Stress (KSI)");
            }else{
                inputController.getInputWindow().setCurrentUnitSystem(Units.METRIC);
                mainController.getMainWindow().getChart().getXYPlot().getDomainAxis().setLabel("Strain (mm/mm)");
                mainController.getMainWindow().getChart().getXYPlot().getRangeAxis().setLabel("Stress (MPa)");
            }
            inputController.getInputWindow().getUnitSelectionBox().setSelectedItem(settingsWindow.getDefaultUnits());

            //set the input gauge length to be the default gauge length
            double value = settingsWindow.getDefaultGaugeLength();
            inputController.getInputWindow().getGaugeLengthInputField().setText(String.valueOf(value));
        }
        catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(settingsWindow,"Default Gauge Length is not a properly formatted number."," Bad Gauge Length",JOptionPane.ERROR_MESSAGE);
        }

        /*
         * Note that we must do this here because the input controller must be made before
         * the settings in order to pass it in, but the settings must update the gauge length
         * before we can pull the input values
         */
        inputController.pullInputValues(); //Initialize the stored values
    }

    //getters
    public SettingsView getSettingsWindow(){
        return settingsWindow;
    }
}
