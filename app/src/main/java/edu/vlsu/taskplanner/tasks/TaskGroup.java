package edu.vlsu.taskplanner.tasks;

import android.content.Context;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import edu.vlsu.taskplanner.R;

public enum TaskGroup {
    EDUCATION(R.string.group_education, "Education"),
    JOB(R.string.group_job, "Job"),
    HOME(R.string.group_home, "Home"),
    OTHER(R.string.group_other, "Other");

    private @StringRes final int name;
    public String systemName;

    TaskGroup(@StringRes int name, String systemName) {
        this.name = name;
        this.systemName = systemName;
    }

    public @StringRes int getName() {
        return name;
    }

    public static TaskGroup getItem(String name){
        for (TaskGroup taskGroup: TaskGroup.values()){
            if (name.equals(taskGroup.systemName))
                return taskGroup;
        }

        return null;
    }

    public static TaskGroup getItem(int id){
        return values()[id];
    }

    public static int getId(TaskGroup taskGroup){
        for (int i = 0; i< values().length; i++){
            if (values()[i] == taskGroup)
                return i;
        }

        return -1;
    }
}
