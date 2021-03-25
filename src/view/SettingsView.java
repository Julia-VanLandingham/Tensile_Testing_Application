package view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.SpringLayout;
import controller.Calculations.Units;

/**
 * Creates a pop-up frame which displays settings options
 */
public class SettingsView extends JFrame{

    //constants used for formatting
    private static final int VERTICAL_BUFFER = 10;
    private static final int HORIZONTAL_BUFFER = 10;

    private final String[] MEASUREMENTS = {"English", "Metric"};

    private JComboBox<String> defaultUnitSelectionBox;
    private Units currentUnitSystem;
    private JTextField gaugeLengthField;
    private JButton saveButton;
    private JLabel gaugeLengthLabel;

    public SettingsView (Scanner userInput) {
        setTitle("Settings");
        setResizable(false);

        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));

        outerPanel.add(createNorthPanel(userInput));
        outerPanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
        outerPanel.add(createSouthPanel());
        outerPanel.add(Box.createVerticalGlue());

        add(outerPanel);

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
        JLabel unitsSelectionLabel = new JLabel("Unit System:");
        gaugeLengthLabel = new JLabel("");
        boolean readSucceeded = false;
        if(userInput != null)  { //if user input values on settings window, they remain through closing and reopening
            try{
                String selectedUnitType = userInput.next();
                if (selectedUnitType.equals("English")){
                    currentUnitSystem = Units.ENGLISH;
                    //update the units displayed on input fields
                    gaugeLengthLabel.setText("Gauge Length (in):");
                }else{
                    currentUnitSystem = Units.METRIC;
                    //update the units displayed on input fields
                    gaugeLengthLabel.setText("Gauge Length (mm):");
                }

                double gaugeLength = userInput.nextDouble();
                gaugeLengthField = new JTextField(String.valueOf(gaugeLength), 12);
                defaultUnitSelectionBox = new JComboBox<>(MEASUREMENTS);
                defaultUnitSelectionBox.setSelectedItem(selectedUnitType);
                readSucceeded = true;
            }
            catch( NoSuchElementException | IllegalStateException e) {
                //do nothing
            }
        }

        if(!readSucceeded){ //if no values input default values show
            gaugeLengthField = new JTextField("0.5");
            defaultUnitSelectionBox = new JComboBox<>(MEASUREMENTS);
            currentUnitSystem = Units.ENGLISH;
        }

        northPanel.add(unitsSelectionLabel);
        unitsSelectionLabel.setLabelFor(defaultUnitSelectionBox);
        northPanel.add(defaultUnitSelectionBox);

        northPanel.add(gaugeLengthLabel);

        gaugeLengthLabel.setLabelFor(gaugeLengthField);
        northPanel.add(gaugeLengthField);
        SpringUtilities.makeCompactGrid(northPanel,2,2,HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER,VERTICAL_BUFFER);

        return northPanel;
    }

    /*
     * Creates a save button that will allow a user to
     * save the settings that were decided on and will persist throughout the test
     */
    private JPanel createSouthPanel(){
        JPanel southPanel = new JPanel();
        saveButton = new JButton("Save");
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

    public JLabel getGaugeLengthLabel(){
        return gaugeLengthLabel;
    }

    public Units getCurrentUnitSystem(){ return currentUnitSystem; }

    public void setCurrentUnitSystem(Units currentUnitSystem){ this.currentUnitSystem = currentUnitSystem; }
}
