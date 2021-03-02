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

    // Reduces Average data
    private static void cleanDataAverage(double[] input, int cleanFactor) {
        double [] tempData = new double[input.length / 100];
        int cleanedDataIndex = 0;
        double [] buffer = new double[100];
        double total = 0.0;
        for(int i = 0; i < input.length; ++i){
            int indexBuffer = i % 100;
            buffer[indexBuffer] = input[i];
            if(indexBuffer == 99){
                for(double num : buffer){
                    total += num;
                }
                tempData[cleanedDataIndex++] = total / 100.0;
                total = 0.0;
            }
        }
        cleanedData = tempData;
    }


    public static void main(String[] args) throws FileNotFoundException {
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
        double maxReading = 0.0;
        double m = 20000.0 / 10.0; // lbs over volts
        // Equation is y = mx + b
        // There is no offset since the 0 is 0
        // Offset should be set by the user when zeroing with the software
        //double x = 10.0; // 10 volts for SpecTester machine tested on 3/2/2021
        double b = 0.0321857337739997;
        double [] actualData = new double[cleanedData.length];
        int dataIndex = 0;
        PrintWriter outputFile = new PrintWriter(new FileOutputStream("actualData.txt"));
        for(double num : cleanedData){
            double y = m * num + b;
            actualData[dataIndex] = y;
            outputFile.println(y);
            dataIndex++;
            if(y > maxReading){
                maxReading = y;
            }
        }
        System.out.println("Max lbs: "+ maxReading);
        System.out.println("End of Test");

    }


}
