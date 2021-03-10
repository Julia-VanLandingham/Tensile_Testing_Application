package controller;

import org.jfree.data.xy.XYSeries;

/**
 * Live graphs data
 */
public class GraphUpdater extends Thread {

    private XYSeries series;
    private  transient boolean done = false;

    public GraphUpdater(XYSeries series) {
        this.series = series;
    }
    @Override
    public void run() {
        boolean keepGoing = true;
        int x = 0;
        while(keepGoing) {//call code from class that gets data from the chip.
            series.add(x,Math.random());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                keepGoing = false;
            }
            synchronized (this){
                if(done) {
                    keepGoing = false;
                }
            }
            x++;
        }
    }
    public synchronized void terminate() {
        done = true;
    }
}
