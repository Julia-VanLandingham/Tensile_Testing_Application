package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class SettingsView extends JFrame{

    private final int frameHeight;
    private final int frameWidth;
    private static final String SAMPLE_RATE_STRING = "Default Sample Rate: ";
    private static final String UNIT_SELECTION_STRING = "Default Units : ";
    private static final int DEFAULT_SAMPLE_RATE = 100;


    private final String [] measurements = {"psi", "pascals", "Kipp"};
    private JPanel jpanelNorth = new JPanel();
    private JPanel jpanelSouth = new JPanel();
    private JPanel sampleRatePanel = new JPanel();
    private JPanel unitSelectionPanel = new JPanel();
    private JButton saveButton = new JButton("Save");
    private JSpinner sampleRateSelection ;
    private JLabel sampleRateLabel = new JLabel(SAMPLE_RATE_STRING);
    private JComboBox<String> unitSelection = new JComboBox<String>(measurements);
    private JLabel unitSelectionLabel = new JLabel(UNIT_SELECTION_STRING);



    public SettingsView () {
        setTitle("Settings");
        setVisible(false);
        setResizable(true);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = (int) (screenSize.getHeight() * .95);
        frameWidth = (int) (screenSize.getWidth() * .95);

        setSize( frameWidth, frameHeight);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(jpanelNorth, BorderLayout.NORTH);
        add(jpanelSouth, BorderLayout.SOUTH);
        jpanelSouth.add(saveButton);
        sampleRateSelection = new JSpinner();
        sampleRatePanel.add(sampleRateSelection, BorderLayout.EAST);
        sampleRatePanel.add(sampleRateLabel, BorderLayout.WEST);
        unitSelectionPanel.add(unitSelection, BorderLayout.EAST);
        unitSelectionPanel.add(unitSelectionLabel, BorderLayout.WEST);
        jpanelNorth.add(sampleRateLabel, BorderLayout.NORTH);
        jpanelNorth.add(unitSelectionPanel, BorderLayout.SOUTH);
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
