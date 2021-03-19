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
    private boolean isStart = true;
    private GraphUpdater updater;

    public MainController(){
        setLookAndFeel();
        mainWindow = new MainWindow();
        inputController = new InputController(this);
        settingsController = new SettingsController(inputController, this);
        updater = new GraphUpdater(mainWindow.getSeries());
        updater.start();

        mainWindow.getInput().addActionListener(e -> inputController.getInputWindow().setVisible(true));
        mainWindow.getSettings().addActionListener(e -> settingsController.getSettingsWindow().setVisible(true));
        mainWindow.getReset().addActionListener(e -> reset());
        mainWindow.getExit().addActionListener(e -> disposeAll());
        mainWindow.getStartButton().addActionListener(e -> {
            if(isStart){
                mainWindow.getStartButton().setText("Stop");
                updater.collect();
                isStart = false;
            }else {
                mainWindow.getStartButton().setText("Start");
                isStart = true;
                updater.pause();
                mainWindow.getStartButton().setEnabled(false);
            }
        });
        mainWindow.getGraphReset().addActionListener(e -> {mainWindow.getSeries().clear(); updater.pause(); mainWindow.getStartButton().setEnabled(true);});
        //disposes of all windows when the main window is closed
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               disposeAll();

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

    /*
     * Disposes of all windows and terminates any current graphing to end the program
     */
    private void disposeAll(){
        inputController.getInputWindow().dispose();
        settingsController.getSettingsWindow().dispose();
        mainWindow.dispose();
        if(updater != null){
            updater.terminate();
        }
    }


    public MainWindow getMainWindow() {
        return mainWindow;
    }
    private void reset(){
        int option = JOptionPane.showOptionDialog(null, "Do you want to reset?", "Reset", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Yes", "No"}, JOptionPane.YES_OPTION);
        if(option == JOptionPane.YES_OPTION){
            mainWindow.getSeries().clear();
            if(updater != null){
                updater.pause();
            }
            mainWindow.getStartButton().setEnabled(true);
            inputController.clear();
            settingsController.updateUnitsSystem();
            inputController.onUnitSystemChange();
        }
    }

    public static void main(String[] args){
        new MainController();
    }
}
