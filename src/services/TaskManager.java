package services;

import entity.Epic;
import entity.Subtask;
import entity.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    int saveTask(Task task);
    int saveEpic(Epic epic);
    int saveSubtask(Subtask subtask);

    ArrayList<Task> getTasksList();
    ArrayList<Epic> getEpicsList();
    ArrayList<Subtask> getSubtaskList();

    void deleteTasks();
    void deleteEpics();
    void deleteSubtasks();

    Task getTaskByIdNumber(int idNumber);
    Epic getEpicTaskByIdNumber(int idNumber);
    Subtask getSubTaskByIdNumber(int idNumber);

    int updateTask(Task task);
    int updateEpic(Epic epic);
    int updateSubtask(Subtask subtask);

    Task deleteTaskById(int idNumber);
    Epic deleteEpicById(int idNumber);
    Subtask deleteSubtaskById(int idNumber);

    ArrayList<Subtask> subtaskList(int idNumber);
    List<Task> getHistory();

    Task creationTask(Task task);
    Epic creationEpic(Epic epic);
    Subtask creationSubtask(Subtask subtask);

    Set<Task> getPrioritizedTasks();
}
