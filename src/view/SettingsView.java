package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.SpringLayout;

/**
 * Creates a pop-up frame which displays settings options
 */
public class SettingsView extends JFrame{

    private static final String SAMPLE_RATE = "Sample Rate: ";
    private static final int DEFAULT_SAMPLE_RATE = 100;
    private JSpinner sampleRateSelection  = new JSpinner(new SpinnerNumberModel(DEFAULT_SAMPLE_RATE,DEFAULT_SAMPLE_RATE,(DEFAULT_SAMPLE_RATE * 100),1));

    private static final String UNIT_SELECTION = "Units: ";
    private final String [] measurements = {"psi", "pascals", "Kipp"};
    private JComboBox<String> unitSelection = new JComboBox<>(measurements);

    private static final String ELONGATION = "Elongation (in): ";
    private JTextField defaultElongationField;

    private JButton saveButton = new JButton("Save");

    public SettingsView () {
        setTitle("Settings");
        setResizable(false);

        add(createNorthPanel(), BorderLayout.NORTH);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(false);
        pack();
        setLocationRelativeTo(null);
    }

    /*
     * Creates fields that allow the user to determine the settings
     * that will be used throughout the test. Those settings include sample rate, units, and elongation
     */
    private JPanel createNorthPanel(){
        JPanel northPanel = new JPanel(new SpringLayout());
        JLabel sampleRateLabel = new JLabel(SAMPLE_RATE,JLabel.TRAILING);
        northPanel.add(sampleRateLabel);
        sampleRateLabel.setLabelFor(sampleRateSelection);
        northPanel.add(sampleRateSelection);

        JLabel unitsSelectionLabel = new JLabel(UNIT_SELECTION,JLabel.TRAILING);
        northPanel.add(unitsSelectionLabel);
        unitsSelectionLabel.setLabelFor(unitSelection);
        northPanel.add(unitSelection);

        JLabel defaultElongationLabel = new JLabel(ELONGATION,JLabel.TRAILING);
        northPanel.add(defaultElongationLabel);
        defaultElongationField = new JTextField("0.5");
        defaultElongationLabel.setLabelFor(defaultElongationField);
        northPanel.add(defaultElongationField);
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
}
