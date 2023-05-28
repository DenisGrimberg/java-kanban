package services;

import entity.Epic;
import entity.SubTask;
import entity.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void updateTask(Task task);

    Task getTaskById(int id);

    ArrayList<Task> getAllTasks();

    void deleteTaskById(int id);

    void deleteAllTasks();

    void addEpic(Epic epic);

    void updateEpic(Epic epic);

    Epic getEpicById(int id);

    ArrayList<Epic> getAllEpics();

    void deleteEpicById(int id);

    void deleteAllEpics();

    void addSubTask(SubTask subtask);

    void updateSubTask(SubTask subtask);

    SubTask getSubTaskById(int id);

    ArrayList<SubTask> getAllSubTasks();

    void deleteSubTaskById(int id);

    void deleteAllSubTask();

    void checkEpicStatus(int epicId);

    List<Task> getHistory();
}
