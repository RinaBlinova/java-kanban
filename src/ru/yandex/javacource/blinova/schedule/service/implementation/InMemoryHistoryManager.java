package ru.yandex.javacource.blinova.schedule.service.implementation;

import ru.yandex.javacource.blinova.schedule.service.HistoryManager;
import ru.yandex.javacource.blinova.schedule.models.tasks.Task;
import ru.yandex.javacource.blinova.schedule.util.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Long, Node> history;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        this.history = new LinkedHashMap<>();
    }

    private void linkLast(Task task) {
        Node newNode = new Node(task);
        if (tail == null) {
            head = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
        }
        tail = newNode;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.getTask());
            current = current.getNext();
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
    }

    @Override
    public void add(Task task) {
        Long taskId = task.getId();
        if (history.containsKey(taskId)) {
            Node existingTask = history.get(taskId);
            removeNode(existingTask);
            history.remove(taskId);
        }
        linkLast(task);
        history.put(taskId, tail);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void remove(Long id) {
        if (id == null) {
            return;
        }
        if (history.containsKey(id)) {
            Node nodeToRemove = history.get(id);
            removeNode(nodeToRemove);
            history.remove(id);
        }
    }
}
