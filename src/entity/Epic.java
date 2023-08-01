package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private final List<Integer> subTasksId;
    private LocalDateTime endTime;

    public Epic(String name, TaskType taskType, String description) {
        super(name, taskType, description, 0, LocalDateTime.MAX.toString());
        this.endTime = LocalDateTime.MIN;
        this.subTasksId = new ArrayList<>();

    }

    public Epic(String value) {
        super(value);
        if (startTime.isEqual(LocalDateTime.MAX)) {
            this.endTime = LocalDateTime.MIN;
        } else {
            this.endTime = this.startTime.plus(duration);
        }
        this.subTasksId = new ArrayList<>();
    }

    public Epic(int id, String name, TaskType taskType, String description, TaskStatus status, List<Integer> epicSubTasks) {
        super(name, taskType, description);
        this.id = id;
        this.status = status;
        this.subTasksId = epicSubTasks;
    }

    public List<Integer> getSubTasksId() {
        return subTasksId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void plusDuration(Duration duration) {
        this.duration = this.duration.plus(duration);
    }

    public void minusDuration(Duration duration) {
        this.duration = this.duration.minus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return epic.name.equals(name) && epic.description.equals(description);
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += 31 * hashCode + name.hashCode() + description.hashCode() + id + status.hashCode() + subTasksId.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return id + "," +
                startTime.toString() + "," +
                duration +
                ",EPIC," +
                name + "," +
                status + "," +
                description;
    }
}