package services;

import entity.Epic;
import entity.Subtask;
import entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    void saveTask(Task task);
    void saveEpic(Epic epic);
    void saveSubtask(Subtask subtask);

    ArrayList<Task> getTasksList();
    ArrayList<Epic> getEpicsList();
    ArrayList<Subtask> getSubtaskList();

    void deleteTasks();
    void deleteEpics();
    void deleteSubtasks();

    Task getTaskByIdNumber(int idNumber);
    Epic getEpicTaskByIdNumber(int idNumber);
    Subtask getSubTaskByIdNumber(int idNumber);

    void updateTask(Task task);
    void updateEpic(Epic epic);
    void updateSubtask(Subtask subtask);

    void deleteTaskById(int idNumber);
    void deleteEpicById(int idNumber);
    void deleteSubtaskById(int idNumber);

    ArrayList<Subtask> subtaskList(int idNumber);
    List<Task> getHistory();

    Task creationTask(Task task);
    Epic creationEpic(Epic epic);
    Subtask creationSubtask(Subtask subtask);

    List<Task> getPrioritizedTasks();
}
