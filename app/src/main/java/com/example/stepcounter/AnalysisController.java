package com.example.stepcounter;
import java.util.Date;

public class AnalysisController
{

    public void AnalysisController()
    {

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
}
