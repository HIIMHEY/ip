package tasque.task;

/**
 * Represents a task tracked by Tasque.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

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
                + this.description;
    }

    @Override
    public String toString() {
        return "[" + this.type.getSymbol() + "][" + this.getStatusIcon() + "] "
                + this.getDescription();
    }
}
