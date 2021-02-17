package view;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class SettingsView {

    private final String [] measurments = {"psi", "pascals", "Kipp"};
    private JFrame jFrame;
    private JPanel jpanelNorth = new JPanel();
    private JPanel jpanelSouth = new JPanel();
    private JButton saveButton = new JButton("Save");
    private JComboBox measurementsList = new JComboBox (measurments);


    public SettingsView (){
        jFrame = new JFrame("Settings");
        jFrame.setVisible(false);
        jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        jFrame.add(jpanelNorth, BorderLayout.NORTH);
        jFrame.add(jpanelSouth, BorderLayout.SOUTH);
        jpanelSouth.add(saveButton);

    }

    public void setVisibile(boolean isVisible){
        jFrame.setVisible(isVisible);
    }

    public ArrayList<String> getInput(){ //for getting all the inputs from the fields before closing
        return null;
    }

    private ArrayList<String> getDefaults(){ //gets the defaults from the class that handles persistent settings
        return null;
    }

    public static void main (String [] args){
        SettingsView viewTest = new SettingsView();
        viewTest.setVisibile(true);
    }
}
