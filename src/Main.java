import entity.Epic;
import entity.SubTask;
import entity.Task;
import services.Managers;
import services.TaskManager;

public class Main {
    static TaskManager manager = Managers.getDefault();

    public static void main(String[] args) {

        Task task1 = new Task("Task1", "DESC");
        manager.addTask(task1);
        Task task2 = new Task("Task2", "DESC");
        manager.addTask(task2);

        Epic epic1 = new Epic("E1", "DESC");
        manager.addEpic(epic1);

        SubTask subtask1 = new SubTask("E1_ST1", "DESC", epic1.getId());
        manager.addSubTask(subtask1);
        SubTask subtask2 = new SubTask("E1_ST2", "DESC", epic1.getId());
        manager.addSubTask(subtask2);
        SubTask subtask3 = new SubTask("E1_ST3", "DESC", epic1.getId());
        manager.addSubTask(subtask3);

        Epic epic2 = new Epic("E2", "DESC");
        manager.addEpic(epic2);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());
        System.out.println("--------------------------");
        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getHistory());
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getHistory());
        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getSubTaskById(5));
        System.out.println(manager.getSubTaskById(6));
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getHistory());

        System.out.println("--------------------------");
        manager.deleteEpicById(3);
        System.out.println(manager.getHistory());
    }
}

