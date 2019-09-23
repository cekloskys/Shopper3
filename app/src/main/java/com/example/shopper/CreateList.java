package com.example.shopper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateList extends AppCompatActivity {

    // declare an Intent
    Intent intent;

    // declare EditTexts
    EditText nameEditText;
    EditText storeEditText;
    EditText dateEditText;

    // declare Calendar
    Calendar calendar;

    // declare database handler
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize EditTexts
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        storeEditText = (EditText) findViewById(R.id.storeEditText);
        dateEditText = (EditText) findViewById(R.id.dateEditText);

        // initialize the Calendar
        calendar = Calendar.getInstance();

        // initialize a DatePickerDialog and register an OnDateSetListener to it
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            // this method gets called when a date is set in the DatePickerDialog
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                // set the Calendar year, month, and day to year, month, and day
                // selected in DatePickerDialog
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // call method that updates date EditText with date set in DatePickerDialog
                updateDueDate();
            }
        };

        // register OnClickListener on the date EditText
        dateEditText.setOnClickListener(new View.OnClickListener(){
            // this method gets called when the date EditText is clicked
            @Override
            public void onClick(View view) {
                // display DatePickerDialog with current date selected
                new DatePickerDialog(CreateList.this,
                        date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // initialize database handler
        dbHandler = new DBHandler(this, null);
    }

    public void updateDueDate() {

        // create a SimpleDateFormat
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        // apply SimpleDateFormat to date in Calendar and set it in the date EditText
        dateEditText.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // get the id of the item that was selected
        switch (item.getItemId()){
            case R.id.action_home :
                // initializing an Intent for the Main Activity, starting it
                // and returning true
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_create_list :
                // initializing an Intent for the Create List Activity, starting it
                // and returning true
                intent = new Intent(this, CreateList.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createList(MenuItem menuItem){

        // get data input in EditTexts and store it in Strings
        String name = nameEditText.getText().toString();
        String store = storeEditText.getText().toString();
        String date = dateEditText.getText().toString();

        // trim Strings and see if they're equal to empty Strings
        if (name.trim().equals("") || store.trim().equals("") || date.trim().equals("")){
            // if any of the Strings are empty, display Please enter ... Toast
            Toast.makeText(this, "Please enter a name, store, and date!", Toast.LENGTH_LONG).show();
        } else {
            // add shopping list to database
            dbHandler.addShoppingList(name, store, date);
            // if none of the Strings are empty, display Shopping List Added Toast
            Toast.makeText(this, "Shopping List Added!", Toast.LENGTH_LONG).show();
        }
    }

}
