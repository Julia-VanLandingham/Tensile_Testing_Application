package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import javax.swing.SpringLayout;

/**
 * Creates a pop-up frame which displays settings options
 */
public class SettingsView extends JFrame{

    private static final String SAMPLE_RATE = "Sample Rate: ";
    private static final int DEFAULT_SAMPLE_RATE = 100;
    private JSpinner sampleRateSelection;

    private static final String UNIT_SELECTION = "Units: ";
    private final String [] MEASUREMENTS = {"English", "Metric"};
    private JComboBox<String> unitSelection;

    private static final String GAUGE_LENGTH = "Gauge Length: ";
    private JTextField gaugeLengthField;

    private JButton saveButton = new JButton("Save");

    public SettingsView (Scanner userInput) {
        setTitle("Settings");
        setResizable(false);

        add(createNorthPanel(userInput), BorderLayout.NORTH);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
        JLabel sampleRateLabel = new JLabel(SAMPLE_RATE,JLabel.TRAILING);
        JLabel unitsSelectionLabel = new JLabel(UNIT_SELECTION,JLabel.TRAILING);
        JLabel gaugeLengthLabel = new JLabel(GAUGE_LENGTH,JLabel.TRAILING);
        boolean readSucceeded = false;
        if(userInput != null)  { //if user input values on settings window, they remain through closing and reopening
            try{
                int sampleRate = userInput.nextInt();
                String selectedUnitType = userInput.next();
                double gaugeLength = userInput.nextDouble();
                sampleRateSelection = new JSpinner(new SpinnerNumberModel(sampleRate,DEFAULT_SAMPLE_RATE,(DEFAULT_SAMPLE_RATE * 100),1));
                gaugeLengthField = new JTextField(gaugeLength+"");
                unitSelection = new JComboBox<>(MEASUREMENTS);
                unitSelection.setSelectedItem(selectedUnitType);
                readSucceeded = true;
            }
            catch( NoSuchElementException | IllegalStateException e) {
            }
        }

        if(!readSucceeded){ //if no values inputted default values show
            sampleRateSelection = new JSpinner(new SpinnerNumberModel(DEFAULT_SAMPLE_RATE,DEFAULT_SAMPLE_RATE,(DEFAULT_SAMPLE_RATE * 100),1));
            gaugeLengthField = new JTextField("0.5");
            unitSelection = new JComboBox<>(MEASUREMENTS);
        }

        northPanel.add(sampleRateLabel);
        sampleRateLabel.setLabelFor(sampleRateSelection);
        northPanel.add(sampleRateSelection);

        northPanel.add(unitsSelectionLabel);
        unitsSelectionLabel.setLabelFor(unitSelection);
        northPanel.add(unitSelection);

        northPanel.add(gaugeLengthLabel);

        gaugeLengthLabel.setLabelFor(gaugeLengthField);
        northPanel.add(gaugeLengthField);
        SpringUtilities.makeCompactGrid(northPanel,3,2,6,6,6,6);

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

    private ArrayList<String> getDefaults(){ //gets the defaults from the class that handles persistent settings
        return null;
    }

    public JButton getSaveButton(){ return saveButton;  }

    public JSpinner getSampleRateSelection(){return sampleRateSelection; }

    public JComboBox<String> getUnitSelection(){return unitSelection; }

    public double getDefaultGaugeLength(){
        return Double.parseDouble(gaugeLengthField.getText().trim());
    }

}
