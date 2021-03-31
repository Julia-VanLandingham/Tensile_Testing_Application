package controller;

import view.ExportWindow;

/**
 * Sets up and controls the functions of the ExportWindow
 */
public class ExportController {
    private final ExportWindow exportWindow;
    protected boolean isUnsaved;

    public ExportController(){
        exportWindow = new ExportWindow();
        isUnsaved = false;

        exportWindow.getCancel().addActionListener(e -> exportWindow.setVisible(false));
        exportWindow.getExport().addActionListener(e -> {
            exportWindow.setVisible(false);
            isUnsaved = false;
        });

        exportWindow.getExportData().addActionListener(e -> {
            exportWindow.getExportValuesWithDataPanel().setVisible(isExportDataSelected());
            exportWindow.getExportValuesCheckBox().setSelected(true);
        });

        exportWindow.getExportImage().addActionListener(e ->{
            exportWindow.getImageOptionPanel().setVisible(isExportImageSelected());
            exportWindow.getJpg().setSelected(true);
        });

        exportWindow.getImageOptionPanel().setVisible(false);
    }

    /**
     * Determines if the export data check box has been selected
     * @return if export data is checked
     */
    public boolean isExportDataSelected(){
        return exportWindow.getExportData().isSelected();
    }

    /**
     * Determines if values and input check box is selected
     * if checked, these values will export along with the data
     * @return if export values check box is selected
     */
    public boolean isExportValuesSelected() {
        return exportWindow.getExportValuesCheckBox().isSelected();
    }

    /**
     * Determines if export image check box is selected
     * this will export image of the graph if selected
     * @return if export image is checked
     */
    public boolean isExportImageSelected(){
        return exportWindow.getExportImage().isSelected();
    }

    /**
     * Determines if jpg check box is selected
     * if selected export jpg file, if not png selected export png file
     * @return if jpg is selected
     */
    public boolean isJpgSelected(){
        return exportWindow.getJpg().isSelected();
    }

    public ExportWindow getExportWindow() {
        return exportWindow;
    }
}
