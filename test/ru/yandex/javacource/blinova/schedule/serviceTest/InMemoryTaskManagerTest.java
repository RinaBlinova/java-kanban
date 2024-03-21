package ru.yandex.javacource.blinova.schedule.serviceTest;

import org.junit.jupiter.api.Test;
import ru.yandex.javacource.blinova.schedule.models.tasks.Epic;
import ru.yandex.javacource.blinova.schedule.models.tasks.Subtask;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;
import ru.yandex.javacource.blinova.schedule.service.implementation.InMemoryHistoryManager;
import ru.yandex.javacource.blinova.schedule.service.implementation.InMemoryTaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.javacource.blinova.schedule.models.enums.TaskStatus.*;


class InMemoryTaskManagerTest {


    final InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    final InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(historyManager);

    @Test
    void createTask() {
        Task task = new Task();
        task.setName("Test createTask");
        task.setDescription("Test createTask description");
        task.setTaskStatus(NEW);
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
        Epic epic = new Epic();
        epic.setName("Test createEpic");
        epic.setDescription("Test createEpic description");
        epic.setTaskStatus(NEW);
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
        Subtask subtask = new Subtask();
        subtask.setName("Test createSubtask");
        subtask.setDescription("Test createSubtask description");
        subtask.setTaskStatus(NEW);
        subtask.setEpicId(1L);
        Epic epic = new Epic();
        epic.setId(subtask.getEpicId());
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
        Task subtask = new Subtask();
        subtask.setId(100L);
        Task epic = new Epic();
        epic.setId(100L);
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
        Task taskWithSpecifiedId = new Task();
        taskWithSpecifiedId.setId(2L);
        inMemoryTaskManager.createTask(taskWithSpecifiedId);
        Subtask taskWithGeneratedId = new Subtask();
        inMemoryTaskManager.createTask(taskWithGeneratedId);
        assertNotEquals(taskWithGeneratedId.getId(), taskWithSpecifiedId.getId());
    }

    @Test
    void checkImmutabilityTaskFieldsWhenAddingToManager() {
        Epic epic = new Epic();
        epic.setId(100L);
        epic.setTaskStatus(IN_PROGRESS);
        epic.setDescription("Тестовое описание");
        epic.setName("Тестовое название");
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
        Subtask subtask = new Subtask();
        subtask.setEpicId(epic.getId());
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
            Task task = new Task();
            task.setId(l);
            inMemoryTaskManager.createTask(task);
        }
        inMemoryTaskManager.deleteTask(3L);

        Task taskAfterRemove = new Task();
        inMemoryTaskManager.createTask(taskAfterRemove);
        assertEquals(6L, taskAfterRemove.getId(), "Ошибка формирования id после удаления");

        Task insteadOfDeletedTask = new Task();
        insteadOfDeletedTask.setId(3L);
        inMemoryTaskManager.createTask(insteadOfDeletedTask);
        assertEquals(6, inMemoryTaskManager.getTasks().size(), "Нарушен порядок добавления id");

        Task newTask = new Task();
        inMemoryTaskManager.createTask(newTask);
        assertEquals(7L, newTask.getId(), "Ошибка формирования id нового объекта после удаления");
    }

    @Test
    void checkEpicStatusAfterChangeSubtaskStatus() {
        Epic epic = new Epic();
        epic.setTaskStatus(NEW);
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setEpicId(epic.getId());
        subtask.setTaskStatus(NEW);
        inMemoryTaskManager.createSubtask(subtask);

        subtask.setTaskStatus(DONE);
        inMemoryTaskManager.epicUpdate(epic);
        assertEquals(DONE, epic.getTaskStatus(), "После изменения статуса подзадачи, статус эпика не изменился");
    }

    @Test
    void checkEpicStatusAfterRemoveSubtask() {
        Epic epic = new Epic();
        epic.setTaskStatus(IN_PROGRESS);
        inMemoryTaskManager.createEpic(epic);

        Subtask subtask = new Subtask();
        subtask.setEpicId(epic.getId());
        subtask.setTaskStatus(NEW);
        inMemoryTaskManager.createSubtask(subtask);

        inMemoryTaskManager.deleteSubtask(subtask.getId());

        assertEquals(NEW, epic.getTaskStatus(), "После удаления подзадачи, статус эпика не изменился");
    }

    @Test
    void checkSubtaskCountInEpic() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);

        Subtask firstSubtask = new Subtask();
        firstSubtask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubtask(firstSubtask);

        Subtask secondSubtask = new Subtask();
        secondSubtask.setEpicId(epic.getId());
        inMemoryTaskManager.createSubtask(secondSubtask);

        assertEquals(2, inMemoryTaskManager.getAllSubtaskByEpic(epic).size(), "Неправильная работа добавления подзадач в эпик");
    }

    @Test
    void checkUpdatingSubtask() {
        Epic epic = new Epic();
        inMemoryTaskManager.createEpic(epic);

        Subtask subtaskBeforeUpdate = new Subtask();
        subtaskBeforeUpdate.setId(100L);
        subtaskBeforeUpdate.setTaskStatus(IN_PROGRESS);
        subtaskBeforeUpdate.setName("Название подзадачи до обновления");
        subtaskBeforeUpdate.setDescription("Описание подзадачи до обновления");
        subtaskBeforeUpdate.setEpicId(epic.getId());
        inMemoryTaskManager.createSubtask(subtaskBeforeUpdate);

        Subtask subtaskAfterUpdate = new Subtask();
        subtaskAfterUpdate.setId(100L);
        subtaskAfterUpdate.setTaskStatus(DONE);
        subtaskAfterUpdate.setName("Название подзадачи после обновления");
        subtaskAfterUpdate.setDescription("Описание подзадачи после обновления");
        subtaskAfterUpdate.setEpicId(epic.getId());
        inMemoryTaskManager.updateSubtask(subtaskAfterUpdate);

        final Subtask checkSubtask = inMemoryTaskManager.getSubtask(100L);

        assertEquals(checkSubtask.getId(), 100L, "Id задачи после update изменился");
        assertEquals(checkSubtask.getTaskStatus(), DONE, "Статус задачи после update изменился");
        assertEquals(checkSubtask.getDescription(), "Описание подзадачи после обновления", "Описание задачи после update изменился");
        assertEquals(checkSubtask.getName(), "Название подзадачи после обновления", "Название задачи после update изменился");
    }
}