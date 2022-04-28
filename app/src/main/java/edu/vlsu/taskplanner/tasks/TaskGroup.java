package edu.vlsu.taskplanner.tasks;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

import edu.vlsu.taskplanner.R;

public enum TaskGroup {
    EDUCATION(R.string.group_education),
    JOB(R.string.group_job),
    HOME(R.string.group_home),
    OTHER(R.string.group_other);

    private @StringRes
    int name;

    TaskGroup(@StringRes int name) {
        this.name = name;
    }

    public int getName() {
        return name;
    }
}
