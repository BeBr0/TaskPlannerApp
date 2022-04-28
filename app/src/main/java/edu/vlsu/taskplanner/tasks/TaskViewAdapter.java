package edu.vlsu.taskplanner.tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.vlsu.taskplanner.EditTaskScreen;
import edu.vlsu.taskplanner.R;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskHolder> {

    public class TaskHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        TextView title, description, timeLeft, datetime, group;
        Button taskOpenButton;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);

            this.title = itemView.findViewById(R.id.title);
            this.description = itemView.findViewById(R.id.description);
            this.timeLeft = itemView.findViewById(R.id.time_left);
            this.datetime = itemView.findViewById(R.id.date_time);
            this.group = itemView.findViewById(R.id.group);

            this.taskOpenButton = itemView.findViewById(R.id.task_open_btn);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            Context context = view.getContext();

            Task.chosenTask = Task.getTaskByName(title.getText().toString());

            contextMenu.add(Menu.NONE, R.id.edit_item, Menu.NONE, context.getString(R.string.edit));
            contextMenu.add(Menu.NONE, R.id.delete_item, Menu.NONE, context.getString(R.string.remove));
            contextMenu.add(Menu.NONE, R.id.done_item, Menu.NONE, context.getString(R.string.mark_done));
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

        Task task = Task.taskList.get(position);

        Context context = holder.title.getContext();

        Button button = holder.taskOpenButton;
        button.setOnLongClickListener((View btn) -> {
            Task.chosenTask = task;
            setPosition(position);

            return false;
        });

        TextView title = holder.title;
        title.setText(task.getDisplayName());

        TextView description = holder.description;
        description.setText(task.getDescription());

        TextView timeLeft = holder.timeLeft;
        String timeText;

        timeText = context.getString(R.string.time_left) + getTimeLeft(task.getStartTime().getTime().getTime() - Calendar.getInstance().getTime().getTime());

        timeLeft.setText(timeText);

        TextView datetime = holder.datetime;
        datetime.setText(getFormattedDate(task.getStartTime().getTime().getTime()));
    }

    private String getTimeLeft(long time){
        if (time <= 0)
            return "0";

        return time / 1000 / 3600 +  ":" + (time / 1000 / 60 - (time / 1000 / 3600) * 60);
    }

    private String getFormattedDate(long time){
        Date date = new Date(time);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);

        return simpleDateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        return Task.taskList.size();
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    
}
