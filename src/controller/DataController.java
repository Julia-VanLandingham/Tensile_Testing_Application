package controller;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;


import kirkwood.nidaq.access.NiDaq;

public class DataController {

    // NiDaq middle layer to call NiDaq function.
    private static NiDaq daq = new NiDaq();

    private static final String CHANNEL_PREPEND = "Dev1/ai";
    private static final String TASK_NAME = "AITask";
    private static final int TRIES = 10000; // Number of tries to make the input start to read
    private static final boolean DEBUG = false; // Turn to true for debugging this class
    private static final int MAX_SAMPLE_RATE = 48000; // Got from NI USB-6009



    /**
     * Gets voltage values from USB device (this was written for: NI USB-6009)
     * NOTE: In order for this class to work you need to add the libraries (in folder libs) to your IDE
     * Ignores Exceptions thrown by Driver Library
     * Uses Libraries from here to interface with National Instruments USB Device:
     * https://github.com/davekirkwood/JNI-for-NI-Drivers
     *
     * For Chip NI USB-6009 Maxiumum AI Sample Rate is 48,000 Samples per second
     *
     * @param channel    channel you would like to read from (must be formatted as the Analog Input Port #:# EX: AI0 = 0:0)
     * @param numberOfSamples    number of samples you would like to read
     * @param bufferSize size of the array you would like to read into (most cases should be same as samples)
     * @return buffer of voltage values as doubles (returns null if operation failed)
     */
    public double[] analogInputStart(String channel, int numberOfSamples, int bufferSize, int sampleRate) {
        if(sampleRate > 48000) {
            System.err.println("DataController: sampleRate exceeded MAX_SAMPLE_RATE:\t" + sampleRate);
            return null;
        }else if(sampleRate <= 0){
            System.err.println("DataController: sampleRate is beneath minimum:\t" + sampleRate);
            return null;
        }
        double[] buffer = new double[bufferSize];
        for (int i = 0; i < TRIES && buffer != null; ++i) {
            Pointer aiTask = null;
            try {
                String physicalChan = CHANNEL_PREPEND + channel;
                aiTask = daq.createTask(TASK_NAME);
                daq.createAIVoltageChannel(aiTask, physicalChan, "", Nicaiu.DAQmx_Val_Cfg_Default, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
                daq.cfgSampClkTiming(aiTask, "", sampleRate, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_FiniteSamps, numberOfSamples);
                daq.startTask(aiTask);
                Integer read = new Integer(0);
                DoubleBuffer inputBuffer = DoubleBuffer.wrap(buffer);
                IntBuffer samplesPerChannelRead = IntBuffer.wrap(new int[]{read});
                daq.readAnalogF64(aiTask, -1, -1, Nicaiu.DAQmx_Val_GroupByChannel, inputBuffer, bufferSize, samplesPerChannelRead);
                Thread.sleep(100);
                daq.stopTask(aiTask);
                daq.clearTask(aiTask);
                return buffer;

            } catch (NiDaqException | InterruptedException e) {
                try {
                    if (DEBUG) {
                        e.printStackTrace();
                        System.out.println("ERROR MESSAGE: " + e.getMessage());
                    }
                    daq.stopTask(aiTask);
                    daq.clearTask(aiTask);
                    return null;
                } catch (NiDaqException e2) {
                    if (DEBUG) {
                        e2.printStackTrace();
                        System.out.println("ERROR MESSAGE: " + e.getMessage());
                    }
                }
            }
        }
        if(DEBUG){
            System.out.println("DATA_CONTROLLER: Debug is enabled:");
            if(buffer!= null){
                System.out.println("Output is not null...");
                System.out.println("Printing the output:");
                System.out.println("*******************************************");
                int nonZero = 0;
                for (double num : buffer) {
                    System.out.println(num);
                    if(num != 0.0) ++nonZero;
                }
                System.out.println("This many non-zero numbers:\t" + nonZero);
            }else{
                System.err.println("DATA_CONTROLLER: Buffer is null");
            }

        }
        return null;
    }

    // TODO: signal from machines is often noisy, this function should take a array of raw data and give back the averaged data set
    // Possibly use a mapping function to return an array or we could just do it inline and take the small samples of data
    // and just use the average of that small sample as one point.
    private double[] signalConditioner (double [] data){
        return null;
    }

    private double [] voltageConversion(double [] rawVoltageData){
        return null;
        /*
        zeroing the machine also zeros the voltage I believe

         */
    }

}
