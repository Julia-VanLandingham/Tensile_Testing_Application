package view;

import javax.swing.*;

/**
 * Creates a pop-up frame for user to select exporting options
 */
public class ExportWindow extends JFrame {

    //constants used for formatting
    private static final int VERTICAL_BUFFER = 10;
    private static final int HORIZONTAL_BUFFER = 10;

    private JPanel imageOptionPanel;
    private JPanel exportValuesWithDataPanel;

    private JRadioButton jpg;
    private JRadioButton png;
    private JButton export;
    private JButton cancel;
    private JCheckBox exportImage;
    private JCheckBox exportValuesCheckBox;
    private JCheckBox exportData;

    public ExportWindow(){
        setTitle("Export");
        JPanel outerPanel =  new JPanel();
        outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.Y_AXIS));
        outerPanel.setBorder(BorderFactory.createEmptyBorder(VERTICAL_BUFFER,HORIZONTAL_BUFFER,VERTICAL_BUFFER,HORIZONTAL_BUFFER));

        setupExportValuesWithDataPanel();
        setupImageOptionPanel();
        outerPanel.add(createExportDataPanel());
        outerPanel.add(exportValuesWithDataPanel);
        outerPanel.add(createExportImagePanel());
        outerPanel.add(imageOptionPanel);
        outerPanel.add(Box.createVerticalGlue());
        outerPanel.add(createButtonPanel());

        add(outerPanel);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(false);
    }

    /*
     * Panel with export image check box
     */
    private JPanel createExportImagePanel(){
        JPanel exportImagePanel = new JPanel();
        exportImagePanel.setLayout(new BoxLayout(exportImagePanel, BoxLayout.X_AXIS));

        exportImage = new JCheckBox("Export Graph");

        exportImagePanel.add(exportImage);
        exportImagePanel.add(Box.createHorizontalGlue());

        return exportImagePanel;
    }

    /*
     * Has the radio buttons for choosing which image type the user wish to export
     */
    private void setupImageOptionPanel(){
        imageOptionPanel = new JPanel();
        imageOptionPanel.setLayout(new BoxLayout(imageOptionPanel, BoxLayout.X_AXIS));

        ButtonGroup imageOptionButtons = new ButtonGroup();
        jpg = new JRadioButton("jpg");
        png = new JRadioButton("png");
        imageOptionButtons.add(jpg);
        imageOptionButtons.add(png);

        imageOptionButtons.setSelected(jpg.getModel(), true);

        imageOptionPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        imageOptionPanel.add(jpg);
        imageOptionPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        imageOptionPanel.add(png);
        imageOptionPanel.add(Box.createHorizontalGlue());

        imageOptionPanel.setVisible(true);
    }

    /*
     * Panel with export data check box
     */
    private JPanel createExportDataPanel(){
        JPanel exportDataPanel = new JPanel();
        exportDataPanel.setLayout(new BoxLayout(exportDataPanel, BoxLayout.X_AXIS));

        exportData = new JCheckBox("Export Data");
        exportData.setSelected(true);

        exportDataPanel.add(exportData);
        exportDataPanel.add(Box.createHorizontalGlue());

        return exportDataPanel;
    }

    /*
     * Panel with check box that allows the user to export critical
     * and input values if checked
     */
    private void setupExportValuesWithDataPanel(){
        exportValuesWithDataPanel = new JPanel();
        exportValuesWithDataPanel.setLayout(new BoxLayout(exportValuesWithDataPanel, BoxLayout.X_AXIS));

        exportValuesCheckBox = new JCheckBox("Include input and critical values");
        exportValuesCheckBox.setSelected(true);

        exportValuesWithDataPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        exportValuesWithDataPanel.add(exportValuesCheckBox);
        exportValuesWithDataPanel.add(Box.createHorizontalGlue());

        exportValuesWithDataPanel.setVisible(true);
    }

    /*
     * Export and Cancel options
     */
    private JPanel createButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(VERTICAL_BUFFER,HORIZONTAL_BUFFER,0,HORIZONTAL_BUFFER));

        export = new JButton("Export");
        cancel = new JButton("Cancel");

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(export);
        buttonPanel.add(Box.createHorizontalStrut(HORIZONTAL_BUFFER));
        buttonPanel.add(cancel);

        return buttonPanel;
    }

    public JPanel getImageOptionPanel() {
        return imageOptionPanel;
    }

    public JPanel getExportValuesWithDataPanel() {
        return exportValuesWithDataPanel;
    }

    public JRadioButton getJpg() {
        return jpg;
    }

    public JRadioButton getPng() {
        return png;
    }

    public JButton getExport() {
        return export;
    }

    public JButton getCancel() {
        return cancel;
    }

    public JCheckBox getExportImage() {
        return exportImage;
    }

    public JCheckBox getExportValuesCheckBox() {
        return exportValuesCheckBox;
    }

    public JCheckBox getExportData() {
        return exportData;
    }

}
