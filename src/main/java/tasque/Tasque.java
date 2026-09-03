package tasque;

import java.util.List;

import tasque.task.Task;
import tasque.task.TaskList;

/**
 * Coordinates the user interface, command parsing, task list, and storage.
 */
public class Tasque {
    private final Parser parser;
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Creates a Tasque session using the specified task data file.
     *
     * @param filePath Path of the task data file.
     */
    public Tasque(String filePath) {
        this.parser = new Parser();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(this.storage.loadTasks());
        this.ui = new Ui();
    }

    /**
     * Runs the command loop until the user enters {@code bye}.
     */
    public void run() {
        this.ui.showWelcome();
        boolean shouldExit = false;
        while (!shouldExit) {
            String userInput = this.ui.readCommand();
            String response = getResponse(userInput);
            if (userInput.equals("bye")) {
                shouldExit = true;
            } else {
                this.ui.showResponse(response);
            }
        }
        this.ui.showExit();
    }

    /**
     * Returns the greeting displayed when a GUI session starts.
     *
     * @return Tasque greeting.
     */
    public String getWelcomeMessage() {
        return this.ui.getWelcomeMessage();
    }

    /**
     * Processes one user command and returns its user-facing response.
     *
     * @param userInput Command entered by the user.
     * @return Response to display to the user.
     */
    public String getResponse(String userInput) {
        try {
            return executeCommand(userInput);
        } catch (TasqueException e) {
            return this.ui.getErrorMessage(e.getMessage());
        }
    }

    private String executeCommand(String userInput) throws TasqueException {
        String command = this.parser.parseCommand(userInput);
        switch (command) {
            case "bye":
                return this.ui.getExitMessage();
            case "list":
                return this.ui.getTaskListMessage(this.tasks.getTasks());
            case "mark":
                return markTask(userInput);
            case "unmark":
                return unmarkTask(userInput);
            case "todo":
                return addTask(this.parser.parseTodo(userInput));
            case "deadline":
                return addTask(this.parser.parseDeadline(userInput));
            case "event":
                return addTask(this.parser.parseEvent(userInput));
            case "delete":
                return deleteTask(userInput);
            case "find":
                return findTask(userInput);
            default:
                throw new TasqueException("I do not recognize that command.");
        }
    }

    private String addTask(Task task) throws TasqueException {
        this.tasks.add(task);
        try {
            saveTasks();
        } catch (TasqueException e) {
            this.tasks.delete(this.tasks.getSize());
            throw e;
        }
        return this.ui.getTaskAddedMessage(task, this.tasks.getSize());
    }

    private String deleteTask(String userInput) throws TasqueException {
        int taskNumber = this.parser.parseTaskNumber(
                userInput, "delete", this.tasks.getSize());
        Task deletedTask = this.tasks.delete(taskNumber);
        try {
            saveTasks();
        } catch (TasqueException e) {
            this.tasks.add(taskNumber, deletedTask);
            throw e;
        }
        return this.ui.getTaskDeletedMessage(deletedTask, this.tasks.getSize());
    }

    private String markTask(String userInput) throws TasqueException {
        int taskNumber = this.parser.parseTaskNumber(
                userInput, "mark", this.tasks.getSize());
        boolean wasDone = this.tasks.getTasks().get(taskNumber - 1).isDone();
        Task markedTask = this.tasks.markAsDone(taskNumber);
        try {
            saveTasks();
        } catch (TasqueException e) {
            restoreCompletion(markedTask, wasDone);
            throw e;
        }
        return this.ui.getTaskMarkedMessage(markedTask);
    }

    private String unmarkTask(String userInput) throws TasqueException {
        int taskNumber = this.parser.parseTaskNumber(
                userInput, "unmark", this.tasks.getSize());
        boolean wasDone = this.tasks.getTasks().get(taskNumber - 1).isDone();
        Task unmarkedTask = this.tasks.markAsNotDone(taskNumber);
        try {
            saveTasks();
        } catch (TasqueException e) {
            restoreCompletion(unmarkedTask, wasDone);
            throw e;
        }
        return this.ui.getTaskUnmarkedMessage(unmarkedTask);
    }

    private void saveTasks() throws TasqueException {
        this.storage.saveTasks(this.tasks.getTasks());
    }

    private void restoreCompletion(Task task, boolean wasDone) {
        if (wasDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
    }

    private String findTask(String userInput) throws TasqueException {
        String keyword = this.parser.parseFindKeyword(userInput);
        List<Task> matchingTasks = this.tasks.findTasks(keyword);
        return this.ui.getMatchingTasksMessage(matchingTasks);
    }

    /**
     * Starts Tasque with its default relative data-file path.
     *
     * @param args Command-line arguments, which are not used.
     */
    public static void main(String[] args) {
        new Tasque("data/tasque.txt").run();
    }
}
