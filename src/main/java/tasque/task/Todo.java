package tasque.task;

/**
 * Represents a task without a date or time.
 */
public class Todo extends Task {
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
