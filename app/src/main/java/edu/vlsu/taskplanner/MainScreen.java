package edu.vlsu.taskplanner;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import edu.vlsu.taskplanner.settings.Theme;
import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskViewAdapter;

public class MainScreen extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();
        if (Settings.currentTheme == Theme.LIGHT){
            theme.applyStyle(R.style.Light_MainScreen, true);
        }
        else if (Settings.currentTheme == Theme.DARK){
            theme.applyStyle(R.style.Dark_MainScreen, true);
        }
        return theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        recyclerView = findViewById(R.id.recycler_view);
        TaskViewAdapter adapter= new TaskViewAdapter();

        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Task.sort((TaskViewAdapter) recyclerView.getAdapter());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

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
            intent.putExtra("task", Task.taskList.indexOf(task));
            startActivity(intent);
        }
        else if (item.getItemId() == R.id.done_item){

        }
        else if (item.getItemId() == R.id.delete_item){
            Task.remove(task);
            recyclerView.removeView(item.getActionView());
            recyclerView.getAdapter().notifyItemRemoved(item.getGroupId());
        }
        return true;
    }
}