package services;

import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskType;
import exception.ManagerLoadException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader {

    private FileLoader() {

    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fbtManager = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(file.getName()))) {
            br.readLine();
            while (br.ready()) {
                String taskLine = br.readLine();
                if (!taskLine.isEmpty()) {
                    tasksFromString(taskLine, fbtManager);
                } else {
                    String historyLine = br.readLine();
                    if (historyLine != null) {
                        historyFromString(historyLine, fbtManager);
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Can't load from file");
        }
        return fbtManager;
    }

    private static void tasksFromString(String taskLine, FileBackedTasksManager fileBackedTasksManager) {
        String[] taskFields = taskLine.split(",");
        try {
            TaskType taskType = TaskType.valueOf(taskFields[3]);
            switch (taskType) {
                case TASK -> {
                    Task task = new Task(taskLine);
                    fileBackedTasksManager.addTasksFromFile(task);
                }
                case EPIC -> {
                    Epic epic = new Epic(taskLine);
                    fileBackedTasksManager.addTasksFromFile(epic);
                }
                case SUBTASK -> {
                    SubTask subTask = new SubTask(taskLine);
                    fileBackedTasksManager.addTasksFromFile(subTask);
                }
            }
        } catch (IllegalArgumentException e) {
            throw new ManagerLoadException("Wrong type of task");
        }
    }

    private static void historyFromString(String historyLine, FileBackedTasksManager fileBackedTasksManager) {
        String[] historyFields = historyLine.split(",");
        for (String field : historyFields) {
            int id = Integer.parseInt(field);
            fileBackedTasksManager.getTaskById(id);
            fileBackedTasksManager.getEpicById(id);
            fileBackedTasksManager.getSubTaskById(id);
        }
    }
}
