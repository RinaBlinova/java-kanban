package ru.yandex.javacource.blinova.schedule.serviceTest;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Epic;
import ru.yandex.javacource.blinova.schedule.service.HistoryManager;
import ru.yandex.javacource.blinova.schedule.service.TaskManager;
import ru.yandex.javacource.blinova.schedule.service.Managers;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void checkInitializedAndReadyToUseInstances() {
        final TaskManager taskManager = Managers.getDefault();
        final HistoryManager historyManager = taskManager.getHistoryManager();

        Epic epic = new Epic();
        taskManager.createEpic(epic);
        assertEquals(1, taskManager.getEpics().size(), "Ошибка при создании TaskManager");

        taskManager.getEpic(epic.getId());
        assertEquals(1, historyManager.getHistory().size(), "Ошибка при создании HistoryManager");
    }
}