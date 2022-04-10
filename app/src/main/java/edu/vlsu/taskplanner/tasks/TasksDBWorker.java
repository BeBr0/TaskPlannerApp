package edu.vlsu.taskplanner.tasks;

import static java.security.AccessController.getContext;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.vlsu.taskplanner.R;

public class TasksDBWorker extends  SQLiteOpenHelper{
    static String name = "database.db";
    static int version = 1;

    static String ID_COLUMN = "id";
    static String NAME_COLUMN = "displayName";
    static String DESCRIPTION_COLUMN = "description";
    static String START_DATE_COLUMN = "startDate";
    static String ALARM_NEEDED = "alarmNeeded";
    static String END_DATE_COLUMN = "endDate";

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
                    END_DATE_COLUMN + " INTEGER," +
                    ALARM_NEEDED + " TEXT" +
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
        contentValues.put(START_DATE_COLUMN, task.getStartTime().getTime().getTime());

        if (task.getEndTime() != null)
            contentValues.put(END_DATE_COLUMN, task.getEndTime().getTime().getTime());
        else
            contentValues.put(END_DATE_COLUMN, -1);

        contentValues.put(ALARM_NEEDED, task.isAlarmNeeded());

        if (!Task.exists(task))
            getWritableDatabase().execSQL("INSERT INTO tasks VALUES" + "(" +
                    contentValues.get(ID_COLUMN) + ", '" +
                    contentValues.get(NAME_COLUMN) + "', '" +
                    contentValues.get(DESCRIPTION_COLUMN) + "', " +
                    contentValues.get(START_DATE_COLUMN) + ", " +
                    contentValues.get(END_DATE_COLUMN) + ", " +
                    contentValues.get(ALARM_NEEDED) +
                    ");");

        else
            getWritableDatabase().update("tasks", contentValues, "id = " + task.getId(), new String[]{});
    }

    public void clearTable(String name){
        getWritableDatabase().execSQL("DELETE FROM " + name);
    }
}
