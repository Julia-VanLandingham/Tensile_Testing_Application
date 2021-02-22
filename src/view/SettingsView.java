package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.SpringLayout;
import javax.swing.Spring;



public class SettingsView extends JFrame{

    private final int frameHeight;
    private final int frameWidth;
    private static final String SAMPLE_RATE_STRING = "Default Sample Rate: ";
    private static final String UNIT_SELECTION_STRING = "Default Units : ";
    private static final int DEFAULT_SAMPLE_RATE = 100;



    private final String [] measurements = {"psi", "pascals", "Kipp"};
    private final String[] SpringLables ={SAMPLE_RATE_STRING, UNIT_SELECTION_STRING};
    private int labelLength = SpringLables.length;
    private JPanel jpanelNorth = new JPanel(new SpringLayout());
    private JLabel sampleRateLabel = new JLabel(SpringLables[0]);
    private JLabel unitsDefaultLabel = new JLabel(SpringLables[1]);
    private JPanel jpanelSouth = new JPanel();
    private JButton saveButton = new JButton("Save");
    private JSpinner sampleRateSelection ;
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
        //default layout for panels is flow
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        add(jpanelNorth, BorderLayout.NORTH);
        add(jpanelSouth, BorderLayout.SOUTH);
        jpanelNorth.add(sampleRateLabel);
        jpanelNorth.add(unitsDefaultLabel);
        jpanelNorth.add(sampleRateSelection);
        jpanelNorth.add(unitSelection);
        //jpanelNorth.putConstraint(jpanelNorth,Sp)-trying to add constraints to spring panel
        jpanelSouth.add(saveButton);
        sampleRateSelection = new JSpinner();
        jpanelNorth.add(sampleRateLabel, BorderLayout.NORTH);
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
