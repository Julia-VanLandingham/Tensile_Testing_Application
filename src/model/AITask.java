package model;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

/**
 * This class encapsulates the NIDaq Library found here:
 * https://github.com/davekirkwood/JNI-for-NI-Drivers/tree/master/src/kirkwood/nidaq
 */
public class AITask {
    // describes specific mode the channel is in
    public enum Mode {
        DIFFERENTIAL, RSE, DEFAULT
    }

    public static final int SAMPLES_PER_SECOND = 50;
    public static final int AVERAGE_FACTOR = 5;
    public static final int UPDATES_PER_SECOND = 2;
    public static final int INPUT_BUFFER_SIZE = SAMPLES_PER_SECOND / UPDATES_PER_SECOND;
    private int channels;
    private NiDaq daq ;
    private Pointer aiTask;
    private int[] read;
    private double[] buffer;
    private double [][] data;
    private DoubleBuffer inputBuffer;
    private IntBuffer samplesPerChannelRead;
    private boolean readyToRun;
    private double[] cleanedData;

    public AITask(){
        try {
            daq = new NiDaq();
            aiTask = daq.createTask("AITask\0");
            readyToRun = false;
            cleanedData = new double [INPUT_BUFFER_SIZE/AVERAGE_FACTOR];
        }catch (NiDaqException e){
            e.printStackTrace();
        }
    }

    /**
     * Creates an Analog Input Channel with the given mode, on the given port
     * @param channelNumber the port that the National Instruments Chip will read from
     * @param channelMode the mode that the National Instruments Chip reads in (see the chip manual for more information)
     */
    public void createAIChannel(int channelNumber, Mode channelMode) throws NiDaqException{
        if(!readyToRun) {
            String channelName = "Dev1/ai" + channelNumber + "\0";

            switch (channelMode) {
                case DIFFERENTIAL:
                    daq.createAIVoltageChannel(aiTask, channelName, "\0", Nicaiu.DAQmx_Val_Diff, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
                    break;
                case RSE:
                    daq.createAIVoltageChannel(aiTask, channelName, "\0", Nicaiu.DAQmx_Val_RSE, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
                    break;
                default:
                    daq.createAIVoltageChannel(aiTask, channelName, "\0", Nicaiu.DAQmx_Val_Default, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
            }
            channels++;

        }else{
            System.err.println("AITask: Ready to run function already called.");
        }
    }

    /**
     * Should be called before starting to try and pull data and after creating Analog Input Channels
     * Sets everything up to be ready to run
     */
    public void readyToRun(){
        try {
            daq.cfgSampClkTiming(aiTask, "\0", SAMPLES_PER_SECOND, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_ContSamps, channels * INPUT_BUFFER_SIZE);
            read = new int[] {0};
            buffer = new double[channels * INPUT_BUFFER_SIZE];
            inputBuffer = DoubleBuffer.wrap(buffer);
            samplesPerChannelRead = IntBuffer.wrap(read);
            data = new double[channels][INPUT_BUFFER_SIZE];
            readyToRun = true;
        } catch (NiDaqException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called to update stored data.
     * Should be called before trying to display data
     */
    public void collectData(){
        if(readyToRun) {
            try {
                daq.readAnalogF64(aiTask, INPUT_BUFFER_SIZE, -1, -1, inputBuffer, channels * INPUT_BUFFER_SIZE, samplesPerChannelRead);
            } catch (NiDaqException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < read[0]; i++) {
                for (int j = 0; j < channels; j++) {
                    data[j][i] = inputBuffer.get();
                }
            }
            inputBuffer.clear();
        }else{
            System.err.println("AITask: Ready to run not called yet.");
        }
    }

    /**
     * Gets the data for the specified channel number
     * @param channelNumber channel number of the data you would like
     * @return a double array of data from the channel given
     */
    public double [] getChannelData(int channelNumber){
        cleanedData = new double[AVERAGE_FACTOR];
        for(int i = 0; i < AVERAGE_FACTOR; i++){
            for(int j = 0; j < AVERAGE_FACTOR; j++){
                cleanedData[i] += data[channelNumber][i * AVERAGE_FACTOR + j];
            }
            cleanedData[i] /= AVERAGE_FACTOR;
        }

        return cleanedData;
    }
}