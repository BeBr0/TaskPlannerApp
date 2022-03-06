package edu.vlsu.taskplanner.tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

import java.util.ArrayList;
import java.util.List;

import edu.vlsu.taskplanner.NotificationHelper;
import edu.vlsu.taskplanner.broadcast.myBroadcastReceiver;

public class Task {
    static NotificationHelper notificationHelper;

    static List<Task> taskList = new ArrayList<>();

    public static void addTask(Task task){
        taskList.add(task);

        DatabaseWorker.sqLiteDatabase.execSQL("INSERT tasks " + task.id + " " + task.displayName +
                " " + task.description + " " + task.calendar.getTime());
    }

    public static void removeTask(Task task){
        taskList.remove(task);

        DatabaseWorker.sqLiteDatabase.execSQL("DELETE FROM tasks WHERE id = " + task.id);
    }

    final int id; // ??

    String displayName;
    String description;

    Calendar calendar;

    public Task(int id, String displayName, String description, Calendar calendar, Context context) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.calendar = calendar;

        notificationHelper = new NotificationHelper(context);
    }

    private void startAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
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
