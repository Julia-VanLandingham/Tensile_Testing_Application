package view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This class creates the main window named "Tensile Testing"
 */

public class MainWindow extends JFrame {

    //constants used for formatting
    protected static final int VERTICAL_BUFFER = 10;
    protected static final int HORIZONTAL_BUFFER = 10;

    private JButton startButton;
    private JButton graphReset;
    private JPanel valuePanel;
    private JPanel eastPanel;
    private JPanel optionsPanel;
    private JPanel graphPanel;
    private JLabel youngsModulus = new JLabel("-");
    private JLabel ultimatePointX = new JLabel("X: -");
    private JLabel yieldPointX = new JLabel("X: -");
    private JLabel failurePointX = new JLabel("X: -");
    private JLabel ultimatePointY = new JLabel("Y: -");
    private JLabel yieldPointY = new JLabel("Y: -");
    private JLabel failurePointY = new JLabel("Y: -");
    private JMenuBar menuBar;
    private JMenuItem settings;
    private JMenuItem exit;
    private JMenuItem export;
    private JMenuItem input;
    private JFreeChart chart;
    private JMenuItem reset;
    private XYSeries series = new XYSeries("Stress-Strain Curve");

    public MainWindow(){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameHeight = (int) (screenSize.getHeight() * .95);
        int frameWidth = (int) (screenSize.getWidth() * .95);

        setTitle("Tensile Testing");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setResizable(true);

        setupGraphPanel();
        setupEastPanel();

        add(graphPanel, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

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
        graphPanel.setLayout(new BorderLayout());
        graphPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER,VERTICAL_BUFFER), BorderFactory.createLineBorder(Color.BLACK, 1)));

        XYSeriesCollection dataset = new XYSeriesCollection(series);
        chart = ChartFactory.createXYLineChart(null,"Strain","Stress",dataset, PlotOrientation.VERTICAL,true,true,true);

        ChartPanel chartPanel = new ChartPanel(chart);

        graphPanel.add(chartPanel,BorderLayout.CENTER);
        graphPanel.validate();
    }

    /*
     * Creates value panel
     */
    private void setupValuePanel(){
        valuePanel = new JPanel();
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.Y_AXIS));
        valuePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Critical Values"),BorderFactory.createEmptyBorder(VERTICAL_BUFFER,HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER)));

        valuePanel.add(new JLabel("Young's Modulus:"));
        valuePanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
        panel1.setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_BUFFER, 0 , 0));
        panel1.add(youngsModulus);
        valuePanel.add(panel1);
        valuePanel.add(Box.createVerticalGlue());

        valuePanel.add(new JLabel("Ultimate Point:"));
        valuePanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
        panel2.setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_BUFFER, 0 , 0));
        panel2.add(ultimatePointX);
        panel2.add(ultimatePointY);
        valuePanel.add(panel2);
        valuePanel.add(Box.createVerticalGlue());

        valuePanel.add(new JLabel("Yield Point:"));
        valuePanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
        JPanel panel3 = new JPanel();
        panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));
        panel3.setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_BUFFER, 0 , 0));
        panel3.add(yieldPointX);
        panel3.add(yieldPointY);
        valuePanel.add(panel3);
        valuePanel.add(Box.createVerticalGlue());

        valuePanel.add(new JLabel("Failure Point:"));
        valuePanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
        panel4.setBorder(BorderFactory.createEmptyBorder(0, HORIZONTAL_BUFFER, 0 , 0));
        panel4.add(failurePointX);
        panel4.add(failurePointY);
        valuePanel.add(panel4);
        valuePanel.add(Box.createVerticalGlue());

        valuePanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
    }

    /*
     * Sets up the panel that holds the buttons for start/stop and clear
     */
    private void setupOptionPanel(){
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        startButton = new JButton("Start");
        graphReset = new JButton("Clear");
        graphReset.setEnabled(false);

        optionsPanel.add(startButton);
        optionsPanel.add(Box.createVerticalStrut(VERTICAL_BUFFER));
        optionsPanel.add(graphReset);
    }

    /*
     * Sets up the far right panel that holds the values and the button options
     */
    private void setupEastPanel(){
        eastPanel = new JPanel();
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(HORIZONTAL_BUFFER,0,HORIZONTAL_BUFFER,VERTICAL_BUFFER));

        setupValuePanel();
        setupOptionPanel();

        eastPanel.add(valuePanel);
        eastPanel.add(Box.createVerticalGlue());
        eastPanel.add(optionsPanel);
        eastPanel.add(Box.createVerticalGlue());
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
        reset = new JMenuItem("Reset");
        edit.add(input);
        edit.add(reset);

        //set hotkeys
        file.setMnemonic(KeyEvent.VK_F);
        edit.setMnemonic(KeyEvent.VK_E);
        exit.setMnemonic(KeyEvent.VK_X);

        KeyStroke keyStrokeToInput = KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.CTRL_DOWN_MASK);
        input.setAccelerator(keyStrokeToInput);
        KeyStroke keyStrokeToReset = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
        reset.setAccelerator(keyStrokeToReset);
        KeyStroke keyStrokeToExport = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        export.setAccelerator(keyStrokeToExport);
        KeyStroke keyStrokeToSettings = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK);
        settings.setAccelerator(keyStrokeToSettings);


        menuBar.add(file);
        menuBar.add(edit);
    }

    //getters
    public JButton getStartButton() {
        return startButton;
    }

    public JButton getClearButton(){ return graphReset;}
    
    public JPanel getValuePanel() {
        return valuePanel;
    }

    public JPanel getGraphPanel() {
        return graphPanel;
    }

    public JFreeChart getChart(){
        return chart;
    }

    public JLabel getYoungsModulus() { return youngsModulus; }

    public JLabel getUltimatePointX() { return ultimatePointX; }

    public JLabel getYieldPointX() { return yieldPointX; }

    public JLabel getFailurePointX() { return failurePointX; }

    public void setYoungsModulus(JLabel youngsModulus) {
        this.youngsModulus = youngsModulus;
    }

    public void setUltimatePointX(JLabel ultimatePointX) {
        this.ultimatePointX = ultimatePointX;
    }

    public void setYieldPointX(JLabel yieldPointX) {
        this.yieldPointX = yieldPointX;
    }

    public void setFailurePointX(JLabel failurePointX) {
        this.failurePointX = failurePointX;
    }

    public JLabel getUltimatePointY() {
        return ultimatePointY;
    }

    public void setUltimatePointY(JLabel ultimatePointY) {
        this.ultimatePointY = ultimatePointY;
    }

    public JLabel getYieldPointY() {
        return yieldPointY;
    }

    public void setYieldPointY(JLabel yieldPointY) {
        this.yieldPointY = yieldPointY;
    }

    public JLabel getFailurePointY() {
        return failurePointY;
    }

    public void setFailurePointY(JLabel failurePointY) {
        this.failurePointY = failurePointY;
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

    public JMenuItem getReset() { return reset; }

    public JMenuItem getInput() {
        return input;
    }

    public XYSeries getSeries() {
        return series;
    }
}
