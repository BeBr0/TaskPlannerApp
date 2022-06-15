package edu.vlsu.taskplanner;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Locale;

import edu.vlsu.taskplanner.settings.Theme;
import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskList;
import edu.vlsu.taskplanner.tasks.TaskViewAdapter;

public class MainScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwitchMaterial themeSwitch;

    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();
        if (SettingsController.currentTheme == Theme.LIGHT){
            theme.applyStyle(R.style.Light_MainScreen, true);
        }
        else if (SettingsController.currentTheme == Theme.DARK){
            theme.applyStyle(R.style.Dark_MainScreen, true);
        }
        return theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        themeSwitch = findViewById(R.id.theme_switcher);
        themeSwitch.setOnClickListener(this::onSwitch);

        TextView themeName = findViewById(R.id.theme_name);
        if (SettingsController.currentTheme == Theme.LIGHT) {
            themeName.setText(R.string.light_theme);
            themeSwitch.setActivated(false);
        }
        else if (SettingsController.currentTheme == Theme.DARK) {
            themeName.setText(R.string.dark_theme);
            themeSwitch.setActivated(true);
        }

        setupRecyclerView();
        setupDrawer();

        findViewById(R.id.add_task_btn).setOnClickListener(view -> {
            Intent intent = new Intent(MainScreen.this, EditTaskScreen.class);
            startActivity(intent);
        });
    }

    private void onSwitch(View view){
        if (themeSwitch.isActivated()){
            SettingsController.currentTheme = Theme.LIGHT;
        }
        else{
            SettingsController.currentTheme = Theme.DARK;
        }

        Intent mStartActivity = new Intent(this, LoadScreen.class);
        Database.getInstance().writeSettings();
        startActivity(mStartActivity);
        System.exit(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setupRecyclerView();
        setupDrawer();

        findViewById(R.id.add_task_btn).setOnClickListener(view -> {
            Intent intent = new Intent(MainScreen.this, EditTaskScreen.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        Task task = Task.chosenTask;

        if (item.getItemId() == R.id.edit_item){
            Intent intent = new Intent(MainScreen.this, EditTaskScreen.class);
            intent.putExtra("task", TaskList.getIndex(task));
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.done_item){
            if (recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyItemRemoved(TaskList.getIndex(task));
                task.markAsDone(this);
            }
        }
        else if (item.getItemId() == R.id.delete_item){
            if (recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyItemRemoved(TaskList.getIndex(task));
                TaskList.remove(task);
            }
        }
        return true;
    }

    private void setupRecyclerView(){
        recyclerView = findViewById(R.id.recycler_view);
        TaskViewAdapter adapter= new TaskViewAdapter();

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        TaskList.sort((TaskViewAdapter) recyclerView.getAdapter());
    }

    private void setupDrawer(){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        findViewById(R.id.side_bar_open_btn).setOnClickListener(view -> {
            if (!drawerLayout.isDrawerOpen(findViewById(R.id.nav)))
                drawerLayout.openDrawer(findViewById(R.id.nav));
            else
                drawerLayout.closeDrawer(findViewById(R.id.nav));
        });
    }
}