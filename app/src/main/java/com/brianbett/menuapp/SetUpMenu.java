package com.brianbett.menuapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.brianbett.menuapp.realm.DayMenu;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SetUpMenu extends AppCompatActivity {
    TextView weekday;
    int counter;

    View parentView;

//    SharedPreferences to check if the user has entered details for all the days
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    AppCompatEditText breakfast1View,breakfast2View,breakfast3View,lunch1View,lunch2View,lunch3View,supper1View,supper2View,supper3View;
    MaterialButton submitButton;
    String [] weekdays;
    Realm realm;

//    background colors
    int[] backgroundColors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_menu);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //initializing the counter from the variable saved in shared preferences
        preferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedCounter=preferences.getString("counter",null);
        counter=savedCounter==null?0:Integer.parseInt(savedCounter);

        parentView=findViewById(R.id.parent_view);

        backgroundColors= new int[]{R.color.darkGreen,Color.BLUE, Color.CYAN, Color.MAGENTA, Color.rgb(239,50,140),Color.GREEN,Color.rgb(9,39,185),Color.rgb(55,240,0),Color.MAGENTA,Color.rgb(4,55,72)};

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColors[counter]));

        parentView.setBackgroundColor(backgroundColors[counter]);


        editor= preferences.edit();



        if(counter==6){
            startActivity(new Intent(SetUpMenu.this,MainActivity.class));
            finish();
        }



//        initializing realm
        Realm.init(getApplicationContext());
        //        configuring realm to allow writes on UI thread .This prevents realm exceptions when we
//        want to update the database on the UI thread
        RealmConfiguration configuration= new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
        realm=Realm.getDefaultInstance();

//        creating the string array of weekdays
        weekdays=new String[]{"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};


//        finding the views
        weekday=findViewById(R.id.day_of_week);

        //        setting the text for the weekday
        weekday.setText(weekdays[counter]);


        breakfast1View=findViewById(R.id.breakfast1);
        breakfast2View=findViewById(R.id.breakfast2);
        breakfast3View=findViewById(R.id.breakfast3);
        lunch1View=findViewById(R.id.lunch1);
        lunch2View=findViewById(R.id.lunch2);
        lunch3View=findViewById(R.id.lunch3);

        supper1View=findViewById(R.id.supper1);
        supper2View=findViewById(R.id.supper2);
        supper3View=findViewById(R.id.supper3);

        submitButton=findViewById(R.id.submit_btn);

        submitButton.setOnClickListener(view -> {
            getInputtedMenuItems();
        });
    }

//    method to save the menu items and reset the input fields so as to enable user to enter the menu for the next day

    public void getInputtedMenuItems(){
        String breakfast1,breakfast2,breakfast3,lunch1,lunch2,lunch3,supper1,supper2,supper3;
        breakfast1=((TextView)breakfast1View).getText().toString();
        breakfast2=((TextView)breakfast2View).getText().toString();
        breakfast3=((TextView)breakfast3View).getText().toString();

        lunch1=((TextView) lunch1View).getText().toString();
        lunch2=((TextView) lunch2View).getText().toString();
        lunch3=((TextView) lunch3View).getText().toString();

        supper1=((TextView)supper1View).getText().toString();
        supper2=((TextView)supper2View).getText().toString();
        supper3=((TextView)supper3View).getText().toString();

//      saving data in Realm
        realm.beginTransaction();
        DayMenu dayMenu=realm.createObject(DayMenu.class,weekdays[counter]);

        dayMenu.setBreakfast1(breakfast1);
        dayMenu.setBreakfast2(breakfast2);
        dayMenu.setBreakfast3(breakfast3);

        dayMenu.setLunch1(lunch1);
        dayMenu.setLunch2(lunch2);
        dayMenu.setLunch3(lunch3);

        dayMenu.setSupper1(supper1);
        dayMenu.setSupper2(supper2);
        dayMenu.setSupper3(supper3);
        realm.commitTransaction();




        if(counter==6){
            realm.close();
            finish();
            startActivity(new Intent(SetUpMenu.this,MainActivity.class));


        }else {

            Toast.makeText(SetUpMenu.this,weekdays[counter]+"'s menu  saved",Toast.LENGTH_SHORT).show();
            breakfast1View.setText("");
            breakfast2View.setText("");
            breakfast3View.setText("");

            lunch1View.setText("");
            lunch2View.setText("");
            lunch3View.setText("");

            supper1View.setText("");
            supper2View.setText("");
            supper3View.setText("");

//            changing the background color
            counter++;
            //            saving the current counter to sharedPreferences
            editor.putString("counter",String.valueOf(counter));
            editor.apply();

            parentView.setBackgroundColor(backgroundColors[counter]);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(backgroundColors[counter]));
            //        setting the text for the weekday
            weekday.setText(weekdays[counter]);
        }
    }

}