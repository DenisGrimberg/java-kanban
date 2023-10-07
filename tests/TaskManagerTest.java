
import services.HistoryManager;
import services.InMemoryHistoryManager;
import services.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import entity.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;
import static entity.TaskStatus.*;

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

        firstTask = new Task("Таск 1", NEW,
                "Описание Таск 1", LocalDateTime.of(2000, 5, 5, 10, 20),
                10);

        secondTask = new Task("Таск 2", NEW,
                "Описание Таск 2", LocalDateTime.of(2000, 6, 10, 11, 25),
                50);

        firstEpic = new Epic("Эпик 1", TaskStatus.NEW,
                "Описание Эпик 1", LocalDateTime.of(2001, 9, 11, 10, 20),
                10);

        secondEpic = new Epic("Эпик 2", TaskStatus.NEW,
                "Описание Эпик 2", LocalDateTime.now().minusMinutes(30), 20);

        firstSubtask = new Subtask("Сабтаск 1", NEW,
                "Описание Сабтаск 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                50, 3);

        secondSubtask = new Subtask("Сабтаск 2",
                TaskStatus.DONE, "Описание Сабтаск 2", LocalDateTime.now().minusMinutes(30), 40,
                3);

        thirdSubtask = new Subtask("Сабтаск 3",
                TaskStatus.DONE, "Описание Сабтаск 3",
                LocalDateTime.of(2015, 6, 14, 11, 30), 40, 4);

        thirdEpic = new Epic("Эпик 3", NEW,
                "Для теста статусов", LocalDateTime.of(2020, 2, 20, 20, 20),
                20);
    }

    void saveTasks(){
        manager.saveTask(firstTask);
        manager.saveTask(secondTask);
        manager.saveEpic(firstEpic);
        manager.saveEpic(secondEpic);
        manager.saveSubtask(firstSubtask);
        manager.saveSubtask(secondSubtask);
        manager.saveSubtask(thirdSubtask);
        manager.saveEpic(thirdEpic);
    }

    @Test
    void testSaveTask() {

        saveTasks();
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

        saveTasks();
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

        saveTasks();
        assertEquals(manager.getSubTaskByIdNumber(5).getName(), firstSubtask.getName());
        assertEquals(manager.getSubTaskByIdNumber(5).getTaskType(), firstSubtask.getTaskType());
        assertEquals(manager.getSubTaskByIdNumber(5).getStatus(), firstSubtask.getStatus());
        assertEquals(manager.getSubTaskByIdNumber(5).getDescription(), firstSubtask.getDescription());
        assertEquals(manager.getSubTaskByIdNumber(5).getStartTime(), firstSubtask.getStartTime());
        assertEquals(manager.getSubTaskByIdNumber(5).getDuration(), firstSubtask.getDuration());

        assertEquals(manager.getEpicTaskByIdNumber(3).getId(), firstSubtask.getEpicID());

        Subtask testSubtask = new Subtask("Саб таск ", NEW,
                "Описание Саб таск ", LocalDateTime.of(2020, 2, 20, 20, 20),
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

        saveTasks();
        List<Task> testList1 = new ArrayList<>(List.of(firstTask, secondTask));
        List<Task> testList2 = manager.getTasksList();
        assertEquals(testList1, testList2);

        manager.deleteTasks();
        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testGetEpicsList() {

        saveTasks();
        List<Epic> testList1 = new ArrayList<>(List.of(firstEpic, secondEpic, thirdEpic));
        List<Epic> testList2 = manager.getEpicsList();
        assertEquals(testList1, testList2);

        manager.deleteEpics();
        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testGetSubtaskList() {

        saveTasks();
        List<Subtask> testList1 = new ArrayList<>(List.of(firstSubtask, secondSubtask, thirdSubtask));
        List<Subtask> testList2 = manager.getSubtaskList();
        assertEquals(testList1, testList2);

        manager.deleteSubtasks();
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testDeleteTasks() {

        saveTasks();
        manager.deleteTasks();
        List<Task> testList = manager.getTasksList();
        assertEquals(0, testList.size());

        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testDeleteEpics() {

        saveTasks();
        manager.deleteEpics();
        List<Epic> testList = manager.getEpicsList();
        assertEquals(0, testList.size());

        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testDeleteSubtasks() {

        saveTasks();
        manager.deleteSubtasks();
        List<Subtask> testList = manager.getSubtaskList();
        assertEquals(0, testList.size());

        assertEquals(manager.getEpicTaskByIdNumber(3).getStatus(), NEW);
        assertEquals(manager.getEpicTaskByIdNumber(8).getStatus(), NEW);

        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testGetTaskByIdNumber() {
        saveTasks();
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
        saveTasks();
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
        saveTasks();
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
        saveTasks();
        Task testTask = new Task("Таск 1", NEW,
                "Описание Таск 1", LocalDateTime.of(2000, 5, 5, 10, 20),
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
        saveTasks();
        Epic testEpic = new Epic("Эпик 1", IN_PROGRESS,
                "Описание Эпик 1", LocalDateTime.of(2010, 1, 11, 11, 40),
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
        saveTasks();
        Subtask testSubtask = new Subtask("Сабтаск 1", NEW,
                "Описание Сабтаск 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                50, 3);
        manager.creationSubtask(testSubtask);
        assertEquals(manager.getSubTaskByIdNumber(5).getName(), testSubtask.getName());
        assertEquals(manager.getSubTaskByIdNumber(5).getTaskType(), testSubtask.getTaskType());
        assertEquals(manager.getSubTaskByIdNumber(5).getStatus(), testSubtask.getStatus());
        assertEquals(manager.getSubTaskByIdNumber(5).getDescription(), testSubtask.getDescription());
        assertEquals(manager.getSubTaskByIdNumber(5).getStartTime(), testSubtask.getStartTime());
        assertEquals(manager.getSubTaskByIdNumber(5).getDuration(), testSubtask.getDuration());

        Subtask testSubtask1 = new Subtask("Саб таск ", NEW,
                "Описание Саб таск ", LocalDateTime.of(2020, 2, 20, 20, 20),
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
        saveTasks();
        Task testTask = new Task("Таск 1", NEW,
                "Описание Таск 1", LocalDateTime.of(2000, 5, 5, 10, 20),
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
        saveTasks();
        List<Integer> list = new ArrayList<>();
        Epic testEpic = new Epic("Эпик 1", IN_PROGRESS,
                "Описание Эпик 1", LocalDateTime.of(2010, 1, 11, 11, 40),
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
        saveTasks();
        Subtask testSubtask = new Subtask("Сабтаск 1", DONE,
                "Описание Сабтаск 1", LocalDateTime.of(2010, 1, 11, 11, 40),
                50, 3);
        manager.updateSubtask(testSubtask);
        assertEquals(manager.getSubTaskByIdNumber(5).getName(), testSubtask.getName());
        assertEquals(manager.getSubTaskByIdNumber(5).getTaskType(), testSubtask.getTaskType());
        assertEquals(manager.getSubTaskByIdNumber(5).getDescription(), testSubtask.getDescription());

        Subtask testSubtask1 = new Subtask("Саб таск ", NEW,
                "Описание Саб таск ", LocalDateTime.of(2020, 2, 20, 20, 20),
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
        saveTasks();
        manager.deleteTaskById(10);
        assertEquals(2, manager.getTasksList().size());

        manager.deleteTaskById(1);
        manager.deleteTaskById(2);
        assertEquals(0, manager.getTasksList().size());

        assertTrue(manager.getTasksList().isEmpty());
    }

    @Test
    void testDeleteEpicById() {
        saveTasks();
        manager.deleteEpicById(10);
        assertEquals(3, manager.getEpicsList().size());

        manager.deleteEpics();
        assertEquals(0, manager.getEpicsList().size());

        assertTrue(manager.getEpicsList().isEmpty());
    }

    @Test
    void testDeleteSubtaskById() {
        saveTasks();

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
        saveTasks();
        assertNull(manager.getEpicTaskByIdNumber(10));

        assertEquals(firstEpic.getId(), firstSubtask.getEpicID());
        assertEquals(firstEpic.getId(), secondSubtask.getEpicID());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusAllSubtaskStatusNew() {
        saveTasks();
        Subtask sub1 = manager.creationSubtask(new Subtask("Саб 1",
                NEW, "Описание Саб 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Саб 2",
                NEW, "Описание Саб 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(NEW, manager.getEpicTaskByIdNumber(8).getStatus());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusAllSubtaskStatusDone() {
        saveTasks();
        Subtask sub1 = manager.creationSubtask(new Subtask("Саб 1",
                DONE, "Описание Саб 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Саб 2",
                DONE, "Описание Саб 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(DONE, manager.getEpicTaskByIdNumber(8).getStatus());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusSubtaskStatusDoneNew() {
        saveTasks();
        Subtask sub1 = manager.creationSubtask(new Subtask("Саб 1",
                DONE, "Описание Саб 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Саб 2",
                NEW, "Описание Саб 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(IN_PROGRESS, manager.getEpicTaskByIdNumber(8).getStatus());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testChangeEpicStatusAllSubtaskStatusInProgress() {
        saveTasks();
        Subtask sub1 = manager.creationSubtask(new Subtask("Саб 1",
                IN_PROGRESS, "Описание Саб 1",
                LocalDateTime.of(2017, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub1);

        Subtask sub2 = manager.creationSubtask(new Subtask("Саб 2",
                IN_PROGRESS, "Описание Саб 2",
                LocalDateTime.of(2016, 7, 18, 11, 30), 40, 8));
        manager.saveSubtask(sub2);

        assertEquals(IN_PROGRESS, manager.getEpicTaskByIdNumber(8).getStatus());

        manager.deleteSubtasks();
        assertEquals(0, manager.getSubtaskList().size());
    }

    @Test
    void testGetPrioritizedTasks() {
        saveTasks();
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

        Set<Task> actual = manager.getPrioritizedTasks();

        assertEquals(expected, actual);

        manager.deleteTasks();
        manager.deleteEpics();
        manager.deleteSubtasks();
        assertTrue(manager.getTasksList().isEmpty());
        assertTrue(manager.getEpicsList().isEmpty());
        assertTrue(manager.getSubtaskList().isEmpty());
    }

    @Test
    void testGetHistory() {
        saveTasks();
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