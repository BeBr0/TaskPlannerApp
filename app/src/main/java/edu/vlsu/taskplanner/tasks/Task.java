package edu.vlsu.taskplanner.tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import edu.vlsu.taskplanner.alarm.NotificationHelper;
import edu.vlsu.taskplanner.alarm.myBroadcastReceiver;

public class Task implements Cloneable{
    static NotificationHelper notificationHelper;
    public static TasksDBWorker dbWorker;

    public static List<Task> taskList = new ArrayList<>();

    public static boolean exists(Task task){
        for (Task t: taskList){
            if (t.id == task.id)
                return true;
        }

        return false;
    }

    /** Используется только при загрузке приложения! */
    public static void addTaskToList(Task task){
        taskList.add(task);
    }

    public static void add(Task task){
        dbWorker.writeTaskToDB(task);

        taskList.add(task);
    }

    public static void remove(Task task){
        taskList.remove(task);

        dbWorker.getWritableDatabase().execSQL("DELETE FROM tasks WHERE id = " + task.id);
    }

    public static void update(Task task){
        if (!exists(task)){
            add(task);
            return;
        }

        taskList.set(task.id, task);

        dbWorker.writeTaskToDB(task);
    }

    public static void sort(TaskViewAdapter taskViewAdapter){
        List<Task> sorted = new ArrayList<>();

        for (int i = 0; i < taskList.size(); i++){
            long minTime = -1;
            Task minTimeTask = null;
            for (int j = 0; j < taskList.size(); j++){
                if ((taskList.get(j).startTime.getTime().getTime() <= minTime || minTime == -1) && !sorted.contains(taskList.get(j))){
                    minTime = taskList.get(j).startTime.getTime().getTime();
                    minTimeTask = taskList.get(j);
                }
            }

            sorted.add(minTimeTask);
            taskViewAdapter.notifyItemMoved(taskList.indexOf(minTimeTask), sorted.size() - 1);
        }

        taskList.clear();
        taskList.addAll(sorted);
    }

    private final int id;

    private String displayName;
    private String description;

    private boolean alarmNeeded = false;

    private Calendar startTime;
    private Calendar endTime;

    SimpleDateFormat dateFormat = new SimpleDateFormat("Дата: dd.MM.yyyy, Время: HH:mm", Locale.ENGLISH);

    /** Используется при создании задачи с помощью кнопки*/
    public Task(){
        this.id = taskList.size();
        this.displayName = "";
        this.description =  "";
    }

    public Task(String displayName, String description, Calendar startTime, Context context, boolean alarmNeeded, @Nullable Calendar endTime) {
        this.id = taskList.size();
        this.displayName = displayName;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;

        notificationHelper = new NotificationHelper(context);

        if (alarmNeeded)
            startAlarm(context);
    }

    public Task(int id, String displayName, String description, Calendar startTime, Context context, boolean alarmNeeded, @Nullable Calendar endTime) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.alarmNeeded = alarmNeeded;

        notificationHelper = new NotificationHelper(context);
    }

    private void startAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), pendingIntent);
        if (endTime != null)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, endTime.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, myBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public String formStartDateString(){
        return dateFormat.format(startTime.getTime());
    }

    public String formEndDateString(){
        return dateFormat.format(endTime.getTime());
    }

    /* notificationHelper.getChannelNotification() */

    @NonNull
    @Override
    public Task clone(){
        try {
            return (Task) super.clone();
        }
        catch (CloneNotSupportedException e){
            e.printStackTrace();
            return this;
        }
    }

            /* Getter & Setter */

    public String getMonthAndDayString(){
        return startTime.get(Calendar.DAY_OF_MONTH) + " " + new DateFormatSymbols().getMonths()[startTime.get(Calendar.MONTH)];
    }

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

    public boolean isAlarmNeeded() {
        return alarmNeeded;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public void setAlarmNeeded(boolean alarmNeeded, Context context) {
        this.alarmNeeded = alarmNeeded;

        if (!alarmNeeded)
            cancelAlarm(context);
        else
            startAlarm(context);
    }
}
