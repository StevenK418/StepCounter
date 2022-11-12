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
    private final double STEP_MAX = 11.6;     // upper mag limit
    private final double STEP_MIN = 8.2;      // lower mag limit
    boolean highLimit = false;      // detect high limit

    double magnitude = 0.0;

    public void AnalysisController()
    {

    }

    //Detects whether step occurred using values from Accelerometer
    public boolean DetectStep(float x, float y, float z)
    {
        //Define a boolean to store result
        boolean isStep = false;

        // Use Pythagorean theorem to get magnitude
        magnitude = round(Math.sqrt((x*x) + (y*y) + (z*z)), 2);

        //Calculate the magnitude by checking against constants
        if ((magnitude > STEP_MAX) && (highLimit == false))
        {
            highLimit = true;
            isStep = false;
        }
        else if((magnitude < STEP_MIN) && (highLimit == true))
        {
            highLimit = false;
            isStep = true;
        }

        return isStep;
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
        float meters = (float)round(steps * 0.8f, 2);
        return meters;
    }

    //Calculate calories burned
    public float GetCalories(int steps)
    {
        float caloriesBurned = (float)round(steps * 0.04f, 2);
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
