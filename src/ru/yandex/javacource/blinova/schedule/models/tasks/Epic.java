package ru.yandex.javacource.blinova.schedule.models.tasks;

import ru.yandex.javacource.blinova.schedule.models.enums.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Long> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, Long id, TaskStatus taskStatus) {
        super(name, description, id, taskStatus);
    }

    public Epic() {

    }

    public Epic(Long id) {
        super(id);
    }

    public ArrayList<Long> getSubtaskIds() {
        return subtaskIds;
    }

    public void addSubtaskId(Long subtaskId) {
        if (subtaskId.equals(this.getId())) {
            return;
        }
        subtaskIds.add(subtaskId);
    }

    public void removeSubtask(Long id) {
        subtaskIds.remove(id);
    }

    public void cleanSubtaskIds() {
        subtaskIds.clear();
    }

    public void setSubtaskIds(ArrayList<Long> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", taskStatus=" + getTaskStatus() +
                '}';
    }
}
