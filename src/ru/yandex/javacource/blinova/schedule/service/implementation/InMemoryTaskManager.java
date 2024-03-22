package ru.yandex.javacource.blinova.schedule.service.implementation;

import ru.yandex.javacource.blinova.schedule.models.enums.TaskStatus;
import ru.yandex.javacource.blinova.schedule.service.HistoryManager;
import ru.yandex.javacource.blinova.schedule.service.TaskManager;
import ru.yandex.javacource.blinova.schedule.models.tasks.Epic;
import ru.yandex.javacource.blinova.schedule.models.tasks.Subtask;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.javacource.blinova.schedule.models.enums.TaskStatus.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Long, Task> tasks;
    private final HashMap<Long, Subtask> subtasks;
    private final HashMap<Long, Epic> epics;

    private Long generatorId;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.generatorId = 0L;
    }

    @Override
    public Long createTask(Task task) {
        Long id = checkId(task);
        task.setId(id);
        TaskStatus taskStatus = checkTaskStatus(task);
        task.setTaskStatus(taskStatus);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public Long createEpic(Epic epic) {
        Long id = checkId(epic);
        epic.setId(id);
        TaskStatus taskStatus = checkTaskStatus(epic);
        epic.setTaskStatus(taskStatus);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    @Override
    public Long createSubtask(Subtask subtask) {
        Long epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        Long id = checkId(subtask);
        subtask.setId(id);
        TaskStatus taskStatus = checkTaskStatus(subtask);
        subtask.setTaskStatus(taskStatus);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        updateEpicStatus(epicId);
        return id;
    }

    @Override
    public Task getTask(Long id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Subtask getSubtask(Long id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public Epic getEpic(Long id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        Long id = subtask.getId();
        Long epicId = subtask.getEpicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateEpicStatus(epicId);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        final Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
//        for (Long subtaskId : savedEpic.getSubtaskList()) {
//            epic.addSubtaskId(subtaskId);
//        }
        epic.setSubtaskIds(savedEpic.getSubtaskIds());
        epic.setTaskStatus(savedEpic.getTaskStatus());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void deleteTask(Long taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteSubtask(Long subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(subtaskId);
        updateEpicStatus(epic.getId());
    }

    @Override
    public void deleteEpic(Long epicId) {
        final Epic epic = epics.remove(epicId);
        for (Long subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
        }
        subtasks.clear();
        epics.clear();
    }

    public void epicUpdate(Epic epic) {
        if (epic != null) {
            updateEpicStatus(epic.getId());
        }
    }

    private Long generateId() {
        return ++generatorId;
    }

    public List<Subtask> getAllSubtaskByEpic(Epic epic) {
        ArrayList<Subtask> result = new ArrayList<>();
        ArrayList<Long> subtaskIds = epic.getSubtaskIds();
        for (Long subtaskId : subtaskIds) {
            result.add(subtasks.get(subtaskId));
        }
        return result;
    }

    @Override
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void updateEpicStatus(Long epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Long> subtaskIds = epic.getSubtaskIds();
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        if (subtaskIds.isEmpty()) {
            epic.setTaskStatus(NEW);
        }
        for (Long subtaskId : subtaskIds) {
            Subtask subTask = subtasks.get(subtaskId);
            if (NEW.equals(subTask.getTaskStatus())) {
                newStatusCounter++;
            }
            if (DONE.equals(subTask.getTaskStatus())) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == subtaskIds.size()) {
            epic.setTaskStatus(NEW);
        } else if (doneStatusCounter == subtaskIds.size()) {
            epic.setTaskStatus(DONE);
        } else {
            epic.setTaskStatus(IN_PROGRESS);
        }
    }

    private Long checkId(Task task) {
        Long id;
        if (task.getId() != null) {
            id = task.getId();
            if (task.getId() > generatorId) {
                generatorId = id;
            }
        } else {
            id = generateId();
        }
        return id;
    }

    private TaskStatus checkTaskStatus(Task task) {
        TaskStatus taskStatus;
        if (task.getTaskStatus() != null) {
            taskStatus = task.getTaskStatus();
        } else {
            taskStatus = NEW;
        }
        return taskStatus;
    }
}

