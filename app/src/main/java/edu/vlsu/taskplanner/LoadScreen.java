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
        Cursor c = Task.dbWorker.getReadableDatabase().rawQuery("SELECT * FROM tasks",null);
        while (c.moveToNext()) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(new Date(Long.parseLong(c.getString(3))));

            Task task;
            if (c.isNull(4)) {
                task = new Task(
                        Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        c.getString(2),
                        startTime,
                        this,
                        null
                );
            }
            else {
                Calendar endTime = Calendar.getInstance();
                endTime.setTime(new Date(Long.parseLong(c.getString(4))));
                task = new Task(
                        Integer.parseInt(c.getString(0)),
                        c.getString(1),
                        c.getString(2),
                        startTime,
                        this,
                        endTime
                );
            }

            Task.addTaskToList(task);
        }

        c.close();
    }

//    /** Проеряет есть ли часы и минуты у даты */
//    private void checkDateFields(Date date){
//        try{
//
//        }
//    }
}
