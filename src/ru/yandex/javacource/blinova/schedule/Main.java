package ru.yandex.javacource.blinova.schedule;

import ru.yandex.javacource.blinova.schedule.enums.TaskStatus;
import ru.yandex.javacource.blinova.schedule.manager.TaskManager;
import ru.yandex.javacource.blinova.schedule.tasks.Epic;
import ru.yandex.javacource.blinova.schedule.tasks.Subtask;
import ru.yandex.javacource.blinova.schedule.tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task();
        firstTask.setName("Задача 1");
        firstTask.setDescription("Описание первой задачи");
        taskManager.createTask(firstTask);

        Task secondTask = new Task();
        secondTask.setName("Задача 2");
        secondTask.setDescription("Описание второй задачи");
        taskManager.createTask(secondTask);

        Epic firstEpic = new Epic();
        firstEpic.setName("Эпик 1");
        firstEpic.setDescription("Описание первого эпика");
        taskManager.createEpic(firstEpic);

        Subtask firstSubtask = new Subtask();
        firstSubtask.setName("Подзадача 1");
        firstSubtask.setDescription("Описание первой подзадачи");
        firstSubtask.setEpicId(firstEpic.getId());
        taskManager.createSubtask(firstSubtask);

        Subtask secondSubtask = new Subtask();
        secondSubtask.setName("Подзадача 2");
        secondSubtask.setDescription("Описание второй подзадачи");
        secondSubtask.setEpicId(firstEpic.getId());
        taskManager.createSubtask(secondSubtask);

        Epic secondEpic = new Epic();
        secondEpic.setName("Эпик 2");
        secondEpic.setDescription("Описание второго эпика");
        taskManager.createEpic(secondEpic);

        Subtask thirdSubtask = new Subtask();
        thirdSubtask.setName("Подзадача 3");
        thirdSubtask.setDescription("Описание третьей подзадачи");
        thirdSubtask.setEpicId(secondEpic.getId());
        taskManager.createSubtask(thirdSubtask);

        System.out.println("Вывод созданных задач:");
        System.out.println(firstTask);
        System.out.println(secondTask);
        System.out.println();

        System.out.println("Вывод созданных подзадач:");
        System.out.println(firstSubtask);
        System.out.println(secondSubtask);
        System.out.println(thirdSubtask);
        System.out.println();

        System.out.println("Вывод созданных эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println();

        System.out.println("Вывод всех задач:");
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics());
        System.out.println();

        System.out.println("Перевод подзадачи в статус DONE. Эпик должен сменить статус на DONE:");
        System.out.println(("Старый статус эпика: " + secondEpic.getTaskStatus()));
        thirdSubtask.setTaskStatus(TaskStatus.DONE);
        taskManager.epicUpdate(secondEpic);
        System.out.println("Статус эпика после изменения статуса подзадачи: " + secondEpic.getTaskStatus());
        System.out.println();

        System.out.println("Удаление подзадачи. Эпик должен сменить статус на NEW:");
        System.out.println(("Старый статус эпика: " + secondEpic.getTaskStatus()));
        taskManager.deleteSubtask(7L);
        taskManager.epicUpdate(secondEpic);
        System.out.println("Статус эпика после удаления подзадачи: " + secondEpic.getTaskStatus());
        System.out.println();

        System.out.println("Вывод всех подзадач эпика:");
        System.out.println(taskManager.getAllSubtaskByEpic(firstEpic));
        System.out.println();

        System.out.println("Удаление всех подзадач :");
        taskManager.deleteSubtasks();
        System.out.println("Статус эпика должен поменяться на NEW");
        System.out.println(firstEpic.getTaskStatus());
        System.out.println();

        System.out.println("Удаление всех эпиков' :");
        taskManager.deleteEpics();
        System.out.println("Все сабтаски должны удалиться");
        System.out.println(taskManager.getSubtasks());
    }
}
