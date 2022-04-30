package edu.vlsu.taskplanner.tasks;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;

import edu.vlsu.taskplanner.R;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskHolder> {

    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener{

        TextView title, description, timeLeft, datetime, group;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.title);
            this.description = itemView.findViewById(R.id.description);
            this.timeLeft = itemView.findViewById(R.id.time_left);
            this.datetime = itemView.findViewById(R.id.date_time);
            this.group = itemView.findViewById(R.id.group);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Context context = view.getContext();

            Task.chosenTask = TaskList.getTaskByName(title.getText().toString());

            contextMenu.add(Menu.NONE, R.id.edit_item, Menu.NONE, context.getString(R.string.edit));
            contextMenu.add(Menu.NONE, R.id.delete_item, Menu.NONE, context.getString(R.string.remove));
            contextMenu.add(Menu.NONE, R.id.done_item, Menu.NONE, context.getString(R.string.mark_done));
        }

        @Override
        public void onClick(View view){

        }
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.task_item, viewGroup, false);

        return new TaskHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = TaskList.getByIndex(position);

        Context context = holder.title.getContext();

        TextView title = holder.title;
        title.setText(task.getDisplayName());

//        TextView description = holder.description;
//        String descText = task.getDescription();
//        if (descText.length() > 50){
//            String newText = "";
//            for (int i = 0; i <= 50; i++){
//                newText += descText.charAt(i);
//            }
//            descText = newText + "...";
//        }
//        description.setText(descText);

        holder.itemView.setOnClickListener((View view) -> {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

            View popUpDatePickerView = layoutInflater.inflate(R.layout.focus_task, null);

            final PopupWindow popupDatePicker = new PopupWindow(popUpDatePickerView, LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, true);

            popupDatePicker.showAtLocation(popUpDatePickerView, Gravity.CENTER, 0, 0);

            TextView focus_title, focus_description, focus_time, focus_group, focus_alarm;
            focus_title = popUpDatePickerView.findViewById(R.id.focus_title);
            focus_description = popUpDatePickerView.findViewById(R.id.focus_description);
            focus_time = popUpDatePickerView.findViewById(R.id.focus_start_time);
            focus_group = popUpDatePickerView.findViewById(R.id.focus_group);
            focus_alarm = popUpDatePickerView.findViewById(R.id.focus_notify);

            focus_title.setText(task.getDisplayName());
            focus_description.setText(task.getDescription());
            focus_time.setText(task.formStartDateString());
            focus_group.setText(context.getString(task.getTaskGroup().getName()));
            if (task.isAlarmNeeded()) {
                focus_alarm.setText(context.getString(R.string.notification_set));
            }
            else{
                focus_alarm.setText(context.getString(R.string.notification_not_set));
            }
        });

        holder.itemView.setOnLongClickListener((View view) -> {
            setPosition(position);
            return false;
        });

        if (task.getStartTime().getTime().getTime() != -1) {

            TextView timeLeft = holder.timeLeft;
            String timeText;

            timeText = getTimeLeft(
                            task.getStartTime().getTime().getTime(),
                            Calendar.getInstance().getTime().getTime(), holder.itemView.getContext()
                    );

            timeLeft.setText(timeText);
            float timePercentage = 1 - (float)(Calendar.getInstance().getTime().getTime() - task.getTimeOfCreation().getTime().getTime()) / (task.getStartTime().getTime().getTime() - Calendar.getInstance().getTime().getTime() );

            if (timePercentage > 0.75){
                timeLeft.setTextColor(context.getColor(R.color.more_than_75));
            }
            else if (timePercentage > 0.5){
                timeLeft.setTextColor(context.getColor(R.color.more_than_50));
            }
            else if (timePercentage > 0.25){
                timeLeft.setTextColor(context.getColor(R.color.more_than_25));
            }
            else{
                timeLeft.setTextColor(context.getColor(R.color.ago));
            }

            TextView datetime = holder.datetime;
            datetime.setText(getFormattedDate(task.getStartTime().getTime().getTime()));
        }
        String groupName = "#" + task.getTaskGroup().name();
        holder.group.setText(groupName);
    }

    private String getTimeLeft(long start, long end, Context context){
        long startMinutes = start / 1000 / 60;
        long endMinutes = end / 1000 / 60;

        boolean passed = false;
        long time = startMinutes - endMinutes;

        if (time < 0)
            passed = true;

        time = Math.abs(time);

        int days = (int) (time / 60 / 24);
        int hours = (int) (time / 60 - days * 24);
        time = time - (long) days * 60 * 24 - hours * 60L;

        String result = "";

        if (days > 0){
            result += days + context.getString(R.string.days) + " ";
        }

        if (hours > 0){
            result += hours + context.getString(R.string.hours) + " ";
        }

        if (time > 0 || (time == 0 && result.equals(""))){
            result += time + context.getString(R.string.minutes) + " ";
        }

        if (passed)
            return  result + context.getString(R.string.ago);

        else
            return result + context.getString(R.string.time_left);
    }

    private String getFormattedDate(long time){
        Date date = new Date(time);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);

        return simpleDateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return TaskList.getCount();
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    
}
