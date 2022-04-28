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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.vlsu.taskplanner.R;
import edu.vlsu.taskplanner.alarm.NotificationHelper;
import edu.vlsu.taskplanner.alarm.NotificationReceiver;

public class Task implements Cloneable{
    static NotificationHelper notificationHelper;
    public static TasksDBWorker dbWorker;
    public static Task chosenTask;

    public static List<Task> taskList = new ArrayList<>();
    List<TaskGroup> taskGroups = new ArrayList<>();

    public static boolean exists(Task task){
        for (Task t: taskList){
            if (t.id == task.id)
                return true;
        }

        return false;
    }

    public static Task getTaskByName(String title){
        for (Task task: taskList){
            if (task.displayName.equals(title)){
                return task;
            }
        }

        return null;
    }

    /** Используется ТОЛЬКО при загрузке приложения! */
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

        for (Task t: taskList){
            if (t.id == task.id)
                taskList.set(taskList.indexOf(t), task);
        }
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

    private TaskGroup taskGroup;

    SimpleDateFormat dateFormat = new SimpleDateFormat("Дата: dd.MM.yyyy, Время: HH:mm", Locale.ENGLISH);

    /** Используется при создании задачи с помощью кнопки*/
    public Task(){
        int ctr = 0;
        while (true) {
            boolean isValid = true;
            for (Task task : taskList) {
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

    public Task(int id, String displayName, String description, Calendar startTime, Context context, boolean alarmNeeded, TaskGroup taskGroup) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.startTime = startTime;
        this.alarmNeeded = alarmNeeded;
        this.taskGroup = taskGroup;

        notificationHelper = new NotificationHelper(context);
    }

    private void setAlarm(Context context){
        if (startTime != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.setData(Uri.parse(displayName + "><" + description));

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), pendingIntent);
        }
    }

    private void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
    }

    public String formStartDateString(){
        return dateFormat.format(startTime.getTime());
    }

    public void markAsDone(Context context){
        Task.remove(this);
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
        this.alarmNeeded = alarmNeeded;

        if (!alarmNeeded)
            cancelAlarm(context);
        else
            setAlarm(context);
    }

    public TaskGroup getTaskGroup() {
        return taskGroup;
    }
}
