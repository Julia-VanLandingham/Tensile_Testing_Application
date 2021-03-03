package controller;

import view.MainWindow;

/**
 * Root of the entire program
 * NOTE: This should be the only main method ever actually run
 */
public class MainController {

    private MainWindow mainWindow;
    private InputController inputController;
    private SettingsController settingsController;

    public MainController(){
        mainWindow = new MainWindow();
        inputController = new InputController();
        settingsController = new SettingsController(inputController);

        mainWindow.getInput().addActionListener(e -> inputController.getInputWindow().setVisible(true));
        mainWindow.getSettings().addActionListener(e -> settingsController.getSettingsWindow().setVisible(true));
    }

    public static void main(String[] args){
        new MainController();
    }
}
