package test;

import entity.Epic;
import entity.SubTask;
import entity.Task;
import entity.TaskType;
import exception.ManagerLoadException;
import exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.FileBackedTasksManager;
import services.FileLoader;
import services.Managers;
import services.TaskManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    FileBackedTasksManager fbtManager;
    File file;

    @Override
    FileBackedTasksManager createTaskManager() {
        return (FileBackedTasksManager) Managers.getFileBacked();
    }

    @BeforeEach
    public void filedBackedSetup() {
        fbtManager = (FileBackedTasksManager) Managers.getFileBacked();
        String fileName = fbtManager.getFileName();
        file = new File(fileName);
        try (Writer fileWriter = new FileWriter(fileName)) {
            fileWriter.write(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLoadWithEmptyListOfTasks() {
        TaskManager loadedFromFile;
        try {
            loadedFromFile = FileLoader.loadFromFile(file);
        } catch (ManagerLoadException e) {
            loadedFromFile = null;
        }
        assertEquals(fbtManager, loadedFromFile, "Objects not equal");
    }

    @Test
    void testSaveAndLoadWithEpicsWithoutSubtasks() {
        Task task1 = new Task("Task1", TaskType.TASK, "Task1", 30, "2023-07-30T12:00:00");
        Task task2 = new Task("Task2", TaskType.TASK, "Task2", 30, "2023-07-28T12:00:00");
        Epic epic1 = new Epic("Epic1", TaskType.EPIC, "Epic1");

        fbtManager.addTask(task1);
        fbtManager.addTask(task2);
        fbtManager.addEpic(epic1);
        fbtManager.getTaskById(1);
        fbtManager.getSubTaskById(2);
        fbtManager.getEpicById(3);

        TaskManager loadedFromFile = FileLoader.loadFromFile(file);
        assertEquals(fbtManager, loadedFromFile, "Objects not equal");
    }

    @Test
    void testSaveAndLoadWithEmptyListOfHistory() {
        Task task1 = new Task("Task1", TaskType.TASK, "Task1", 30, "2022-08-30T12:00:00");
        Task task2 = new Task("Task2", TaskType.TASK, "Task2", 30, "2022-08-28T12:00:00");
        Epic epic1 = new Epic("Epic1", TaskType.EPIC, "Epic1");

        fbtManager.addTask(task1);
        fbtManager.addTask(task2);
        fbtManager.addEpic(epic1);

        TaskManager loadedFromFile = FileLoader.loadFromFile(file);
        assertEquals(fbtManager, loadedFromFile, "Objects not equal");
    }

    @Test
    void loadWithTasksEpicsSubtasksHistory() {
        Task task1 = new Task("Task1", TaskType.TASK, "Task1", 30, "2022-08-30T12:00:00");
        Task task2 = new Task("Task2", TaskType.TASK, "Task2", 30, "2022-08-30T15:00:00");
        Epic epic1 = new Epic("Epic1", TaskType.EPIC, "Epic1");
        SubTask subTask1 = new SubTask("Subtask1", TaskType.SUBTASK, "Subtask1"
                , 30, "2022-08-30T07:00:00", 3);
        SubTask subTask2 = new SubTask("Subtask2", TaskType.SUBTASK, "Subtask2"
                , 30, "2022-08-30T09:00:00", 3);

        fbtManager.addTask(task1);
        fbtManager.addTask(task2);
        fbtManager.addEpic(epic1);
        fbtManager.addSubTask(subTask1);
        fbtManager.addSubTask(subTask2);
        fbtManager.getTaskById(1);
        fbtManager.getTaskById(2);
        fbtManager.getEpicById(3);
        fbtManager.getSubTaskById(4);
        fbtManager.getSubTaskById(5);

        TaskManager loadedFromFile = FileLoader.loadFromFile(file);
        assertEquals(fbtManager, loadedFromFile, "Objects not equal");
    }

    @Test
    void shouldNotLoadFromFile() {
        File failFile = new File("fail.csv");
        Task task1 = new Task("Task1", TaskType.TASK, "Task1", 30, "2022-08-30T12:00:00");
        Task task2 = new Task("Task2", TaskType.TASK, "Task2", 30, "2022-08-28T12:00:00");
        Epic epic1 = new Epic("Epic1", TaskType.EPIC, "Epic1");

        fbtManager.addTask(task1);
        fbtManager.addTask(task2);
        fbtManager.addEpic(epic1);
        fbtManager.getTaskById(1);
        fbtManager.getTaskById(2);
        fbtManager.getEpicById(3);

        Exception exception = assertThrows(ManagerLoadException.class, () -> FileLoader.loadFromFile(failFile));
        assertEquals("Can't load from file", exception.getMessage(), "Exception not thrown");
    }

    @Test
    void shouldNotGetTaskFromString() {
        Task testTask = new Task("Task1", TaskType.TASK, "Task1", 30, "2022-08-30T12:00:00");
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,startTime,endTime,type,name,status,description,epic\n");
            fileWriter.write(testTask.getId() + "," +
                    testTask.getStartTime().toString() + ","
                    + testTask.getEndTime().toString() +
                    ",TASKK," + testTask.getName() + "," + testTask.getStatus() +
                    "," + testTask.getDescription());
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file" + e.getMessage());
        }
        Exception exception = assertThrows(ManagerLoadException.class, () -> FileLoader.loadFromFile(file));
        assertEquals("Wrong type of task", exception.getMessage(), "Exception not thrown");
    }

    @Test
    void shouldNotSaveToFile() {
        Task task1 = new Task("Task1", TaskType.TASK, "Task1", 30, "2022-08-30T12:00:00");
        fbtManager.setFileName(".");
        Exception exception = assertThrows(ManagerSaveException.class, () -> fbtManager.addTask(task1));
        assertEquals("Can't save to file", exception.getMessage(), "Exception not thrown");
    }
}
