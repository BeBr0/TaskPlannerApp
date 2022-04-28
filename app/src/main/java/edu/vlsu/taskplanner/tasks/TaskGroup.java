package edu.vlsu.taskplanner.tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskGroup {

    private String name;
    private String description;

    private final List<Task> contentTask = new ArrayList<>();

    public TaskGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Task> getContentTask() {
        return contentTask;
    }
}
