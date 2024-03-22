package ru.yandex.javacource.blinova.schedule.models.tasks;

import ru.yandex.javacource.blinova.schedule.models.enums.TaskStatus;

public class Subtask extends Task {
    private Long epicId;

    public Subtask(String name, String description, Long id, TaskStatus taskStatus, Long epicId) {
        super(name, description, id, taskStatus);
        this.epicId = epicId;
    }

    public Subtask() {
    }

    public Subtask(Long id) {
        super(id);
    }

    public Long getEpicId() {
        return epicId;
    }

    public void setEpicId(Long epicId) {
        if (epicId.equals(this.getId())) {
            return;
        }
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
