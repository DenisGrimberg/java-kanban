package services;

import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public int generateId() {
        return ++id;
    }

    @Override
    public void addTask(Task task) {
        if (task != null) {
            task.setId(generateId());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);

        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic != null) {
            epic.setId(generateId());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int idUpdatedEpic = epic.getId();

        if (epics.containsKey(idUpdatedEpic)) {
            Epic newEpic = new Epic(idUpdatedEpic, epic.getName(), epic.getDescription(),
                    epic.getStatus(), epic.getEpicSubTasks());

            epics.put(idUpdatedEpic, newEpic);
            checkEpicStatus(idUpdatedEpic);
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);

        return epic;
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);

            for (Integer subtaskId : epic.getEpicSubTasks()) {
                historyManager.remove(subtaskId);
                subTasks.remove(subtaskId);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        subTasks.clear();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        if (subtask != null) {
            subtask.setId(generateId());
            int epicId = subtask.getEpicId();
            subTasks.put(subtask.getId(), subtask);
            if (epics.containsKey(epicId)) {
                epics.get(epicId).getEpicSubTasks().add(subtask.getId());
                checkEpicStatus(epicId);
            }
        }
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        if (subTasks.containsKey(subtask.getId())) {
            subTasks.put(subtask.getId(), subtask);
            checkEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        historyManager.add(subTask);

        return subTask;
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (subTasks.containsKey(id)) {
            int epicId = subTasks.get(id).getEpicId();
            epics.get(epicId).deleteIdOfSubTask(id);
            historyManager.remove(id);
            subTasks.remove(id);
            checkEpicStatus(epicId);
        }
    }

    @Override
    public void deleteAllSubTask() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            SubTask subTask = subTasks.get(id);
            if (subTask != null) {
                Epic epic = epics.get(subTask.getEpicId());
                if (epic != null) {
                    epic.getEpicSubTasks().clear();
                    checkEpicStatus(epic.getId());
                }
            }
        }
        subTasks.clear();
    }

    @Override
    public void checkEpicStatus(int epicId) {

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

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
