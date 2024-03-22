package ru.yandex.javacource.blinova.schedule.models.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    public void isEpicCannotAddedToItselfAsSubTask() {
        Epic epic = new Epic(100L);
        epic.addSubtaskId(100L);
        assertNotEquals(epic.getSubtaskIds().size(), 1, "Эпик может добавить самого себя как подзадачу");
    }
}