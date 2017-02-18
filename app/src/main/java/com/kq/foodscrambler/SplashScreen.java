package com.kq.foodscrambler;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        TextView splashText = (TextView) findViewById(R.id.splashText);
        Typeface typeFace= Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/LANE.ttf");
        splashText.setTypeface(typeFace);

        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable()
        {
            public void run()
            {
                Intent selectMenu = new Intent(getApplicationContext(), SelectMenu.class);
                startActivity(selectMenu);
            }
        }, 1500);
    }

    public void onSplashPageClick(View v)
    {
        //Intent selectMenu = new Intent(getApplicationContext(), SelectMenu.class);
        //startActivity(selectMenu);
    }
}
