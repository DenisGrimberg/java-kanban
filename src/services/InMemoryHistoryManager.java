package services;

import entity.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int INITIAL_CAPACITY = 10;

    private final LinkedList<Task> listLastTasks = new LinkedList<>();

    public List<Task> getHistory() {
        return new LinkedList<>(listLastTasks);
    }

    public void add(Task task) {
        if (listLastTasks.size() == INITIAL_CAPACITY) {
            listLastTasks.removeFirst();
        }
        listLastTasks.add(task);
    }
}
