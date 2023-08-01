package test;

import services.InMemoryTaskManager;
import services.Managers;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createTaskManager() {
        return (InMemoryTaskManager) Managers.getFileBacked();
    }
}
