package controller;

import view.MainWindow;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Root of the entire program
 * NOTE: This should be the only main method ever actually run
 */
public class MainController {

    private MainWindow mainWindow;
    private InputController inputController;
    private SettingsController settingsController;

    public MainController(){
        setLookAndFeel();
        mainWindow = new MainWindow();
        inputController = new InputController();
        settingsController = new SettingsController();

        mainWindow.getInput().addActionListener(e -> inputController.getInputWindow().setVisible(true));
        mainWindow.getSettings().addActionListener(e -> settingsController.getSettingsWindow().setVisible(true));
        //disposes of all windows when the main window is closed
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                inputController.getInputWindow().dispose();
                settingsController.getSettingsWindow().dispose();
                mainWindow.dispose();
            }
        });
    }

    /*
     * Set the look and feel for all the windows
     */
    private void setLookAndFeel(){
        try {
            //Set system look and feel
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new MainController();
    }
}