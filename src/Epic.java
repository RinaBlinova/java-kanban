import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<SubTask> subTaskList;

    public Epic(String name, String description, Long id, TaskStatus taskStatus) {
        super(name, description, id,  taskStatus);
        this.subTaskList = new ArrayList<>();
    }

    public ArrayList<SubTask> getSubTaskList() {
        return subTaskList;
    }

    public void addSubTask(SubTask subTask) {
        subTaskList.add(subTask);
    }

    public TaskStatus checkTaskStatusInSubTasksList() {
        int newStatusCounter = 0;
        int doneStatusCounter = 0;
        if (subTaskList.isEmpty()) {
            return TaskStatus.NEW;
        }
        for (SubTask subTask : subTaskList) {
            if (TaskStatus.NEW.equals(subTask.getTaskStatus())) {
                newStatusCounter++;
            }
            if (TaskStatus.DONE.equals(subTask.getTaskStatus())) {
                doneStatusCounter++;
            }
        }
        if (newStatusCounter == subTaskList.size()) {
            return TaskStatus.NEW;
        }
        if (doneStatusCounter == subTaskList.size()) {
            return TaskStatus.DONE;
        }
        return TaskStatus.IN_PROGRESS;
    }
}
