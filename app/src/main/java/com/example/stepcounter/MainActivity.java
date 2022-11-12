package com.example.stepcounter;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Gather Analysis Data
    //New controller Instance
    public AnalysisController ac;

    int counter = 0;

    //Declare UI elements
    TextView timerDisplay;
    TextView tvMag, tvSteps;
    TextView caloriesDisplay;
    TextView distanceDisplay;
    Button startButton;
    Button stopButton;
    Button resetButton;

    //Declare sensor instances
    private SensorManager mSensorManager;
    private Sensor mSensor;

    // Time variables used by timer
    int seconds = 0;
    String time = "";

    // Is the timer currently running?
    boolean running;
    boolean wasRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the Analysis Controller instance
        ac = new AnalysisController();

        //Initialize the running bool to false
        running = false;

        //Initialize the UI elements
        tvMag = findViewById(R.id.tvMag);
        tvSteps = findViewById(R.id.tvSteps);
        timerDisplay = findViewById(R.id.timerDisplay);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);
        caloriesDisplay = findViewById(R.id.calorieDisplay);
        distanceDisplay = findViewById(R.id.homedistanceDisplay);

        // Initialize the sensor services
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Disable the stop button for now
        stopButton.setEnabled(false);
        // Disable the reset button for now
        resetButton.setEnabled(false);

        if (savedInstanceState != null)
        {
            // Get the previous state of the stopwatch
            // if the activity has been
            // destroyed and recreated.
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        //Run the timer routine
        runTimer();
    }

    // Save the current
    // state of the stopwatch if it's getting destroyed
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        savedInstanceState.putBoolean("wasRunning", wasRunning);
        super.onSaveInstanceState(savedInstanceState);
    }

    // Sets the NUmber of seconds on the timer.
    // The runTimer() method uses a Handler
    // to increment the seconds and
    // update the text view.
    private void runTimer()
    {
        // Get the text view.
        final TextView timeView = (TextView)findViewById(R.id.timerDisplay);

        // Create a new Handler
        final Handler handler = new Handler();

        // Call the post() method which runs on a delay.
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                //Divide up the time values accordingly
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                //Get the time as a formatted string of Hours, Mins and Seconds.
                time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);

                // Set the text view text.
                timeView.setText(time);

                //Update the calories & Distance
                DisplayCalories();
                DisplayDistance();

                //If active, increment the seconds
                if (running)
                {
                    seconds++;
                }

                // Run code once more with one second delay.
                handler.postDelayed(this, 1000);
            }
        });
    }

    //Timer control Methods
    public void doStart(View view)
    {
        if(!running)
        {
            //Set running Flag to true
            running = true;
            startButton.setText("PAUSE");
            //Enable the stop button now we are running thr routine
            stopButton.setEnabled(true);
            resetButton.setEnabled(true);
        }
        else
        {
            Pause();
            startButton.setText("RESUME");
            //Disable the stop button now we are running thr routine
            stopButton.setEnabled(false);
            resetButton.setEnabled(false);
        }
    }

    public void doStop(View view)
    {
        if(ac != null)
        {
            //Reset Running flag to false
            running = false;

            //Get the date and format it
            //Format Date
            SimpleDateFormat DateFor = new SimpleDateFormat("MM/dd/yyyy");
            //Feed the raw date value into the format method and store
            String currentDate = DateFor.format(ac.GetDate());
            //Get the meters travelled
            String distance = String.valueOf(ac.GetDistance(counter));
            //Get the calories burned
            String calories = String.valueOf(ac.GetCalories(counter));

            //Create new intent instance for the Summary Page
            Intent summaryPage = new Intent(this, Summary.class);
            //Pass all the analysis data to the new intent
            summaryPage.putExtra("date", currentDate);
            summaryPage.putExtra("distance", distance);
            summaryPage.putExtra("calories", calories);
            //Lastly, pass over the time
            summaryPage.putExtra("time", time);
            //Load up the new activity
            startActivity(summaryPage);
        }
    }

    //Manual pause mode allowing the app to be paused.
    public void Pause()
    {
        wasRunning = running;
        running = false;
    }

    public void doReset(View view)
    {
        running = false;
        seconds = 0;
    }

    // When the app is in focus
    protected void onResume()
    {
        super.onResume();
        // Reactivate the sensor
        mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    //When app is running but not in focus
    protected void onPause()
    {
        super.onPause();
        //Deactivate the sensor (Optimization)
        mSensorManager.unregisterListener(this);    // turn off listener to save power
        wasRunning = running;
        running = false;
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(ac != null && running)
        {
            //Display the magnitude of the step
            tvMag.setText(String.valueOf(ac.magnitude));

            //Pass the values of the 3 Axes to the step detect method
            boolean isStep =  ac.DetectStep(event.values[0],event.values[1],event.values[2]);
            if(isStep)
            {
                counter++;
                tvSteps.setText(String.valueOf(counter));
            }
        }
    }

    //Displays the Calories on the Main Page
    public void DisplayCalories()
    {
        String calories = String.valueOf(ac.GetCalories(counter));
        caloriesDisplay.setText(calories);
    }

    //Displays the Distance on the main page
    public void DisplayDistance()
    {
        String distance = String.valueOf(ac.GetDistance(counter));
        distanceDisplay.setText(distance);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // not used
    }
}