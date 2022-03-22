package edu.vlsu.taskplanner.tasks;

import static java.security.AccessController.getContext;

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
                    END_DATE_COLUMN + " INTEGER" +
                    ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(sqLiteDatabase);
    }
}
