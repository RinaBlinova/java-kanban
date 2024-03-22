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

    @Test
    void checkHistoryListSize() {
        for (long l = 1; l <= 10; l++) {
            Task task = new Task();
            task.setId(l);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size());

        Task newTask = new Task();
        newTask.setId(11L);
        historyManager.add(newTask);

        assertEquals(10, historyManager.getHistory().size());
        assertEquals(2L, historyManager.getHistory().get(0).getId());
        assertEquals(11L, historyManager.getHistory().get(historyManager.getHistory().size() - 1).getId());
    }
}