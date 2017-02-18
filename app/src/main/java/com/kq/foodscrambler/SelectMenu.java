package com.kq.foodscrambler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SelectMenu extends AppCompatActivity
{
    boolean exit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_menu);

        getSupportActionBar().setTitle("Food Scrambler");

        final Button restScrambleButton = (Button) findViewById(R.id.restScrambleButton);
        final Button menuScrambleButton = (Button) findViewById(R.id.menuScrambleButton);
        final Button randomRestaurantButton = (Button) findViewById(R.id.randomRestaurantButton);

        restScrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                exit = false;
                Intent restScramble = new Intent(getApplicationContext(), RestaurantScramble.class);
                startActivity(restScramble);
            }
        });

        menuScrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                exit = false;
                Intent menuScramble = new Intent(getApplicationContext(), MenuScramble.class);
                startActivity(menuScramble);
            }
        });

        randomRestaurantButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                exit = false;
                Intent randRest = new Intent(getApplicationContext(), RandomRestaurant.class);
                startActivity(randRest);
            }
        });

    }


    @Override
    public void onBackPressed()
    {
        if(!exit)
        {
            exit = true;
            Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            exit = false;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
