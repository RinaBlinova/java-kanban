package ru.yandex.javacource.blinova.schedule.service.implementation;

import ru.yandex.javacource.blinova.schedule.service.HistoryManager;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final List<Task> history;

    private static final int HISTORY_SIZE = 10;

    public InMemoryHistoryManager() {
        this.history = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() == HISTORY_SIZE) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
