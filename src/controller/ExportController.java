package controller;

import view.ExportWindow;

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

    public boolean isExportDataSelected(){
        return exportWindow.getExportData().isSelected();
    }

    public boolean isExportValuesSelected() {
        return exportWindow.getExportValuesCheckBox().isSelected();
    }

    public boolean isExportImageSelected(){
        return exportWindow.getExportImage().isSelected();
    }

    public boolean isJpgSelected(){
        return exportWindow.getJpg().isSelected();
    }

    public ExportWindow getExportWindow() {
        return exportWindow;
    }
}
