package com.brianbett.menuapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.brianbett.menuapp.realm.DayMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;


public class HomeFragment extends Fragment {


    static DayMenu selectedDayMenu;
    public HomeFragment(DayMenu todayMenu) {
       selectedDayMenu=todayMenu;

    }

    @SuppressLint("StaticFieldLeak")
    private static TextView breakfast,lunch,supper,dayOfWeek;
    private TextView currentTimeView;
    
    private Realm realm;

    private RealmChangeListener<DayMenu> changeListener;

    PopupWindow popupWindow;


//    views for the edit menu popup
    AppCompatEditText breakfast1View,breakfast2View,breakfast3View,lunch1View,lunch2View,lunch3View,supper1View,supper2View,supper3View;
    TextView popupDayOfWeek;
    View popupView;
    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        //        configuring realm to allow writes on UI thread .This prevents realm exceptions when we
//        want to update the database on the UI thread
        RealmConfiguration configuration= new RealmConfiguration.Builder()
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
//        initializing realm
        realm=Realm.getDefaultInstance();




//        inflating the popupView

        popupView=inflater.inflate(R.layout.edit_menu_popup,null);

//        finding the popupWindow views

        popupDayOfWeek=popupView.findViewById(R.id.edit_day);
        breakfast1View=popupView.findViewById(R.id.edit_breakfast1);
        breakfast2View=popupView.findViewById(R.id.edit_breakfast2);
        breakfast3View=popupView.findViewById(R.id.edit_breakfast3);

        lunch1View=popupView.findViewById(R.id.edit_lunch1);
        lunch2View=popupView.findViewById(R.id.edit_lunch2);
        lunch3View=popupView.findViewById(R.id.edit_lunch3);

        supper1View=popupView.findViewById(R.id.edit_supper1);
        supper2View=popupView.findViewById(R.id.edit_supper2);
        supper3View=popupView.findViewById(R.id.edit_supper3);

//        finding the views

        breakfast= rootView.findViewById(R.id.breakfast);
        lunch= rootView.findViewById(R.id.lunch);
        supper= rootView.findViewById(R.id.supper);


        currentTimeView= rootView.findViewById(R.id.current_time);
        dayOfWeek= rootView.findViewById(R.id.day);

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.editMenu);

//        showing popup when fab is clicked
        floatingActionButton.setOnClickListener((this::showPopup));

        //setting the default displayed menu as the current day menu
        String breakfastMeal= selectedDayMenu.getBreakfast1()+"  "+selectedDayMenu.getBreakfast2()+"  "+selectedDayMenu.getBreakfast3();
        String lunchMeal= selectedDayMenu.getLunch1()+"  "+ selectedDayMenu.getLunch2()+"  "+selectedDayMenu.getLunch3();
        String supperMeal= selectedDayMenu.getSupper1()+"  "+selectedDayMenu.getSupper2()+"  "+ selectedDayMenu.getSupper3();

        breakfast.setText(breakfastMeal);
        lunch.setText(lunchMeal);
        supper.setText(supperMeal);
        dayOfWeek.setText("Today's menu");

        //    adding an onchange listener'
        changeListener= dayMenu -> {
            selectedDayMenu=dayMenu;
            String breakfastMeal1 = selectedDayMenu.getBreakfast1()+"  "+selectedDayMenu.getBreakfast2()+"  "+selectedDayMenu.getBreakfast3();
            String lunchMeal1 = selectedDayMenu.getLunch1()+"  "+ selectedDayMenu.getLunch2()+"  "+selectedDayMenu.getLunch3();
            String supperMeal1 = selectedDayMenu.getSupper1()+"  "+selectedDayMenu.getSupper2()+"  "+ selectedDayMenu.getSupper3();

            breakfast.setText(breakfastMeal1);
            lunch.setText(lunchMeal1);
            supper.setText(supperMeal1);
            dayOfWeek.setText("Today's menu");
        };



//        setting up a timer to get the time each second
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getCurrentDateTime();
            }
        },0,1000);

        return rootView;
    }

    public void getCurrentDateTime(){
        TimeZone timezone= TimeZone.getDefault();


        GregorianCalendar calendar=new GregorianCalendar(timezone);

        String currentTime=calendar.getTime().toLocaleString();

//        SimpleDateFormat dateFormat=new SimpleDateFormat("")
        currentTimeView.setText(currentTime);
    }
    @SuppressLint("SetTextI18n")
    public static void getSelectedDay(DayMenu dayMenu){

        selectedDayMenu=dayMenu;
        String breakfastMeal= dayMenu.getBreakfast1()+"  "+dayMenu.getBreakfast2()+"  "+dayMenu.getBreakfast3();
        String lunchMeal= dayMenu.getLunch1()+"  "+ dayMenu.getLunch2()+"  "+dayMenu.getLunch3();
        String supperMeal= dayMenu.getSupper1()+"  "+dayMenu.getSupper2()+"  "+ dayMenu.getSupper3();

        breakfast.setText(breakfastMeal);
        lunch.setText(lunchMeal);
        supper.setText(supperMeal);
        dayOfWeek.setText(dayMenu.getDayOfWeek()+"'s menu");
    }



//    showing the edit menu popup


    private void showPopup(View parentView) {
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,true);

        popupWindow.showAtLocation(parentView, Gravity.CENTER,0,0);

        popupDayOfWeek.setText(selectedDayMenu.getDayOfWeek());

        breakfast1View.setText(selectedDayMenu.getBreakfast1());
        breakfast2View.setText(selectedDayMenu.getBreakfast2());
        breakfast3View.setText(selectedDayMenu.getBreakfast3());

        lunch1View.setText(selectedDayMenu.getLunch1());
        lunch2View.setText(selectedDayMenu.getLunch2());
        lunch3View.setText(selectedDayMenu.getLunch3());
        supper1View.setText(selectedDayMenu.getSupper1());
        supper2View.setText(selectedDayMenu.getSupper2());
        supper3View.setText(selectedDayMenu.getSupper3());
        popupView.findViewById(R.id.discard).setOnClickListener(view->popupWindow.dismiss());

        popupView.findViewById(R.id.edit_btn).setOnClickListener(this::editMenuOnRealm);


        popupWindow.setOnDismissListener(() -> {
            breakfast1View.setText("");
            breakfast2View.setText("");
            breakfast3View.setText("");

            lunch1View.setText("");
            lunch2View.setText("");
            lunch3View.setText("");

            supper1View.setText("");
            supper2View.setText("");
            supper3View.setText("");
        });
        

    }
    public void editMenuOnRealm(View view){
        Toast.makeText(getContext(),"Edit was successful",Toast.LENGTH_SHORT).show();

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

//      updating data data in Realm

        final DayMenu dayMenu=realm.where(DayMenu.class).equalTo("dayOfWeek",selectedDayMenu.getDayOfWeek()).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                assert  dayMenu!=null;
                dayMenu.setBreakfast1(breakfast1);
                dayMenu.setBreakfast2(breakfast2);
                dayMenu.setBreakfast3(breakfast3);

                dayMenu.setLunch1(lunch1);
                dayMenu.setLunch2(lunch2);
                dayMenu.setLunch3(lunch3);

                dayMenu.setSupper1(supper1);
                dayMenu.setSupper2(supper2);
                dayMenu.setSupper3(supper3);
                realm.copyToRealmOrUpdate(dayMenu);
                realm.close();
            }
        });


        Toast.makeText(getContext(),"Menu updated successfully",Toast.LENGTH_SHORT).show();
        popupWindow.dismiss();

        }







}