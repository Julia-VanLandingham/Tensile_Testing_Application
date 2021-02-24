package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.SpringLayout;


public class SettingsView extends JFrame{

    private final int frameHeight;
    private final int frameWidth;
    private static final String SAMPLE_RATE_STRING = "Default Sample Rate: ";
    private static final String UNIT_SELECTION_STRING = "Default Units : ";
    private static final String DEFAULT_ELONGATION = "Default Elongation: ";
    private static final int DEFAULT_SAMPLE_RATE = 100;
    private JTextField defaultElongationField;



    private final String [] measurements = {"psi", "pascals", "Kipp"};


    private JButton saveButton = new JButton("Save");
    private JSpinner sampleRateSelection  = new JSpinner(new SpinnerNumberModel(1,1,10,1));
    private JComboBox<String> unitSelection = new JComboBox<>(measurements);
    private JLabel unitSelectionLabel = new JLabel(UNIT_SELECTION_STRING);





    public SettingsView () {
        setTitle("Settings");
        setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = (int) (screenSize.getHeight() * .95);
        frameWidth = (int) (screenSize.getWidth() * .95);
        setSize( frameWidth, frameHeight);

        add(createNorthPanel(), BorderLayout.NORTH);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createNorthPanel(){
        JPanel northPanel = new JPanel(new SpringLayout());
        JLabel sampleRateLabel = new JLabel(SAMPLE_RATE_STRING,JLabel.TRAILING);
        northPanel.add(sampleRateLabel);
        sampleRateLabel.setLabelFor(sampleRateSelection);
        northPanel.add(sampleRateSelection);

        JLabel unitsDefaultLabel = new JLabel(UNIT_SELECTION_STRING,JLabel.TRAILING);
        northPanel.add(unitsDefaultLabel);
        unitsDefaultLabel.setLabelFor(unitSelection);
        northPanel.add(unitSelection);

        JLabel defaultElongationLabel = new JLabel(DEFAULT_ELONGATION ,JLabel.TRAILING);
        northPanel.add(defaultElongationLabel);
        defaultElongationField = new JTextField("0.5");
        defaultElongationLabel.setLabelFor(defaultElongationField);
        northPanel.add(defaultElongationField);
        SpringUtilities.makeCompactGrid(northPanel,3,2,6,6,6,6);
        return northPanel;
    }

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

    public static void main (String [] args){
        SettingsView viewTest = new SettingsView();
    }
}
