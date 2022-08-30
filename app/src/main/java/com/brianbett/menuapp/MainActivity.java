package com.brianbett.menuapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.brianbett.menuapp.realm.DayMenu;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    DayMenu selectedDayMenu;
    Realm realm;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.green)));


//        creating a notification channel for latest APIs i.e OREO onwards
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("My menu","My menu", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }






//        getting the current day of the week;

            String dayOfWeek= new SimpleDateFormat("EEEE", Locale.ENGLISH).format(System.currentTimeMillis());


//        initializing realm
        Realm.init(getApplicationContext());
        realm=Realm.getDefaultInstance();
        DayMenu currentDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek",dayOfWeek).findFirst();






        HomeFragment homeFragment=new HomeFragment(currentDayMenu);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
        NavigationView navigationView= findViewById(R.id.navigation_view);
        
        Menu navigationMenu=navigationView.getMenu();

        //        scheduling notifications
        scheduleNotification(MainActivity.this,100);
        Calendar calendar=Calendar.getInstance();

//        updating the navigation menu to show the current day as today
        int day_of_week=calendar.get(Calendar.DAY_OF_WEEK);
        navigationMenu.getItem(day_of_week-1).setTitle("Today");
        handleSelectedItem(navigationView);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleSelectedItem(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switchFragmentContents(item);

                return true;
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private  void switchFragmentContents(MenuItem item){
        switch (item.getItemId()){
            case R.id.Monday:
               selectedDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek","Monday").findFirst();
               break;
            case R.id.Tuesday:
                selectedDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek","Tuesday").findFirst();

                break;
            case R.id.Wednesday:
                selectedDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek","Wednesday").findFirst();
                break;
            case R.id.Thursday:
                selectedDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek","Thursday").findFirst();
                break;
            case R.id.Friday:
                selectedDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek","Friday").findFirst();
                break;
            case R.id.Saturday:
                selectedDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek","Saturday").findFirst();
                break;
            case R.id.Sunday:
                selectedDayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek","Sunday").findFirst();
                break;


        }


        HomeFragment.getSelectedDay(selectedDayMenu);
        drawerLayout.closeDrawer(GravityCompat.START);

    }

//    onPostCreate is called after the activity is started

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }

    //    called when configuration changes such as orientation change
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);

    }


    public void scheduleNotification(Context context,int notificationId) {

        Intent notificationIntent = new Intent(context, NotificationHandler.class);
        notificationIntent.putExtra(NotificationHandler.NOTIFICATION_ID, notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.add(Calendar.HOUR_OF_DAY,18);
//
        calendar.add(Calendar.MINUTE,47);
        calendar.add(Calendar.SECOND,00);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_DAY,pendingIntent);
//        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), pendingIntent);
    }


}