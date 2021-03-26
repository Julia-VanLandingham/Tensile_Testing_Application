package controller;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;
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
    private static NiDaq daq = new NiDaq();
    private static final int INPUT_BUFFER_SIZE = 1000;
    private static final int SAMPLES_PER_SECOND = 100;
    private Channel channel0;
    private Channel channel1;

    public GraphUpdater(XYSeries series) {
        // Create Channels here:
        channel0 = new Channel("Dev1/ai0", SAMPLES_PER_SECOND, INPUT_BUFFER_SIZE, Channel.Mode.DIFFERENTIAL, daq);
        channel1 = new Channel("Dev1/ai1", SAMPLES_PER_SECOND, INPUT_BUFFER_SIZE, Channel.Mode.RSE, daq);

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
            int[] channel0Read = channel0.read();
            int[] channel1Read = channel1.read();
            double[] channel0Buffer = channel0.getBuffer();
            double[] channel1Buffer = channel1.getBuffer();

            if(channel0Read[0]<= channel1Read[0]){
                for(int i = 0; i < channel0Read[0]; i++ ){
                    series.add(channel1Buffer[i],channel0Buffer[i]);
                }
            }else{
                for(int i = 0; i < channel1Read[0]; i++){
                    series.add(channel1Buffer[i],channel0Buffer[i]);
                }
            }


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
