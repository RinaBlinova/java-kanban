package ru.yandex.javacource.blinova.schedule.service;

import ru.yandex.javacource.blinova.schedule.models.tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();

    void remove(Long id);
}
