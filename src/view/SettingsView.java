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
    private static final int DEFAULT_SAMPLE_RATE = 100;



    private final String [] measurements = {"psi", "pascals", "Kipp"};

    private JPanel southPanel = new JPanel();
    private JButton saveButton = new JButton("Save");
    private JSpinner sampleRateSelection  = new JSpinner(new SpinnerNumberModel(1,1,10,1));
    private JComboBox<String> unitSelection = new JComboBox<>(measurements);
    private JLabel unitSelectionLabel = new JLabel(UNIT_SELECTION_STRING);



    public SettingsView () {
        setTitle("Settings");
        setVisible(false);
        setResizable(true);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = (int) (screenSize.getHeight() * .95);
        frameWidth = (int) (screenSize.getWidth() * .95);

        setSize( frameWidth, frameHeight);
        //default layout for panels is flow
        JPanel northPanel = new JPanel(new SpringLayout());
        JLabel sampleRateLabel = new JLabel(SAMPLE_RATE_STRING,JLabel.TRAILING);
        JLabel unitsDefaultLabel = new JLabel(UNIT_SELECTION_STRING,JLabel.TRAILING);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
        northPanel.add(sampleRateLabel);
        sampleRateLabel.setLabelFor(sampleRateSelection);
        northPanel.add(sampleRateSelection);
        northPanel.add(unitsDefaultLabel);
        unitsDefaultLabel.setLabelFor(unitSelection);
        northPanel.add(unitSelection);
        SpringUtilities.makeCompactGrid(northPanel,2,2,5,5,5,5);
        southPanel.add(saveButton);

        pack();
        setLocationRelativeTo(null);
    }

    public void setVisibile(boolean isVisible){
        setVisible(isVisible);
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
        viewTest.setVisibile(true);
    }
}
