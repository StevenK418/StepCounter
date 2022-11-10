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

    //Summary Data
    String date;
    String distance;
    String calories;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Initialize the UI elements
        dateDisplay = findViewById(R.id.dateDisplay);
        distanceDisplay = findViewById(R.id.distanceDisplay);
        caloriesDisplay = findViewById(R.id.caloriesDisplay);

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
    }

    //Returns to the Home page
    public void doReturn(View view)
    {
        Intent MainPage = new Intent(this, MainActivity.class);
        startActivity(MainPage);
    }
}