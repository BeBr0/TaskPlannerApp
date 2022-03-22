package edu.vlsu.taskplanner.tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.vlsu.taskplanner.alarm.NotificationHelper;
import edu.vlsu.taskplanner.alarm.myBroadcastReceiver;

public class Task {
    static NotificationHelper notificationHelper;
    public static TasksDBWorker dbWorker;

    public static List<Task> taskList = new ArrayList<>();

    /** Используется только при загрузке приложения! */
    public static void addTaskToList(Task task){
        taskList.add(task);
    }

    public static void addTask(Task task){
        taskList.add(task);

        if (task.endTime == null)
            dbWorker.getWritableDatabase().execSQL("INSERT INTO tasks VALUES" +
                    "(" + task.id + ", '" + task.displayName + "', '" + task.description + "', " +
                    task.startTime.getTime().getTime() + -1 + ");");
        else
            dbWorker.getWritableDatabase().execSQL("INSERT INTO tasks VALUES" +
                    "(" + task.id + ", '" + task.displayName + "', '" + task.description + "', " +
                    task.startTime.getTime().getTime() + task.endTime.getTime().getTime() + ");");
    }

    public static void removeTask(Task task){
        taskList.remove(task);

        dbWorker.getWritableDatabase().execSQL("DELETE FROM tasks WHERE id = " + task.id);
    }

    private final int id;

    private String displayName;
    private String description;

    private final Calendar startTime;
    private final Calendar endTime;

    public Task(String displayName, String description, Calendar startTime, Context context, @Nullable Calendar endTime) {
        this.id = taskList.size();
        this.displayName = displayName;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;

        notificationHelper = new NotificationHelper(context);
    }

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

    public Calendar getStartTime() {
        return startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }
}
