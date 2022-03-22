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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // EDIT

        Task task = new Task("name", "desc", new GregorianCalendar(0, 0, 0, 10, 10), this, null);
        System.out.println(task.getStartTime());
        Task.addTask(task);
    }


}