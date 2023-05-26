package services;

import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class KanbanManager {

    private static int id = 0;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;

    public KanbanManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    private static int generateId() {
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
        return tasks.get(id);
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
        int idUpdatedEpic = epic.getId();

        if (epics.containsKey(idUpdatedEpic)) {
            Epic newEpic = new Epic(idUpdatedEpic, epic.getName(), epic.getDescription(),
                    epic.getStatus(), epic.getEpicSubTasks());

            epics.put(idUpdatedEpic, newEpic);
            checkEpicStatus(idUpdatedEpic);
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
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void addSubTask(SubTask subtask) {
        subtask.setId(generateId());
        subtask.setStatus(TaskStatus.NEW);
        int epicId = subtask.getEpicId();
        subTasks.put(subtask.getId(), subtask);
        if (epics.containsKey(epicId)) {
            epics.get(epicId).getEpicSubTasks().add(subtask.getId());
            checkEpicStatus(epicId);
        }
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
            epics.get(epicId).deleteIdOfSubTask(id);
            subTasks.remove(id);
            checkEpicStatus(epicId);

        }
    }

    public void deleteAllSubTask() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            checkEpicStatus(epic.getId());
            epic.getEpicSubTasks().clear();
        }
    }

    private void checkEpicStatus(int epicId) {

        Epic epic = epics.get(epicId);

        if (epic.getEpicSubTasks().isEmpty()) {
            epics.get(epicId).setStatus(TaskStatus.NEW);
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer epicSubtaskId : epic.getEpicSubTasks()) {
            TaskStatus status = subTasks.get(epicSubtaskId).getStatus();
            if (!status.equals(TaskStatus.NEW)) {
                allTaskIsNew = false;
            }
            if (!status.equals(TaskStatus.DONE)) {
                allTaskIsDone = false;
            }
        }

        if (allTaskIsDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allTaskIsNew) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
