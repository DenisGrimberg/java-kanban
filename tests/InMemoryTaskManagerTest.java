import services.InMemoryTaskManager;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @Override
    public InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }
}
