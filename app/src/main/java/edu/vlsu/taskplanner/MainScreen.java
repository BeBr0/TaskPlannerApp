package edu.vlsu.taskplanner;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.GregorianCalendar;
import android.os.Bundle;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskViewAdapter;

public class MainScreen extends AppCompatActivity {

    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(Settings.currentTheme, true);
        return theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        RecyclerView recyclerView = findViewById(R.id.tasks_recycler_view);
        recyclerView.setAdapter(new TaskViewAdapter());
        Task.sort((TaskViewAdapter) recyclerView.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // EDIT
    }
}