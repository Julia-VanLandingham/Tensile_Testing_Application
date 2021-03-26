package model;

import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

/**
 * Channel Class provides abstraction of Analog Input Channel
 */
public class Channel {

    // describes specific mode the channel is in
    public enum Mode {
        DIFFERENTIAL, RSE, DEFAULT
    }

    String channelName;
    int samplesPerSecond;
    int bufferSize;
    private NiDaq daq ;
    private int mode;
    private Pointer aiTask;
    int[] read;
    private DoubleBuffer inputBuffer;
    private double[] buffer ;
    private IntBuffer samplesPerChannelRead;


    /** Constructor creates task and assigns Analog Input channel to that task and provides
     *
     * @param channelNumber number of channel (0-7) depending on the mode
     * @param samplesPerSecond rate of samples you would like to read
     * @param bufferSize number of samples you would like to read
     * @param mode has to be a Mode enum
     */
    public Channel(int channelNumber, int samplesPerSecond, int bufferSize, Mode mode, NiDaq daq){
        channelName = "Dev1/ai" + channelNumber;
        this.samplesPerSecond = samplesPerSecond;
        this.bufferSize = bufferSize;
        this.daq = daq;
        buffer = new double[bufferSize*2];

        switch (mode){
            case DIFFERENTIAL:
                this.mode = Nicaiu.DAQmx_Val_Diff;
                break;
            case RSE:
                this.mode = Nicaiu.DAQmx_Val_RSE;
                break;
            default:
                this.mode = Nicaiu.DAQmx_Val_Default;
        }

        try {
            aiTask = daq.createTask("Task" + channelNumber);

            daq.createAIVoltageChannel(aiTask, channelName, "", this.mode, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
            daq.cfgSampClkTiming(aiTask, "", this.samplesPerSecond, Nicaiu.DAQmx_Val_Rising, Nicaiu.DAQmx_Val_ContSamps, this.bufferSize);
            daq.startTask(aiTask);
            read = new int[]{0};
            inputBuffer = DoubleBuffer.wrap(buffer);
            samplesPerChannelRead = IntBuffer.wrap(read);
        }catch (NiDaqException e){
            try {
                System.out.println("Trying to stop task");
                System.out.println(e.getMessage());
                e.printStackTrace();
                daq.stopTask(aiTask);
                daq.clearTask(aiTask);
            } catch(NiDaqException e2) {}

        }
    }

    public int read(){
        try {
            daq.readAnalogF64(aiTask, -1, -1, Nicaiu.DAQmx_Val_GroupByChannel, inputBuffer, this.bufferSize * 2, samplesPerChannelRead);
        } catch (NiDaqException e) {
            e.printStackTrace();
        }
        return this.read[0];
    }

    public DoubleBuffer getBuffer(){
        return this.inputBuffer;
    }

    public void cleanup(){
        try {
            daq.stopTask(aiTask);
            daq.clearTask(aiTask);
        } catch (NiDaqException e) {
            e.printStackTrace();
        }
    }


}
