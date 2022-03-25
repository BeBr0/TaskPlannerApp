package edu.vlsu.taskplanner;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import edu.vlsu.taskplanner.tasks.Task;

public class EditTaskScreen extends AppCompatActivity {

    private Task task;

    @Override
    protected void onCreate(Bundle savedScreenInstance) {
        super.onCreate(savedScreenInstance);
        setContentView(R.layout.edit_task_screen);

        int index = getIntent().getIntExtra("task", -1);

        TextView startPickerButton = findViewById(R.id.form_start_date);
        TextView endPickerButton = findViewById(R.id.form_end_date);

        startPickerButton.setOnClickListener(this::onPopUpTimeBtnClicked);
        endPickerButton.setOnClickListener(this::onPopUpTimeBtnClicked);

        final Task initialTask;

        if (index == -1){
            initialTask = null;
            task = null;
        }
        else{
            task = Task.taskList.get(index);
            initialTask = task.clone();

            update();
        }

        findViewById(R.id.submit_task).setOnClickListener((View view) -> {
            if (task != null) {
                task.setDisplayName(((TextView) findViewById(R.id.form_title)).getText().toString());
                task.setDescription(((TextView) findViewById(R.id.form_description)).getText().toString());
                task.setAlarmNeeded(((CheckBox)findViewById(R.id.notification_check)).isChecked());

                Task.taskList.set(index, task);

                Task.update(task);
            }

            Intent intent = new Intent(EditTaskScreen.this, MainScreen.class);
            startActivity(intent);
        });

        findViewById(R.id.cancel_task).setOnClickListener((View view) -> {
            Task.taskList.set(index, initialTask);

            Intent intent = new Intent(EditTaskScreen.this, MainScreen.class);
            startActivity(intent);
        });
    }

    private void update(){
        ((TextView) findViewById(R.id.form_title)).setText(task.getDisplayName());
        ((TextView) findViewById(R.id.form_description)).setText(task.getDescription());
        String startTime = task.formStartDateString();
        ((TextView) findViewById(R.id.form_start_date)).setText(startTime);

        if (task.getEndTime() != null) {
            String endTime = task.formEndDateString();
            ((TextView) findViewById(R.id.form_end_date)).setText(endTime);
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
            int index = getIntent().getIntExtra("task", -1);

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
                    task.setStartTime(calendar);
                else
                    task.setEndTime(calendar);

                update();

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