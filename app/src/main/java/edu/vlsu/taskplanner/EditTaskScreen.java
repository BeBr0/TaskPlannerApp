package edu.vlsu.taskplanner;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import edu.vlsu.taskplanner.tasks.Task;

public class EditTaskScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedScreenInstance) {
        super.onCreate(savedScreenInstance);
        setContentView(R.layout.edit_task_screen);

        int index = getIntent().getIntExtra("task", -1);

        TextView startPickerButton = findViewById(R.id.form_start_date);
        TextView endPickerButton = findViewById(R.id.form_end_date);

        startPickerButton.setOnClickListener(this::onPopUpBtnClicked);
        endPickerButton.setOnClickListener(this::onPopUpBtnClicked);

        if (index == -1){

        }
        else{
            Task task = Task.taskList.get(index);
            ((TextView) findViewById(R.id.form_title)).setText(task.getDisplayName());
            ((TextView) findViewById(R.id.form_description)).setText(task.getDescription());
            String startTime = task.formStartDateString();
            ((TextView) findViewById(R.id.form_start_date)).setText(startTime);

            if (task.getEndTime() != null) {
                String endTime = task.formEndDateString();
                ((TextView) findViewById(R.id.form_end_date)).setText(endTime);
            }
        }
    }

    private void onPopUpBtnClicked(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popUpDatePickerView = layoutInflater.inflate(R.layout.popup_date_picker, null);

        final PopupWindow popupDatePicker = new PopupWindow(popUpDatePickerView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        popupDatePicker.showAtLocation(popUpDatePickerView, Gravity.CENTER, 0, 0);

        Button submit = popupDatePicker.getContentView().findViewById(R.id.submit_date);
        Button cancel = popupDatePicker.getContentView().findViewById(R.id.cancel_date);

        View popupTimePickerView = layoutInflater.inflate(R.layout.popup_time_picker, null);
        ;
        submit.setOnClickListener((View v) -> {
            popupDatePicker.dismiss();
            DatePicker datePicker = popUpDatePickerView.findViewById(R.id.date_picker);
            Task task = Task.taskList.get(getIntent().getIntExtra("task", -1));

            Calendar calendar = Calendar.getInstance();
            calendar.getTime().setYear(datePicker.getYear());
            calendar.getTime().setMonth(datePicker.getMonth());
            calendar.getTime().setDate(datePicker.getDayOfMonth());
            calendar.getTime().setHours(task.getStartTime().getTime().getHours());
            calendar.getTime().setMinutes(task.getStartTime().getTime().getMinutes());

            task.setStartTime(calendar);

            final PopupWindow popUpTimePicker = new PopupWindow(popupTimePickerView, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);

            popUpTimePicker.showAtLocation(popupTimePickerView, Gravity.CENTER, 0, 0);

            Button submitTime = popUpTimePicker.getContentView().findViewById(R.id.submit_time);
            Button cancelTime = popUpTimePicker.getContentView().findViewById(R.id.cancel_time);

            submitTime.setOnClickListener((View view1) -> {
                TimePicker timePicker = popupTimePickerView.findViewById(R.id.time_picker);
                calendar.getTime().setHours(timePicker.getHour());
                calendar.getTime().setMinutes(timePicker.getMinute());

                task.setStartTime(calendar);
                Intent intent = new Intent(EditTaskScreen.this, EditTaskScreen.class);
                intent.putExtra("task", Task.taskList.indexOf(task));

                startActivity(intent);

                System.out.println(datePicker.getDayOfMonth() + " - " + task.getStartTime().getTime().getDay() + " " + task.getStartTime().getTime().getDate());
                System.out.println(datePicker.getMonth() + " - " + task.getStartTime().getTime().getMonth());
                System.out.println(datePicker.getYear() + " - " + task.getStartTime().getTime().getYear());
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
