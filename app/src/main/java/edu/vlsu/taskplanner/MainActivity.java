package edu.vlsu.taskplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Date;

import edu.vlsu.taskplanner.tasks.Task;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}