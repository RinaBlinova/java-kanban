package ru.yandex.javacource.blinova.schedule.tasks;

import ru.yandex.javacource.blinova.schedule.enums.TaskStatus;

public class Subtask extends Task {
    private String name;
    private String description;
    private Long id;
    public TaskStatus taskStatus;
    private Long epicId;


    public Subtask(String name, String description, Long id, TaskStatus taskStatus) {
        super(name, description, id, taskStatus);
    }

    public Subtask() {
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        this.epicId = epicId;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", taskStatus=" + taskStatus +
                ", epicId=" + epicId +
                '}';
    }
}
