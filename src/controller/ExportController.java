package controller;

import org.jfree.data.xy.XYSeries;
import view.ExportWindow;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Sets up and controls the functions of the ExportWindow
 */
public class ExportController {
    private final ExportWindow exportWindow;
    protected boolean isUnsaved;
    private File file;
    private XYSeries xySeries;
    private MainController mainController;
    private InputController inputController;

    public ExportController(XYSeries xySeries, MainController mainController, InputController inputController){
        exportWindow = new ExportWindow();
        isUnsaved = false;
        this.xySeries = xySeries;
        this.mainController = mainController;
        this.inputController = inputController;

        exportWindow.getCancel().addActionListener(e -> exportWindow.setVisible(false));

        exportWindow.getExport().addActionListener(e -> {
            exportWindow.setVisible(false);
            PrintWriter outfile = null;

            JFileChooser fc = new JFileChooser();
            int r = fc.showSaveDialog(null);
            if(r == JFileChooser.APPROVE_OPTION){
                file = fc.getSelectedFile();
            }

            try {
                outfile = new PrintWriter(new FileOutputStream(file));
                if(isExportValuesSelected()){//unit system, input values, gauge length
                    outfile.write("Unit System: " + mainController.getUnitSystem() + "\n");
                    outfile.write("Gauge Length: " + mainController.getGaugeLength() + "\n");
                    if(inputController.isRectangularSelected()){
                        outfile.write("Width: " + mainController.getWidth() + "\n");
                        outfile.write("Depth: " + mainController.getDepth() + "\n");
                    }else{
                        outfile.write("Diameter: " + mainController.getDiameter() + "\n");
                    }
                    outfile.write("\n");
                }
                double [][] data = xySeries.toArray();
                outfile.write("Strain,Stress\n");
                for(int i = 0; i < data[0].length; i++){
                    outfile.format("%.6f,%.6f%n", data[0][i], data[1][i]);
                }
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }finally{
                if(outfile != null) {
                    outfile.close();
                }
            }
            isUnsaved = false;
        });


    }

    /**
     * Determines if values and input check box is selected
     * if checked, these values will export along with the data
     * @return if export values check box is selected
     */
    public boolean isExportValuesSelected() {
        return exportWindow.getExportValuesCheckBox().isSelected();
    }

    public ExportWindow getExportWindow() {
        return exportWindow;
    }
}
