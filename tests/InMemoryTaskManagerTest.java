import services.Managers;
import services.TaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {
    TaskManager manager = Managers.getDefault();

    @Override
    TaskManager createTaskManager() {
        return manager;
    }
}
