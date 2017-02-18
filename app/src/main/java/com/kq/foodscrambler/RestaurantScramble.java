package com.kq.foodscrambler;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;


import java.util.ArrayList;

public class RestaurantScramble extends AppCompatActivity
{
    TinyDB restaurantDB;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_scramble);
        getSupportActionBar().setTitle("Restaurant Scramble");

        final ListView restList = (ListView) findViewById(R.id.restaurantList);
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button scrambleButton = (Button) findViewById(R.id.scrambleButton);

        restaurantDB = new TinyDB(this);
        final ArrayList<String> restaurantList = restaurantDB.getListString("restaurants");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, restaurantList);
        restList.setAdapter(adapter);

        // Add restaurant
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                AlertDialog.Builder addPrompt = new AlertDialog.Builder(v.getContext());
                addPrompt.setTitle("Add A Restaurant");
                final EditText input = new EditText(v.getContext());
                addPrompt.setView(input);
                input.setLayoutParams(new LinearLayout.LayoutParams(50, 30));
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                addPrompt.setPositiveButton("Add", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String restaurantName = input.getText().toString();
                        if (!restaurantName.isEmpty())
                        {
                            restaurantList.add(restaurantName);
                            restaurantDB.putListString("restaurants", restaurantList);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                addPrompt.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // do nothing
                    }
                });

                AlertDialog dialog = addPrompt.create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener()
                {
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        InputMethodManager imm = (InputMethodManager) getSystemService(v.getContext().INPUT_METHOD_SERVICE);
                        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
                dialog.show();
            }
        });

        // Send to scramble activity
        scrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (restaurantList.isEmpty())
                {
                    AlertDialog.Builder addPrompt = new AlertDialog.Builder(v.getContext());
                    addPrompt.setMessage("No restaurants to scramble!\nAdd some restaurants and try again!");
                    addPrompt.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            // do nothing
                        }
                    });
                    addPrompt.show();
                }
                else
                {
                    Intent randRest = new Intent(getApplicationContext(), RestaurantSelector.class);
                    startActivity(randRest);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });

        // Remove item from list when tapped on
        restList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View v,
                                    final int position, long id)
            {
                final String item = (String) parent.getItemAtPosition(position);
                v.animate().setDuration(100).alpha(0).withEndAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        restaurantList.remove(item);
                        restaurantDB.putListString("restaurants", restaurantList);
                        adapter.notifyDataSetChanged();
                        v.setAlpha(1);
                    }
                });
            }
        });

    }


}
