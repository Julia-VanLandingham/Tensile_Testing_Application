package controller;

import org.jfree.data.xy.XYSeries;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Live graphs data
 */
public class GraphUpdater extends Thread {

    private XYSeries series;
    private AtomicBoolean done = new AtomicBoolean(false);
    private AtomicBoolean run = new AtomicBoolean(false);

    public GraphUpdater(XYSeries series) {
        this.series = series;
    }
    @Override
    public void run() {
        boolean keepGoing = true;
        int x = 0;
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
            series.add(x,Math.random());
            x++;
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
}
