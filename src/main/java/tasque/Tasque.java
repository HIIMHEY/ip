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
            try {
                String command = this.parser.parseCommand(userInput);
                switch (command) {
                    case "bye":
                        shouldExit = true;
                        break;
                    case "list":
                        this.ui.showTaskList(this.tasks.getTasks());
                        break;
                    case "mark":
                        markTask(userInput);
                        break;
                    case "unmark":
                        unmarkTask(userInput);
                        break;
                    case "todo":
                        addTask(this.parser.parseTodo(userInput));
                        break;
                    case "deadline":
                        addTask(this.parser.parseDeadline(userInput));
                        break;
                    case "event":
                        addTask(this.parser.parseEvent(userInput));
                        break;
                    case "delete":
                        deleteTask(userInput);
                        break;
                    case "find":
                        findTask(userInput);
                        break;
                    default:
                        throw new TasqueException("I do not recognize that command.");
                }
            } catch (TasqueException e) {
                this.ui.showError(e.getMessage());
            }
        }
        this.ui.showExit();
    }

    private void addTask(Task task) throws TasqueException {
        this.tasks.add(task);
        try {
            saveTasks();
        } catch (TasqueException e) {
            this.tasks.delete(this.tasks.getSize());
            throw e;
        }
        this.ui.showTaskAdded(task, this.tasks.getSize());
    }

    private void deleteTask(String userInput) throws TasqueException {
        int taskNumber = this.parser.parseTaskNumber(
                userInput, "delete", this.tasks.getSize());
        Task deletedTask = this.tasks.delete(taskNumber);
        try {
            saveTasks();
        } catch (TasqueException e) {
            this.tasks.add(taskNumber, deletedTask);
            throw e;
        }
        this.ui.showTaskDeleted(deletedTask, this.tasks.getSize());
    }

    private void markTask(String userInput) throws TasqueException {
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
        this.ui.showTaskMarked(markedTask);
    }

    private void unmarkTask(String userInput) throws TasqueException {
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
        this.ui.showTaskUnmarked(unmarkedTask);
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

    private void findTask(String userInput) throws TasqueException {
        String keyword = this.parser.parseFindKeyword(userInput);
        List<Task> matchingTasks = this.tasks.findTasks(keyword);
        this.ui.showMatchingTasks(matchingTasks);
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
