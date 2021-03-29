package model;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

public class AITask {
    // describes specific mode the channel is in
    public enum Mode {
        DIFFERENTIAL, RSE, DEFAULT
    }

    private static final int INPUT_BUFFER_SIZE = 1000;
    private static final int SAMPLES_PER_SECOND = 100;
    String channelName;
    int samplesPerSecond;
    int bufferSize;
    private NiDaq daq ;
    private Pointer aiTask;
    int[] read;
    private DoubleBuffer inputBuffer;
    private double[] buffer ;
    private IntBuffer samplesPerChannelRead;

    public AITask(){
        try {
            daq = new NiDaq();
            aiTask = daq.createTask("AITask");


        }catch (NiDaqException e){
            e.printStackTrace();
        }
    }

    public void createAIChannel(int channelNumber, Mode channelMode){
        String channelName = "Dev1/ai" + channelNumber;
        try {
            switch (channelMode) {
                case DIFFERENTIAL:
                    daq.createAIVoltageChannel(aiTask, channelName, "", Nicaiu.DAQmx_Val_Diff, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
                    this.bufferSize += INPUT_BUFFER_SIZE;
                    break;
                case RSE:
                    daq.createAIVoltageChannel(aiTask, channelName, "", Nicaiu.DAQmx_Val_RSE, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
                    this.bufferSize += INPUT_BUFFER_SIZE;
                    break;
                default:
                    daq.createAIVoltageChannel(aiTask, channelName, "", Nicaiu.DAQmx_Val_Default, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
            }

        }catch (NiDaqException e){
            e.printStackTrace();
        }
    }

    public void readyToRun(){
        try {
            daq.cfgSampClkTiming(aiTask, "", SAMPLES_PER_SECOND, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_ContSamps, this.bufferSize);
            read = new int[] {new Integer(0)};
            double[] buffer = new double[bufferSize];
            inputBuffer = DoubleBuffer.wrap(buffer);
            samplesPerChannelRead = IntBuffer.wrap(read);
        } catch (NiDaqException e) {
            e.printStackTrace();
        }
    }

    public double[][] getValues (){
        try{
            daq.readAnalogF64(aiTask, SAMPLES_PER_SECOND, -1, -1, inputBuffer, bufferSize, samplesPerChannelRead);
        } catch (NiDaqException e) {
            e.printStackTrace();
        }
        double [][] data = new double[2][INPUT_BUFFER_SIZE];
        for(int i = 0; i < INPUT_BUFFER_SIZE; i += 2){
            data[0][i] =  inputBuffer.get();
            data[1][i] =  inputBuffer.get();
        }
        return data;
    }
}
