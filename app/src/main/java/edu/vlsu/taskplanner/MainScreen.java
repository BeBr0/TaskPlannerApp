package edu.vlsu.taskplanner;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import edu.vlsu.taskplanner.settings.Theme;
import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskViewAdapter;

public class MainScreen extends AppCompatActivity {

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
        RecyclerView recyclerView = findViewById(R.id.tasks_recycler_view);
        recyclerView.setAdapter(new TaskViewAdapter());
        Task.sort((TaskViewAdapter) recyclerView.getAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_main);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        findViewById(R.id.side_bar_open_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Called");
                if (!drawerLayout.isDrawerOpen(findViewById(R.id.nav)))
                    drawerLayout.openDrawer(findViewById(R.id.nav));
                else
                    drawerLayout.closeDrawer(findViewById(R.id.nav));
            }
        });
    }


}