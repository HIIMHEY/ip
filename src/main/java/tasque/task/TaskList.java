package tasque.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the tasks tracked by Tasque.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the task list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Deletes and returns the task at the specified task number.
     *
     * @param taskNumber One-based number of the task to delete.
     * @return Deleted task.
     */
    public Task delete(int taskNumber) {
        return this.tasks.remove(taskNumber - 1);
    }

    private Task getTask(int taskNumber) {
        return this.tasks.get(taskNumber - 1);
    }

    /**
     * Marks and returns the specified task as done.
     *
     * @param taskNumber One-based number of the task to mark.
     * @return Marked task.
     */
    public Task markAsDone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsDone();
        return task;
    }

    /**
     * Marks and returns the specified task as not done.
     *
     * @param taskNumber One-based number of the task to unmark.
     * @return Unmarked task.
     */
    public Task markAsNotDone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        return task;
    }

    public int getSize() {
        return this.tasks.size();
    }

    public List<Task> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }
}
