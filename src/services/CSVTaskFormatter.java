package services;

import entity.*;

import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormatter {

    public static String toString(Task task) {
        return task.getId() + "," + task.getTaskType() + "," + task.getName() + ","
                + task.getStatus() + "," + task.getDescription();
    }

    public static Task fromString(String value) {
        String[] arrayTask = value.split(",");
        int taskId = Integer.parseInt(arrayTask[0]);
        TaskType taskType = TaskType.valueOf(arrayTask[1]);
        String taskName = arrayTask[2];
        TaskStatus taskStatus = TaskStatus.valueOf(arrayTask[3]);
        String taskDescription = arrayTask[4];
        Task task = null;

        switch (taskType) {
            case TASK:
                task = new Task(taskName, taskType, taskDescription);
                task.setStatus(taskStatus);
                break;
            case EPIC:
                task = new Epic(taskName, taskType, taskDescription);
                task.setStatus(taskStatus);
                break;
            case SUBTASK:
                task = new SubTask(taskName, taskType, taskDescription, Integer.parseInt(arrayTask[5]));
                task.setStatus(taskStatus);
                break;
        }
        task.setId(taskId);
        return task;
    }

    public static String historyToString(HistoryManager manager) {
        String lastLine;
        List<Task> taskList = manager.getHistory();
        List<String> newList = new ArrayList<>();

        for (Task task : taskList) {
            newList.add(String.valueOf(task.getId()));
        }

        lastLine = String.join(",", newList);

        return lastLine;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> newList = new ArrayList<>();
        String[] line = value.split(",");

        for (String str : line) {
            newList.add(Integer.parseInt(str));
        }
        return newList;
    }
}
