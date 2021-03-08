package controller;

import view.SettingsView;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class SettingsController {
    private static final String CONFIG_FILE = "settings.cfg"; //file settings are stored in
    private SettingsView settingsWindow;

    public SettingsController(InputController inputController){
        Scanner input = null;
        try{
           input = new Scanner(new File(CONFIG_FILE));
        } catch (FileNotFoundException e) {
        }
        settingsWindow = new SettingsView(input);

        settingsWindow.getSaveButton().addActionListener(e -> settingsWindow.setVisible(false));
        if(input != null){
            input.close();
        }

        //retrieves settings info and stores in file
        settingsWindow.getSaveButton().addActionListener(e -> {
            try {
                double value = settingsWindow.getDefaultGaugeLength();
                inputController.getInputWindow().getGaugeLengthInputField().setText(String.valueOf(value)); //settings gauge length to input gauge length
                inputController.getInputWindow().getUnitSelectionBox().setSelectedItem(settingsWindow.getDefaultUnits());

                PrintWriter out = new PrintWriter(new FileOutputStream(CONFIG_FILE));
                out.println(settingsWindow.getDefaultUnitSelectionBox().getSelectedItem());
                out.println(value);
                out.close();
            } catch (FileNotFoundException exception) {
            }
            catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(settingsWindow,"Default Gauge Length is not a properly formatted number."," Bad Gauge Length",JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public SettingsView getSettingsWindow(){
        return settingsWindow;
    }

}
