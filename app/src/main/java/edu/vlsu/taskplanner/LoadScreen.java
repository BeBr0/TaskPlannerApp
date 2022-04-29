package edu.vlsu.taskplanner;

import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.sql.Date;

import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskGroup;
import edu.vlsu.taskplanner.tasks.TasksDBWorker;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Light_MainScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_screen);
        Task.dbWorker = new TasksDBWorker(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        loadDataFromDataBase();

        startActivity(new Intent(LoadScreen.this, MainScreen.class));
    }

    private void loadDataFromDataBase(){
        Cursor cursor = Task.dbWorker.getReadableDatabase().rawQuery("SELECT * FROM tasks",null);
        while (cursor.moveToNext()) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(new Date(Long.parseLong(cursor.getString(3))));

            Task task;
            task = new Task(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    startTime,
                    this,
                    Boolean.parseBoolean(cursor.getString(4)),
                    TaskGroup.getItemByName(cursor.getString(5))
            );

            Task.addTaskToList(task);
        }

        cursor.close();
    }
}
