package services;

import entity.*;
import exception.ManagerSaveException;

import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;

public class FileBackedTasksManager extends InMemoryTaskManager {

    public static final String HEADER_STR = "id,startTime,duration,type,name,status,description,epicId";
    public String fileName = "src/data/testFile.csv";

    private void save() {
        try (Writer fileWriter = new FileWriter(fileName)) {

            fileWriter.write(HEADER_STR + "\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(task.toString() + "\n");
            }

            for (Epic epic : getAllEpics()) {
                fileWriter.write(epic.toString() + "\n");
            }

            for (SubTask subTask : getAllSubTasks()) {
                fileWriter.write(subTask.toString() + "\n");
            }

            fileWriter.write("\n");
            for (Task task : getHistory()) {
                fileWriter.write(task.getId() + ",");
            }
        } catch (IOException ex) {
            throw new ManagerSaveException(ex.getMessage());
        }
    }

    public void addTasksFromFile(Task taskFromFile) {
        if (taskFromFile.getTaskType() == TaskType.TASK) {
            tasks.put(taskFromFile.getId(), taskFromFile);
        } else if (taskFromFile.getTaskType() == TaskType.EPIC) {
            epics.put(taskFromFile.getId(), (Epic) taskFromFile);
        } else {
            subTasks.put(taskFromFile.getId(), (SubTask) taskFromFile);
            SubTask subTask = (SubTask) taskFromFile;
            epics.get(subTask.getEpicId()).getSubTasksId().add(subTask.getId());
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
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
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        if (task != null) {
            save();
            return task;
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        if (epic != null) {
            save();
            return epic;
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = super.getSubTaskById(id);
        if (subTask != null) {
            save();
            return subTask;
        } else {
            return null;
        }
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
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
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
    public void deleteSubTaskById(Integer id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileBackedTasksManager that = (FileBackedTasksManager) o;
        return tasks.equals(that.tasks) &&
                epics.equals(that.epics) &&
                subTasks.equals(that.subTasks) &&
                historyManager.equals(that.historyManager);
    }
}
