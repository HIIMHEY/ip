package tasque;

import java.time.format.DateTimeParseException;

import tasque.task.Deadline;
import tasque.task.Event;
import tasque.task.Task;
import tasque.task.Todo;

public class Parser {
    public String parseCommand(String userInput) throws TasqueException {
        if (userInput.equals("bye") || userInput.equals("list")) {
            return userInput;
        }

        String[] commandsWithArguments = {
            "mark", "unmark", "todo", "deadline", "event", "delete"
        };
        for (String command : commandsWithArguments) {
            if (userInput.equals(command) || userInput.startsWith(command + " ")) {
                return command;
            }
        }

        throw new TasqueException("I do not recognize that command.");
    }

    public int parseTaskNumber(String userInput, String command, int numberOfTasks)
            throws TasqueException {
        String taskNumberText = userInput.substring(command.length()).trim();
        if (taskNumberText.isEmpty()) {
            throw new TasqueException("The " + command + " command needs a task number.");
        }

        int taskNumber;
        try {
            taskNumber = Integer.parseInt(taskNumberText);
        } catch (NumberFormatException e) {
            throw new TasqueException("The task number must be a positive whole number.");
        }

        if (taskNumber <= 0) {
            throw new TasqueException("The task number must be a positive whole number.");
        }
        if (taskNumber > numberOfTasks) {
            throw new TasqueException("Task " + taskNumber + " does not exist in the list.");
        }
        return taskNumber;
    }

    public Task parseTodo(String userInput) throws TasqueException {
        String description = userInput.substring("todo".length()).trim();
        if (description.isEmpty()) {
            throw new TasqueException("The description of a todo cannot be empty.");
        }
        return new Todo(description);
    }

    public Task parseDeadline(String userInput) throws TasqueException {
        String deadlineDetails = userInput.substring("deadline".length()).trim();
        if (deadlineDetails.isEmpty()) {
            throw new TasqueException("The description of a deadline cannot be empty.");
        }

        int byIndex = deadlineDetails.indexOf("/by");
        if (byIndex == -1) {
            throw new TasqueException("A deadline must include /by followed by when it is due.");
        }

        String description = deadlineDetails.substring(0, byIndex).trim();
        if (description.isEmpty()) {
            throw new TasqueException("The description of a deadline cannot be empty.");
        }

        String by = deadlineDetails.substring(byIndex + "/by".length()).trim();
        if (by.isEmpty()) {
            throw new TasqueException("The /by value of a deadline cannot be empty.");
        }

        try {
            return new Deadline(description, by);
        } catch (DateTimeParseException e) {
            throw new TasqueException("Please enter the date as yyyy-MM-dd.");
        }
    }

    public Task parseEvent(String userInput) throws TasqueException {
        String eventDetails = userInput.substring("event".length()).trim();
        if (eventDetails.isEmpty()) {
            throw new TasqueException("The description of an event cannot be empty.");
        }

        int fromIndex = eventDetails.indexOf("/from");
        if (fromIndex == -1) {
            throw new TasqueException("An event must include /from followed by its start.");
        }

        String description = eventDetails.substring(0, fromIndex).trim();
        if (description.isEmpty()) {
            throw new TasqueException("The description of an event cannot be empty.");
        }

        int toIndex = eventDetails.indexOf("/to");
        if (toIndex == -1) {
            throw new TasqueException("An event must include /to followed by its end.");
        }
        if (toIndex < fromIndex) {
            throw new TasqueException("Use the format: event DESCRIPTION /from START /to END.");
        }

        String from = eventDetails.substring(fromIndex + "/from".length(), toIndex).trim();
        if (from.isEmpty()) {
            throw new TasqueException("The /from value of an event cannot be empty.");
        }

        String to = eventDetails.substring(toIndex + "/to".length()).trim();
        if (to.isEmpty()) {
            throw new TasqueException("The /to value of an event cannot be empty.");
        }

        return new Event(description, from, to);
    }
}
