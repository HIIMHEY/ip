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

    public int size() {
        return this.tasks.size();
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }
}
