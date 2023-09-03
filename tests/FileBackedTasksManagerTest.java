import org.junit.jupiter.api.Test;
import services.FileBackedTasksManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    public FileBackedTasksManager createTaskManager() {
        return new FileBackedTasksManager(new File("src/resources/testFile.csv"));
    }

    @Test
    public void testLoadFromFile() {

        FileBackedTasksManager standartManager = FileBackedTasksManager.loadFromFile(new File(
                "tests/testFile.csv"));

        assertEquals(2, standartManager.getEpicsList().size());
        assertEquals(2, standartManager.getTasksList().size());
        assertEquals(2, standartManager.getSubtaskList().size());
        assertEquals(4, standartManager.getHistory().size());
    }

    @Test
    public void testLoadWithEmptyEpicAndSubtaskList() {

        FileBackedTasksManager managerWithEmptyEpicsList = FileBackedTasksManager.loadFromFile(new File(
                "tests/testFile2.csv"));

        assertEquals(0, managerWithEmptyEpicsList.getEpicsList().size());
        assertEquals(1, managerWithEmptyEpicsList.getTasksList().size());
        assertEquals(1, managerWithEmptyEpicsList.getHistory().size());
    }

    @Test
    public void testLoadWithEmptyAllTasksList() {

        FileBackedTasksManager managerWithEmptyAllTasksList = FileBackedTasksManager.loadFromFile(new File(
                "tests/testFile1.csv"));

        assertEquals(0, managerWithEmptyAllTasksList.getEpicsList().size());
        assertEquals(0, managerWithEmptyAllTasksList.getTasksList().size());
        assertEquals(0, managerWithEmptyAllTasksList.getSubtaskList().size());
    }

    @Test
    public void testLoadWithEmptyHistory() {

        FileBackedTasksManager managerWithEmptyHistoryList = FileBackedTasksManager.loadFromFile(new File(
                "tests/testFile3.csv"));

        assertEquals(1, managerWithEmptyHistoryList.getEpicsList().size());
        assertEquals(1, managerWithEmptyHistoryList.getTasksList().size());
        assertEquals(1, managerWithEmptyHistoryList.getSubtaskList().size());
        assertEquals(0, managerWithEmptyHistoryList.getHistory().size());
    }
}
