package controller;

import com.sun.corba.se.impl.resolver.INSURLOperationImpl;
import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;
import model.AITask;
import model.Channel;
import org.jfree.data.xy.XYSeries;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Live graphs data
 */
public class GraphUpdater extends Thread {

    private XYSeries series;
    private AtomicBoolean done = new AtomicBoolean(false);
    private AtomicBoolean run = new AtomicBoolean(false);
    private static final int INPUT_BUFFER_SIZE = 1000;
    private static final int SAMPLES_PER_SECOND = 100;
    AITask aiTask;

    public GraphUpdater(XYSeries series) {
        aiTask = new AITask();

        aiTask.createAIChannel(0, AITask.Mode.DIFFERENTIAL);
        aiTask.createAIChannel(1, AITask.Mode.RSE);

        aiTask.readyToRun();


        this.series = series;
    }

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

            // first half of buffer is one channel and other half is other channel

            double[][] data = aiTask.getValues();
            for(int i = 0; i < 1000; i++){
                series.add(data[0][i], data[1][i]);
            }
            /*
            for(int i = 0; i < channel0Read && i < channel1Read; i++ ){
                series.add(channel1Buffer.get(),channel0Buffer.get());
            }
            channel0Buffer.clear();
            channel1Buffer.clear();


             */





        }
    }

    public void pause(){
        run.set(false);
    }

    public synchronized void collect(){
        run.set(true);
        notifyAll();
    }

    public synchronized void terminate() {
        done.set(true);
        notifyAll();
    }

}
