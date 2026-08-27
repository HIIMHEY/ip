package tasque.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that must be completed by a specific date.
 */
public class Deadline extends Task {
    protected LocalDate by;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy");

    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = LocalDate.parse(by);
    }

    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + this.by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + this.by.format(this.formatter) + ")";
    }
}
