package ru.yandex.javacource.blinova.schedule.service.implementation;

import ru.yandex.javacource.blinova.schedule.service.HistoryManager;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    List<Task> historyTaskIdList;

    public InMemoryHistoryManager() {
        this.historyTaskIdList = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyTaskIdList.size() == 10) {
            historyTaskIdList.remove(0);
        }
        historyTaskIdList.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return historyTaskIdList;
    }
}
