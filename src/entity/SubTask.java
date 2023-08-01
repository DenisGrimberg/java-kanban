package entity;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, TaskType taskType, String description, int epicId) {
        super(name, taskType, description);
        this.epicId = epicId;
    }

    public SubTask(String name, TaskType taskType, String description, long durationMinutes, String startTime, int epicId) {
        super(name, taskType, description, durationMinutes, startTime);
        this.epicId = epicId;
    }

    public SubTask(String value) {
        super(value);
        String[] subTask = value.split(",");
        this.epicId = Integer.parseInt(subTask[7]);
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getTaskType() {
        return TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubTask)) return false;
        SubTask subTask = (SubTask) o;
        return subTask.name.equals(name) && subTask.description.equals(description);
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += 31 * hashCode + name.hashCode() + description.hashCode() + id + status.hashCode() + epicId;
        return hashCode;
    }

    @Override
    public String toString() {
        return id + "," +
                startTime.toString() + "," +
                duration +
                ",SUBTASK," +
                name + "," +
                status + "," +
                description + "," +
                epicId;
    }
}