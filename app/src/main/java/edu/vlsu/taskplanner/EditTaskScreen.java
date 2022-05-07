package edu.vlsu.taskplanner;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Date;

import edu.vlsu.taskplanner.settings.Theme;
import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskGroup;
import edu.vlsu.taskplanner.tasks.TaskList;

public class EditTaskScreen extends AppCompatActivity {

    private Task newTask;

    private Spinner spinner;
    private TextView date, title, description;

    private DatePicker datePicker;
    private TimePicker timePicker;

    private PopupWindow popupPicker, popupTimePicker;

    private Calendar taskDate;

    private CheckBox notify;

    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();
        if (SettingsController.currentTheme == Theme.LIGHT)
            theme.applyStyle(R.style.Light_EditTaskScreen, true);
        else if (SettingsController.currentTheme == Theme.DARK){
            theme.applyStyle(R.style.Dark_EditTaskScreen, true);
        }
        return theme;
    }

    @Override
    protected void onCreate(Bundle savedScreenInstance) {
        super.onCreate(savedScreenInstance);
        setContentView(R.layout.edit_task_screen);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        createSpinner();

        int index = getIntent().getIntExtra("task", -1);

        date = findViewById(R.id.form_start_date);
        date.setOnClickListener((View view) -> popupPicker(R.layout.popup_date_picker, true));

        if (index == -1){
            newTask = new Task();
        }
        else{
            newTask = TaskList.getByIndex(index).clone();
            updateScreenTexts();
            ((CheckBox) findViewById(R.id.notification_check)).setChecked(newTask.isAlarmNeeded());
        }

        findViewById(R.id.submit_task).setOnClickListener(this::submitTask);

        findViewById(R.id.cancel_task).setOnClickListener((View view) -> {
            Intent intent = new Intent(EditTaskScreen.this, MainScreen.class);
            startActivity(intent);
        });
    }

    private void updateScreenTexts(){
        if (TaskList.getTaskByName(newTask.getDisplayName()) != null) {
            ((TextView) findViewById(R.id.form_title)).setText(newTask.getDisplayName());
            ((TextView) findViewById(R.id.form_description)).setText(newTask.getDescription());
        }

        if (newTask.getStartTime().getTime().getTime() != -1) {
            String startTime = newTask.formStartDateString();
            ((TextView) findViewById(R.id.form_start_date)).setText(startTime);
        }
    }

    private void popupPicker(@LayoutRes int layout, boolean isDate){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popupPickerView = layoutInflater.inflate(layout, null);

        popupPicker = new PopupWindow(popupPickerView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupPicker.showAtLocation(popupPickerView, Gravity.CENTER, 0, 0);

        Button submit, cancel;

        if (isDate) {
            submit = popupPicker.getContentView().findViewById(R.id.submit_date);
            submit.setOnClickListener(this::dateSubmit);

            cancel = popupPicker.getContentView().findViewById(R.id.cancel_date);
        }
        else{
            submit = popupPicker.getContentView().findViewById(R.id.submit_time);
            submit.setOnClickListener(this::timeSubmit);
            cancel = popupPicker.getContentView().findViewById(R.id.cancel_time);
        }

        cancel.setOnClickListener((View view) -> popupPicker.dismiss());
    }

    private void dateSubmit(View view){
        popupPicker.dismiss();
        datePicker = popupPicker.getContentView().findViewById(R.id.date_picker);

        popupPicker(R.layout.popup_time_picker, false);
    }

    private void timeSubmit(View view){
        popupPicker.dismiss();
        timePicker = popupPicker.getContentView().findViewById(R.id.time_picker);

        taskDate = Calendar.getInstance();
        taskDate.set(
                datePicker.getYear(),
                datePicker.getMonth(),
                datePicker.getDayOfMonth(),
                timePicker.getHour(),
                timePicker.getMinute(),
                0
        );

        newTask.setStartTime(taskDate);

        if (view.getId() == R.id.form_start_date)
            newTask.setStartTime(taskDate);

        updateScreenTexts();
    }

    private void submitTask(View view){
        title = findViewById(R.id.form_title);
        description = findViewById(R.id.form_description);
        notify = findViewById(R.id.notification_check);

        if (userMadeMistake())
            return;

        if (newTask.getStartTime() == null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(-1));
            newTask.setStartTime(calendar);
        }
        newTask.setDisplayName(title.getText().toString());
        newTask.setDescription(description.getText().toString());
        newTask.setAlarmNeeded(notify.isChecked(), view.getContext());

        newTask.setTaskGroup(TaskGroup.getItemByName(spinner.getSelectedItem().toString()));

        newTask.setTimeOfCreation(Calendar.getInstance());

        TaskList.update(newTask);

        Intent intent = new Intent(EditTaskScreen.this, MainScreen.class);
        startActivity(intent);
    }

    private boolean userMadeMistake(){
        for (Task task: TaskList.getTaskListClone()){
            if (task.getDisplayName().equals(title.getText().toString()) && newTask.getId() != task.getId()){
                Toast.makeText(this, getString(R.string.error_title_exists), Toast.LENGTH_LONG).show();
                return true;
            }
        }
        if (title.getText().toString().equals("")){
            Toast.makeText(this, getString(R.string.error_title_missing), Toast.LENGTH_LONG).show();
            return true;
        }
        if (date.getText().toString().equals("Start time") && notify.isChecked()){
            Toast.makeText(this, R.string.error_time_missing, Toast.LENGTH_LONG).show();
            return true;
        }
        if (title.getText().toString().contains("><")){
            Toast.makeText(this, getString(R.string.error_forbidden_symbols), Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }

    private void createSpinner(){
        String[] spinnerItems = new String[TaskGroup.values().length];
        for (int i = 0; i < TaskGroup.values().length; i++){
            spinnerItems[i] = getString(TaskGroup.values()[i].getName());
        }
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, spinnerItems);
        spinner.setAdapter(adapter);
    }
}
