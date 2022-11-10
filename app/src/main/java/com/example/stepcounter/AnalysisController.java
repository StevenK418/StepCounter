/*
* Class containing all business logic as used by the app.
* Used to make code more modular, and testable by separating
* concerns of logic versus UI.
*/
package com.example.stepcounter;
import java.util.Date;

public class AnalysisController
{

    // experimental values for hi and lo magnitude limits
    private final double HI_STEP = 11.0;     // upper mag limit
    private final double LO_STEP = 8.0;      // lower mag limit
    boolean highLimit = false;      // detect high limit

    double magnitude = 0.0;

    public void AnalysisController()
    {

    }

    //Detects whether step occurred using values from Accelerometer
    public boolean DetectStep(float x, float y, float z)
    {
        // get a magnitude number using Pythagorus's Theorem
        magnitude = round(Math.sqrt((x*x) + (y*y) + (z*z)), 2);

        //Calculate the magnitude
        if ((magnitude > HI_STEP) && (highLimit == false))
        {
            highLimit = true;
        }
        if ((magnitude < LO_STEP) && (highLimit == true))
        {
            highLimit = false;
        }

        return highLimit;
    }


    //Returns the current Date of the run
    public Date GetDate()
    {
        //Get current date
        Date date = new Date();
        return date;
    }

    //Calculates meters covered
    public float GetDistance(int steps)
    {
        float meters = steps * 0.8f;
        return meters;
    }

    //Calculate calories burned
    public float GetCalories(int steps)
    {
        float caloriesBurned = steps * 0.04f;
        return caloriesBurned;
    }

    //Rounding method used to round values
    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
