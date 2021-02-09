package input.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import com.sun.jna.Pointer;
import kirkwood.nidaq.access.NiDaq;
import kirkwood.nidaq.access.NiDaqException;
import kirkwood.nidaq.jna.Nicaiu;

/*
David Kirkwood Sent this code to show more clearly how the library is supposed to be used
 */

public class ArrowvaleDaq implements Runnable {



    private static final int SEND_SOCK_LOCAL = 59980;

    private static final int SEND_SOCK_REMOTE = 59983;

    private static final int RECV_SOCK_LOCAL = 59982;







    /**
     * Pointer to digital-in DAQ tasks.
     */

    private Pointer diTask1 = null;
    private Pointer diTask2 = null;
    private Pointer diTask3 = null;
    private Pointer diTask4 = null;





    /**
     * NiDAQ DLL Wrapper. Used to control the National Instruments hardware
     * interface.
     */

    private static NiDaq daq;

    /**
     * Pointer to analogue-in DAQ task.
     */

    private Pointer aiTask = null;

    /**
     * Number of NI-DAQ analog lines in the DAQ devices.
     */

    private static final int DEVICE_COUNT = 16;
    /**
     * Settings array is populated by the NI-DAQ and contains the current or
     * latest known settings for all of the controllers for the cab.
     */

    private byte[] settings = new byte[DEVICE_COUNT];

    private DatagramSocket sendSocket;
    private DatagramSocket recvSocket;
    private InetAddress address;



    private byte di1;
    private byte di2;
    private byte di3;
    private byte di4;

    /**
     *
     */

    public ArrowvaleDaq() {

        super();

        try {
            sendSocket = new DatagramSocket(SEND_SOCK_LOCAL);
            recvSocket = new DatagramSocket(RECV_SOCK_LOCAL);
            recvSocket.setSoTimeout(100);
            address = InetAddress.getByName("127.0.0.1");

        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (SocketException e) {
            e.printStackTrace();
            System.exit(1);
        }
        new Thread(this).start(); // Starts DAQ
    }





    @Override

    public void run() {
        daq = new NiDaq();
        setActive(true);

        new Thread(new Runnable() {
            public void run() {
                while(isActive()) {
                    try {
                        byte[] buf = new byte[3];
                        DatagramPacket recv = new DatagramPacket(buf, buf.length);
                        recvSocket.receive(recv);
                        if(buf[0] == 'Q') {
                            setActive(false);
                        }
                    } catch (IOException e) {
//                      e.printStackTrace();
                    }
                    if(isActive()) {
                        sendResp();
                    }
                }
            }
        }).start();
        try {



            while(isActive()) {
                initAiTask();
                initDiTasks();
                try {
                    while(isActive()) {
                        aiTask();
                        di1 = diTask(diTask1);
                        di2 = diTask(diTask2);
                        di3 = diTask(diTask3);
                        di4 = diTask(diTask4);

                    }
                    closeAiTask();
                    closeDiTask();
                } catch(Exception e) {
                    if(isActive()) {
                        sendResp();
                    }
                    try { Thread.sleep(10); } catch(InterruptedException ex) {}
                }
            }
        } catch(NiDaqException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }



    private void sendResp() {
        byte[] resp = new byte[settings.length + 4];
        for(int i=0; i<settings.length; i++) {
            resp[i] = settings[i];
            resp[settings.length + 0] = di1;
            resp[settings.length + 1] = di2;
            resp[settings.length + 2] = di3;
            resp[settings.length + 3] = di4;
        }



        DatagramPacket packet = new DatagramPacket(resp, resp.length, address, SEND_SOCK_REMOTE);
        try {
            sendSocket.send(packet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    int i=0;
    private void initAiTask() throws NiDaqException {
        int tries = 0;
        while(tries++ < 10000) {
            try {
                System.out.println("Init AI attempt");
                aiTask = daq.createTask("AITask" + (i++));
                daq.createAIVoltageChannel(aiTask, "Dev1/ai0:7", "", Nicaiu.DAQmx_Val_Cfg_Default, -10.0, 10.0, Nicaiu.DAQmx_Val_Volts, null);
                daq.cfgSampClkTiming(aiTask, "", 100.0, Nicaiu.DAQmx_Val_Rising,Nicaiu.DAQmx_Val_ContSamps, 8);
                daq.startTask(aiTask);
                System.out.println("Init AI");
                return;
            } catch(NiDaqException e) {
            }
        }
        aiTask = null;
        System.exit(20);
    }





    private void initDiTasks() throws NiDaqException {
        diTask1 = initDiTask("Dev1/port0/line0:3");
        diTask2 = initDiTask("Dev2/port0/line0:7");
        diTask3 = initDiTask("Dev2/port1/line0:7");
        diTask4 = initDiTask("Dev2/port2/line0:7");
    }





    private Pointer initDiTask(String devStr) throws NiDaqException{
        Pointer diTask = null;
        int tries = 0;

        while(tries++ < 10000) {

            try {

                System.out.println("Init DI attempt");

                diTask = daq.createTask("DITask" + (i++));

                daq.createDIChan(diTask, devStr, "", Nicaiu.DAQmx_Val_ChanForAllLines);

                daq.startTask(diTask);

                System.out.println("Init DI");

                return diTask;

            } catch(NiDaqException e) {



            }

        }

        System.exit(21);

        return null;

    }



    private void closeAiTask() {



        try {

            daq.stopTask(aiTask);

        } catch (NiDaqException e) {

        }

    }





    private void closeDiTask() {

        try {

            daq.stopTask(diTask1);

        } catch (NiDaqException e) {

        }

        try {

            daq.stopTask(diTask2);

        } catch (NiDaqException e) {

        }

        try {

            daq.stopTask(diTask3);

        } catch (NiDaqException e) {

        }

        try {

            daq.stopTask(diTask4);

        } catch (NiDaqException e) {

        }

    }







    private void aiTask() throws NiDaqException {

        int inputBufferSize = 8;



        Integer read = new Integer(0);

        double[] buffer = new double[inputBufferSize];



        DoubleBuffer inputBuffer = DoubleBuffer.wrap(buffer);

        IntBuffer samplesPerChannelRead = IntBuffer.wrap(new int[] {read} );

        daq.readAnalogF64(aiTask, -1, 100.0, Nicaiu.DAQmx_Val_GroupByChannel, inputBuffer, inputBufferSize, samplesPerChannelRead);



        if(buffer[0] != 0) {

            for(int i=0; i<inputBufferSize; i++) {

                settings[i] = (byte)buffer[i];

            }

        }

    }





    private byte diTask(Pointer task) throws NiDaqException {

        int inputBufferSize = 8;

        Integer read = new Integer(0);

        double[] buffer = new double[inputBufferSize];

        DoubleBuffer inputBuffer = DoubleBuffer.wrap(buffer);

        IntBuffer samplesPerChannelRead = IntBuffer.wrap(new int[] {read} );

        daq.readDigitalF64(task, -1, 100.0, Nicaiu.DAQmx_Val_GroupByChannel, inputBuffer, inputBufferSize, samplesPerChannelRead);

        return (byte)Double.doubleToRawLongBits(buffer[0]);

    }



    private boolean active;



    /**

     * @param active

     *            the active to set

     */

    public synchronized void setActive(boolean active) {

        this.active = active;

        if(!active) {

            closeAiTask();

            closeDiTask();

            sendSocket.close();

            recvSocket.close();

        }

        System.out.print("\n Active:" + this.active);

    }





    /**

     * @return the active

     */

    public synchronized boolean isActive() {

        return active;

    }



    public static void main(String[] args) {

        new ArrowvaleDaq();

    }



}