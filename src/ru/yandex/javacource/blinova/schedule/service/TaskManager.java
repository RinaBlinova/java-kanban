package ru.yandex.javacource.blinova.schedule.service;

import ru.yandex.javacource.blinova.schedule.models.tasks.Epic;
import ru.yandex.javacource.blinova.schedule.models.tasks.Subtask;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;

import java.util.List;

public interface TaskManager {
    Long createTask(Task task);

    Long createEpic(Epic epic);

    Long createSubtask(Subtask subtask);

    Task getTask(Long id);

    Subtask getSubtask(Long id);

    Epic getEpic(Long id);

    void updateSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void deleteTask(Long taskId);

    void deleteSubtask(Long subtaskId);

    void deleteEpic(Long epicId);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    List<Subtask> getAllSubtaskByEpic(Epic epic);

    HistoryManager getHistoryManager();
}
