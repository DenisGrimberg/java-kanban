package services;

import com.google.gson.*;
import entity.Epic;
import entity.Subtask;
import entity.Task;
import servers.KVTaskClient;
import servers.adapters.FileAdapter;
import servers.adapters.HistoryManagerAdapter;
import servers.adapters.LocalDateTimeAdapter;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(File.class, new FileAdapter())
            .registerTypeAdapter(HistoryManager.class, new HistoryManagerAdapter())
            .serializeNulls().create();
    private final String key;
    private final String url;

    public HttpTaskManager(String url, String key) {
        this.url = url;
        this.key = key;
    }

    @Override
    public void save() {
        KVTaskClient taskClient = new KVTaskClient(url);
        taskClient.put("task", gson.toJson(tasks.values()));
        taskClient.put("subtask", gson.toJson(subtasks.values()));
        taskClient.put("epic", gson.toJson(epics.values()));
        taskClient.put("tasks", gson.toJson(getPrioritizedTasks()));
        List<Integer> historyIds = getHistory()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toList());
        taskClient.put("history", gson.toJson(historyIds));
    }

    public void load() {
        loadTasks("task");
        loadTasks("subtask");
        loadTasks("epic");
        loadHistry();
    }

    private void loadTasks(String key) {
        JsonElement jsonElement = JsonParser.parseString(new KVTaskClient(url).load(key));
        JsonArray jsonTasksArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonTasksArray) {
            Task task;
            Epic epic;
            Subtask subtask;
            switch (key) {
                case "task":
                    task = gson.fromJson(element.getAsJsonObject(), Task.class);
                    tasks.put(task.getId(), task);
                    break;
                case "subtask":
                    subtask = gson.fromJson(element.getAsJsonObject(), Subtask.class);
                    subtasks.put(subtask.getId(), subtask);
                    break;
                case "epic":
                    epic = gson.fromJson(element.getAsJsonObject(), Epic.class);
                    epics.put(epic.getId(), epic);
                    break;
                default:
                    System.out.println("Unable to upload tasks");
                    return;
            }
        }
    }

    private void loadHistry() {
        JsonElement jsonElement = JsonParser.parseString(new KVTaskClient(url).load("history"));
        JsonArray jsonHistoryArray = jsonElement.getAsJsonArray();
        for (JsonElement element : jsonHistoryArray) {
            int id = element.getAsInt();
            if (tasks.containsKey(id)) {
                historyManager.add(tasks.get(id));
            } else if (epics.containsKey(id)) {
                historyManager.add(epics.get(id));
            } else if (subtasks.containsKey(id)) {
                historyManager.add(subtasks.get(id));
            }
        }
    }
}