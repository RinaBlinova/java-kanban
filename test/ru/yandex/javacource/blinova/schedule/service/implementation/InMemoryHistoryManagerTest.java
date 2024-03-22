package ru.yandex.javacource.blinova.schedule.service.implementation;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void add() {
        Task task = new Task(100L);
        historyManager.add(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void checkHistoryListSize() {
        for (long l = 1; l <= 100; l++) {
            Task task = new Task(l);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "Размер истории после добавления 100 " +
                "элементов неверен");
        historyManager.getHistory().clear();

        for (long l = 1; l <= 10; l++) {
            Task task = new Task(l);
            historyManager.add(task);
        }
        assertEquals(10, historyManager.getHistory().size(), "Размер истории после добавления 10 элементов" +
                " неверен");

        Task newTask = new Task(11L);
        historyManager.add(newTask);

        assertEquals(10, historyManager.getHistory().size(), "Размер истории неверен");
        assertEquals(2L, historyManager.getHistory().get(0).getId(), "1 элемент не удаляется после добавления лишней задачи");
        assertEquals(11L, historyManager.getHistory().get(historyManager.getHistory().size() - 1).getId(), "Последний элемент добавился неверно");
    }
}