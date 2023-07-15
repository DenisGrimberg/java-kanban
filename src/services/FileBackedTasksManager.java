package services;

import entity.*;
import exception.ManagerSaveException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static final String HEADER_STR = "id,type,name,status,description,epic";
    private final File file;

    public FileBackedTasksManager(File file) {
        super();
        this.file = file;
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {

            bufferedWriter.write(HEADER_STR + "\n");
            for (Task task : getAllTasks()) {
                bufferedWriter.write(CSVTaskFormatter.toString(task) + "\n");
            }

            for (Epic epic : getAllEpics()) {
                bufferedWriter.write(CSVTaskFormatter.toString(epic) + "\n");
            }

            for (SubTask subTask : getAllSubTasks()) {
                bufferedWriter.write(CSVTaskFormatter.toString(subTask) + "," + subTask.getEpicId() + "\n");
            }

            bufferedWriter.write("\n" + CSVTaskFormatter.historyToString(historyManager));
        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage(), exception.getCause());
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        List<String> stringList;
        Map<Integer, Task> taskMap = new HashMap<>();
        int newID = 0;

        try {
            stringList = Files.readAllLines(file.toPath());

            for (int i = 1; i < stringList.size(); i++) {
                String line = stringList.get(i);

                if (line.isBlank()) {
                    break;
                }

                Task task = CSVTaskFormatter.fromString(stringList.get(i));
                taskMap.put(task.getId(), task);

                if (task.getTaskType() == TaskType.TASK) {
                    manager.tasks.put(task.getId(), task);
                }

                if (task.getTaskType() == TaskType.EPIC) {
                    Epic epic = (Epic) task;
                    manager.epics.put(epic.getId(), epic);
                }

                if (task.getTaskType() == TaskType.SUBTASK) {
                    SubTask subTask = (SubTask) task;
                    manager.subTasks.put(subTask.getId(), subTask);

                    if (!manager.epics.isEmpty()) {
                        Epic epicInSub = manager.epics.get(subTask.getEpicId());
                        epicInSub.getEpicSubTasks().add(subTask.getId());
                    }
                }

                if (task.getId() > newID) {
                    newID = task.getId();
                }
            }

            if (stringList.size() > 1) {
                String history = stringList.get(stringList.size() - 1);
                List<Integer> list = CSVTaskFormatter.historyFromString(history);

                for (Integer id : list) {
                    manager.historyManager.add(taskMap.get(id));
                }
            }
            id = newID;
            return manager;

        } catch (IOException exception) {
            throw new ManagerSaveException(exception.getMessage(), exception.getCause());
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        save();
        return subTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    public static void main(String[] args) {
        File testFile = new File("src/data/testFile.csv");
        FileBackedTasksManager fbTasksManager = new FileBackedTasksManager(testFile);

        fbTasksManager.addTask(new Task("Task1", TaskType.TASK, "task1_desc"));
        fbTasksManager.addTask(new Task("Task2", TaskType.TASK, "task2_desc"));
        fbTasksManager.addEpic(new Epic("Epic1", TaskType.EPIC, "epic1_desc"));
        fbTasksManager.addEpic(new Epic("Epic2", TaskType.EPIC, "epic2_desc"));
        fbTasksManager.addSubTask(new SubTask("SubTask1", TaskType.SUBTASK, "subTask1_desc", 3));
        fbTasksManager.addSubTask(new SubTask("SubTask2", TaskType.SUBTASK, "subTask2_desc", 3));

        fbTasksManager.getTaskById(1);
        fbTasksManager.getTaskById(2);
        fbTasksManager.getEpicById(3);
        fbTasksManager.getSubTaskById(5);
        fbTasksManager.getSubTaskById(6);

        FileBackedTasksManager fbTasksManager2 = loadFromFile(new File("src/data/testFile.csv"));
        List<Task> tasks = fbTasksManager2.getAllTasks();
        for (Task task : tasks) {
            System.out.println(task);
        }

        List<Epic> epics = fbTasksManager2.getAllEpics();
        for (Epic epic : epics) {
            System.out.println(epic);
        }

        List<SubTask> subTasks = fbTasksManager2.getAllSubTasks();
        for (SubTask subTask : subTasks) {
            System.out.println(subTask);
        }
    }
}
