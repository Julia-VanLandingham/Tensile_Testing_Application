package controller;

import view.MainWindow;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Root of the entire program, controls all the main window functionality and some interactions between windows
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
        mainWindow.getExit().addActionListener(e -> {
            if(!isStart){
                exitMidPull();
            }else{
                disposeAll();
            }
        });
        mainWindow.getClearButton().addActionListener(e -> clearGraph());

        mainWindow.getStartButton().addActionListener(e -> {
            if(isStart){
                //if no input values at all give a warning
                if(!inputController.haveInputs()){
                    JOptionPane.showMessageDialog(null, "No cross section inputs given!", "Input Warning", JOptionPane.ERROR_MESSAGE);
                //if the input values are from the previous round
                }else if(inputController.areInputsFromPreviousRun()) {
                    int option = JOptionPane.showOptionDialog(null, "Input values have not been changed.\nDo you want to update them?\n", "Input Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new Object[]{"Update", "Continue"}, JOptionPane.YES_OPTION);
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

        //disposes of all windows when the main window is closed
        mainWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if(!isStart){
                    exitMidPull();
                }else{
                    disposeAll();
                }
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
        mainWindow.getReset().setEnabled(false);//do not allow rest while data is being pulled
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
        mainWindow.getReset().setEnabled(true);
        isStart = true;
        if(updater != null) {
            updater.pause();
        }
    }

    /*
     * Clear the graph and reset buttons appropriately
     */
    private void clearGraph(){
        mainWindow.getSeries().clear();
        mainWindow.getStartButton().setEnabled(true);
        mainWindow.getClearButton().setEnabled(false);
    }

    /*
     * Resets the graph, data, and input values
     */
    private void reset(){
        int option = JOptionPane.showOptionDialog(null, "Do you want to reset?", "Reset", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[] {"Yes", "No"}, JOptionPane.YES_OPTION);
        if(option == JOptionPane.YES_OPTION){
            stopDataCollection();
            clearGraph();

            //clear the inputs convert back to the default units from the settings
            inputController.clear();
            settingsController.updateUnitsSystem();
            inputController.onUnitSystemChange();
        }
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    private void exitMidPull() {
        //display yes/no message box
        int exitMessage = JOptionPane.showOptionDialog(null,"You are currently pulling data. Are you sure you want to close the program?","Attempting to close",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE,null, new Object[] {"yes", "no"},JOptionPane.YES_OPTION);
        //if yes call close method
        if(exitMessage == JOptionPane.YES_OPTION) {
            stopDataCollection();
            disposeAll();
        }
    }

    public static void main(String[] args){
        new MainController();
    }
}
