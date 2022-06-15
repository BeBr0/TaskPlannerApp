package edu.vlsu.taskplanner.tasks;

import java.util.ArrayList;
import java.util.List;

import edu.vlsu.taskplanner.Database;

public abstract class TaskList{
    private static final List<Task> taskList = new ArrayList<>();

    public static Task getByIndex(int index){
        return taskList.get(index);
    }

    public static List<Task> getTaskListClone(){
        return new ArrayList<>(taskList);
    }

    public static int getIndex(Task task){
        return taskList.indexOf(task);
    }

    public static int getCount(){
        return taskList.size();
    }

    public static Task getTaskByName(String title){
        for (Task task: taskList){
            if (task.getDisplayName().equals(title)){
                return task;
            }
        }

        return null;
    }

    /** Используется ТОЛЬКО при загрузке приложения! */
    public static void addToListOnly(Task task){
        TaskList.taskList.add(task);
    }

    public static void add(Task task){
        Database.getInstance().writeTask(task);

        TaskList.taskList.add(task);
    }

    public static void remove(Task task){
        TaskList.taskList.remove(task);

        Database.getInstance().getWritableDatabase().execSQL("DELETE FROM tasks WHERE id = " + task.getId());
    }

    public static void update(Task task){
        if (getTaskByName(task.getDisplayName()) == null){
            add(task);
            return;
        }

        for (Task t: TaskList.taskList){
            if (t.getDisplayName().equals(task.getDisplayName())) {
                TaskList.taskList.set(TaskList.taskList.indexOf(t), task);
                remove(task);
                add(task);
            }
        }
    }

    public static void sort(TaskViewAdapter taskViewAdapter){
        List<Task> sorted = new ArrayList<>();
        for (TaskGroup taskGroup: TaskGroup.values()) {
            for (int i = 0; i < taskList.size(); i++) {
                if (taskList.get(i).getTaskGroup() == taskGroup) {
                    long minTime = -2;
                    Task minTimeTask = null;
                    for (int j = 0; j < taskList.size(); j++) {
                        if (taskList.get(i).getTaskGroup() == taskGroup) {
                            if ((taskList.get(j).getStartTime().getTime().getTime() <= minTime || minTime == -2) && !sorted.contains(taskList.get(j))) {
                                minTime = taskList.get(j).getStartTime().getTime().getTime();
                                minTimeTask = taskList.get(j);
                            }
                        }
                    }
                    sorted.add(minTimeTask);
                    taskViewAdapter.notifyItemMoved(taskList.indexOf(minTimeTask), sorted.size() - 1);
                }
            }
        }
        taskList.clear();
        taskList.addAll(sorted);
    }
}
