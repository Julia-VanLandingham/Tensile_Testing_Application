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
        if (value > EPSILON) {
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
        throw new IllegalArgumentException("Length cannot be less than or equal to zero");
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
        if (value > EPSILON) {
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
        throw new IllegalArgumentException("Force cannot be less than or equal to zero");
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
        if (value > EPSILON) {
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
        throw new IllegalArgumentException("Pressure cannot be less than or equal to zero");
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
    //Find the derivative of the data.
    public static double[] findDerivative(double [] XArray, double [] YArray){
        double[] derivative = new double[XArray.length];
        derivative[0] = (YArray[1]-YArray[0])/(XArray[1]-XArray[0]);
        for(int i = 1; i < derivative.length -1; i ++){
            //average slope from i-1 to i with slope from i to i + 1
            double slope1 = (YArray[i]-YArray[i-1])/(XArray[i]-XArray[i-1]);
            double slope2 = (YArray[i+1]-YArray[i])/(XArray[i+1]-XArray[i]);
            derivative[i] = (slope1 + slope2) *.5;
        }
        return derivative;
    }
    //Find an ArrayList of points on the graph where the slope is zero.
    public static  ArrayList<Integer> findZeros(double [] XArray, double [] YArray){
        ArrayList<Integer> zeroes = new ArrayList<>();
        double [] derivative = findDerivative(XArray, YArray);
        for(int i = 1; i < derivative.length; i++){
            if((derivative[i -1]  >= 0 && derivative[i] < 0)||(derivative[i]  >= 0 && derivative[i-1] < 0)){
                zeroes.add(i);
            }
        }
        return zeroes;
    }

    //Find the max y value of the data points.
    public static Point2D.Double findUltimatePoint(double [] XArray, double [] YArray){
        int index = 0;
        for(int i = 0; i < YArray.length; i++){
            if(YArray[index] <= YArray[i]) {
                index = i;
            }
        }
        return new Point2D.Double(XArray[index], YArray[index]);
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
    /*
     Take a moving derivative of the data
     During the linear part this will be relatively average.
     Store all these values for later use.
     When this derivative goes to zero (may need to have some tolerance)
     then we have hit the yield point.
     */
    public static Point2D.Double findYieldPoint(double [] XArray, double [] YArray, ArrayList<Integer> zeros){
        if(zeros.size() > 0){
            //index of the  point where the first time the index was zero.
            int firstZero = zeros.get(0);
            return new Point2D.Double(XArray[firstZero], YArray[firstZero]);
        }
        else{
            //should probably throw an exception
            return null;
        }
    }
    //After we found the yield point, take the average of all the derivatives from the start to the yield point
    // }NOT DONE!{
    public static double findYoungsModulus(double [] XArray, double [] YArray,ArrayList<Integer> zeros){
        int count = 0;
        int stepsFromMidpoint = 0;
        int midPoint = zeros.get(0)/2;
        double[] derivative = findDerivative(XArray,YArray);// we want the index in our derivative array.
        double currentAverage = derivative[midPoint];
        while(count < midPoint || Math.abs((derivative[midPoint + stepsFromMidpoint] - currentAverage)) < LINEAR_TOLERANCE || Math.abs((derivative[midPoint - stepsFromMidpoint] - currentAverage)) < LINEAR_TOLERANCE){
            currentAverage = (currentAverage * count) + (derivative[midPoint + stepsFromMidpoint]) + (derivative[midPoint - stepsFromMidpoint]);
            count += 2;
            currentAverage /= count;
        }
        return currentAverage;
    }
}
