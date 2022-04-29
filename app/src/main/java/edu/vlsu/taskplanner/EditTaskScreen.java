package edu.vlsu.taskplanner;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.w3c.dom.Text;

import java.util.Date;

import edu.vlsu.taskplanner.settings.Theme;
import edu.vlsu.taskplanner.tasks.Task;
import edu.vlsu.taskplanner.tasks.TaskGroup;

public class EditTaskScreen extends AppCompatActivity {

    private Task newTask;

    @Override
    public Resources.Theme getTheme(){
        Resources.Theme theme = super.getTheme();
        if (Settings.currentTheme == Theme.LIGHT)
            theme.applyStyle(R.style.Light_EditTaskScreen, true);
        else if (Settings.currentTheme == Theme.DARK){
            theme.applyStyle(R.style.Dark_EditTaskScreen, true);
        }
        return theme;
    }

    @Override
    protected void onCreate(Bundle savedScreenInstance) {
        super.onCreate(savedScreenInstance);
        setContentView(R.layout.edit_task_screen);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        String[] spinnerItems = new String[TaskGroup.values().length];
        for (int i = 0; i < TaskGroup.values().length; i++){
            spinnerItems[i] = getString(TaskGroup.values()[i].getName());
        }
        Spinner spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, spinnerItems);
        spinner.setAdapter(adapter);

        int index = getIntent().getIntExtra("task", -1);

        TextView startPickerButton = findViewById(R.id.form_start_date);

        startPickerButton.setOnClickListener(this::onPopUpTimeBtnClicked);

        if (index == -1){
            newTask = new Task();
        }
        else{
            newTask = Task.taskList.get(index).clone();
            updateScreenTexts();
            ((CheckBox) findViewById(R.id.notification_check)).setChecked(newTask.isAlarmNeeded());
        }

        findViewById(R.id.submit_task).setOnClickListener((View view) -> {
            TextView title = findViewById(R.id.form_title);
            TextView description = findViewById(R.id.form_description);
            CheckBox notify = (CheckBox) findViewById(R.id.notification_check);
            for (Task task: Task.taskList){
                if (task.getDisplayName().equals(title.getText().toString()) && newTask.getId() != task.getId()){
                    Toast.makeText(this, getString(R.string.error_title_exists), Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if (title.getText().toString().equals("")){
                Toast.makeText(this, getString(R.string.error_title_missing), Toast.LENGTH_LONG).show();
                return;
            }
            System.out.println(startPickerButton.getText().toString());
            if (startPickerButton.getText().toString().equals("Start time") && notify.isChecked()){
                Toast.makeText(this, R.string.error_time_missing, Toast.LENGTH_LONG).show();
                return;
            }
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

            Task.update(newTask);

            Intent intent = new Intent(EditTaskScreen.this, MainScreen.class);
            startActivity(intent);
        });

        findViewById(R.id.cancel_task).setOnClickListener((View view) -> {
            Intent intent = new Intent(EditTaskScreen.this, MainScreen.class);
            startActivity(intent);
        });
    }

    private void updateScreenTexts(){
        if (Task.exists(newTask)) {
            ((TextView) findViewById(R.id.form_title)).setText(newTask.getDisplayName());
            ((TextView) findViewById(R.id.form_description)).setText(newTask.getDescription());
        }

        if (newTask.getStartTime().getTime().getTime() != -1) {
            String startTime = newTask.formStartDateString();
            ((TextView) findViewById(R.id.form_start_date)).setText(startTime);
        }
    }

    private void onPopUpTimeBtnClicked(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popUpDatePickerView = layoutInflater.inflate(R.layout.popup_date_picker, null);

        final PopupWindow popupDatePicker = new PopupWindow(popUpDatePickerView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupDatePicker.showAtLocation(popUpDatePickerView, Gravity.CENTER, 0, 0);

        Button submit = popupDatePicker.getContentView().findViewById(R.id.submit_date);
        Button cancel = popupDatePicker.getContentView().findViewById(R.id.cancel_date);

        View popupTimePickerView = layoutInflater.inflate(R.layout.popup_time_picker, null);

        submit.setOnClickListener((View v) -> {
            popupDatePicker.dismiss();
            DatePicker datePicker = popUpDatePickerView.findViewById(R.id.date_picker);

            Calendar calendar = Calendar.getInstance();
            calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

            final PopupWindow popUpTimePicker = new PopupWindow(popupTimePickerView, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);

            popUpTimePicker.showAtLocation(popupTimePickerView, Gravity.CENTER, 0, 0);

            Button submitTime = popUpTimePicker.getContentView().findViewById(R.id.submit_time);
            Button cancelTime = popUpTimePicker.getContentView().findViewById(R.id.cancel_time);

            submitTime.setOnClickListener((View view1) -> {
                TimePicker timePicker = popupTimePickerView.findViewById(R.id.time_picker);
                calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), timePicker.getHour(), timePicker.getMinute());

                if (view.getId() == R.id.form_start_date)
                    newTask.setStartTime(calendar);

                updateScreenTexts();

                popUpTimePicker.dismiss();
            });

            cancelTime.setOnClickListener((View view1) -> {
                popUpTimePicker.dismiss();
            });

            popupTimePickerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popUpTimePicker.dismiss();
                    return false;
                }
            });
        });

        cancel.setOnClickListener((View v) -> {
            popupDatePicker.dismiss();
        });

        popUpDatePickerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupDatePicker.dismiss();
                return true;
            }
        });
    }
}
