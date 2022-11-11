package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.view.View;

public class Summary extends AppCompatActivity {

    //Define UI elements
    TextView dateDisplay;
    TextView distanceDisplay;
    TextView caloriesDisplay;
    TextView timeDisplay;

    //Summary Data
    String date;
    String distance;
    String calories;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Initialize the UI elements
        dateDisplay = findViewById(R.id.dateDisplay);
        distanceDisplay = findViewById(R.id.distanceDisplay);
        caloriesDisplay = findViewById(R.id.caloriesDisplay);
        timeDisplay = findViewById(R.id.timeDisplay);

        //Create new intent instance
        Intent intent = getIntent();

        //Get the date from the homepage and display
        date = intent.getStringExtra("date");
        dateDisplay.setText(date);

        //Get the data from the homepage and display
        calories = intent.getStringExtra("calories");
        caloriesDisplay.setText(calories);

        //Get the distance from the homepage and display
        distance = intent.getStringExtra("distance");
        distanceDisplay.setText(distance);

        //Get the time from the Homepage and display
        time = intent.getStringExtra("time");
        timeDisplay.setText(time);
    }

    //Returns to the Home page
    public void doReturn(View view)
    {
        Intent MainPage = new Intent(this, MainActivity.class);
        startActivity(MainPage);
    }
}