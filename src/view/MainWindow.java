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
    private JPanel optionsPanel;
    private JPanel graphPanel;
    private JMenuBar menuBar;
    private JMenuItem settings;
    private JMenuItem exit;
    private JMenuItem export;
    private JMenuItem input;
    private JFreeChart chart;
    private JMenuItem reset;
    private final XYSeries series = new XYSeries("Stress-Strain Curve");

    public MainWindow(){

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int frameHeight = (int) (screenSize.getHeight() * .95);
        int frameWidth = (int) (screenSize.getWidth() * .95);

        setTitle("Tensile Testing");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setResizable(true);

        setupGraphPanel();
        setupOptionPanel();

        add(graphPanel, BorderLayout.CENTER);
        add(optionsPanel, BorderLayout.SOUTH);

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
     * Sets up the panel that holds the buttons for start/stop and clear
     */
    private void setupOptionPanel(){
        optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(0,VERTICAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER));

        startButton = new JButton("Start");
        graphReset = new JButton("Clear");
        graphReset.setToolTipText("Click this to clear graph, but not inputs");
        graphReset.setEnabled(false);

        optionsPanel.add(Box.createHorizontalGlue());
        optionsPanel.add(startButton);
        optionsPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        optionsPanel.add(graphReset);
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
        reset.setToolTipText("Click this to reset both graph and inputs");
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
        KeyStroke keyStrokeToSettings = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.ALT_DOWN_MASK + KeyEvent.CTRL_DOWN_MASK);
        settings.setAccelerator(keyStrokeToSettings);

        menuBar.add(file);
        menuBar.add(edit);
    }

    //getters
    public JButton getStartButton() {
        return startButton;
    }

    public JButton getClearButton(){ return graphReset;}

    public JFreeChart getChart(){
        return chart;
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
