package services;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(String url, String key) {
        return new HttpTaskManager(url, key);
    }
}
