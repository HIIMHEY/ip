package tasque.task;

/**
 * A task that occurs between a specified start and end time.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Creates an event with the supplied description and time range.
     *
     * @param description Description of the task.
     * @param from Start of the event.
     * @param to End of the event.
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event in the task storage format with its time range.
     *
     * @return Storage record including the event's start and end.
     */
    @Override
    public String toStorageString() {
        return super.toStorageString() + " | " + this.from + " | " + this.to;
    }

    /**
     * Returns this event in the user-facing display format.
     *
     * @return Task display text including the event's start and end.
     */
    @Override
    public String toString() {
        return super.toString() + " (from: " + this.from + " to: " + this.to + ")";
    }
}
