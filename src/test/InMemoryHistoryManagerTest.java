package test;

import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.Managers;
import services.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    TaskManager inMemoryTaskManager;

    @BeforeEach
    public void setup() {
        inMemoryTaskManager = Managers.getDefault();
        Task task1 = new Task("Task1", TaskType.TASK, "Task1", 30, "2022-08-30T06:00:00");
        Task task2 = new Task("Task2", TaskType.TASK, "Task2", 30, "2022-08-30T08:00:00");
        Epic epic1 = new Epic("Epic1", TaskType.EPIC, "Epic1");
        SubTask subTask1 = new SubTask("SubTask1", TaskType.SUBTASK,  "SubTask1", 30, "2022-08-30T10:00:00", 3);

        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addSubTask(subTask1);
    }

    @Test
    void shouldAddTaskToEmptyHistory() {
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getSubTaskById(4);
        List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }

    @Test
    void addTaskWithSameTaskInHistory() {
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(1);
        List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "History don't return");
        int expected = 1;
        assertEquals(expected, history.size(), "History size not equal");
    }

    @Test
    void shouldRemoveFromBegin() {
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getHistoryManager().delete(1);
        List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }

    @Test
    void shouldRemoveFromMiddle() {
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getHistoryManager().delete(2);
        List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }

    @Test
    void shouldRemoveFromEnd() {
        inMemoryTaskManager.getTaskById(1);
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getEpicById(3);
        inMemoryTaskManager.getHistoryManager().delete(3);
        List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "History don't return");
        int expected = 2;
        assertEquals(expected, history.size(), "History size not equal");
    }

    @Test
    void shouldReturnHistory() {
        inMemoryTaskManager.getTaskById(1);
        List<Task> history = inMemoryTaskManager.getHistory();
        assertNotNull(history, "History don't return");
    }
}
