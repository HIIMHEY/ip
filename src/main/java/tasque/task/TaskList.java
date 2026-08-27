package tasque.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) {
        this.tasks.add(task);
    }

    public Task delete(int taskNumber) {
        return this.tasks.remove(taskNumber - 1);
    }

    private Task getTask(int taskNumber) {
        return this.tasks.get(taskNumber - 1);
    }

    public Task markAsDone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsDone();
        return task;
    }

    public Task markAsNotDone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns tasks whose descriptions contain the specified keyword.
     *
     * @param keyword Keyword to search for in task descriptions.
     * @return Matching tasks in their existing list order.
     */
    public List<Task> findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        for (Task task : this.tasks) {
            if (task.getDescription().contains(keyword)) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }

    public int size() {
        return this.tasks.size();
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }
}
