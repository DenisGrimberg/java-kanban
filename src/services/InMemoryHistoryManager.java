package services;

import entity.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int INITIAL_CAPACITY = 10;

    private final List<Task> listLastTasks = new ArrayList<>(INITIAL_CAPACITY);

    public List<Task> getHistory() {
        return listLastTasks;
    }

    public void add(Task task) {
        if (listLastTasks.size() == INITIAL_CAPACITY) {
            listLastTasks.remove(0);
        }
        listLastTasks.add(task);
    }
}
