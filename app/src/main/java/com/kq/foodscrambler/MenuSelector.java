package com.kq.foodscrambler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MenuSelector extends AppCompatActivity
{
    TinyDB menuDB;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_selector);
        getSupportActionBar().setTitle("Menu Scramble");

        final Button scrambleButton = (Button) findViewById(R.id.scrambleButton);
        final TextView restaurantName = (TextView) findViewById(R.id.foodItem);

        menuDB = new TinyDB(this);
        final ArrayList<String> menuItems = menuDB.getListString("menu");

        final Random rand = new Random(System.currentTimeMillis());
        restaurantName.setText(menuItems.get(rand.nextInt(menuItems.size())).trim());

        scrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                restaurantName.setText(menuItems.get(rand.nextInt(menuItems.size())).trim());
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
