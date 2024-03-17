package ru.yandex.javacource.blinova.schedule.manager;

import ru.yandex.javacource.blinova.schedule.enums.TaskStatus;
import ru.yandex.javacource.blinova.schedule.tasks.Epic;
import ru.yandex.javacource.blinova.schedule.tasks.Subtask;
import ru.yandex.javacource.blinova.schedule.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Long, Task> tasks;
    private final HashMap<Long, Subtask> subtasks;
    private final HashMap<Long, Epic> epics;

    private Long generatorId;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.generatorId = 0L;
    }

    public Long createTask(Task task) {
        task.setId(generateId());
        task.setTaskStatus(TaskStatus.NEW);
        tasks.put(task.getId(), task);
        return task.getId();
    }

    public Long createEpic(Epic epic) {
        epic.setId(generateId());
        epic.setTaskStatus(TaskStatus.NEW);
        epics.put(epic.getId(), epic);
        return epic.getId();
    }

    public Long createSubtask(Subtask subtask) {
        Long epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        Long id = generateId();
        subtask.setId(id);
        subtask.setTaskStatus(TaskStatus.NEW);
        subtasks.put(id, subtask);
        epic.addSubtaskId(id);
        updateEpicStatus(epicId);
        return id;
    }

    public Task getTaskById(Long id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(Long id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(Long id) {
        return epics.get(id);
    }


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

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic existedEpic = epics.get(epic.getId());
            existedEpic.setName(epic.getName());
            existedEpic.setDescription(epic.getDescription());
            epics.put(existedEpic.getId(), existedEpic);
        }
    }

    public void deleteTask(Long taskId) {
        tasks.remove(taskId);
    }

    public void deleteSubtask(Long subtaskId) {
        Subtask subtask = subtasks.remove(subtaskId);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(subtaskId);
        updateEpicStatus(epic.getId());
    }

    public void deleteEpic(Long epicId) {
        final Epic epic = epics.remove(epicId);
        for (Long subtaskId : epic.getSubtaskList()) {
            subtasks.remove(subtaskId);
        }
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            updateEpicStatus(epic.getId());
        }
        subtasks.clear();
    }

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

    public ArrayList<Subtask> getAllSubtaskByEpic(Epic epic) {
        ArrayList<Subtask> result = new ArrayList<>();
        ArrayList<Long> subtaskIds = epic.getSubtaskList();
        for (Long subtaskId : subtaskIds) {
            result.add(subtasks.get(subtaskId));
        }
        return result;
    }

    private void updateEpicStatus(Long epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Long> subtaskIds = epic.getSubtaskList();
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        if (subtaskIds.isEmpty()) {
            epic.setTaskStatus(TaskStatus.NEW);
        }
        for (Long subtaskId : subtaskIds) {
            Subtask subTask = subtasks.get(subtaskId);
            if (TaskStatus.NEW.equals(subTask.getTaskStatus())) {
                newStatusCounter++;
            }
            if (TaskStatus.DONE.equals(subTask.getTaskStatus())) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == subtaskIds.size()) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (doneStatusCounter == subtaskIds.size()) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        }
    }
}

