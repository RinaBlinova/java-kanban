package ru.yandex.javacource.blinova.schedule.models.tasks;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    public void isTwoTasksWithSameIdEqual() {
        Task firstTask = new Task(100L);
        Task secondTask = new Task(100L);
        assertEquals(firstTask, secondTask, "Две задачи с одинаковым Id не равны");
    }

}