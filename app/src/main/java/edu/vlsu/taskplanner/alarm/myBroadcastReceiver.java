package edu.vlsu.taskplanner.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class myBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("1", "1");
        notificationHelper.getManager().notify(1, nb.build());
    }
}
