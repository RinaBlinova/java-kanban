package ru.yandex.javacource.blinova.schedule.service;

import ru.yandex.javacource.blinova.schedule.service.implementation.InMemoryHistoryManager;
import ru.yandex.javacource.blinova.schedule.service.implementation.InMemoryTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
