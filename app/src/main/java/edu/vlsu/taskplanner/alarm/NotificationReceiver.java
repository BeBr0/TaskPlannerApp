package edu.vlsu.taskplanner.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        String[] data = intent.getDataString().split("><");

        if (data.length == 1){
            data = new String[]{data[0], ""};
        }

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification(data[0], data[1]);
        notificationHelper.getManager().notify(1, nb.build());
    }
}
