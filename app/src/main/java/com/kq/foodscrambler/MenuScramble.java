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
import android.widget.ListView;

import java.util.ArrayList;

public class MenuScramble extends AppCompatActivity
{
    TinyDB menuDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_scramble);
        getSupportActionBar().setTitle("Menu Scramble");

        final ListView restList = (ListView) findViewById(R.id.foodList);
        final Button addButton = (Button) findViewById(R.id.addButton);
        final Button scrambleButton = (Button) findViewById(R.id.scrambleButton);

        menuDB = new TinyDB(this);
        final ArrayList<String> foodList = menuDB.getListString("menu");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foodList);
        restList.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View v)
            {
                AlertDialog.Builder addPrompt = new AlertDialog.Builder(v.getContext());
                addPrompt.setTitle("Add A Menu Item");

                final EditText input = new EditText(v.getContext());
                addPrompt.setView(input);
                input.setInputType(InputType.TYPE_CLASS_TEXT);

                addPrompt.setPositiveButton("Add", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        String menuItem = input.getText().toString();
                        if (!menuItem.isEmpty())
                        {
                            foodList.add(menuItem);
                            menuDB.putListString("menu", foodList);
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

        scrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (foodList.isEmpty())
                {
                    AlertDialog.Builder addPrompt = new AlertDialog.Builder(v.getContext());
                    addPrompt.setMessage("No menu items to scramble!\nAdd some food and try again!");
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
                    Intent randFood = new Intent(getApplicationContext(), MenuSelector.class);
                    startActivity(randFood);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });

        restList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id)
            {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(100).alpha(0).withEndAction(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        foodList.remove(item);
                        menuDB.putListString("menu", foodList);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }
        });
    }
}
