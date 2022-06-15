package edu.vlsu.taskplanner.tasks;

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

    public static TaskGroup getItemByName(String name){
        for (TaskGroup taskGroup: TaskGroup.values()){
            if (taskGroup.systemName.equals(name))
                return taskGroup;
        }

        return null;
    }
}
