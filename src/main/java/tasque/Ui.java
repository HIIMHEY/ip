package tasque;

import java.util.List;
import java.util.Scanner;

import tasque.task.Task;

/**
 * Reads console commands, formats responses, and displays console messages.
 */
public class Ui {
    private static final String BANNER = "========================================\n"
            + "                 TASQUE                 \n"
            + "========================================";
    private static final String WELCOME_MESSAGE = "Hello! I'm Tasque.\nWhat can I do for you?";
    private static final String EXIT_MESSAGE = "Goodbye! See you again soon.";

    private final Scanner scanner;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command from standard input.
     *
     * @return Next full line read from standard input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the welcome message and application banner.
     */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println();
        System.out.println(WELCOME_MESSAGE);
    }

    /**
     * Returns the greeting shown when Tasque starts.
     *
     * @return Tasque greeting.
     */
    public String getWelcomeMessage() {
        return WELCOME_MESSAGE;
    }

    /**
     * Displays a response produced by Tasque.
     *
     * @param response Response to print.
     */
    public void showResponse(String response) {
        System.out.println(response);
    }

    /**
     * Returns a formatted error response.
     *
     * @param errorMessage Message describing the error.
     * @return Formatted error response.
     */
    public String getErrorMessage(String errorMessage) {
        return "OOPS!!! " + errorMessage;
    }

    /**
     * Displays the message shown when the application exits.
     */
    public void showExit() {
        System.out.print("\n" + EXIT_MESSAGE);
    }

    /**
     * Returns the message shown when the application exits.
     *
     * @return Exit message.
     */
    public String getExitMessage() {
        return EXIT_MESSAGE;
    }

    /**
     * Returns all tasks with their one-based list positions.
     *
     * @param tasks Tasks to include.
     * @return Formatted task-list response.
     */
    public String getTaskListMessage(List<Task> tasks) {
        return getNumberedTaskList("Here are the tasks in your list:", tasks);
    }

    /**
     * Returns the tasks whose descriptions match a Find keyword.
     *
     * @param tasks Matching tasks to include.
     * @return Formatted matching-task response.
     */
    public String getMatchingTasksMessage(List<Task> tasks) {
        return getNumberedTaskList("Here are the matching tasks in your list:", tasks);
    }

    /**
     * Returns confirmation after a task has been added.
     *
     * @param task Task that was added.
     * @param numberOfTasks Number of tasks after the addition.
     * @return Formatted task-added response.
     */
    public String getTaskAddedMessage(Task task, int numberOfTasks) {
        return "Got it. I've added this task:\n"
                + task + "\n"
                + "Now you have " + numberOfTasks + " tasks in the list";
    }

    /**
     * Returns confirmation after a task has been marked done.
     *
     * @param task Task that was marked done.
     * @return Formatted task-marked response.
     */
    public String getTaskMarkedMessage(Task task) {
        return "Nice! I've marked this task as done:\n" + task;
    }

    /**
     * Returns confirmation after a task has been marked not done.
     *
     * @param task Task that was marked not done.
     * @return Formatted task-unmarked response.
     */
    public String getTaskUnmarkedMessage(Task task) {
        return "OK, I've marked this task as not done yet:\n" + task;
    }

    /**
     * Returns confirmation after a task has been deleted.
     *
     * @param task Task that was deleted.
     * @param numberOfTasks Number of tasks after the deletion.
     * @return Formatted task-deleted response.
     */
    public String getTaskDeletedMessage(Task task, int numberOfTasks) {
        return "Noted. I've removed this task:\n"
                + task + "\n"
                + "Now you have " + numberOfTasks + " tasks in the list";
    }

    private String getNumberedTaskList(String heading, List<Task> tasks) {
        StringBuilder message = new StringBuilder(heading);
        for (int i = 0; i < tasks.size(); i++) {
            message.append("\n").append(i + 1).append(".").append(tasks.get(i));
        }
        return message.toString();
    }
}
