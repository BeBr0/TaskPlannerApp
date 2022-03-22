package edu.vlsu.taskplanner.tasks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.vlsu.taskplanner.R;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskHolder> {

    public class TaskHolder extends RecyclerView.ViewHolder{

//        Button taskButton;
        TextView title, description, time;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
//            this.taskButton = itemView.findViewById(R.id.task_item_button);
            this.title = itemView.findViewById(R.id.title);
            this.description = itemView.findViewById(R.id.description);
            this.time = itemView.findViewById(R.id.time);
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

        // Set item views based on your views and data model
        TextView title = holder.title;
        title.setText(task.getDisplayName());

        TextView description = holder.description;
        description.setText(task.getDescription());

        TextView time = holder.time;
        String timeText;
        if (task.getEndTime() == null) {
            timeText = task.getStartTime().getTime().getHours() + ":" + task.getStartTime().getTime().getMinutes();
        }
        else{
            timeText = task.getStartTime().getTime().getHours() + ":" + task.getStartTime().getTime().getMinutes() +
                    " - " + task.getEndTime().getTime().getHours() + ":" + task.getEndTime().getTime().getMinutes();
        }
        time.setText(timeText);
    }

    @Override
    public int getItemCount() {
        return Task.taskList.size();
    }
}
