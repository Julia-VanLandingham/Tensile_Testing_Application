package view;

import controller.Calculations;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.SpringLayout;
import controller.Calculations.Units;

/**
 * Creates a pop-up frame which displays settings options
 */
public class SettingsView extends JFrame{

    private static final String UNIT_SELECTION = "Unit System: ";
    private final String[] MEASUREMENTS = {"English", "Metric"};
    private JComboBox<String> defaultUnitSelectionBox;
    private Units currentUnitSystem;
    private static final String GAUGE_LENGTH = "Gauge Length: ";
    private JTextField gaugeLengthField;

    private JButton saveButton = new JButton("Save");

    public SettingsView (Scanner userInput) {
        setTitle("Settings");
        setResizable(false);

        add(createNorthPanel(userInput), BorderLayout.NORTH);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(false);
        pack();
        setLocationRelativeTo(null);
    }

    /*
     * Creates fields that allow the user to determine the settings
     * that will be used throughout the test. Those settings include sample rate, units, and gauge length
     * Takes a scanner to aid in persisting the settings throughout
     */
    private JPanel createNorthPanel(Scanner userInput){
        JPanel northPanel = new JPanel(new SpringLayout());
        JLabel unitsSelectionLabel = new JLabel(UNIT_SELECTION,JLabel.TRAILING);
        JLabel gaugeLengthLabel = new JLabel(GAUGE_LENGTH,JLabel.TRAILING);
        boolean readSucceeded = false;
        if(userInput != null)  { //if user input values on settings window, they remain through closing and reopening
            try{
                String selectedUnitType = userInput.next();
                if (selectedUnitType.equals("English")){
                    currentUnitSystem = Units.ENGLISH;
                }else{
                    currentUnitSystem = Units.METRIC;
                }

                double gaugeLength = userInput.nextDouble();
                gaugeLengthField = new JTextField(String.valueOf(gaugeLength), 10);
                defaultUnitSelectionBox = new JComboBox<>(MEASUREMENTS);
                defaultUnitSelectionBox.setFocusable(false);
                defaultUnitSelectionBox.setSelectedItem(selectedUnitType);
                readSucceeded = true;
            }
            catch( NoSuchElementException | IllegalStateException e) {
            }
        }

        if(!readSucceeded){ //if no values input default values show
            gaugeLengthField = new JTextField("0.5");
            defaultUnitSelectionBox = new JComboBox<>(MEASUREMENTS);
            currentUnitSystem = Units.ENGLISH;
            defaultUnitSelectionBox.setFocusable(false);
        }

        northPanel.add(unitsSelectionLabel);
        unitsSelectionLabel.setLabelFor(defaultUnitSelectionBox);
        northPanel.add(defaultUnitSelectionBox);

        northPanel.add(gaugeLengthLabel);

        gaugeLengthLabel.setLabelFor(gaugeLengthField);
        northPanel.add(gaugeLengthField);
        SpringUtilities.makeCompactGrid(northPanel,2,2,6,6,6,6);

        return northPanel;
    }

    /*
     * Creates a save button that will allow a user to
     * save the settings that were decided on and will persist throughout the test
     */
    private JPanel createSouthPanel(){
        JPanel southPanel = new JPanel();
        southPanel.add(saveButton);
        return southPanel;
    }

    public ArrayList<String> getInput(){ //for getting all the inputs from the fields before closing
        return null;
    }

    public JButton getSaveButton(){ return saveButton;  }

    public JComboBox<String> getDefaultUnitSelectionBox(){return defaultUnitSelectionBox; }

    public JTextField getDefaultGaugeLengthField(){ return gaugeLengthField; }

    public double getDefaultGaugeLength(){
        return Double.parseDouble(gaugeLengthField.getText().trim());
    }

    public String getDefaultUnits(){
        return (String) defaultUnitSelectionBox.getSelectedItem();
    }

    public Units getCurrentUnitSystem(){ return currentUnitSystem; }

    public void setCurrentUnitSystem(Units currentUnitSystem){ this.currentUnitSystem = currentUnitSystem; }
}
