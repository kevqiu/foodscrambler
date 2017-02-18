package com.kq.foodscrambler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class LocationSelector extends AppCompatActivity
{
    //private GooglePlaces googlePlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_selector);
        getSupportActionBar().setTitle("Random Restaurant");

        final Button scrambleButton = (Button) findViewById(R.id.scrambleButton);
        final Random rand = new Random(System.currentTimeMillis());



        scrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
