package edu.vlsu.taskplanner.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TasksDBWorker extends  SQLiteOpenHelper{
    static String name = "database.db";
    static int version = 1;

    static String ID_COLUMN = "id";
    static String NAME_COLUMN = "displayName";
    static String DESCRIPTION_COLUMN = "description";
    static String START_DATE_COLUMN = "startDate";
    static String ALARM_NEEDED = "alarmNeeded";
    static String GROUP_COLUMN = "task_group";

    public TasksDBWorker(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tasks (" +
                    ID_COLUMN + " INTEGER PRIMARY KEY," +
                    NAME_COLUMN + " TEXT," +
                    DESCRIPTION_COLUMN + " TEXT," +
                    START_DATE_COLUMN + " INTEGER," +
                    ALARM_NEEDED + " TEXT," +
                    GROUP_COLUMN + " TEXT" +
                    ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(sqLiteDatabase);
    }

    public void writeTaskToDB(Task task){
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_COLUMN, task.getId());
        contentValues.put(NAME_COLUMN, task.getDisplayName());
        contentValues.put(DESCRIPTION_COLUMN, task.getDescription());
        if (task.getStartTime() != null) {
            contentValues.put(START_DATE_COLUMN, task.getStartTime().getTime().getTime());
        }
        else
            contentValues.put(START_DATE_COLUMN, -1);

        contentValues.put(ALARM_NEEDED, task.isAlarmNeeded());
        contentValues.put(GROUP_COLUMN, task.getTaskGroup().getName());

        if (!Task.exists(task))
            getWritableDatabase().execSQL("INSERT INTO tasks VALUES" + "(" +
                    contentValues.get(ID_COLUMN) + ", '" +
                    contentValues.get(NAME_COLUMN) + "', '" +
                    contentValues.get(DESCRIPTION_COLUMN) + "', " +
                    contentValues.get(START_DATE_COLUMN) + ", " +
                    contentValues.get(ALARM_NEEDED) +
                    ");");

        else
            getWritableDatabase().update("tasks", contentValues, "id = " + task.getId(), new String[]{});
    }

    public void clearTable(String name){
        getWritableDatabase().execSQL("DELETE FROM " + name);
    }
}
