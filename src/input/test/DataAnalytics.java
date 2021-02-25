package input.test;

import com.sun.glass.ui.Clipboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class Used for Testing Purposes to analyze voltage data output from TestStressStrainInput.java
 *
 */
public class DataAnalytics {
    private static ArrayList <Double> dataSet;

    private static void readData(){
        dataSet = new ArrayList<Double>();
        try {
            File myObj = new File("outfile.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                double num = Double.parseDouble(data);
                dataSet.add(num);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        readData();
        double [] array = new double[dataSet.size()];
        for(int i = 0; i < dataSet.size(); i++){
            array[i] = dataSet.get(i);
        }
        System.out.println("DATA ANALYSIS");
        System.out.println("***************************************");
        int numberOfDataPoints = 0;
        double total = 0;

        for(Double num : array){
            numberOfDataPoints++;
            total += num;
        }
        System.out.println("Number of Data Points:\t\t" + numberOfDataPoints);
        System.out.println("Total:\t\t\t\t\t\t" + total);
        double average = total / (double) numberOfDataPoints;
        System.out.println("Average of Entire Dataset:\t" + average);
        System.out.println("***************************************");
        System.out.println("Testing Voltage Conversion:");
        double m = 124.0 / 0.07868153834685204; // lbs over volts
        // Equation is y = mx + b
        // There is no offset since the 0 is 0
        // Offset should be set by the user when zeroing with the software



    }
}
