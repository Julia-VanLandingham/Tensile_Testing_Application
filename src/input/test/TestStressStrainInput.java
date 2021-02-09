package input.test;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;


import kirkwood.nidaq.access.NiDaq;

public class TestStressStrainInput {

    /**
     * NiDaq middle layer to call NiDaq function.
     */
    private static NiDaq daq = new NiDaq();

    private static final int inputBufferSize = 100000000;

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
            daq.createAIVoltageChannel(aiTask, physicalChan, "", Nicaiu.DAQmx_Val_Cfg_Default, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
            daq.cfgSampClkTiming(aiTask, "", 100.0, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_FiniteSamps, 8);
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

    public static void main (String [] args) throws NiDaqException, InterruptedException {
        String channel = "1:5";
        double [] out = analogInputTest(channel);
        while(out==null) {
            if (out != null) {
                System.out.println("Output is not null.");
                System.out.println("Printing the output:");
                System.out.println("*******************************************");
                for (double num : out) {
                    System.out.println(num);
                }
            } else {
                System.out.println("Output was null :(");
            }
            out = analogInputTest(channel);
        }
        if (out != null) {
            System.out.println("Output is not null.");
            System.out.println("Printing the output:");
            System.out.println("*******************************************");
            for (double num : out) {
                System.out.println(num);
            }
        } else {
            System.out.println("Output was null :(");
        }
    }
}


