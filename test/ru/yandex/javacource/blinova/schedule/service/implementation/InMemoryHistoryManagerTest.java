package ru.yandex.javacource.blinova.schedule.service.implementation;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;
import ru.yandex.javacource.blinova.schedule.service.HistoryManager;
import ru.yandex.javacource.blinova.schedule.service.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    void testAddTaskInHistory() {
        Task task = new Task(100L);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void checkDeletingTaskDuplicates() {
        Task task = new Task();
        historyManager.add(task);
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Повторяющиеся задания не удаляются из истории");

        for (int i = 0; i < 10; i++) {
            historyManager.add(task);
        }
        assertEquals(1, historyManager.getHistory().size(), "Повторяющиеся задания не удаляются из истории");
    }

    @Test
    void checkRemovingTask() {
        for (long l = 0; l < 10; l++) {
            Task task = new Task(l);
            historyManager.add(task);
        }
        historyManager.remove(1L);
        historyManager.remove(2L);
        historyManager.remove(3L);
        historyManager.remove(9L);
        assertEquals(6, historyManager.getHistory().size(), "Задания не удаляются из истории");

    }
}