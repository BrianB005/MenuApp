package com.brianbett.menuapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.brianbett.menuapp.realm.DayMenu;

public class NotificationHandler extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification_id";
    public static String NOTIFICATION = "notification";
    private  DayMenu currentDayMenu;



    @Override
    public void onReceive(Context context, Intent intent) {

//        code that resets an alarm when the device is rebooted
//        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
//
//        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"My menu")
                .setContentTitle("Breakfast menu")
                .setContentText("Your supper menu is Rice beans and avocado")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.app_icon)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(context);
        int notificationId = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(notificationId, builder.build());
    }
}
