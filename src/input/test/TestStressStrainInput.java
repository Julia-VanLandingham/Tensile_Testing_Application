package input.test;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

import java.io.*;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


import kirkwood.nidaq.access.NiDaq;

public class TestStressStrainInput {

    /**
     * NiDaq middle layer to call NiDaq function.
     */
    private static NiDaq daq = new NiDaq();

    private static final int inputBufferSize = 480000;
    private static final double samplesPerSecond = 48000.0;
    private static final int samplesInChannel = inputBufferSize;

    /**
     * Tests the Analog inputs on the DAQ National Instruments 6009 Chip for the given channel pair
     * @param channel must be formatted in "pinNumber1:pinNumber2" EX: "1:2"
     * @return Output in volts?
     * @throws NiDaqException
     */
    private static double[] analogInputTest(String channel) throws NiDaqException, InterruptedException {
        Pointer aiTask = null;
        try {
            String physicalChan = "Dev1/ai" + channel;
            aiTask = daq.createTask("AITask");
            System.out.println("aiTask assigned" + aiTask);
            daq.createAIVoltageChannel(aiTask, physicalChan, "", Nicaiu.DAQmx_Val_RSE, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
            daq.cfgSampClkTiming(aiTask, "", samplesPerSecond, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_FiniteSamps, samplesInChannel);
            daq.startTask(aiTask);
            System.out.println("Task Started!");
            Integer read = new Integer(0);
            double[] buffer = new double[inputBufferSize];

            DoubleBuffer inputBuffer = DoubleBuffer.wrap(buffer);
            IntBuffer samplesPerChannelRead = IntBuffer.wrap(new int[] {read} );
            daq.readAnalogF64(aiTask, -1, -1, Nicaiu.DAQmx_Val_GroupByChannel, inputBuffer, inputBufferSize, samplesPerChannelRead);
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Waited a second...");
            daq.stopTask(aiTask);
            daq.clearTask(aiTask);
            System.out.println("Buffer:\t" + buffer);
            return buffer;

        } catch(NiDaqException | InterruptedException e) {
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

    private static double[] convertToLbs (double [] input){
        if(input == null) return null;
        double [] output = new double[input.length];
        for(int i = 0; i < input.length; i++){
            //Do Conversion Here
        }
        return output;
    }

    public static void main (String [] args) throws NiDaqException, InterruptedException {
        String channel = "0:0";
        double [] out = null;
        while(out==null) {
            out = analogInputTest(channel);
            Thread.sleep(100);
        }
        System.out.println("Output is not null.");
        System.out.println("Printing the output:");
        System.out.println("*******************************************");
        /*
        int nonZero = 0;
        for (double num : out) {
            System.out.println(num);
            if(num != 0.0) ++nonZero;
        }
        System.out.println("This many non-zero numbers:\t" + nonZero);
        */
        double total = 0.0;
        try {
            PrintWriter outputFile = new PrintWriter(new FileOutputStream("outfile.txt"));
            for(double num : out){
                outputFile.println(num);
                num += total;
            }
            outputFile.close();
            System.out.println("File created successfully");
        }
        catch ( IOException e) {
        }
        System.out.println("Average of output is: " + (total / (double) out.length));
        double [] cleanedData = new double[out.length / 100];
        int cleanedDataIndex = 0;
        double [] buffer = new double[100];
        total = 0.0;
        for(int i = 0; i < out.length; ++i){
            int indexBuffer = i % 100;
            buffer[indexBuffer] = out[i];
            if(indexBuffer == 99){
                for(double num : buffer){
                    total += num;
                }
                cleanedData[cleanedDataIndex++] = total / 100.0;
                total = 0.0;
            }
        }
        System.out.println("CLEANED DATA:\t" + cleanedData.length + " values");
        System.out.println("Output to outfile_cleaned.txt");
        try {
            PrintWriter outputFile = new PrintWriter(new FileOutputStream("outfile_cleaned.txt"));
            for(double num : cleanedData){
                outputFile.println(num);
            }
            outputFile.close();
            System.out.println("File created successfully");
        }
        catch ( IOException e) {
        }
    }


}


