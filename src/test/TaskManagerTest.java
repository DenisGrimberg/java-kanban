package test;

import entity.*;

import static org.junit.jupiter.api.Assertions.*;

import exception.TaskOverlapAnotherTaskException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.TaskManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {

    private T taskManager;
    private T emptyTaskManager;

    abstract T createTaskManager();

    @BeforeEach
    public void beforeEach() {
        taskManager = createTaskManager();
        emptyTaskManager = createTaskManager();

        Task task1 = new Task("Task1", TaskType.TASK, "desc_Task1", 20, "2023-07-30T06:00:00");
        Task task2 = new Task("Task2", TaskType.TASK, "desc_Task2", 20, "2023-07-30T14:00:00");

        Epic epic1 = new Epic("Epic1", TaskType.EPIC, "desc_Epic1");
        Epic epic2 = new Epic("Epic2", TaskType.EPIC, "desc_Epic2");
        Epic epic3 = new Epic("Epic3", TaskType.EPIC, "desc_Epic3");

        SubTask subTask1 = new SubTask("SubTask1", TaskType.SUBTASK, "desc_SubTask1", 20, "2023-07-30T09:00:00", 3);
        SubTask subTask2 = new SubTask("SubTask2", TaskType.SUBTASK, "desc_SubTask2", 20, "2023-07-30T10:00:00", 3);
        SubTask subTask3 = new SubTask("SubTask3", TaskType.SUBTASK, "desc_SubTask3", 20, "2023-07-29T09:00:00", 4);
        SubTask subTask4 = new SubTask("SubTask4", TaskType.SUBTASK, "desc_SubTask4", 20, "2023-07-28T12:00:00", 4);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);
        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.addSubTask(subTask4);
    }

    @Test
    void shouldReturnListOfTasksWithSize2() {
        List<Task> testedListOfTasks = taskManager.getAllTasks();
        Task expectedTask = taskManager.getTaskById(1);
        int expectedSize = 2;
        assertNotNull(testedListOfTasks, "Tasks don't return");
        assertEquals(expectedSize, testedListOfTasks.size(), "Wrong tasks size");
        assertEquals(expectedTask, testedListOfTasks.get(0), "Tasks not equal");
    }

    @Test
    void shouldReturnListOfTasksWithSize0() {
        List<Task> testedListOfTasks = emptyTaskManager.getAllTasks();
        int expectedSize = 0;
        assertNotNull(testedListOfTasks, "Tasks don't return");
        assertEquals(expectedSize, testedListOfTasks.size(), "Wrong tasks size");
    }

    @Test
    void shouldRemoveAllTasks() {
        List<Task> beforeRemoveListOfTasks = taskManager.getAllTasks();
        int beforeRemoveExpectedSize = 2;
        int afterRemoveExpectedSize = 0;
        taskManager.deleteAllTasks();
        List<Task> afterRemoveListOfTasks = taskManager.getAllTasks();
        assertEquals(beforeRemoveExpectedSize, beforeRemoveListOfTasks.size(), "Wrong tasks size");
        assertEquals(afterRemoveExpectedSize, afterRemoveListOfTasks.size(), "Wrong tasks size");
    }

    @Test
    void shouldReturnTaskWithId1() {
        Task testedTask = taskManager.getTaskById(1);
        int expectedId = 1;
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedId, testedTask.getId(), "Tasks not equal");
    }

    @Test
    void shouldNotReturnTaskWithId5() {
        Task testedTask = taskManager.getTaskById(5);
        assertNull(testedTask, "Task found");
    }

    @Test
    void shouldAddNewTask() {
        Task expectedTask = new Task("Test Task", TaskType.TASK, "Test Task", 30, "2023-07-31T15:00:00");
        taskManager.addTask(expectedTask);
        Task testedTask = taskManager.getTaskById(10);
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedTask, testedTask, "Tasks not equal");
    }

    @Test
    void shouldNotAddSameTask() {
        int expectedSize = 2;
        assertEquals(expectedSize, taskManager.getAllTasks().size());
        Task sameTask = taskManager.getTaskById(1);
        taskManager.addTask(sameTask);
        assertEquals(expectedSize, taskManager.getAllTasks().size());
    }

    @Test
    void shouldUpdateTask() {
        Task expectedTask = new Task("Updated Task", TaskType.TASK, "Updated Task", 30, "2023-07-30T12:00:00");
        expectedTask.setId(1);
        taskManager.updateTask(expectedTask);
        Task testedTask = taskManager.getTaskById(1);
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedTask, testedTask, "Tasks not equal");
    }

    @Test
    void shouldNotUpdateTask() {
        Task expectedTask = taskManager.getTaskById(1);
        taskManager.updateTask(null);
        Task testedTask = taskManager.getTaskById(1);
        assertNotNull(testedTask, "Task not found");
        assertEquals(expectedTask, testedTask, "Tasks not equal");
    }

    @Test
    void shouldRemoveTaskWithId1() {
        taskManager.deleteTaskById(1);
        Task expectedTask = taskManager.getTaskById(1);
        assertNull(expectedTask, "Task found");
    }

    @Test
    void shouldNotRemoveTaskWithId3() {
        taskManager.deleteTaskById(3);
        Epic expectedEpic = taskManager.getEpicById(3);
        assertNotNull(expectedEpic, "Epic was deleted");
    }

    @Test
    void shouldReturnListOfEpicsWithSize3() {
        List<Epic> testedListOfEpics = taskManager.getAllEpics();
        int expectedSize = 3;
        Epic expectedEpic = taskManager.getEpicById(3);
        assertNotNull(testedListOfEpics, "Epics don't return");
        assertEquals(expectedSize, testedListOfEpics.size(), "Wrong epics size");
        assertEquals(expectedEpic, testedListOfEpics.get(0), "Epics not equal");
    }

    @Test
    void shouldReturnListOfEpicsWithSize0() {
        List<Epic> testedListOfEpics = emptyTaskManager.getAllEpics();
        int expectedSize = 0;
        assertNotNull(testedListOfEpics, "Epics don't return");
        assertEquals(expectedSize, testedListOfEpics.size(), "Wrong epics size");
    }

    @Test
    void shouldRemoveAllEpics() {
        List<Epic> beforeRemoveListOfEpics = taskManager.getAllEpics();
        int beforeRemoveExpectedSize = 3;
        int afterRemoveExpectedSize = 0;
        taskManager.deleteAllEpics();
        List<Epic> afterRemoveListOfEpics = taskManager.getAllEpics();
        assertEquals(beforeRemoveExpectedSize, beforeRemoveListOfEpics.size(), "Wrong epics size");
        assertEquals(afterRemoveExpectedSize, afterRemoveListOfEpics.size(), "Wrong epics size");
    }

    @Test
    void shouldReturnEpicWithId3() {
        Epic testedEpic = taskManager.getEpicById(3);
        int expectedId = 3;
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedId, testedEpic.getId(), "Epics not equal");
    }

    @Test
    void shouldNotReturnEpicWithId1() {
        Epic testedEpic = taskManager.getEpicById(1);
        assertNull(testedEpic, "Epic found");
    }

    @Test
    void shouldAddNewEpic() {
        Epic expectedEpic = new Epic("Test Epic", TaskType.EPIC, "Test Epic");
        taskManager.addEpic(expectedEpic);
        Epic testedEpic = taskManager.getEpicById(10);
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedEpic, testedEpic, "Epics not equal");
    }

    @Test
    void shouldNotAddSameEpic() {
        int expectedSize = 3;
        Epic sameEpic = taskManager.getEpicById(3);
        assertEquals(expectedSize, taskManager.getAllEpics().size());
        taskManager.addEpic(sameEpic);
        assertEquals(expectedSize, taskManager.getAllEpics().size());
    }

    @Test
    void shouldUpdateEpic() {
        Epic expectedEpic = new Epic("Updated Epic", TaskType.EPIC, "Updated Epic");
        expectedEpic.setId(3);
        taskManager.updateEpic(expectedEpic);
        Epic testedEpic = taskManager.getEpicById(3);
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedEpic, testedEpic, "Epics not equal");
    }

    @Test
    void shouldNotUpdateEpic() {
        Epic expectedEpic = taskManager.getEpicById(3);
        taskManager.updateEpic(null);
        Epic testedEpic = taskManager.getEpicById(3);
        assertNotNull(testedEpic, "Epic not found");
        assertEquals(expectedEpic, testedEpic, "Epics not equal");
    }

    @Test
    void shouldRemoveEpicWithId3() {
        taskManager.deleteEpicById(3);
        Epic expectedEpic = taskManager.getEpicById(3);
        assertNull(expectedEpic, "Epic was not deleted");
    }

    @Test
    void shouldNotRemoveEpicWithId1() {
        taskManager.deleteEpicById(1);
        Task expectedTask = taskManager.getTaskById(1);
        assertNotNull(expectedTask, "Task was deleted");
    }

    @Test
    void shouldReturnListSubtasksOfEpicWithSize2() {
        Epic testEpic = taskManager.getEpicById(3);
        SubTask expectedSubtask = taskManager.getSubTaskById(6);
        List<Integer> testedSubtasksOfEpic = taskManager.getSubTasksOfEpic(testEpic);
        int expectedSize = 2;
        assertNotNull(testedSubtasksOfEpic, "Subtasks don't return");
        assertEquals(expectedSize, testedSubtasksOfEpic.size(), "Wrong subtasks size");
        assertEquals(expectedSubtask.getId(), testedSubtasksOfEpic.get(0), "subtasks not equal");
    }

    @Test
    void shouldReturnListSubtasksOfEpicWithSize0() {
        Epic epicWithoutSubTasks = taskManager.getEpicById(5);
        List<Integer> testedSubtasksOfEpic = taskManager.getSubTasksOfEpic(epicWithoutSubTasks);
        int expectedSize = 0;
        assertNotNull(testedSubtasksOfEpic, "Subtasks don't return");
        assertEquals(expectedSize, testedSubtasksOfEpic.size(), "Wrong subtasks size");
    }

    @Test
    void shouldReturnListOfSubtasksWithSize4() {
        List<SubTask> testedListOfSubtasks = taskManager.getAllSubTasks();
        int expectedSize = 4;
        SubTask expectedSubtask = taskManager.getSubTaskById(6);
        assertNotNull(testedListOfSubtasks, "Subtasks don't return");
        assertEquals(expectedSize, testedListOfSubtasks.size(), "Wrong subtasks size");
        assertEquals(expectedSubtask, testedListOfSubtasks.get(0), "Subtasks not equal");
    }

    @Test
    void shouldReturnListOfSubtasksWithSize0() {
        List<SubTask> testedListOfSubtasks = emptyTaskManager.getAllSubTasks();
        int expectedSize = 0;
        assertNotNull(testedListOfSubtasks, "Subtasks don't return");
        assertEquals(expectedSize, testedListOfSubtasks.size(), "Wrong subtasks size");
    }

    @Test
    void shouldRemoveAllSubtasks() {
        List<SubTask> beforeRemoveListOfSubtasks = taskManager.getAllSubTasks();
        int beforeRemoveExpectedSize = 4;
        int afterRemoveExpectedSize = 0;
        taskManager.deleteAllSubTasks();
        List<SubTask> afterRemoveListOfSubtasks = taskManager.getAllSubTasks();
        assertEquals(beforeRemoveExpectedSize, beforeRemoveListOfSubtasks.size(), "Wrong subtasks size");
        assertEquals(afterRemoveExpectedSize, afterRemoveListOfSubtasks.size(), "Wrong subtasks size");
    }

    @Test
    void shouldReturnSubtaskWithId6() {
        SubTask expectedSubtask = taskManager.getSubTaskById(6);
        int expectedId = 6;
        assertNotNull(expectedSubtask, "Subtask not found");
        assertEquals(expectedId, expectedSubtask.getId(), "Epics not equal");
    }

    @Test
    void shouldNotReturnSubtaskWithId1() {
        SubTask testedSubtask = taskManager.getSubTaskById(1);
        assertNull(testedSubtask, "Epic found");
    }

    @Test
    void shouldAddNewSubtask() {
        SubTask expectedSubtask = new SubTask("Test Subtask", TaskType.SUBTASK, "Test Subtask",
                30, "2023-07-30T07:00:00", 3);
        taskManager.addSubTask(expectedSubtask);
        SubTask testedSubtask = taskManager.getSubTaskById(10);
        assertNotNull(testedSubtask, "Subtask not found");
        assertEquals(expectedSubtask, testedSubtask, "Subtask not equal");
    }

    @Test
    void shouldUpdateStartTimeOfEpicAfterAddNewSubtask() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SubTask expectedSubtask = new SubTask("Test Subtask", TaskType.SUBTASK,  "Test Subtask",
                300, "2023-07-29T10:00:00", 3);
        taskManager.addSubTask(expectedSubtask);
        assertEquals(expectedSubtask.getStartTime().format(dateTimeFormatter), taskManager.getEpicById(3).getStartTime().format(dateTimeFormatter), "Start time of epic don't update");
    }

    @Test
    void shouldUpdateSubtask() {
        SubTask expectedSubtask = new SubTask("Updated Subtask", TaskType.SUBTASK, "Updated Subtask", 30, "2022-08-30T07:00:00", 3);
        expectedSubtask.setId(6);
        taskManager.updateSubTask(expectedSubtask);
        SubTask testedSubtask = taskManager.getSubTaskById(6);
        assertNotNull(testedSubtask, "Subtask not found");
        assertEquals(expectedSubtask, testedSubtask, "Subtasks not equal");
    }

    @Test
    void shouldUpdateStartTimeOfEpicAfterUpdateSubtask() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SubTask expectedSubtask = new SubTask("Test Subtask", TaskType.SUBTASK, "Test Subtask",
                30, "2023-07-30T08:00:00", 3);
        expectedSubtask.setId(6);
        taskManager.updateSubTask(expectedSubtask);
        assertEquals(expectedSubtask.getStartTime().format(dateTimeFormatter), taskManager.getEpicById(3).getStartTime().format(dateTimeFormatter), "Start time of epic don't update");
    }

    @Test
    void shouldNotUpdateSubtask() {
        SubTask expectedSubtask = taskManager.getSubTaskById(6);
        taskManager.updateSubTask(null);
        SubTask testedSubtask = taskManager.getSubTaskById(6);
        assertNotNull(testedSubtask, "Subtask not found");
        assertEquals(expectedSubtask, testedSubtask, "Subtasks not equal");
    }

    @Test
    void shouldNotUpdateSubtaskWithWrongIdInSubTasksOfEpic() {
        SubTask expectedSubtask = new SubTask("Updated Subtask", TaskType.SUBTASK, "Updated Subtask",
                30, "2023-07-30T07:00:00", 3);
        expectedSubtask.setId(6);
        Epic testEpic = taskManager.getEpicById(3);
        testEpic.getSubTasksId().set(0, 20);
        taskManager.updateSubTask(expectedSubtask);
        SubTask testedSubtask = taskManager.getSubTaskById(6);
        assertNotNull(testedSubtask, "Subtask not found");
        assertNotEquals(expectedSubtask, testedSubtask, "Subtasks is equal");
    }

    @Test
    void shouldRemoveSubtaskWithId6() {
        taskManager.deleteSubTaskById(6);
        SubTask expectedSubtask = taskManager.getSubTaskById(6);
        assertNull(expectedSubtask, "Subtask was not deleted");
    }

    @Test
    void shouldUpdateStartTimeOfEpicAfterRemoveSubtask() {
        SubTask removedSubtask = taskManager.getSubTaskById(6);
        SubTask expectedSubtask = taskManager.getSubTaskById(7);
        assertEquals(removedSubtask.getStartTime(), taskManager.getEpicById(3).getStartTime(), "Start time of epic don't update");
        taskManager.deleteSubTaskById(6);
        assertEquals(expectedSubtask.getStartTime(), taskManager.getEpicById(3).getStartTime(), "Start time of epic don't update");
    }

    @Test
    void shouldNotRemoveSubtaskWithId1() {
        taskManager.deleteSubTaskById(1);
        Task expectedTask = taskManager.getTaskById(1);
        assertNotNull(expectedTask, "Task was deleted");
    }

    @Test
    void shouldReturnHistoryWithSize2() {
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        List<Task> testedListOfHistory = taskManager.getHistory();
        int expectedSize = 2;
        Epic expectedEpic = taskManager.getEpicById(3);
        assertNotNull(testedListOfHistory, "History don't return");
        assertEquals(expectedSize, testedListOfHistory.size(), "Wrong history size");
        assertEquals(expectedEpic, testedListOfHistory.get(1), "Epics not equal");
    }

    @Test
    void shouldReturnHistoryWithSize0() {
        List<Task> testedListOfHistory = taskManager.getHistory();
        int expectedSize = 0;
        assertNotNull(testedListOfHistory, "History don't return");
        assertEquals(expectedSize, testedListOfHistory.size(), "Wrong history size");
    }


    @Test
    void statusEpicNewWithEmptyListOfSubtasks() {
        TaskStatus expectedState = TaskStatus.NEW;
        TaskStatus testState = taskManager.getEpicById(5).getStatus();
        assertEquals(expectedState, testState, "Status of epic is not NEW");
    }

    @Test
    void statusEpicNewWithAllSubtasksNew() {
        TaskStatus expectedState = TaskStatus.NEW;
        TaskStatus testState = taskManager.getEpicById(3).getStatus();
        assertEquals(expectedState, testState, "Status of epic is not NEW");
    }

    @Test
    void statusEpicDoneWithAllSubtasksDone() {
        TaskStatus expectedState = TaskStatus.DONE;
        SubTask testSubtask1 = taskManager.getSubTaskById(6);
        testSubtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(testSubtask1);
        SubTask testSubTask2 = taskManager.getSubTaskById(7);
        testSubTask2.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(testSubTask2);
        TaskStatus testState = taskManager.getEpicById(3).getStatus();
        assertEquals(expectedState, testState, "Status of epic is not DONE");
    }

    @Test
    void statusEpicInProgressWithSubtasksNewAndDone() {
        TaskStatus expectedState = TaskStatus.IN_PROGRESS;
        SubTask testSubtask1 = taskManager.getSubTaskById(6);
        testSubtask1.setStatus(TaskStatus.DONE);
        taskManager.updateSubTask(testSubtask1);
        TaskStatus testState = taskManager.getEpicById(3).getStatus();
        assertEquals(expectedState, testState, "Status of epic is not IN_PROGRESS");
    }

    @Test
    void statusEpicInProgressWithAllSubtasksInProgress() {
        TaskStatus expectedState = TaskStatus.IN_PROGRESS;
        SubTask testSubtask1 = taskManager.getSubTaskById(6);
        testSubtask1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(testSubtask1);
        SubTask testSubTask2 = taskManager.getSubTaskById(7);
        testSubTask2.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(testSubTask2);
        TaskStatus testState = taskManager.getEpicById(3).getStatus();
        assertEquals(expectedState, testState, "Status of epic is not IN_PROGRESS");
    }

    @Test
    void shouldReturnPrioritizedTasksByStartTimeAfterAddNewTask() {
        List<Task> beforeAddNewTaskSortByPriorityTasks = taskManager.getPrioritizedTasks();
        int beforeAddNewTaskSize = 6;
        SubTask beforeAddNewTask = taskManager.getSubTaskById(9);
        Task beforeAddNewTaskFirstTask = beforeAddNewTaskSortByPriorityTasks.get(0);
        assertEquals(beforeAddNewTaskSize, beforeAddNewTaskSortByPriorityTasks.size(), "Size not equal");
        assertEquals(beforeAddNewTask, beforeAddNewTaskFirstTask, "Task not equal");
        Task expectedTask = new Task("Test Task", TaskType.TASK, "Test Task", 30, "2022-08-27T12:00:00");
        taskManager.addTask(expectedTask);
        List<Task> afterAddNewTaskSortByPriorityTasks = taskManager.getPrioritizedTasks();
        Task afterAddNewTask = taskManager.getTaskById(10);
        Task afterAddNewTaskFirstTask = afterAddNewTaskSortByPriorityTasks.get(0);
        int afterAddNewTaskSize = 7;
        assertEquals(afterAddNewTaskSize, afterAddNewTaskSortByPriorityTasks.size(), "Size not equal");
        assertEquals(afterAddNewTask, afterAddNewTaskFirstTask, "Task not equal");
    }

    @Test
    void shouldNotAddTaskOverlapByTime() {
        Task task = new Task("TestTask", TaskType.TASK, "TestTask",
                30, "2023-07-30T06:00:00");
        Task overlapSubTask = taskManager.getTaskById(1);
        Exception exception = assertThrows(TaskOverlapAnotherTaskException.class, () -> taskManager.addTask(task));
        assertEquals("Fail to addTask because " + task.getName() + " overlap " + overlapSubTask.getName(),
                exception.getMessage(), "Exception not thrown");
    }

    @Test
    void shouldNotUpdateTaskOverlapByTime() {
        LocalDateTime expectedTimeOfTask = LocalDateTime.parse("2023-07-30T06:00:00");
        Task beforeUpdateTask = taskManager.getTaskById(1);
        assertEquals(expectedTimeOfTask, beforeUpdateTask.getStartTime(), "StartTime of task not equal");
        Task updatedTask = new Task("Task1", TaskType.TASK, "Task1",
                30, "2023-07-30T09:00:00");
        updatedTask.setId(1);
        SubTask overlapSubTask = taskManager.getSubTaskById(6);

        Exception exception = assertThrows(TaskOverlapAnotherTaskException.class,
                () -> taskManager.updateTask(updatedTask));
        assertEquals("Fail to updateTask because " + updatedTask.getName() + " overlap "
                + overlapSubTask.getName(), exception.getMessage(), "Exception not thrown");
        assertNotEquals(expectedTimeOfTask, updatedTask.getStartTime(), "StartTime of task not equal");
    }

    @Test
    void shouldNotAddSubTaskOverlapByTime() {
        SubTask subTask = new SubTask("TestTask", TaskType.SUBTASK, "TestTask",
                30, "2023-07-30T06:00:00", 3);
        Task overlapSubTask = taskManager.getTaskById(1);
        Exception exception = assertThrows(TaskOverlapAnotherTaskException.class,
                () -> taskManager.addSubTask(subTask));
        assertEquals("Fail to addSubTask because " + subTask.getName() + " overlap "
                + overlapSubTask.getName(), exception.getMessage(), "Exception not thrown");
    }

    @Test
    void shouldNotUpdateSubTaskOverlapByTime() {
        LocalDateTime expectedTimeOfTask = LocalDateTime.parse("2023-07-30T10:00:00");
        SubTask beforeUpdateSubTask = taskManager.getSubTaskById(7);
        assertEquals(expectedTimeOfTask, beforeUpdateSubTask.getStartTime(), "StartTime of subTasks not equal");
        SubTask updatedSubTask = new SubTask("SubTask2", TaskType.SUBTASK, "SubTask2",
                30, "2023-07-30T09:00:00", 3);
        updatedSubTask.setId(7);
        SubTask overlapSubTask = taskManager.getSubTaskById(6);

        Exception exception = assertThrows(TaskOverlapAnotherTaskException.class,
                () -> taskManager.updateSubTask(updatedSubTask));
        assertEquals("Fail to updateSubTask because " + updatedSubTask.getName() + " overlap "
                + overlapSubTask.getName(), exception.getMessage(), "Exception not thrown");
        assertNotEquals(expectedTimeOfTask, updatedSubTask.getStartTime(), "StartTime of subTasks not equal");
    }

    @Test
    void shouldRemoveLastSubTaskOfEpic() {
        SubTask subTask = new SubTask("TestSubTask", TaskType.SUBTASK, "TestSubTask",
                30, "2023-07-26T09:00:00", 5);
        taskManager.addSubTask(subTask);
        taskManager.deleteSubTaskById(10);
        Epic epic = taskManager.getEpicById(5);
        LocalDateTime expectedStartTimeOfEpic = LocalDateTime.MAX;
        LocalDateTime expectedEndTimeOfEpic = LocalDateTime.MIN;
        assertEquals(expectedStartTimeOfEpic, epic.getStartTime(), "StartTime not equal");
        assertEquals(expectedEndTimeOfEpic, epic.getEndTime(), "EndTime not equal");
    }

    @Test
    void removedSubTaskStartTimeAndEndTimeNotEqualTOEpicsTime() {
        SubTask subTask1 = new SubTask("TestSubTask1", TaskType.SUBTASK, "TestSubTask1",
                30, "2023-07-26T09:00:00", 5);
        SubTask subTask2 = new SubTask("TestSubTask2", TaskType.SUBTASK, "TestSubTask2",
                30, "2023-07-26T10:00:00", 5);
        SubTask subTask3 = new SubTask("TestSubTask3", TaskType.SUBTASK, "TestSubTask3",
                30, "2023-07-26T12:00:00", 5);

        taskManager.addSubTask(subTask1);
        taskManager.addSubTask(subTask2);
        taskManager.addSubTask(subTask3);
        taskManager.deleteSubTaskById(11);
        Epic epic = taskManager.getEpicById(5);
        LocalDateTime expectedStartTimeOfEpic = subTask1.getStartTime();
        LocalDateTime expectedEndTimeOfEpic = subTask3.getEndTime();
        assertEquals(expectedStartTimeOfEpic, epic.getStartTime(), "StartTime not equal");
        assertEquals(expectedEndTimeOfEpic, epic.getEndTime(), "EndTime not equal");
    }

    @Test
    void shouldNotAddSubTaskIfExists() {
        SubTask testSubTask = new SubTask("SubTask1", TaskType.SUBTASK, "desc_SubTask1",
                20, "2023-07-30T09:00:00", 3);
        taskManager.addSubTask(testSubTask);
        SubTask expectedSubTask = taskManager.getSubTaskById(10);
        assertNull(expectedSubTask, "Subtask found");
    }

    @Test
    void shouldReturnEmptyListIfEpicNotExists() {
        Epic testEpic = new Epic("TestEpic", TaskType.EPIC, "TestEpic");
        List<Integer> subTasksOfEpic = taskManager.getSubTasksOfEpic(testEpic);
        int expectedSize = 0;
        assertEquals(expectedSize, subTasksOfEpic.size());
    }

    @Test
    void endTimeOfTaskIfNoStartTimeEqualsMin() {
        Task testTask = new Task("testTask", TaskType.TASK, "testTask");
        LocalDateTime expectedEndTime = LocalDateTime.MIN;
        assertEquals(expectedEndTime, testTask.getEndTime());
    }

    @Test
    void endTimeOfSubTaskIfNoStartTimeEqualsMin() {
        SubTask testSubTask = new SubTask("testSubTask", TaskType.SUBTASK, "testSubTask", 3);
        LocalDateTime expectedEndTime = LocalDateTime.MIN;
        assertEquals(expectedEndTime, testSubTask.getEndTime());
    }

}
