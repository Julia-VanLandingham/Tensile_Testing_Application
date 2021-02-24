package controller;

import view.SettingsView;

public class SettingsController {

    private SettingsView settingsWindow;


    public SettingsController(){
        settingsWindow = new SettingsView();

        settingsWindow.getSaveButton().addActionListener(e -> settingsWindow.setVisible(false));
    }


    public SettingsView getSettingsWindow(){
        return settingsWindow;
    }
}
