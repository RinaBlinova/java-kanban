package ru.yandex.javacource.blinova.schedule.modelsTest;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Epic;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void isEpicCannotAddedToItselfAsSubTask() {
        Epic epic = new Epic();
        epic.setId(100L);
        epic.addSubtaskId(100L);
        assertNotEquals(epic.getSubtaskList().size(), 1, "Эпик может добавить самого себя как подзадачу");
    }
}