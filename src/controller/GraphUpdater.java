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
        SlidingAverage forceAveraged = new SlidingAverage(AITask.UPDATES_PER_SECOND);
        SlidingAverage elongationAveraged = new SlidingAverage(AITask.UPDATES_PER_SECOND);
        int measurementTime = 0;
        int counter = 0;
        while(!done.get()) {//call code from class that gets data from the chip.
            try {
                if(!run.get()){
                    synchronized (this){
                        wait();
                    }
                }
                //Thread.sleep(Math.max((1000 / AITask.UPDATES_PER_SECOND) - measurementTime, 0));
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            if(done.get()) return;
            long start = System.nanoTime();
            aiTask.collectData();
            double force = aiTask.getChannelData(0);
            double length = aiTask.getChannelData(1); // raw voltage data

            double forceValue = (LBS_PER_VOLT * (forceAveraged.addData(force)  - stressZero));
            double elongationValue = (INCHES_PER_VOLT * (elongationAveraged.addData(length)  - strainZero));
            if(mainController.getUnitSystem().equals("Metric")){
                forceValue = Calculations.convertForce(Calculations.Units.ENGLISH, Calculations.Units.METRIC,forceValue);
                elongationValue = Calculations.convertLength(Calculations.Units.ENGLISH, Calculations.Units.METRIC, elongationValue);
            }else{
                forceValue /= 1000;
            }

            double stressValue = Calculations.calculateStress(forceValue, mainController.findArea());
            double strainValue = Calculations.calculateStrain(elongationValue, mainController.getGaugeLength());

            /*
            counter++;
            if(counter == AITask.UPDATES_PER_SECOND){
                series.add(strainValue, stressValue, true);
                counter = 0;
            }else{
                series.add(strainValue, stressValue, false);
            }
            */

            //measurementTime = (int) ((System.nanoTime() - start)/1000000);
            measurementTime = 0;
            series.add(strainValue, stressValue, true);
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

        double forceTotal = 0.0;
        double elongationTotal = 0.0;
        aiTask.collectData();


        for(int i = 0; i < AITask.UPDATES_PER_SECOND; i++){
            double channel0 = aiTask.getChannelData(AITask.FORCE_CHANNEL);
            double channel1 = aiTask.getChannelData(AITask.LENGTH_CHANNEL);

            forceTotal += channel0;
            elongationTotal += channel1;
        }

        stressZero = forceTotal / AITask.UPDATES_PER_SECOND;
        strainZero = elongationTotal / AITask.UPDATES_PER_SECOND;
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
