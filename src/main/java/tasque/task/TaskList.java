package tasque.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages the ordered collection of tasks in the current session.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list initialized with a copy of the supplied tasks.
     *
     * @param tasks Tasks with which to initialize the list.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Appends a task to the end of the list.
     *
     * @param task Task to append.
     */
    public void add(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes and returns the task at a one-based list position.
     *
     * @param taskNumber One-based position of the task to remove.
     * @return Removed task.
     * @throws IndexOutOfBoundsException If {@code taskNumber} is outside the list.
     */
    public Task delete(int taskNumber) {
        return this.tasks.remove(taskNumber - 1);
    }

    private Task getTask(int taskNumber) {
        return this.tasks.get(taskNumber - 1);
    }

    /**
     * Marks and returns the task at a one-based list position as done.
     *
     * @param taskNumber One-based position of the task to mark.
     * @return Task marked as done.
     * @throws IndexOutOfBoundsException If {@code taskNumber} is outside the list.
     */
    public Task markAsDone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsDone();
        return task;
    }

    /**
     * Marks and returns the task at a one-based list position as not done.
     *
     * @param taskNumber One-based position of the task to unmark.
     * @return Task marked as not done.
     * @throws IndexOutOfBoundsException If {@code taskNumber} is outside the list.
     */
    public Task markAsNotDone(int taskNumber) {
        Task task = getTask(taskNumber);
        task.markAsNotDone();
        return task;
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Current number of tasks.
     */
    public int size() {
        return this.tasks.size();
    }

    /**
     * Returns an unmodifiable view of the tasks in list order.
     *
     * @return Unmodifiable view backed by the current task list.
     */
    public List<Task> getTasks() {
        return Collections.unmodifiableList(this.tasks);
    }
}
