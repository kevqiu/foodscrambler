package com.kq.foodscrambler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class RestaurantSelector extends AppCompatActivity
{
    TinyDB restaurantDB;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_selector);
        getSupportActionBar().setTitle("Restaurant Scramble");

        final Button scrambleButton = (Button) findViewById(R.id.scrambleButton);
        final TextView restaurantName = (TextView) findViewById(R.id.restaurantName);

        restaurantDB = new TinyDB(this);
        final ArrayList<String> restaurantList = restaurantDB.getListString("restaurants");

        final Random rand = new Random(System.currentTimeMillis());
        restaurantName.setText(restaurantList.get(rand.nextInt(restaurantList.size())).trim());

        scrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restaurantName.setText(restaurantList.get(rand.nextInt(restaurantList.size())).trim());
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
