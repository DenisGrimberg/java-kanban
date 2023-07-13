package entity;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, TaskType taskType, String description, int epicId) {
        super(name, taskType, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public String taskToString() {
        return getId() + "," + TaskType.SUBTASK  + "," + getName()  + "," + getStatus() + ","
                + getDescription()  + "," + getEpicId();
    }
}