package ru.yandex.javacource.blinova.schedule.tasks;

import ru.yandex.javacource.blinova.schedule.enums.TaskStatus;

public class Subtask extends Task {
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

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
