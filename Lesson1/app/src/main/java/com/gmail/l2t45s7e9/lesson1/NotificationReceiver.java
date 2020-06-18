package com.gmail.l2t45s7e9.lesson1;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NotificationReceiver extends BroadcastReceiver {
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent resultIntent = new Intent(context, MainActivity.class);
        int id = intent.getIntExtra("id", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), id, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context.getApplicationContext(), CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setColor(context.getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(String.valueOf(R.string.notificationBirthDay))
                .setContentText(R.string.bDay + intent.getStringExtra("birthDateName"));
        createChannelIfNeeded(notificationManager);
        assert notificationManager != null;
        notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
        repeatAlarm(context,id);
    }
    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }
    private void repeatAlarm(Context context,int id) {
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent("com.gmail.l2t45s7e9.lesson1");
        intent.putExtra("id", id);
        GregorianCalendar birthOfDay = new GregorianCalendar();
        if((!birthOfDay.isLeapYear(birthOfDay.get(Calendar.YEAR)+1)) && birthOfDay.get(Calendar.MONTH)==Calendar.FEBRUARY && birthOfDay.get(Calendar.DATE)==29){
            birthOfDay.roll(Calendar.YEAR,1);
            birthOfDay.roll(Calendar.DATE,-1);
        }
        else{
            birthOfDay.add(Calendar.YEAR,1);
        }
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context,id,intent,0);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.RTC, birthOfDay.getTimeInMillis(),alarmIntent);
    }

}
