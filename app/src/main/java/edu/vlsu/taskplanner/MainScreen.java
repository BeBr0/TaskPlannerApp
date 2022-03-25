package edu.vlsu.taskplanner;

import android.icu.util.GregorianCalendar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskViewAdapter;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        RecyclerView recyclerView = findViewById(R.id.tasks_recycler_view);
        recyclerView.setAdapter(new TaskViewAdapter());

        // EDIT

        System.out.println(Task.taskList);

        Task.sort((TaskViewAdapter) recyclerView.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}