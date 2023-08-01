package services;

import entity.Epic;
import entity.SubTask;
import entity.Task;

import java.util.List;

public interface TaskManager {

    void addTask(Task task);
    void updateTask(Task task);
    Task getTaskById(int id);
    List<Task> getAllTasks();
    void deleteTaskById(int id);
    void deleteAllTasks();

    void addEpic(Epic epic);
    void updateEpic(Epic epic);
    Epic getEpicById(int id);
    List<Epic> getAllEpics();
    void deleteEpicById(int id);
    void deleteAllEpics();
    List<Integer> getSubTasksOfEpic(Epic epic);

    void addSubTask(SubTask subTask);
    void updateSubTask(SubTask subTask);
    SubTask getSubTaskById(int id);
    List<SubTask> getAllSubTasks();
    void deleteSubTaskById(Integer id);
    void deleteAllSubTasks();
    void checkEpicStatus(Epic epic);
    List<Task> getHistory();
    HistoryManager getHistoryManager();
    List<Task> getPrioritizedTasks();
}
