package edu.vlsu.taskplanner.tasks;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import edu.vlsu.taskplanner.MainActivity;
import edu.vlsu.taskplanner.NotificationHelper;
import edu.vlsu.taskplanner.R;
import edu.vlsu.taskplanner.broadcast.myBroadcastReceiver;

public class Task {
    static NotificationHelper notificationHelper;
    static CardView tasksCardView;
    public static DatabaseWorker dbWorker;

    static List<Task> taskList = new ArrayList<>();

    public static void addTask(Task task){
        taskList.add(task);

        dbWorker.getWritableDatabase().execSQL("INSERT INTO tasks VALUES" +
                "(" + task.id + ", '" + task.displayName + "', '" + task.description + "', " + task.startTime.getTime().getTime() + ");");
    }

    public static void removeTask(Task task){
        taskList.remove(task);

        dbWorker.getWritableDatabase().execSQL("DELETE FROM tasks WHERE id = " + task.id);
    }

    final int id; // ??

    String displayName;
    String description;

    Calendar startTime;
    Calendar endTime;

    public Task(int id, String displayName, String description, Calendar startTime, Context context, @Nullable Calendar endTime) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;

        notificationHelper = new NotificationHelper(context);
    }

    private void startAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    /* notificationHelper.getChannelNotification() */


            /* Getter & Setter */

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
