package edu.vlsu.taskplanner.tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.vlsu.taskplanner.R;
import edu.vlsu.taskplanner.alarm.NotificationReceiver;

public class Task implements Cloneable{
    public static Task chosenTask;

    private final int id;

    private String displayName;
    private String description;

    private boolean alarmNeeded = false;

    private Calendar startTime;

    private Calendar timeOfCreation;

    private TaskGroup taskGroup;

    private PendingIntent alarmIntent;

    SimpleDateFormat dateFormat = new SimpleDateFormat("Дата: dd.MM.yyyy, Время: HH:mm", Locale.ENGLISH);

    /** Используется при создании задачи с помощью кнопки*/
    public Task(){
        int ctr = 0;
        while (true) {
            boolean isValid = true;
            for (Task task : TaskList.getTaskListClone()) {
                if (task.id == ctr) {
                    isValid = false;
                    break;
                }
            }

            if (isValid)
                break;
            else
                ctr++;
        }
        this.id = ctr;
        this.displayName = "";
        this.description =  "";
    }

    public Task(int id, String displayName, String description, Calendar startTime, boolean alarmNeeded, TaskGroup taskGroup) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.startTime = startTime;
        this.alarmNeeded = alarmNeeded;
        this.taskGroup = taskGroup;
    }

    private void setAlarm(Context context){
        if (startTime != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.setData(Uri.parse(displayName + "><" + description));

            alarmIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), alarmIntent);
        }
    }

    private void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(alarmIntent);
    }

    public String formStartDateString(){
        return dateFormat.format(startTime.getTime());
    }

    public void markAsDone(Context context){
        TaskList.remove(this);
        Toast.makeText(context, context.getString(R.string.mark_done), Toast.LENGTH_LONG).show();
    }

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

    public boolean isAlarmNeeded() {
        return alarmNeeded;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public void setAlarmNeeded(boolean alarmNeeded, Context context) {
        if (!alarmNeeded && this.alarmNeeded)
            cancelAlarm(context);
        else if (alarmNeeded && !this.alarmNeeded)
            setAlarm(context);

        this.alarmNeeded = alarmNeeded;
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(TaskGroup taskGroup) {
        this.taskGroup = taskGroup;
    }

    public void setTimeOfCreation(Calendar timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }


    public Calendar getTimeOfCreation() {
        return timeOfCreation;
    }
}
