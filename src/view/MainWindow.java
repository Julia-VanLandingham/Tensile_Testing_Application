package view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final int frameHeight;
    private final int frameWidth;

    private JPanel rightPanel;
    private JPanel buttonPanel;
    private JButton startButton;
    private JPanel valuePanel;
    private JPanel graphPanel;
    private JMenuBar menuBar;
    private JMenuItem settings;
    private JMenuItem exit;
    private JMenuItem export;
    private JMenuItem input;


    public MainWindow(){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameHeight = (int) (screenSize.getHeight() * .95);
        frameWidth = (int) (screenSize.getWidth() * .95);

        setTitle("Tensile Testing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(frameWidth,frameHeight);
        setMinimumSize(new Dimension(frameWidth, frameHeight));
        setResizable(true);

        setupGraphPanel();
        setupValuePanel();

        add(graphPanel, BorderLayout.CENTER);
        add(valuePanel, BorderLayout.EAST);


        setupMenuBar();
        this.setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupGraphPanel(){
        graphPanel = new JPanel();
        graphPanel.setSize(new Dimension((int) ( frameWidth * .75), frameHeight));
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    private void setupValuePanel(){
        valuePanel = new JPanel();
        valuePanel.add(new JLabel("Wombats"));
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        valuePanel.add(Box.createVerticalGlue());
        startButton = new JButton("Start");
        valuePanel.add(startButton);
        valuePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Critical Values"),BorderFactory.createEmptyBorder(50,50,50,50)));

    }


    private void setupMenuBar(){
        menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        settings = new JMenuItem("Settings");
        export = new JMenuItem("Export");
        exit = new JMenuItem("Exit");
        file.add(export);
        file.add(settings);
        file.add(exit);

        JMenu edit = new JMenu("Edit");
        input = new JMenuItem("Input Measurements");
        edit.add(input);


        menuBar.add(file);
        menuBar.add(edit);

    }
    public static void main(String[] args){
        MainWindow mainWindow = new MainWindow();

    }

}
