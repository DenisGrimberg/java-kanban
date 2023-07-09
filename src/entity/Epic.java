package entity;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> epicSubTasks;

    public Epic(String name, String description) {
        super(name, description);
        this.epicSubTasks = new ArrayList<>();
    }

    public Epic(int id, String name, String description, TaskStatus status, ArrayList<Integer> epicSubTasks) {
        super(name, description);
        this.id = id;
        this.status = status;
        this.epicSubTasks = epicSubTasks;
    }

    public ArrayList<Integer> getEpicSubTasks() {
        return epicSubTasks;
    }

    public void deleteIdOfSubTask(Integer id) {
        epicSubTasks.remove(id);

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