package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class SettingsView extends JFrame{

    private final String [] measurments = {"psi", "pascals", "Kipp"};
    private JPanel jpanelNorth = new JPanel();
    private JPanel jpanelSouth = new JPanel();
    private JButton saveButton = new JButton("Save");
    private JComboBox measurementsList = new JComboBox (measurments);
    private final int frameHeight;
    private final int frameWidth;


    public SettingsView () {
        setTitle("Settings");
        setVisible(false);
        setResizable(true);


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = (int) (screenSize.getHeight() * .95);
        frameWidth = (int) (screenSize.getWidth() * .95);

        setSize( new Dimension(frameWidth, frameHeight));

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        add(jpanelNorth, BorderLayout.NORTH);
        add(jpanelSouth, BorderLayout.SOUTH);
        jpanelSouth.add(saveButton);
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
