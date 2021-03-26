package controller;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;
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
    private double[] buffer = new double[INPUT_BUFFER_SIZE*2];
    private static final int SAMPLES_PER_SECOND = 100;
    private IntBuffer samplesPerChannelRead;
    private Pointer aiTask = null;
    private DoubleBuffer inputBuffer;

    public GraphUpdater(XYSeries series) {
        try {
            String physicalChan = "Dev1/ai0:1";
            aiTask = daq.createTask("AITask");
            daq.createAIVoltageChannel(aiTask, physicalChan, "", Nicaiu.DAQmx_Val_Diff, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
            daq.cfgSampClkTiming(aiTask, "", SAMPLES_PER_SECOND, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_ContSamps, INPUT_BUFFER_SIZE);
            daq.startTask(aiTask);
            Integer read = new Integer(0);
            inputBuffer = DoubleBuffer.wrap(buffer);
            samplesPerChannelRead = IntBuffer.wrap(new int[] {read} );
        } catch(NiDaqException e) {
            try {
                System.out.println("Trying to stop task");
                System.out.println(e.getMessage());
                e.printStackTrace();
                daq.stopTask(aiTask);
                daq.clearTask(aiTask);
            } catch(NiDaqException e2) {}

        }
        this.series = series;
    }

    @Override
    public void run() {
        boolean keepGoing = true;
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
            try {
                daq.readAnalogF64(aiTask, -1, -1, Nicaiu.DAQmx_Val_GroupByChannel, inputBuffer, INPUT_BUFFER_SIZE, samplesPerChannelRead);
            } catch (NiDaqException e) {
                e.printStackTrace();
            }
            // first half of buffer is one channel and other half is other channel
            double [] channel0 = new double[INPUT_BUFFER_SIZE];
            double [] channel1 = new double[INPUT_BUFFER_SIZE];
            for(int i = 0; i < INPUT_BUFFER_SIZE/SAMPLES_PER_SECOND; i++){
                for(int j = 0; j < SAMPLES_PER_SECOND; j++){
                    channel0[i*SAMPLES_PER_SECOND + j] = buffer[2*i*SAMPLES_PER_SECOND + j];
                    channel1[i*SAMPLES_PER_SECOND + j] = buffer[(2*i+1)*SAMPLES_PER_SECOND + j];
                }
            }

            for(int i = 0; i < INPUT_BUFFER_SIZE; i++){
                series.add(channel0[i],channel1[i]);
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

    public void cleanup() throws NiDaqException {
        daq.stopTask(aiTask);
        daq.clearTask(aiTask);
    }
}
