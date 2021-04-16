package controller;

public class SlidingAverage {
    private double [] queue;
    private int index = 0;
    private double total = 0.0;
    private int count = 0;

    public SlidingAverage(int size){
        queue = new double[size];
    }

    public double addData (double newPoint){
        total += newPoint;
        if(count >= queue.length){
            total -= queue[index];
        }else{
            count ++;
        }
        queue[index] = newPoint;
        index = (index + 1) % queue.length;
        return total / count;
    }
}
