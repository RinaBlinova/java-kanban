package ru.yandex.javacource.blinova.schedule.modelsTest;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Subtask;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    public void isSubtaskCantMakeOwnEpic() {
        Subtask subtask = new Subtask();
        subtask.setId(100L);
        subtask.setEpicId(100L);
        assertNotEquals(subtask.getEpicId(), 100L, "Сабтаску можно сделать своим эпиком");
    }
}