public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task firstTask = new Task("Задача 1", "Описание первой задачи", taskManager.generateId(), TaskStatus.NEW);
        taskManager.createTask(firstTask);
        Task secondTask = new Task("Задача 2", "Описание второй задачи", taskManager.generateId(), TaskStatus.NEW);
        taskManager.createTask(secondTask);

        SubTask firstSubTask = new SubTask("Подзадача 1", "Описание первой подзадачи",
                taskManager.generateId(), TaskStatus.NEW);
        taskManager.createTask(firstSubTask);
        SubTask secondSubTask = new SubTask("Подзадача 2", "Описание второй подзадачи",
                taskManager.generateId(), TaskStatus.NEW);
        taskManager.createTask(secondSubTask);

        Epic firstEpic = new Epic("Эпик 1", "Описание первого эпика",
                taskManager.generateId(), TaskStatus.NEW);
        firstEpic.addSubTask(firstSubTask);
        firstEpic.addSubTask(secondSubTask);
        taskManager.createTask(firstEpic);

        SubTask thirdSubTask = new SubTask("Подзадача 3", "Описание третьей подзадачи",
                taskManager.generateId(), TaskStatus.NEW);
        taskManager.createTask(thirdSubTask);
        Epic secondEpic = new Epic("Эпик 2", "Описание второго эпика",
                taskManager.generateId(), TaskStatus.NEW);
        secondEpic.addSubTask(thirdSubTask);
        taskManager.createTask(secondEpic);

        System.out.println("Вывод созданных задач:");
        System.out.println(firstTask);
        System.out.println(secondTask);
        System.out.println("");

        System.out.println("Вывод созданных подзадач:");
        System.out.println(firstSubTask);
        System.out.println(secondSubTask);
        System.out.println(thirdSubTask);
        System.out.println("");

        System.out.println("Вывод созданных эпиков:");
        System.out.println(firstEpic);
        System.out.println(secondEpic);
        System.out.println("");

        System.out.println("Вывод всех задач:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("");

        System.out.println("Перевод подзадачи в статус DONE. Эпик должен сменить статус на DONE:");
        System.out.println(("Старый статус эпика: " + secondEpic.getTaskStatus()));
        thirdSubTask.setTaskStatus(TaskStatus.DONE);
        taskManager.epicUpdate(secondEpic);
        System.out.println("Статус эпика после изменения статуса подзадачи: " + secondEpic.getTaskStatus());
        System.out.println("");

        System.out.println("Удаление подзадачи. Эпик должен сменить статус на NEW:");
        System.out.println(("Старый статус эпика: " + secondEpic.getTaskStatus()));
        taskManager.deleteTaskById(6L);
        taskManager.epicUpdate(secondEpic);
        System.out.println("Статус эпика после удаления подзадачи: " + secondEpic.getTaskStatus());
        System.out.println("");

        System.out.println("Вывод всех подзадач эпика:");
        System.out.println(taskManager.getAllSubTaskByEpic(firstEpic));
        System.out.println("");

        System.out.println("Удаление задачи:");
        taskManager.deleteTaskById(1L);
        System.out.println(taskManager.getTaskById(1L));
        System.out.println("");

        System.out.println("Удаление эпика:");
        taskManager.deleteTaskById(7L);
        System.out.println(taskManager.getTaskById(7L));
    }
}
