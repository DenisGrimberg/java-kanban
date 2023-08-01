package services;

import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskStatus;
import exception.TaskOverlapAnotherTaskException;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int id = 0;
    protected final HashMap<Integer, Task> tasks;
    protected final HashMap<Integer, SubTask> subTasks;
    protected final HashMap<Integer, Epic> epics;
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> tasksSortedByStartTime;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.tasksSortedByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    private int generateId() {
        return ++id;
    }

    @Override
    public void addTask(Task task) {
        if (task == null || tasks.containsValue(task) || isTaskOverlapTasksByTime(task, "addTask")) {
            return;
        }
        task.setId(generateId());
        tasks.put(task.getId(), task);
        tasksSortedByStartTime.add(task);
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId()) &&
                !isTaskOverlapTasksByTime(task, "updateTask")) {
            tasks.put(task.getId(), task);
            tasksSortedByStartTime.remove(task);
            tasksSortedByStartTime.add(task);
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        } else {
            return null;
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.delete(id);
            tasksSortedByStartTime.remove(tasks.get(id));
            tasks.remove(id);
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.delete(id);
        }
        tasks.clear();
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null || epics.containsValue(epic)) {
            return;
        }
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);

            for (Integer epicSubTaskId : epic.getSubTasksId()) {
                historyManager.delete(epicSubTaskId);
                subTasks.remove(epicSubTaskId);
            }
            historyManager.delete(id);
            epics.remove(id);
        }
    }

    private Epic getEpicBySubTask(SubTask subTask) {
        for (Epic epic : epics.values()) {
            if (epic.getSubTasksId().contains(subTask.getId())) {
                return epic;
            }
        }
        return null;
    }

    @Override
    public List<Integer> getSubTasksOfEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            return epic.getSubTasksId();
        }
        return Collections.emptyList();
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.delete(id);
        }
        for (Map.Entry<Integer, SubTask> subTaskEntry : subTasks.entrySet()) {
            tasksSortedByStartTime.remove(subTaskEntry.getValue());
            historyManager.delete(subTaskEntry.getKey());
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        if (subTask == null || subTasks.containsValue(subTask) ||
                isTaskOverlapTasksByTime(subTask, "addSubTask")) {
            return;
        }
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        tasksSortedByStartTime.add(subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.getSubTasksId().add(subTask.getId());
        setDurationStartTimeAndEndTimeOfEpic(epic, subTask);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        if (subTask != null &&
                subTasks.containsKey(subTask.getId()) &&
                !isTaskOverlapTasksByTime(subTask, "updateSubTask")) {
            Epic epic = getEpicBySubTask(subTask);
            if (epic != null) {
                SubTask oldSubTask = subTasks.get(subTask.getId());
                epic.minusDuration(oldSubTask.getDuration());
                setDurationStartTimeAndEndTimeOfEpic(epic, subTask);
                subTasks.put(subTask.getId(), subTask);
                checkEpicStatus(epic);
                tasksSortedByStartTime.remove(subTask);
                tasksSortedByStartTime.add(subTask);
            }
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            historyManager.add(subTasks.get(id));
            return subTask;
        } else {
            return null;
        }
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public void deleteSubTaskById(Integer id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            tasksSortedByStartTime.remove(subTask);
            historyManager.delete(id);
            Epic epic = getEpicBySubTask(subTask);
            if (epic != null) {
                List<Integer> subTasksOfEpic = epic.getSubTasksId();
                subTasksOfEpic.remove(id);
                if (subTasksOfEpic.isEmpty()) {
                    epic.setStartTime(LocalDateTime.MAX);
                    epic.setEndTime(LocalDateTime.MIN);
                } else {
                    if (isEqualSubTasksTimeToEpicsTime(subTask, epic)) {
                        epic.minusDuration(subTask.getDuration());
                        findNewTimeAndTimeOfEpic(epic);
                    }
                }
                checkEpicStatus(epic);
            }
        }
    }


    @Override
    public void deleteAllSubTasks() {
        for (Map.Entry<Integer, SubTask> removedSubTask : subTasks.entrySet()) {
            tasksSortedByStartTime.remove(removedSubTask.getValue());
            historyManager.delete(removedSubTask.getKey());
        }
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubTasksId().clear();
            epic.setDuration(0);
        }
    }

    @Override
    public void checkEpicStatus(Epic epic) {

        if (epic.getSubTasksId().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer epicSubTaskId : epic.getSubTasksId()) {
            TaskStatus status = subTasks.get(epicSubTaskId).getStatus();
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

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void findNewTimeAndTimeOfEpic(Epic epic) {
        List<Integer> subTaskOfEpic = new ArrayList<>(epic.getSubTasksId());
        epic.setStartTime(LocalDateTime.MAX);
        epic.setEndTime(LocalDateTime.MIN);
        for (Integer subTaskId : subTaskOfEpic) {
            SubTask subTask = subTasks.get(subTaskId);
            LocalDateTime startTimeOfEpic = epic.getStartTime();
            LocalDateTime startTimeOfSubTask = subTask.getStartTime();
            LocalDateTime endTimeOfEpic = epic.getEndTime();
            LocalDateTime endTimeOfSubTask = subTask.getEndTime();
            if (startTimeOfEpic.isAfter(startTimeOfSubTask)) {
                epic.setStartTime(startTimeOfSubTask);
            }
            if (endTimeOfEpic.isBefore(endTimeOfSubTask)) {
                epic.setEndTime(endTimeOfSubTask);
            }
        }
    }

    public void setDurationStartTimeAndEndTimeOfEpic(Epic epic, SubTask subTask) {
        epic.plusDuration(subTask.getDuration());
        LocalDateTime startTimeOfEpic = epic.getStartTime();
        LocalDateTime startTimeOfSubTask = subTask.getStartTime();
        LocalDateTime endTimeOfEpic = epic.getEndTime();
        LocalDateTime endTimeOfSubTask = subTask.getEndTime();
        if (startTimeOfEpic.isAfter(startTimeOfSubTask)) {
            epic.setStartTime(startTimeOfSubTask);
        }
        if (endTimeOfEpic.isBefore(endTimeOfSubTask)) {
            epic.setEndTime(endTimeOfSubTask);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksSortedByStartTime);
    }

    public boolean isTaskOverlapTasksByTime(Task checkedTask, String methodName) {
        for (Task task : tasksSortedByStartTime) {
            if (task.equals(checkedTask)) {
                return false;
            }
            if (isTaskOverlapAnotherTask(checkedTask, task)) {
                throw new TaskOverlapAnotherTaskException("Fail to " + methodName + " because " +
                        checkedTask.getName() + " overlap " + task.getName());
            }
        }
        return false;
    }

    public boolean isTaskOverlapAnotherTask(Task checkedTask, Task anotherTask) {
        LocalDateTime checkedTaskStartTime = checkedTask.getStartTime();
        LocalDateTime checkedTaskEndTime = checkedTask.getEndTime();
        LocalDateTime taskStartTime = anotherTask.getStartTime();
        LocalDateTime taskEndTime = anotherTask.getEndTime();
        return checkedTaskStartTime.isEqual(taskStartTime) && checkedTaskEndTime.isEqual(taskEndTime) ||
                (checkedTaskStartTime.isBefore(taskEndTime)) && (taskStartTime.isBefore(checkedTaskEndTime));
    }

    private boolean isEqualSubTasksTimeToEpicsTime(SubTask subTask, Epic epic) {
        return subTask.getStartTime().isEqual(epic.getStartTime()) ||
                subTask.getEndTime().isEqual(epic.getEndTime());
    }
}
