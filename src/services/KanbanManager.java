package services;

import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class KanbanManager {

    private static int id = 0;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;

    public KanbanManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public static int generateId() {
        return ++id;
    }

    public void addTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public Task getTaskById(int id) {
        return tasks.getOrDefault(id, null);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void addEpic(Epic epic) {
        epic.setId(generateId());
        epic.setStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            checkEpicStatus(epic.getId());
        }
    }

    public Epic getEpicById(int id) {
        return epics.getOrDefault(id, null);
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.getEpicSubTasks()) {
                subTasks.remove(subtaskId);
            }
            epic.setEpicSubTasks(new ArrayList<>());
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void addSubTask(SubTask subtask) {
        subtask.setId(generateId());
        subtask.setStatus(TaskStatus.NEW);
        subTasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).getEpicSubTasks().add(subtask.getId());
        checkEpicStatus(subtask.getEpicId());
    }

    public void updateSubTask(SubTask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            checkEpicStatus(subtask.getEpicId());
        }
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.getOrDefault(id, null);
    }

    public ArrayList<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public void deleteSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            int epicId = subTasks.get(id).getEpicId();
            int epicSubTaskId = epics.get(epicId).getEpicSubTasks().indexOf(id);
            epics.get(epicId).getEpicSubTasks().remove(epicSubTaskId);
            subTasks.remove(id);
            checkEpicStatus(epicId);

        }
    }

    public void deleteAllSubTask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.setStatus(TaskStatus.NEW);
            epic.setEpicSubTasks(new ArrayList<>());
        }
    }

    private void checkEpicStatus(int epicId) {

        if (epics.get(epicId).getEpicSubTasks().size() == 0) {
            epics.get(epicId).setStatus(TaskStatus.NEW);
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer epicSubtaskId : epics.get(epicId).getEpicSubTasks()) {
            TaskStatus status = subTasks.get(epicSubtaskId).getStatus();
            if (!status.equals(TaskStatus.NEW)) {
                allTaskIsNew = false;
            }
            if (!status.equals(TaskStatus.DONE)) {
                allTaskIsDone = false;
            }
        }

        if (allTaskIsDone) {
            epics.get(epicId).setStatus(TaskStatus.DONE);
        } else if (allTaskIsNew) {
            epics.get(epicId).setStatus(TaskStatus.NEW);
        } else {
            epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
