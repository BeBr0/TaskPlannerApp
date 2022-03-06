package edu.vlsu.taskplanner.broadcast;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import edu.vlsu.taskplanner.NotificationHelper;

public class myBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmBroadCastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("1", "1");
        notificationHelper.getManager().notify(1, nb.build());
    }
}
