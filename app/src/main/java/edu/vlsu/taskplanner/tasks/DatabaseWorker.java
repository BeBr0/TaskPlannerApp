package edu.vlsu.taskplanner.tasks;

import static java.security.AccessController.getContext;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseWorker{
    static final SQLiteOpenHelper sqLiteOpenHelper = new SQLiteOpenHelper(null, "database.db", null, 1) {
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

        }
    };

    public static final SQLiteDatabase sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();


}
