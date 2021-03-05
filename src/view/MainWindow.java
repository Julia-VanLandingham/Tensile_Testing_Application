package view;

import javax.swing.*;
import java.awt.*;

/**
 * This class creates the main window named "Tensile Testing"
 */

public class MainWindow extends JFrame {

    private final int frameHeight;
    private final int frameWidth;

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
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(frameWidth, frameHeight));
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

    /*
     * Creates graph panel
     */
    private void setupGraphPanel(){
        graphPanel = new JPanel();
        graphPanel.setSize(new Dimension((int) ( frameWidth * .75), frameHeight));
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    /*
     * Creates value panel and start/stop button
     */
    private void setupValuePanel(){
        valuePanel = new JPanel();
        valuePanel.add(new JLabel("Wombats"));
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        valuePanel.add(Box.createVerticalGlue());
        startButton = new JButton("Start");
        startButton.setFocusable(false);
        valuePanel.add(startButton);
        valuePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Critical Values"),BorderFactory.createEmptyBorder(50,50,50,50)));
    }

    /*
     * Creates menu bar
     */
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
    
    public int getFrameHeight() {
        return frameHeight;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public JButton getStartButton() {
        return startButton;
    }

    public JPanel getValuePanel() {
        return valuePanel;
    }

    public JPanel getGraphPanel() {
        return graphPanel;
    }

    public JMenuItem getSettings() {
        return settings;
    }

    public JMenuItem getExit() {
        return exit;
    }

    public JMenuItem getExport() {
        return export;
    }

    public JMenuItem getInput() {
        return input;
    }
}
