package controller;

import model.AITask;
import org.jfree.data.xy.XYSeries;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Live graphs data
 */
public class GraphUpdater extends Thread {
    private XYSeries series;
    private AtomicBoolean done = new AtomicBoolean(false);
    private AtomicBoolean run = new AtomicBoolean(false);
    AITask aiTask;

    public GraphUpdater(XYSeries series) {
        aiTask = new AITask();
        aiTask.createAIChannel(0, AITask.Mode.DIFFERENTIAL);
        aiTask.createAIChannel(1, AITask.Mode.RSE);
        aiTask.readyToRun();

        this.series = series;
    }

    /**
     * Runs the graph updater thread.
     * Adds data from National Instruments Chip to the series
     */
    @Override
    public void run() {
        boolean keepGoing = true;
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
                keepGoing = false;
            }

            aiTask.collectData();
            double [] force = aiTask.getChannelData(0);
            double [] length = aiTask.getChannelData(1); // raw voltage data
            for(int i = 0; i < AITask.INPUT_BUFFER_SIZE; i++){
                series.add(force[i], length[i]);
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

}
