package controller;

import kirkwood.nidaq.access.NiDaqException;
import model.AITask;
import org.jfree.data.xy.XYSeries;
import controller.Calculations.Units;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Live graphs data
 */
public class GraphUpdater extends Thread{
    private XYSeries series;
    private XYSeries updatedSeries;
    private AtomicBoolean done = new AtomicBoolean(false);
    private AtomicBoolean run = new AtomicBoolean(false);
    private AITask aiTask;
    private MainController mainController;
    private InputController inputController;
    private double stressZero = 0.0; //force = stress
    private double strainZero = 0.0; //elongation = strain (Extensometer)
    private SettingsController settingsController;

    private static double LBS_PER_VOLT;
    private static double INCHES_PER_VOLT;

    public GraphUpdater(XYSeries series, MainController mainController, InputController inputController, SettingsController settingsController) throws NiDaqException {
        aiTask = new AITask();
        aiTask.createAIChannel(settingsController.getSettingsWindow().getElongationChannel(), settingsController.getSettingsWindow().getElongationMode()); //Elongation
        aiTask.createAIChannel(settingsController.getSettingsWindow().getForceChannel(), settingsController.getSettingsWindow().getForceMode()); //Force
        aiTask.readyToRun();
        LBS_PER_VOLT = settingsController.getSettingsWindow().getForceVoltage2UnitConstant();
        INCHES_PER_VOLT = settingsController.getSettingsWindow().getElongationVoltage2UnitConstant();
        this.mainController = mainController;
        this.inputController = inputController;
        this.settingsController = settingsController;
        this.series = series;
    }

    /**
     * Runs the graph updater thread.
     * Adds data from National Instruments Chip to the series
     */
    @Override
    public void run() {
        int x = 0;
        while(!done.get()) {//call code from class that gets data from the chip.
            try {
                if(!run.get()){
                    synchronized (this){
                        wait();
                    }
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            if(done.get()) return;

            aiTask.collectData();
            double [] force = aiTask.getChannelData(0);
            double [] length = aiTask.getChannelData(1); // raw voltage data
            for(int i = 0; i < AITask.AVERAGE_FACTOR; i++){
                double forceValue = (LBS_PER_VOLT * (force[i]  - stressZero));
                double elongationValue = (INCHES_PER_VOLT * (length[i]  - strainZero));
                if(mainController.getUnitSystem().equals("Metric")){
                    forceValue = Calculations.convertForce(Calculations.Units.ENGLISH, Calculations.Units.METRIC,forceValue);
                    elongationValue = Calculations.convertLength(Calculations.Units.ENGLISH, Calculations.Units.METRIC, elongationValue);
                }else{
                    forceValue /= 1000;
                }

                double stressValue = Calculations.calculateStress(forceValue, mainController.findArea());
                double strainValue = Calculations.calculateStrain(elongationValue, mainController.getGaugeLength());

                series.add(strainValue, stressValue);
            }
        }
    }

    /**
     * Pauses the graph updater thread
     */
    public void pause(){
        run.set(false);
    }

    /**
     * Resumes the graph updater thread
     */
    public synchronized void collect(){
        run.set(true);
        notifyAll();
    }

    /**
     * Stops the tread permanently
     */
    public synchronized void terminate() {
        done.set(true);
        notifyAll();
    }

    public void updateZeros(){
        ArrayList<Double> force = new ArrayList<>();
        ArrayList<Double> elongation = new ArrayList<>();
        double forceTotal = 0.0;
        double elongationTotal = 0.0;
        aiTask.collectData();
        double [] channel0 = aiTask.getChannelData(0);
        double [] channel1 = aiTask.getChannelData(1);
        for(int j = 0; j < channel0.length; j++){
            force.add(channel0[j]);
            elongation.add(channel1[j]);
        }

        for(double num : force){
            forceTotal += num;
        }
        for(double num : elongation){
            elongationTotal += num;
        }

        stressZero = forceTotal / force.size();
        strainZero = elongationTotal / elongation.size();
    }

    /**
     * Updates the graph units based on starting and ending units
     * @param startingUnits the units that currently appear on the graph
     * @param endingUnits the units a user wants to convert to
     * @param series that data in (x,y) format that is shown on the graph
     */
    public void updateGraphUnits(Units startingUnits, Units endingUnits, XYSeries series){
        for(int i = 0; i < series.getItemCount(); i++){
            double yValue = series.getY(i).doubleValue();
            yValue = Calculations.convertPressure(startingUnits, endingUnits, yValue);
            series.updateByIndex(i, yValue);
        }
    }
}
