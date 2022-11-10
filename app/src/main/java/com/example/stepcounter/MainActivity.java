package com.example.stepcounter;
import androidx.appcompat.app.AppCompatActivity;
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


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Gather Analysis Data
    //New controller Instance
    public AnalysisController ac;

    //Flags (Used for state control)
    boolean isRunning;

    int counter = 0;

    //Declare UI elements
    TextView timerDisplay;
    TextView tvMag, tvSteps;
    Button startButton;
    Button stopButton;
    Button resetButton;

    //Declare sensor instances
    private SensorManager mSensorManager;
    private Sensor mSensor;

    //Define a new Timer
    CountUpTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize the Analysis Controller instance
        ac = new AnalysisController();

        //Initialize the isRunning bool to false
        isRunning = false;

        //Initialize the UI elements
        tvMag = findViewById(R.id.tvMag);
        tvSteps = findViewById(R.id.tvSteps);
        timerDisplay = findViewById(R.id.timerDisplay);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);

        // Initialize the sensor services
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Initialize the new timer object
        timer = new CountUpTimer(300000) {
            @Override
            public void onTick(int seconds) {
                timerDisplay.setText(String.valueOf(seconds));
                //startButton.setText(String.valueOf(seconds));
            }
        };
        //Disable the stop button for now
        stopButton.setEnabled(false);
        //Disable the reset button for now
        resetButton.setEnabled(false);
    }

    //Timer control Methods
    public void doStart(View view)
    {
        //Set isRunning Flag to true
        isRunning = true;
        //Start the timer
        timer.start();

        //Enable the stop button now we are running thr routine
        stopButton.setEnabled(true);
        resetButton.setEnabled(true);
    }

    public void doStop(View view)
    {
        if(ac != null)
        {
            //Reset isRunning flag to false
            isRunning = false;

            //Cancel the timer instance
            timer.cancel();

            //Display message via toast to inform user detection has stopped.
            Toast.makeText(this, "Stopped Run", Toast.LENGTH_LONG).show();

            //Get the date:
            String currentDate = ac.GetDate().toString();
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
            //Load up the new activity
            startActivity(summaryPage);
        }
    }

    public void doReset(View view) {
        timerDisplay.setText("0");
        Toast.makeText(this, "Reset", Toast.LENGTH_LONG).show();
    }

    /*
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume() {
        super.onResume();
        // turn on the sensor
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * App running but not on screen - in the background
     */
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);    // turn off listener to save power
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(ac != null && isRunning)
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        // not used
    }


}