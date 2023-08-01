package entity;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected TaskStatus status;
    protected TaskType taskType;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, TaskType taskType, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.taskType = taskType;
        this.duration = Duration.ofMinutes(0);
        this.startTime = LocalDateTime.MAX;
    }

    public Task(String name, TaskType taskType, String description, long durationMinutes, String startTime) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
        this.taskType = taskType;
        this.duration = Duration.ofMinutes(durationMinutes);
        this.startTime = LocalDateTime.parse(startTime);
    }

    public Task(String value) {
        String[] task = value.split(",");
        this.id = Integer.parseInt(task[0]);
        this.startTime = LocalDateTime.parse(task[1]);
        this.duration = Duration.parse(task[2]);
        this.taskType = TaskType.valueOf(task[3]);
        this.name = task[4];
        this.status = TaskStatus.valueOf(task[5]);
        this.description = task[6];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getTaskType() {
        return TaskType.TASK;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(long minutes) {
        this.duration = Duration.ofMinutes(minutes);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime.isEqual(LocalDateTime.MAX)) {
            return LocalDateTime.MIN;
        } else {
            return startTime.plus(duration);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return task.name.equals(name) && task.description.equals(description) && task.startTime.isEqual(startTime) &&
                task.duration.equals(duration);
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += 31 * hashCode + name.hashCode() + description.hashCode() + status.hashCode();
        return hashCode;
    }

    @Override
    public String toString() {
        return id + "," +
                startTime.toString() + "," +
                duration +
                ",TASK," +
                name + "," +
                status + "," +
                description;
    }
}
