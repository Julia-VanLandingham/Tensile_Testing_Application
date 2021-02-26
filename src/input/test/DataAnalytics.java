package input.test;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class Used for Testing Purposes to analyze voltage data output from TestStressStrainInput.java
 *
 */
public class DataAnalytics {
    private static ArrayList <Double> dataSet;
    private static double[] cleanedData;


    private static void readData(){
        dataSet = new ArrayList<Double>();
        try {
            File myObj = new File("D:\\5_min-1.txt");
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

    // Reduces Average data
    private static void cleanDataAverage(double[] input, int cleanFactor) {
        int cleanedDataIndex = 0;
        cleanedData = new double[input.length / cleanFactor];
        double total = 0;
        for(int i = 0; i < input.length; ++i){
            int indexBuffer = i % cleanFactor;
            double [] buffer = new double[input.length];
            buffer[indexBuffer] = input[i];
            if(indexBuffer == (cleanFactor - 1)){
                for(double num : buffer){
                    total += num;
                }
                cleanedData[cleanedDataIndex++] = total / 100.0;
                total = 0.0;
            }
        }

    }

    private static void cleanDataOutliers(double[] input, int cleanFactor) {
        int cleanedDataIndex = 0;
        cleanedData = new double[input.length/cleanFactor];
        double total = 0;
        for(int i = 0; i < input.length; ++i){
            int indexBuffer = i % cleanFactor;
            double [] buffer = new double[cleanFactor];
            buffer[indexBuffer] = input[i];
            if(indexBuffer == (cleanFactor - 1)){
                for(double num : buffer){
                    total += num;
                }
                double averageOfSet = total / 100.0;
                double upperBound = averageOfSet + 0.01;
                double lowerBound = averageOfSet - 0.01;
                for(double num: buffer){
                    if(!(num > upperBound | num < lowerBound)){
                        cleanedData[cleanedDataIndex / cleanFactor] = num;
                    }else{
                        cleanedData[cleanedDataIndex / cleanFactor] = averageOfSet;
                    }
                    cleanedDataIndex++;
                }

                total = 0.0;
            }
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
        System.out.println("Cleaning data...");
        cleanDataAverage(array, 100);
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
        System.out.println("Output Cleaned Data.");
        System.out.println("***************************************");

        System.out.println("Testing Voltage Conversion:");
        double m = 124.0 / 0.07868153834685204; // lbs over volts
        // Equation is y = mx + b
        // There is no offset since the 0 is 0
        // Offset should be set by the user when zeroing with the software



    }


}
