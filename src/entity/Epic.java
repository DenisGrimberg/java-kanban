package entity;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> epicSubTasks;

    public Epic(String name, TaskType taskType, String description) {
        super(name, taskType, description);
        this.epicSubTasks = new ArrayList<>();

    }

    public Epic(int id, String name, TaskType taskType, String description, TaskStatus status, List<Integer> epicSubTasks) {
        super(name, taskType, description);
        this.id = id;
        this.status = status;
        this.epicSubTasks = epicSubTasks;
    }

    public List<Integer> getEpicSubTasks() {
        return epicSubTasks;
    }

    public void deleteIdOfSubTask(Integer id) {
        epicSubTasks.remove(id);

    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", epicSubTasks='" + epicSubTasks + '\'' +
                '}';
    }
}