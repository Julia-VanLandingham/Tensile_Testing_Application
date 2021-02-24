package controller;

import view.MainWindow;

/**
 * Root of the entire program
 * NOTE: This should be the only main method ever actually run
 */
public class MainController {

    private MainWindow mainWindow;
    private InputController inputController;

    public MainController(){
        mainWindow = new MainWindow();
        inputController = new InputController();

        mainWindow.getInput().addActionListener(e -> inputController.getInputWindow().setVisible(true));
    }

    public static void main(String[] args){
        new MainController();
    }
}
