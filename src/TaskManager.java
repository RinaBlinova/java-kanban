import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private final HashMap<Long, Task> taskHashMap;
    private final HashMap<Long, SubTask> subTaskHashMap;
    private final HashMap<Long, Epic> epicHashMap;

    private Long nextId;

    public TaskManager () {
        this.taskHashMap = new HashMap<>();
        this.subTaskHashMap = new HashMap<>();
        this.epicHashMap = new HashMap<>();
        this.nextId = 1L;
    }

    public void createTask (Task task) {
        if (task.getClass().equals(SubTask.class)) {
            subTaskHashMap.put(task.getId(), (SubTask) task);
        } else if (task.getClass().equals(Task.class)) {
            taskHashMap.put(task.getId(), task);
        } else if (task.getClass().equals(Epic.class)) {
            epicHashMap.put(task.getId(), (Epic) task);
        }
    }

    public Task getTaskById (Long id) {
        if (subTaskHashMap.containsKey(id)) {
            return subTaskHashMap.get(id);
        } else if (taskHashMap.containsKey(id)) {
            return taskHashMap.get(id);
        } else if (epicHashMap.containsKey(id)) {
            return epicHashMap.get(id);
        } else return null;
    }

    public void updateTask(Task updatedTask) {
        if (updatedTask.getClass().equals(SubTask.class)) {
            subTaskHashMap.put(updatedTask.getId(), (SubTask) updatedTask);
        } else if (updatedTask.getClass().equals(Task.class)) {
            taskHashMap.put(updatedTask.getId(), updatedTask);
        } else if (updatedTask.getClass().equals(Epic.class)) {
            epicHashMap.put(updatedTask.getId(), (Epic) updatedTask);
        }
    }

    public void deleteTaskById(Long id) {
        if (taskHashMap.containsKey(id)) {
            taskHashMap.remove(id);
        } else if (subTaskHashMap.containsKey(id)) {
            SubTask subTask = subTaskHashMap.get(id);
            for (Epic epic : epicHashMap.values()) {
                ArrayList<SubTask> subTaskList = epic.getSubTaskList();
                if (subTaskList.contains(subTask)) {
                    subTaskList.remove(subTask);
                }
            }
            subTaskHashMap.remove(id);
        } else if (epicHashMap.containsKey(id)) {
            epicHashMap.remove(id);
        }
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> result = new ArrayList<>();
        result.addAll(taskHashMap.values());
        result.addAll(subTaskHashMap.values());
        result.addAll(epicHashMap.values());
        return result;
    }

    public void deleteAllTasks() {
        taskHashMap.clear();
    }

    public void deleteAllSubTasks() {
        subTaskHashMap.clear();
    }

    public void deleteAllEpics() {
        epicHashMap.clear();
    }

    public void epicUpdate(Epic epic) {
        if (epic != null) {
            epic.setTaskStatus(epic.checkTaskStatusInSubTasksList());
        }
    }

    public Long generateId() {
        return nextId++;
    }

    public ArrayList<SubTask> getAllSubTaskByEpic(Epic epic) {
        return epic.getSubTaskList();
    }
}

