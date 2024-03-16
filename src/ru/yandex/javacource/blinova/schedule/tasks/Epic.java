package ru.yandex.javacource.blinova.schedule.tasks;

import ru.yandex.javacource.blinova.schedule.enums.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Long> subtaskIds = new ArrayList<>();
    private String name;
    private String description;
    private Long id;
    public TaskStatus taskStatus;

    public Epic(String name, String description, Long id, TaskStatus taskStatus) {
        super(name, description, id, taskStatus);
    }

    public Epic() {

    }

    public ArrayList<Long> getSubtaskList() {
        return subtaskIds;
    }

    public void addSubtaskId(Long subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(Long id) {
        subtaskIds.remove(id);
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    @Override
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
