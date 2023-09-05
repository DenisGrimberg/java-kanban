import entity.Epic;
import entity.Subtask;
import entity.Task;
import entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.HistoryManager;
import services.InMemoryHistoryManager;
import services.TaskManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static entity.TaskStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {
    public T manager;

    abstract T createTaskManager();

    Task firstTask;
    Task secondTask;
    Epic firstEpic;
    Epic secondEpic;
    Epic thirdEpic;
    Subtask firstSubtask;
    Subtask secondSubtask;
    Subtask thirdSubtask;
    HistoryManager historyManager;

    @BeforeEach
    public void allTasksForTests() {

        manager = createTaskManager();

        firstTask = manager.creationTask(new Task("Task 1", NEW,
                "Description Task 1", LocalDateTime.of(2000, 5, 5, 10, 20),
                10));
        manager.saveTask(firstTask);

        secondTask = manager.creationTask(new Task("Task 2", NEW,
                "Description Task 2", LocalDateTime.of(2000, 6, 10, 11, 25),
                50));
        manager.saveTask(secondTask);

        firstEpic = manager.creationEpic(new Epic("Epic 1", TaskStatus.NEW,
                "Description Epic 1", LocalDateTime.of(2001, 9, 11, 10, 20),
                10));
        manager.saveEpic(firstEpic);

        secondEpic = manager.creationEpic(new Epic("Epic 2", TaskStatus.NEW,
                "Description Epic 2", LocalDateTime.now().minusMinutes(30), 20));
        manager.saveEpic(secondEpic);

        firstSubtask = manager.creationSubtask(new Subtask("Subtask 1", NEW,
                "Description Subtask 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                50, 3));
        manager.saveSubtask(firstSubtask);

        secondSubtask = manager.creationSubtask(new Subtask("Subtask 2",
                DONE, "Description Subtask 2", LocalDateTime.now().minusMinutes(30), 40,
                3));
        manager.saveSubtask(secondSubtask);

        thirdSubtask = manager.creationSubtask(new Subtask("Subtask 3",
                DONE, "Description Subtask 3",
                LocalDateTime.of(2015, 6, 14, 11, 30), 40, 4));
        manager.saveSubtask(thirdSubtask);

        thirdEpic = manager.creationEpic(new Epic("Epic 3", NEW,
                "For test TaskStatus", LocalDateTime.of(2020, 2, 20, 20, 20),
                20));
        manager.saveEpic(thirdEpic);
    }

    @Test
    void testSaveTask() {

        assertEquals(manager.getTaskByIdNumber(1).getName(), firstTask.getName());
        assertEquals(manager.getTaskByIdNumber(1).getTaskType(), firstTask.getTaskType());
        assertEquals(manager.getTaskByIdNumber(1).getStatus(), firstTask.getStatus());
        assertEquals(manager.getTaskByIdNumber(1).getDescription(), firstTask.getDescription());
        assertEquals(manager.getTaskByIdNumber(1).getStartTime(), firstTask.getStartTime());
        assertEquals(manager.getTaskByIdNumber(1).getDuration(), firstTask.getDuration());

        manager.deleteTasks();
        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testSaveEpic() {

        assertEquals(manager.getEpicTaskByIdNumber(3).getName(), firstEpic.getName());
        assertEquals(manager.getEpicTaskByIdNumber(3).getTaskType(), firstEpic.getTaskType());
        assertEquals(manager.getEpicTaskByIdNumber(3).getStatus(), firstEpic.getStatus());
        assertEquals(manager.getEpicTaskByIdNumber(3).getDescription(), firstEpic.getDescription());
        assertEquals(manager.getEpicTaskByIdNumber(3).getStartTime(), firstEpic.getStartTime());
        assertEquals(manager.getEpicTaskByIdNumber(3).getDuration(), firstEpic.getDuration());

        manager.deleteEpics();
        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testSaveSubtask() {

        assertEquals(manager.getSubTaskByIdNumber(5).getName(), firstSubtask.getName());
        assertEquals(manager.getSubTaskByIdNumber(5).getTaskType(), firstSubtask.getTaskType());
        assertEquals(manager.getSubTaskByIdNumber(5).getStatus(), firstSubtask.getStatus());
        assertEquals(manager.getSubTaskByIdNumber(5).getDescription(), firstSubtask.getDescription());
        assertEquals(manager.getSubTaskByIdNumber(5).getStartTime(), firstSubtask.getStartTime());
        assertEquals(manager.getSubTaskByIdNumber(5).getDuration(), firstSubtask.getDuration());

        assertEquals(manager.getEpicTaskByIdNumber(3).getId(), firstSubtask.getEpicID());

        Subtask testSubtask = new Subtask("Subtask ", NEW,
                "Description Subtask ", LocalDateTime.of(2020, 2, 20, 20, 20),
                50, 8);
        manager.saveSubtask(testSubtask);
        assertEquals(manager.getEpicTaskByIdNumber(8).getStatus(), testSubtask.getStatus());
        assertEquals(manager.getEpicTaskByIdNumber(8).getStartTime(), testSubtask.getStartTime());
        assertEquals(manager.getEpicTaskByIdNumber(8).getDuration(), testSubtask.getDuration());

        manager.deleteSubtasks();
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testGetTasksList() {

        List<Task> testList1 = new ArrayList<>(List.of(firstTask, secondTask));
        List<Task> testList2 = manager.getTasksList();
        assertEquals(testList1, testList2);

        manager.deleteTasks();
        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testGetEpicsList() {

        List<Epic> testList1 = new ArrayList<>(List.of(firstEpic, secondEpic, thirdEpic));
        List<Epic> testList2 = manager.getEpicsList();
        assertEquals(testList1, testList2);

        manager.deleteEpics();
        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testGetSubtaskList() {

        List<Subtask> testList1 = new ArrayList<>(List.of(firstSubtask, secondSubtask, thirdSubtask));
        List<Subtask> testList2 = manager.getSubtaskList();
        assertEquals(testList1, testList2);

        manager.deleteSubtasks();
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testDeleteTasks() {

        manager.deleteTasks();
        List<Task> testList = manager.getTasksList();
        assertEquals(0, testList.size());

        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testDeleteEpics() {

        manager.deleteEpics();
        List<Epic> testList = manager.getEpicsList();
        assertEquals(0, testList.size());

        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testDeleteSubtasks() {

        manager.deleteSubtasks();
        List<Subtask> testList = manager.getSubtaskList();
        assertEquals(0, testList.size());

        assertEquals(manager.getEpicTaskByIdNumber(3).getStatus(), NEW);
        assertEquals(manager.getEpicTaskByIdNumber(8).getStatus(), NEW);

        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testGetTaskByIdNumber() {

        assertNull(manager.getTaskByIdNumber(10));

        assertEquals(manager.getTaskByIdNumber(2).getName(), secondTask.getName());
        assertEquals(manager.getTaskByIdNumber(2).getTaskType(), secondTask.getTaskType());
        assertEquals(manager.getTaskByIdNumber(2).getStatus(), secondTask.getStatus());
        assertEquals(manager.getTaskByIdNumber(2).getDescription(), secondTask.getDescription());
        assertEquals(manager.getTaskByIdNumber(2).getStartTime(), secondTask.getStartTime());
        assertEquals(manager.getTaskByIdNumber(2).getDuration(), secondTask.getDuration());

        manager.deleteTasks();
        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testGetEpicTaskByIdNumber() {

        assertNull(manager.getEpicTaskByIdNumber(10));

        assertEquals(manager.getEpicTaskByIdNumber(4).getName(), secondEpic.getName());
        assertEquals(manager.getEpicTaskByIdNumber(4).getTaskType(), secondEpic.getTaskType());
        assertEquals(manager.getEpicTaskByIdNumber(4).getStatus(), secondEpic.getStatus());
        assertEquals(manager.getEpicTaskByIdNumber(4).getDescription(), secondEpic.getDescription());
        assertEquals(manager.getEpicTaskByIdNumber(4).getStartTime(), secondEpic.getStartTime());
        assertEquals(manager.getEpicTaskByIdNumber(4).getDuration(), secondEpic.getDuration());

        manager.deleteEpics();
        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testGetSubTaskByIdNumber() {

        assertNull(manager.getSubTaskByIdNumber(10));

        assertEquals(manager.getSubTaskByIdNumber(6).getName(), secondSubtask.getName());
        assertEquals(manager.getSubTaskByIdNumber(6).getTaskType(), secondSubtask.getTaskType());
        assertEquals(manager.getSubTaskByIdNumber(6).getStatus(), secondSubtask.getStatus());
        assertEquals(manager.getSubTaskByIdNumber(6).getDescription(), secondSubtask.getDescription());
        assertEquals(manager.getSubTaskByIdNumber(6).getStartTime(), secondSubtask.getStartTime());
        assertEquals(manager.getSubTaskByIdNumber(6).getDuration(), secondSubtask.getDuration());

        manager.deleteSubtasks();
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testCreationTask() {

        Task testTask = new Task("Task 1", NEW,
                "Description Task 1", LocalDateTime.of(2000, 5, 5, 10, 20),
                10, 1);
        manager.creationTask(testTask);
        assertEquals(manager.getTaskByIdNumber(1).getId(), testTask.getId());
        assertEquals(manager.getTaskByIdNumber(1).getName(), testTask.getName());
        assertEquals(manager.getTaskByIdNumber(1).getTaskType(), testTask.getTaskType());
        assertEquals(manager.getTaskByIdNumber(1).getStatus(), testTask.getStatus());
        assertEquals(manager.getTaskByIdNumber(1).getDescription(), testTask.getDescription());
        assertEquals(manager.getTaskByIdNumber(1).getStartTime(), testTask.getStartTime());
        assertEquals(manager.getTaskByIdNumber(1).getDuration(), testTask.getDuration());

        manager.deleteTasks();
        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testCreationEpic() {

        Epic testEpic = new Epic("Epic 1", IN_PROGRESS,
                "Description Epic 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                90);
        manager.creationEpic(testEpic);
        assertEquals(manager.getEpicTaskByIdNumber(3).getName(), testEpic.getName());
        assertEquals(manager.getEpicTaskByIdNumber(3).getTaskType(), testEpic.getTaskType());
        assertEquals(manager.getEpicTaskByIdNumber(3).getStatus(), testEpic.getStatus());
        assertEquals(manager.getEpicTaskByIdNumber(3).getDescription(), testEpic.getDescription());
        assertEquals(manager.getEpicTaskByIdNumber(3).getStartTime(), testEpic.getStartTime());
        assertEquals(manager.getEpicTaskByIdNumber(3).getDuration(), testEpic.getDuration());

        manager.deleteEpics();
        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testCreationSubtask() {

        Subtask testSubtask = new Subtask("Subtask 1", NEW,
                "Description Subtask 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                50, 3);
        manager.creationSubtask(testSubtask);
        assertEquals(manager.getSubTaskByIdNumber(5).getName(), testSubtask.getName());
        assertEquals(manager.getSubTaskByIdNumber(5).getTaskType(), testSubtask.getTaskType());
        assertEquals(manager.getSubTaskByIdNumber(5).getStatus(), testSubtask.getStatus());
        assertEquals(manager.getSubTaskByIdNumber(5).getDescription(), testSubtask.getDescription());
        assertEquals(manager.getSubTaskByIdNumber(5).getStartTime(), testSubtask.getStartTime());
        assertEquals(manager.getSubTaskByIdNumber(5).getDuration(), testSubtask.getDuration());

        Subtask testSubtask1 = new Subtask("Subtask ", NEW,
                "Description Subtask ", LocalDateTime.of(2020, 2, 20, 20, 20),
                50, 8);
        manager.saveSubtask(testSubtask1);
        assertEquals(manager.getEpicTaskByIdNumber(8).getStatus(), testSubtask1.getStatus());
        assertEquals(manager.getEpicTaskByIdNumber(8).getStartTime(), testSubtask1.getStartTime());
        assertEquals(manager.getEpicTaskByIdNumber(8).getDuration(), testSubtask1.getDuration());

        manager.deleteSubtasks();
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testUpdateTask() {

        Task testTask = new Task("Task 1", NEW,
                "Description Task 1", LocalDateTime.of(2000, 5, 5, 10, 20),
                10L, 1);
        manager.updateTask(testTask);
        assertEquals(manager.getTaskByIdNumber(1).getId(), testTask.getId());
        assertEquals(manager.getTaskByIdNumber(1).getName(), testTask.getName());
        assertEquals(manager.getTaskByIdNumber(1).getTaskType(), testTask.getTaskType());
        assertEquals(manager.getTaskByIdNumber(1).getStatus(), testTask.getStatus());
        assertEquals(manager.getTaskByIdNumber(1).getDescription(), testTask.getDescription());
        assertEquals(manager.getTaskByIdNumber(1).getStartTime(), testTask.getStartTime());
        assertEquals(manager.getTaskByIdNumber(1).getDuration(), testTask.getDuration());

        manager.deleteTasks();
        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testUpdateEpic() {

        List<Integer> list = new ArrayList<>();
        Epic testEpic = new Epic("Epic 1", IN_PROGRESS,
                "Description Epic 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                90, 3, list);
        manager.updateEpic(testEpic);
        assertEquals(manager.getEpicTaskByIdNumber(3).getName(), testEpic.getName());
        assertEquals(manager.getEpicTaskByIdNumber(3).getTaskType(), testEpic.getTaskType());
        assertEquals(manager.getEpicTaskByIdNumber(3).getStatus(), testEpic.getStatus());
        assertEquals(manager.getEpicTaskByIdNumber(3).getDescription(), testEpic.getDescription());
        assertEquals(manager.getEpicTaskByIdNumber(3).getStartTime(), testEpic.getStartTime());
        assertEquals(manager.getEpicTaskByIdNumber(3).getDuration(), testEpic.getDuration());

        manager.deleteEpics();
        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testUpdateSubtask() {

        Subtask testSubtask = new Subtask("Subtask 1", DONE,
                "Description Subtask 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                50, 3);
        manager.updateSubtask(testSubtask);
        assertEquals(manager.getSubTaskByIdNumber(5).getName(), testSubtask.getName());
        assertEquals(manager.getSubTaskByIdNumber(5).getTaskType(), testSubtask.getTaskType());
        assertEquals(manager.getSubTaskByIdNumber(5).getDescription(), testSubtask.getDescription());

        Subtask testSubtask1 = new Subtask("Subtask", NEW,
                "Description Subtask", LocalDateTime.of(2020, 2, 20, 20, 20),
                50, 8);
        manager.saveSubtask(testSubtask1);
        assertEquals(manager.getEpicTaskByIdNumber(8).getStatus(), testSubtask1.getStatus());
        assertEquals(manager.getEpicTaskByIdNumber(8).getStartTime(), testSubtask1.getStartTime());
        assertEquals(manager.getEpicTaskByIdNumber(8).getDuration(), testSubtask1.getDuration());

        manager.deleteSubtasks();
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testDeleteTaskById() {

        manager.deleteTaskById(10);
        assertEquals(2, manager.getTasksList().size());

        manager.deleteTaskById(1);
        manager.deleteTaskById(2);
        assertEquals(0, manager.getTasksList().size());

        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testDeleteEpicById() {

        manager.deleteEpicById(10);
        assertEquals(3, manager.getEpicsList().size());

        manager.deleteEpics();
        assertEquals(0, manager.getEpicsList().size());

        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testDeleteSubtaskById() {

        manager.deleteSubtaskById(10);
        assertEquals(3, manager.getSubtaskList().size());

        manager.deleteSubtaskById(firstSubtask.getId());
        manager.deleteSubtaskById(secondSubtask.getId());
        manager.deleteSubtaskById(thirdSubtask.getId());
        assertEquals(0, manager.getSubtaskList().size());

        assertEquals(manager.getEpicTaskByIdNumber(3).getStatus(), NEW);
        assertEquals(manager.getEpicTaskByIdNumber(8).getStatus(), NEW);

        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testSubtaskList() {

        assertNull(manager.getEpicTaskByIdNumber(10));

        assertEquals(firstEpic.getId(), firstSubtask.getEpicID());
        assertEquals(firstEpic.getId(), secondSubtask.getEpicID());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusAllSubtaskStatusNew() {

        Subtask sub1 = manager.creationSubtask(new Subtask("Subtask 1",
                NEW, "Description Subtask 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Subtask 2",
                NEW, "Description Subtask 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(NEW, manager.getEpicTaskByIdNumber(8).getStatus());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusAllSubtaskStatusDone() {

        Subtask sub1 = manager.creationSubtask(new Subtask("Subtask 1",
                DONE, "Description Subtask 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Subtask 2",
                DONE, "Description Subtask 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(DONE, manager.getEpicTaskByIdNumber(8).getStatus());


        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusSubtaskStatusDoneNew() {

        Subtask sub1 = manager.creationSubtask(new Subtask("Subtask 1",
                DONE, "Description Subtask 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Subtask 2",
                NEW, "Description Subtask 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(IN_PROGRESS, manager.getEpicTaskByIdNumber(8).getStatus());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusAllSubtaskStatusInProgress() {

        Subtask sub1 = manager.creationSubtask(new Subtask("Subtask 1",
                IN_PROGRESS, "Description Subtask 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Subtask 2",
                IN_PROGRESS, "Description Subtask 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(IN_PROGRESS, manager.getEpicTaskByIdNumber(8).getStatus());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testGetPrioritizedTasks() {

        Set<Task> expected = new TreeSet<>((o1, o2) -> {
            if ((o1.getStartTime() != null) && (o2.getStartTime() != null)) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            } else if (o1.getStartTime() == null) {
                return 1;
            } else if (o2.getStartTime() == null) {
                return -1;
            } else {
                return 0;
            }
        });
        expected.addAll(manager.getTasksList());
        expected.addAll(manager.getSubtaskList());


        List<Task> actual = manager.getPrioritizedTasks();

        List<Task> expectedList = new ArrayList<>(expected);


        assertEquals(expectedList, actual);

        manager.deleteTasks();
        manager.deleteEpics();
        manager.deleteSubtasks();
        assertTrue(manager.getTasksList().isEmpty());
        assertTrue(manager.getEpicsList().isEmpty());
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testGetHistory() {

        historyManager = new InMemoryHistoryManager();

        assertTrue(historyManager.getHistory().isEmpty());

        historyManager.add(firstTask);
        historyManager.add(secondTask);
        historyManager.add(firstEpic);
        historyManager.add(firstEpic);
        historyManager.add(firstSubtask);
        historyManager.add(thirdSubtask);

        assertEquals(5, historyManager.getHistory().size());

        historyManager.remove(1);
        historyManager.remove(3);
        historyManager.remove(7);

        assertEquals(2, historyManager.getHistory().size());
    }
}
