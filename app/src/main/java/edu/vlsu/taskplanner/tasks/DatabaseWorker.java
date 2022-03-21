package edu.vlsu.taskplanner.tasks;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseWorker extends  SQLiteOpenHelper{
    static String name = "database.db";
    static int version = 1;

    public DatabaseWorker(@Nullable Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tasks (" +
                    "id INTEGER PRIMARY KEY," +
                    "displayName TEXT," +
                    "description TEXT," +
                    "date INTEGER" +
                    ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(sqLiteDatabase);
    }
}
