package entity;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> epicSubTasks;

    public Epic(String title, String description) {
        super(title, description);
        this.epicSubTasks = new ArrayList<>();
    }

    public ArrayList<Integer> getEpicSubTasks() {
        return epicSubTasks;
    }

    public void setEpicSubTasks(ArrayList<Integer> epicSubTasks) {
        this.epicSubTasks = epicSubTasks;
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