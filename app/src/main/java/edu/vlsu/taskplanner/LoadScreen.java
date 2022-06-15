package edu.vlsu.taskplanner;

import android.content.Intent;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskGroup;
import edu.vlsu.taskplanner.tasks.TaskList;

public class LoadScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Light_MainScreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_screen);

        Task.dateFormat = new SimpleDateFormat("'" + getString(R.string.date) + "' dd.MM.yyyy, '" + getString(R.string.time) + "' HH:mm", Locale.forLanguageTag("ru"));

        Database.createInstance(this);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        loadDataFromDataBase();

        startActivity(new Intent(LoadScreen.this, MainScreen.class));
    }

    private void loadDataFromDataBase(){
        Database.getInstance().loadSettings();

        Cursor cursor = Database.getInstance().getReadableDatabase().rawQuery("SELECT * FROM tasks",null);
        while (cursor.moveToNext()) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(new Date(Long.parseLong(cursor.getString(3))));

            Task task;
            task = new Task(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    startTime,
                    Boolean.parseBoolean(cursor.getString(4)),
                    TaskGroup.getItem(cursor.getString(5))
            );
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(Long.parseLong(cursor.getString(6))));
            task.setTimeOfCreation(calendar);

            TaskList.addToListOnly(task);
        }

        cursor.close();
    }
}
