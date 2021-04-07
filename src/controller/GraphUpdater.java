package controller;

import model.AITask;
import org.jfree.data.xy.XYSeries;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Live graphs data
 */
public class GraphUpdater extends Thread {
    private XYSeries series;
    private AtomicBoolean done = new AtomicBoolean(false);
    private AtomicBoolean run = new AtomicBoolean(false);
    private AITask aiTask;
    private MainController mainController;
    private double stressZero = 0.0; //force = stress
    private double strainZero = 0.0; //elongation = strain (Extensometer)

    private static final double LBS_PER_VOLT = 1960.574197;
    private static final double INCHES_PER_VOLT = 0.041814743;

    public GraphUpdater(XYSeries series, MainController mainController) {
        aiTask = new AITask();
        aiTask.createAIChannel(3, AITask.Mode.DIFFERENTIAL);
        aiTask.createAIChannel(1, AITask.Mode.RSE);
        aiTask.readyToRun();
        this.mainController = mainController;
        this.series = series;
    }

    /**
     * Runs the graph updater thread.
     * Adds data from National Instruments Chip to the series
     */
    @Override
    public void run() {
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
                double stressValue = Calculations.calculateStress((LBS_PER_VOLT * (force[i]  - stressZero)) / 1000, mainController.findArea());
                double strainValue = Calculations.calculateStrain((INCHES_PER_VOLT * (length[i]  - strainZero)), mainController.getGaugeLength());

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

}
