package ru.yandex.javacource.blinova.schedule.serviceTest;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;
import ru.yandex.javacource.blinova.schedule.service.implementation.InMemoryHistoryManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void add() {
        Task task = new Task();
        task.setId(100L);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
}