package ru.yandex.javacource.blinova.schedule.tasks;

import ru.yandex.javacource.blinova.schedule.enums.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Long> subtaskIds = new ArrayList<>();

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
