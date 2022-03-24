package edu.vlsu.taskplanner;

import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Date;

import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TasksDBWorker;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_screen);
        Task.dbWorker = new TasksDBWorker(this);

        loadDataFromDataBase();

        startActivity(new Intent(LoadScreen.this, MainScreen.class));
    }

    private void loadDataFromDataBase(){
        Cursor cursor = Task.dbWorker.getReadableDatabase().rawQuery("SELECT * FROM tasks",null);
        while (cursor.moveToNext()) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(new Date(Long.parseLong(cursor.getString(3))));

            Task task;
            if (cursor.isNull(4)) {
                task = new Task(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        startTime,
                        this,
                        Boolean.parseBoolean(cursor.getString(3)),
                        null
                );
            }
            else {
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(new Date(Long.parseLong(cursor.getString(4))));
                task = new Task(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        startTime,
                        this,
                        Boolean.parseBoolean(cursor.getString(3)),
                        endTime
                );
            }

            Task.addTaskToList(task);
        }

        cursor.close();
    }
}
