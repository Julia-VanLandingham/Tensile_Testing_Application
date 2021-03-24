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

    private final MainWindow mainWindow;
    private final InputController inputController;
    private final SettingsController settingsController;
    private boolean isStart = true;
    private final GraphUpdater updater;

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
        mainWindow.getExit().addActionListener(e -> { if(isStart == false){exitMidPull();} else{disposeAll();} });//change?

        mainWindow.getStartButton().addActionListener(e -> {
            if(isStart){
                //if no input values at all give a warning
                if(!inputController.haveInputs()){
                    JOptionPane.showMessageDialog(null, "No cross section inputs given!", "Input Warning", JOptionPane.ERROR_MESSAGE);
                //if the input values are from the previous round
                }else if(inputController.areInputsFromPreviousRun()) {
                    int option = JOptionPane.showOptionDialog(null, "Input values have not been changed.\n Do you want to update them?", "Input Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Update", "Continue"}, null);
                    if (option == JOptionPane.NO_OPTION) {
                        inputController.pullInputValues();
                        startDataCollection();
                    } else {
                        inputController.getInputWindow().setVisible(true);
                    }
                }else{
                    inputController.pullInputValues();
                    startDataCollection();
                }
            }else {
                stopDataCollection();
            }
        });

        mainWindow.getClearButton().addActionListener(e -> {
            mainWindow.getSeries().clear();
            updater.pause();
            mainWindow.getStartButton().setEnabled(true);
            mainWindow.getClearButton().setEnabled(false);
        });

        //disposes of all windows when the main window is closed
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               exitMidPull();
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

    /*
     * Starts collecting data
     */
    private void startDataCollection(){
        mainWindow.getStartButton().setText("Stop");
        mainWindow.getClearButton().setEnabled(false);
        updater.collect();
        isStart = false;
    }

    /*
     * Stops collecting data
     */
    private void stopDataCollection(){
        mainWindow.getStartButton().setText("Start");
        mainWindow.getClearButton().setEnabled(true);
        mainWindow.getStartButton().setEnabled(false);
        isStart = true;
        updater.pause();
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    /*
     * Resets the graph, data, and input values
     */
    private void reset(){
        int option = JOptionPane.showOptionDialog(null, "Do you want to reset?", "Reset", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Yes", "No"}, JOptionPane.YES_OPTION);
        if(option == JOptionPane.YES_OPTION){
            mainWindow.getSeries().clear();
            if(updater != null){
                updater.pause();
            }
            mainWindow.getStartButton().setEnabled(true);
            mainWindow.getClearButton().setEnabled(false);
            inputController.clear();
            settingsController.updateUnitsSystem();
            inputController.onUnitSystemChange();
        }
    }

    private void exitMidPull() {
        //display yes/no message box
        int exitMessage = JOptionPane.showOptionDialog(null,"You are currently pulling data. Are you sure you want to close the program?","Attempting to close",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null, new Object[] {"yes", "no"},JOptionPane.YES_OPTION);
        //if yes call close method
        if(exitMessage == JOptionPane.YES_OPTION) {
            stopDataCollection();
            disposeAll();
        }
        if(exitMessage == JOptionPane.NO_OPTION) {
            return;
        }
    }

    public static void main(String[] args){
        new MainController();
    }
}
