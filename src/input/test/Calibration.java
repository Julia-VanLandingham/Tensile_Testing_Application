package input.test;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

/**
 * This Class creates an interactive text based calibration tool for finding the Volts to Units constant.
 */
public class Calibration {

    /**
     * NiDaq middle layer to call NiDaq function.
     */
    private static NiDaq daq = new NiDaq();

    // 5 min :      1500000
    // 2.5 min :     750000
    private static final int seconds = 3;

    private static final double samplesPerSecond = 100.0;
    private static final int inputBufferSize = (int) Math.ceil(seconds * samplesPerSecond);
    private static final int samplesInChannel = inputBufferSize;

    /**
     * Actually Calls the internal functions to get a finite amount of raw samples
     * @param channel Channel on National Instruments Device that corresponds to the device you are trying to calibrate
     * @param mode The type of signal output from the machine that needs calibrated
     * @return a double filled with raw voltage values to be processed
     * @throws NiDaqException
     * @throws InterruptedException
     */
    private static double[] analogInputTest(String channel, int mode) throws NiDaqException, InterruptedException {
        Pointer aiTask = null;
        try {
            String physicalChan = "Dev1/ai" + channel + "\0";
            aiTask = daq.createTask("AITask\0");
            System.out.println("aiTask assigned" + aiTask);
            daq.createAIVoltageChannel(aiTask, physicalChan, "\0", mode, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
            daq.cfgSampClkTiming(aiTask, "\0", samplesPerSecond, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_FiniteSamps, samplesInChannel);
            daq.startTask(aiTask);
            System.out.println("Task Started!");
            Integer read = new Integer(0);
            double[] buffer = new double[inputBufferSize];

            DoubleBuffer inputBuffer = DoubleBuffer.wrap(buffer);
            IntBuffer samplesPerChannelRead = IntBuffer.wrap(new int[] {read} );
            daq.readAnalogF64(aiTask, -1, -1, Nicaiu.DAQmx_Val_GroupByChannel, inputBuffer, inputBufferSize, samplesPerChannelRead);
            daq.stopTask(aiTask);
            daq.clearTask(aiTask);
            System.out.println("Buffer:\t" + buffer);
            return buffer;

        } catch(NiDaqException e) {
            try {
                System.out.println("Trying to stop task");
                System.out.println(e.getMessage());
                e.printStackTrace();
                daq.stopTask(aiTask);
                daq.clearTask(aiTask);
                return null;
            } catch(NiDaqException e2) {}
            throw(e);
        }


    }


    public static void main (String[] args){

    }
}
