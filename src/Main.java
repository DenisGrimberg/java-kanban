import entity.Epic;
import entity.SubTask;
import entity.Task;
import services.Managers;
import services.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        Task task1 = new Task("Task1", "DESCRIPTION");
        manager.addTask(task1);
        Task task2 = new Task("Task2", "DESCRIPTION");
        manager.addTask(task2);

        Epic epic1 = new Epic("E1", "DESCRIPTION");
        manager.addEpic(epic1);

        SubTask subtask1 = new SubTask("E1_ST1", "DESCRIPTION", epic1.getId());
        manager.addSubTask(subtask1);
        SubTask subtask2 = new SubTask("E1_ST2", "DESCRIPTION", epic1.getId());
        manager.addSubTask(subtask2);

        Epic epic2 = new Epic("E2", "DESCRIPTION");
        manager.addEpic(epic2);

        SubTask subtask3 = new SubTask("E1_ST1", "DESCRIPTION", epic2.getId());
        manager.addSubTask(subtask3);

        System.out.println(manager.getAllTasks());
        System.out.println(manager.getAllEpics());
        System.out.println(manager.getAllSubTasks());

        System.out.println("\n--------------\n");

        System.out.println(manager.getTaskById(1));
        System.out.println(manager.getHistory());
        System.out.println(manager.getEpicById(3));
        System.out.println(manager.getHistory());
        System.out.println(manager.getSubTaskById(4));
        System.out.println(manager.getHistory());

    }
}
