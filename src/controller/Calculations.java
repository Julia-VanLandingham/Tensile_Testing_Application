package controller;

import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYSeries;

import java.util.ArrayList;
import java.util.Collections;

public class Calculations {

    //used to ensure entered values for area are not zero
    private static final double EPSILON = 0.0001;

    public enum Units{
        ENGLISH,
        METRIC
    }
    public static final double LINEAR_TOLERANCE = 0.01;
    public static final double ZERO_TOLERANCE = 0.001;

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
    //Find the max y value of the data points, Collections.max()
    public static double findUltimatePoint(XYSeries data){
        double ultimatePoint = data.getMaxY();
        return ultimatePoint;
    }
    //Find the point at which the force is 0 (may need user input)
    public static double findFailurePoint(XYSeries data){
        double failurePoint = 0.0;
        //actually find failurePoint
        return failurePoint;
    }
    /*
     Take a moving derivative of the data
     During the linear part this will be relatively average.
     Store all these values for later use.
     When this derivative goes to zero (may need to have some tolerance)
     then we have hit the yield point.
     */
    public static int findYieldPoint(XYSeries data){//should take an X array and Y array and a derivative
        int yieldPoint = 0;
        double[][] dataArray = data.toArray();
        final int X = 0;
        final int Y = 1;
        //This code finds the derivative might need to be in a separate method.
        double[] derivative = new double[dataArray[X].length];
        derivative[0] = (dataArray[Y][1]-dataArray[Y][0])/(dataArray[X][1]-dataArray[X][0]);
        for(int i = 1; i < derivative.length -1; i ++){
            //average slope from i-1 to i with slope from i to i+
            double slope1 = (dataArray[Y][i]-dataArray[Y][i-1])/(dataArray[X][i]-dataArray[X][i-1]);
            double slope2 = (dataArray[Y][i+1]-dataArray[Y][i])/(dataArray[X][i+1]-dataArray[X][i]);
            derivative[i] = (slope1 + slope2) *.5;
        }
        //Derivative found
        int last = derivative.length -1;
        derivative[last] = (dataArray[Y][last]-dataArray[Y][last-1])/(dataArray[X][last]-dataArray[X][last-1]);
        //two ways to find linear portions of graph. Best way may be determined through testing
        //1.linear portion by seeing if slope of several points are similar
        //Currently using this for this method -> 2. Or try to find points where derivative is zero
        //-zero points are: gone from positive slope to negative slope or negative slope to positive slope
        ArrayList<Integer> zeroes = new ArrayList<>();//list of zero points on graph
        for(int i = 1; i < derivative.length; i++){
            if((derivative[i -1]  >= 0 && derivative[i] < 0)||(derivative[i]  >= 0 && derivative[i-1] < 0)){
                zeroes.add(i);
            }
        }
        //finds yield point but is not a realistic estimate.
        if(zeroes.size() > 0){
            return zeroes.get(0);
        }
        else{
            return -1;
        }
    }
    //After we found the yield point, take the average of all the derivatives from the start to the yield point
    public static double findYoungsModulus(XYSeries data){
        double youngsModulus = 0.0;
        int yieldPoint = findYieldPoint(data);
        //actually find the young's modulus
        return youngsModulus;
    }
}
