package controller;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class Calculations {

    //used to ensure entered values for area are not zero
    private static final double EPSILON = 0.0001;

    public enum Units{
        ENGLISH,
        METRIC
    }
    public static final double LINEAR_TOLERANCE = 1;

    /**
     * Converts length value from one unit system to a different unit system.
     * Note that in the english system length is measured in inches
     * and in the metric system length is measured in millimeters.
     * @param startingUnits the current unit system of the passed value
     * @param endingUnits the unit system wanted
     * @param value the current length value in the starting unit system
     * @return the length value converted to the ending unit system
     */
    public static double convertLength(Units startingUnits, Units endingUnits, double value){
        switch (startingUnits) {
            case ENGLISH:
            default :
                switch (endingUnits) {
                    case METRIC:
                        return value * 25.4;//inches to mm
                    case ENGLISH:
                    default:
                        return value;
                }
            case METRIC:
                switch (endingUnits) {
                    case METRIC:
                    default:
                        return value;
                    case ENGLISH:
                        return value / 25.4;//mm to inches
                }
        }
    }

    /**
     * Converts force value from one unit system to a different unit system.
     * Note that in the english system force is measured in pounds
     * and in the metric system force is measured in newtons.
     * @param startingUnits the current unit system of the passed value
     * @param endingUnits the unit system wanted
     * @param value the current force value in the starting unit system
     * @return the force value converted to the ending unit system
     */
    public static double convertForce(Units startingUnits, Units endingUnits, double value){
        switch (startingUnits) {
            case ENGLISH:
            default :
                switch (endingUnits) {
                    case METRIC:
                        return value * 4.4482216;//pounds to newtons
                    case ENGLISH:
                    default :
                        return value;
                }
            case METRIC:
                switch (endingUnits) {
                    case METRIC:
                    default :
                        return value;
                    case ENGLISH:
                        return value / 4.4482216;//newtons to pounds
                }
        }
    }

    /**
     * Converts pressure value from one unit system to a different unit system.
     * Note that in the english system pressure is measured in KSI (Kip/squared inches - kip = 1,000 pounds)
     * and in the metric system pressure is measured in megapascals (1,000,000 pascals - newtons/squared meters).
     * @param startingUnits the current unit system of the passed value
     * @param endingUnits the unit system wanted
     * @param value the current pressure value in the starting unit system
     * @return the pressure value converted to the ending unit system
     */
    public static double convertPressure(Units startingUnits, Units endingUnits, double value){
        switch (startingUnits) {
            case ENGLISH:
            default :
                switch (endingUnits) {
                    case METRIC:
                        return value * 6.89475728 ;//KSI to MPa
                    case ENGLISH:
                    default :
                        return value;
                }
            case METRIC:
                switch (endingUnits) {
                    case METRIC:
                    default :
                        return value;
                    case ENGLISH:
                        return value / 6.89475728 ;//MPa to KSI
                }
        }
    }

    /**
     * Calculate strain
     * @param elongation elongation measured
     * @param gaugeLength the gauge length
     * @return strain
     */
    public static double calculateStrain(double elongation, double gaugeLength){
        return elongation/gaugeLength;
    }

    /**
     * Calculate stress
     * @param force force measured
     * @param area area calculated
     * @return stress
     */
    public static double calculateStress(double force, double area){
        return force/area;
    }

    /**
     * Calculate the area of the cross section for a rectangular piece
     * @param width width of the cross section
     * @param depth depth of the cross section
     * @return area
     */
    public static double calculateArea(double width, double depth){
        if (width <= EPSILON){
            throw new IllegalArgumentException("Width cannot be zero");
        }else if(depth <= EPSILON){
            throw new IllegalArgumentException("Depth cannot be zero");
        }
        return width*depth;
    }

    /**
     * Calculate the area of the cross section for a circular piece
     * @param diameter diameter of the cross section
     * @return area
     */
    public static double calculateArea(double diameter){
        if (diameter <= EPSILON){
            throw new IllegalArgumentException("Diameter cannot be zero");
        }
        return Math.PI * (diameter/2) * (diameter/2);
    }
    /**
     * Find the derivative of the  graphed data
     * @param xArray the x axis values of our graphed data
     * @param yArray the y axis values of our graphed data
     * @return an array of doubles containing the derivatives of our data
     */

    public static double[] findDerivative(double [] xArray, double [] yArray){
        double[] derivative = new double[xArray.length];
        derivative[0] = (yArray[1]-yArray[0])/(xArray[1]-xArray[0]);
        for(int i = 1; i < derivative.length -1; i ++){
            //average slope from i-1 to i with slope from i to i + 1
            double slope1 = (yArray[i]-yArray[i-1])/(xArray[i]-xArray[i-1]);
            double slope2 = (yArray[i+1]-yArray[i])/(xArray[i+1]-xArray[i]);
            derivative[i] = (slope1 + slope2) *.5;
        }
        return derivative;
    }

    /**
     * Find the points on the graph where the slope is zero
     * @param xArray the x axis values of our graphed data
     * @param yArray the y axis values of our graphed data
     * @return an ArrayList of indexes with a slope of zero
     */
    public static  ArrayList<Integer> findZeros(double [] xArray, double [] yArray){
        ArrayList<Integer> zeroes = new ArrayList<>();
        double [] derivative = findDerivative(xArray, yArray);
        for(int i = 1; i < derivative.length; i++){
            if((derivative[i -1]  >= 0 && derivative[i] < 0)||(derivative[i]  >= 0 && derivative[i-1] < 0)){
                zeroes.add(i);
            }
        }
        return zeroes;
    }

    /**
     * Find the max y value of the data points.
     * @param xArray the x axis values of our graphed data
     * @param yArray the y axis values of our graphed data
     * @return The point on the graph with the largest Y value as an X and Y coordinate
     */
    public static Point2D.Double findUltimatePoint(double [] xArray, double [] yArray){
        int index = 0;
        for(int i = 0; i < yArray.length; i++){
            if(yArray[index] <= yArray[i]) {
                index = i;
            }
        }
        return new Point2D.Double(xArray[index], yArray[index]);
    }
    //Find the point at which the force is 0 (may need user input).
    // Need to view  sample data to properly implement }NOT DONE!{
    public static double findFailurePoint(XYDataItem [] dataSection){
        double failurePoint = 0.0;
        double currentX = 0.0;
        double currentY = 0.0;
        for(XYDataItem current : dataSection){
            currentX = current.getXValue();
            currentY = current.getYValue();
            if(currentX == 0.0){
                failurePoint = currentX;
                return failurePoint;
            }
        }
        return -1;
    }

    /**
     * Finds the yield point on the graphed data
     * @param xArray the x axis values of our graphed data
     * @param yArray the y axis values of our graphed data
     * @param zeros an ArrayList of indexes of linear points on the graph
     * @return the point on the graph that represents the yield point as an X and Y coordinate
     */
    public static Point2D.Double findYieldPoint(double [] xArray, double [] yArray, ArrayList<Integer> zeros){
        if(zeros.size() > 0){
            //index of the  point where the first time the index was zero.
            int firstZero = zeros.get(0);
            return new Point2D.Double(xArray[firstZero], yArray[firstZero]);
        }
        else{
            //should probably throw an exception
            return null;
        }
    }

    /**
     * finds the Young's modulus of the graphed data
     * @param xArray the x axis values of our graphed data
     * @param yArray the y axis values of our graphed data
     * @param derivative an array of doubles containing the derivatives of our data
     * @param zeros an ArrayList of indexes of linear points on the graph
     * @return a double that is the average of the derivatives
     */
    public static double findYoungsModulus(double [] xArray, double [] yArray,double [] derivative,ArrayList<Integer> zeros){
        //find length of interval and cut of ten percent of interval- start at first ten percent and then go until length minus 1
        //cut off some end of the values and average those
        int start = derivative.length/10;
        double sum = 0.0;
        int count = 0;
        //take middle 80%
        for(int i = start; i < derivative.length - start; i++){
            sum+= derivative[i];
            count++;
        }
        return sum/count;
    }
}
