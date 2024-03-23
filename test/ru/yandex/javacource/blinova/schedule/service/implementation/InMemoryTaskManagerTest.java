package ru.yandex.javacource.blinova.schedule.service.implementation;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Epic;
import ru.yandex.javacource.blinova.schedule.models.tasks.Subtask;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;
import ru.yandex.javacource.blinova.schedule.service.HistoryManager;
import ru.yandex.javacource.blinova.schedule.service.Managers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.javacource.blinova.schedule.models.enums.TaskStatus.*;


class InMemoryTaskManagerTest {

    final InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
    final HistoryManager historyManager = inMemoryTaskManager.getHistoryManager();

    @Test
    void createTask() {
        Task task = new Task("Test createTask", "Test createTask description", null, NEW);
        final Long taskId = inMemoryTaskManager.createTask(task);

        final Task savedTask = inMemoryTaskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = inMemoryTaskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void createEpic() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description", null, NEW);
        inMemoryTaskManager.createEpic(epic);
        final Long epicId = inMemoryTaskManager.createEpic(epic);

        final Epic savedEpic = inMemoryTaskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Эпик не найден.");
        assertEquals(epic, savedEpic, "Эпики не совпадают.");

        final List<Epic> epics = inMemoryTaskManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic, epics.get(0), "Эпики не совпадают.");
    }

    @Test
    void createSubtask() {
        Subtask subtask = new Subtask("Test createSubtask", "Test createSubtask description", null, NEW, 1L);
        Epic epic = new Epic(subtask.getEpicId());
        inMemoryTaskManager.createEpic(epic);
        inMemoryTaskManager.createSubtask(subtask);
        final Long subtaskId = inMemoryTaskManager.createSubtask(subtask);

        final Subtask savedSubtask = inMemoryTaskManager.getSubtask(subtaskId);

        assertNotNull(savedSubtask, "Подзадача не найдена.");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают.");

        final List<Epic> epics = inMemoryTaskManager.getEpics();

        final List<Subtask> subtasks = inMemoryTaskManager.getSubtasks();

        assertNotNull(epics, "Подзадачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(subtask.getEpicId(), epics.get(0).getId(), "Эпики не совпадают.");
    }

    @Test
    void isTaskHeirAreEqualIfTheirIdAreEqual() {
        Task subtask = new Subtask(100L);
        Task epic = new Epic(100L);
        assertEquals(subtask, epic, "Наследники задачи с одинаковым Id не равны");
    }

    @Test
    void getTask() {
        Task task = new Task();
        inMemoryTaskManager.createTask(task);
        assertEquals(task, inMemoryTaskManager.getTask(task.getId()), "Задачи не совпадают.");
    }

    @Test
    void getSubtask() {
        Subtask subtask = new Subtask();
        inMemoryTaskManager.createTask(subtask);
        assertEquals(subtask, inMemoryTaskManager.getTask(subtask.getId()));
    }

    @Test
    void getEpic() {
        Epic epic = new Epic();
        inMemoryTaskManager.createTask(epic);
        assertEquals(epic, inMemoryTaskManager.getTask(epic.getId()));
    }

    @Test
    void isTasksWithSpecifiedIdAndGeneratedIdNotConflict() {
        Task taskWithSpecifiedId = new Task(2L);
        inMemoryTaskManager.createTask(taskWithSpecifiedId);
        Subtask taskWithGeneratedId = new Subtask();
        inMemoryTaskManager.createTask(taskWithGeneratedId);
        assertNotEquals(taskWithGeneratedId.getId(), taskWithSpecifiedId.getId());
    }

    @Test
    void checkImmutabilityTaskFieldsWhenAddingToManager() {
        Epic epic = new Epic("Тестовое название", "Тестовое описание", 100L, IN_PROGRESS);
        inMemoryTaskManager.createTask(epic);
        assertEquals(epic.getId(), 100L, "Id задачи после обработки manager изменился");
        assertEquals(epic.getTaskStatus(), IN_PROGRESS, "Статус задачи после обработки manager изменился");
        assertEquals(epic.getDescription(), "Тестовое описание", "Описание задачи после обработки manager изменился");
        assertEquals(epic.getName(), "Тестовое название", "Название задачи после обработки manager изменился");
    }

    @Test
    void checkAddingTaskInHistoryWhenGetCall() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);
        Subtask subtask = new Subtask(null, null, null, null, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);
        Task task = new Task();
        inMemoryTaskManager.createTask(task);

        inMemoryTaskManager.getTask(task.getId());
        inMemoryTaskManager.getSubtask(subtask.getId());
        inMemoryTaskManager.getEpic(epic.getId());

        assertEquals(3, historyManager.getHistory().size(), "В историю не добавляются задачи при " +
                "вызове get метода");
    }

    @Test
    void checkIdsAfterRemove() {
        for (long l = 1; l <= 5; l++) {
            Task task = new Task(l);
            inMemoryTaskManager.createTask(task);
        }
        inMemoryTaskManager.deleteTask(3L);

        Task taskAfterRemove = new Task();
        inMemoryTaskManager.createTask(taskAfterRemove);
        assertEquals(6L, taskAfterRemove.getId(), "Ошибка формирования id после удаления");

        Task insteadOfDeletedTask = new Task(3L);
        inMemoryTaskManager.createTask(insteadOfDeletedTask);
        assertEquals(6, inMemoryTaskManager.getTasks().size(), "Нарушен порядок добавления id");

        Task newTask = new Task();
        inMemoryTaskManager.createTask(newTask);
        assertEquals(7L, newTask.getId(), "Ошибка формирования id нового объекта после удаления");
    }

    @Test
    void checkEpicStatusAfterChangeSubtaskStatus() {
        Epic epic = new Epic(null, null, null, NEW);
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask(null, null, null, NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        subtask.setTaskStatus(DONE);
        inMemoryTaskManager.updateSubtask(subtask);

        inMemoryTaskManager.updateEpic(epic);
        assertEquals(DONE, epic.getTaskStatus(), "После изменения статуса подзадачи, статус эпика не изменился");
    }

    @Test
    void checkEpicStatusAfterRemoveSubtask() {
        Epic epic = new Epic(null, null, null, IN_PROGRESS);
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask(null, null, null, NEW, epic.getId());
        inMemoryTaskManager.createSubtask(subtask);

        inMemoryTaskManager.deleteSubtask(subtask.getId());

        assertEquals(NEW, epic.getTaskStatus(), "После удаления подзадачи, статус эпика не изменился");
    }

    @Test
    void checkSubtaskCountInEpic() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);

        Subtask firstSubtask = new Subtask(null, null, null, null, epic.getId());
        inMemoryTaskManager.createSubtask(firstSubtask);

        Subtask secondSubtask = new Subtask(null, null, null, null, epic.getId());
        inMemoryTaskManager.createSubtask(secondSubtask);

        assertEquals(2, inMemoryTaskManager.getAllSubtaskByEpic(epic).size(), "Неправильная работа добавления подзадач в эпик");
    }

    @Test
    void checkUpdatingSubtaskInHistory() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);

        Subtask subtaskBeforeUpdate = new Subtask("Название подзадачи до обновления",
                "Описание подзадачи до обновления", 100L, IN_PROGRESS, epic.getId());
        inMemoryTaskManager.createSubtask(subtaskBeforeUpdate);
        inMemoryTaskManager.getSubtask(subtaskBeforeUpdate.getId());

        Subtask subtaskAfterUpdate = new Subtask("Название подзадачи после обновления",
                "Описание подзадачи после обновления", 100L, DONE, epic.getId());
        inMemoryTaskManager.updateSubtask(subtaskAfterUpdate);

        final Subtask checkingSubtask = (Subtask) historyManager.getHistory().get(0);

        assertEquals(checkingSubtask.getId(), 100L, "Id подзадачи после update изменился");
        assertEquals(checkingSubtask.getTaskStatus(), IN_PROGRESS, "Статус подзадачи после update изменился");
        assertEquals(checkingSubtask.getDescription(), "Описание подзадачи до обновления", "Описание подзадачи после update изменился");
        assertEquals(checkingSubtask.getName(), "Название подзадачи до обновления", "Название подзадачи после update изменился");
    }

    @Test
    void checkUpdatingTaskInHistory() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);

        Task taskBeforeUpdate = new Task("Название задачи до обновления",
                "Описание задачи до обновления", 100L, IN_PROGRESS);
        inMemoryTaskManager.createTask(taskBeforeUpdate);
        inMemoryTaskManager.getTask(taskBeforeUpdate.getId());

        Task taskAfterUpdate = new Task("Название задачи после обновления",
                "Описание задачи после обновления", 100L, DONE);
        inMemoryTaskManager.updateTask(taskAfterUpdate);

        final Task checkingTask = historyManager.getHistory().get(0);

        assertEquals(checkingTask.getId(), 100L, "Id задачи после update изменился");
        assertEquals(checkingTask.getTaskStatus(), IN_PROGRESS, "Статус задачи после update изменился");
        assertEquals(checkingTask.getDescription(), "Описание задачи до обновления", "Описание задачи после update изменился");
        assertEquals(checkingTask.getName(), "Название задачи до обновления", "Название задачи после update изменился");
    }

    @Test
    void checkUpdatingEpicInHistory() {
        Epic epicBeforeUpdate = new Epic("Название эпика до обновления", "Описание эпика до обновления",
                100L, IN_PROGRESS);
        inMemoryTaskManager.createEpic(epicBeforeUpdate);

        inMemoryTaskManager.createEpic(epicBeforeUpdate);
        inMemoryTaskManager.getEpic(epicBeforeUpdate.getId());

        Epic epicAfterUpdate = new Epic("Название эпика после обновления",
                "Описание эпика после обновления", 100L, DONE);
        inMemoryTaskManager.updateEpic(epicAfterUpdate);

        final Epic checkingEpic = (Epic) historyManager.getHistory().get(0);

        assertEquals(checkingEpic.getId(), 100L, "Id эпика после update изменился");
        assertEquals(checkingEpic.getTaskStatus(), IN_PROGRESS, "Статус эпика после update изменился");
        assertEquals(checkingEpic.getDescription(), "Описание эпика до обновления", "Описание эпика после update изменился");
        assertEquals(checkingEpic.getName(), "Название эпика до обновления", "Название эпика после update изменился");
    }
}