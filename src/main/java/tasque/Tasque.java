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
            if (isExitCommand(userInput)) {
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

    /**
     * Checks whether the input is the exit command or its alias.
     *
     * @param userInput Command entered by the user.
     * @return Whether the interface should exit.
     */
    public boolean isExitCommand(String userInput) {
        return this.parser.normalizeCommand(userInput).equals("bye");
    }

    private String executeCommand(String rawUserInput) throws TasqueException {
        String userInput = this.parser.normalizeCommand(rawUserInput);
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
        assert 1 <= taskNumber && taskNumber <= this.tasks.getSize()
                : "Check that taskNumber is within range of the task list";
        Task markedTask = updateTaskCompletion(taskNumber, true);
        assert markedTask.isDone()
                : "Check if task is actually marked as done";
        return this.ui.getTaskMarkedMessage(markedTask);
    }

    private String unmarkTask(String userInput) throws TasqueException {
        int taskNumber = this.parser.parseTaskNumber(
                userInput, "unmark", this.tasks.getSize());
        Task unmarkedTask = updateTaskCompletion(taskNumber, false);
        return this.ui.getTaskUnmarkedMessage(unmarkedTask);
    }

    private Task updateTaskCompletion(int taskNumber, boolean isDone) throws TasqueException {
        boolean wasDone = this.tasks.getTasks().get(taskNumber - 1).isDone();
        Task updatedTask = isDone
                ? this.tasks.markAsDone(taskNumber)
                : this.tasks.markAsNotDone(taskNumber);
        try {
            saveTasks();
        } catch (TasqueException e) {
            restoreCompletion(updatedTask, wasDone);
            throw e;
        }
        return updatedTask;
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
        assert task.isDone() == wasDone
                : "Rollback must restore the previous completion state";
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
