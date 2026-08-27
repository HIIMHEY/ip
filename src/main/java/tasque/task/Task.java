package tasque.task;

/**
 * Represents a task with a description, type, and completion state.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    /**
     * Creates an incomplete task with the specified description and type.
     *
     * @param description Description of the task.
     * @param type Type of the task.
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns {@code X} for a completed task and a space otherwise.
     *
     * @return {@code X} if completed, or a space otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the task description.
     *
     * @return Description supplied when this task was created.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns this task in its storage-file representation.
     *
     * @return Storage representation of this task.
     */
    public String toStorageString() {
        return this.type.getSymbol() + " | "
                               + (this.isDone ? "1" : "0") + " | "
                               +  this.description;
    }

    /**
     * Returns the task in the format displayed by the console UI.
     *
     * @return Task display text including its type, status, and description.
     */
    @Override
    public String toString() {
        return "[" + this.type.getSymbol() + "][" + this.getStatusIcon() + "] "
                + this.getDescription();
    }
}
