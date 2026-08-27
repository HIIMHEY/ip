package tasque;

import java.util.List;
import java.util.Scanner;

import tasque.task.Task;

/**
 * Reads commands from and displays messages in the console.
 */
public class Ui {
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
        String banner = "========================================\n"
                + "                 TASQUE                 \n"
                + "========================================";
        String greet = "\nHello! I'm Tasque.\nWhat can I do for you?";
        System.out.println(banner);
        System.out.println(greet);
    }

    /**
     * Displays an error message returned by command processing.
     *
     * @param errorMessage Error message to print.
     */
    public void showError(String errorMessage) {
        System.out.println("OOPS!!! " + errorMessage);
    }

    /**
     * Displays the message shown when the application exits.
     */
    public void showExit() {
        System.out.print("\nGoodbye! See you again soon.");
    }

    /**
     * Displays all tasks with their one-based list positions.
     *
     * @param tasks Tasks to print.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation after a task has been added.
     *
     * @param task Task that was added.
     * @param numberOfTasks Number of tasks after the addition.
     */
    public void showTaskAdded(Task task, int numberOfTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list");
    }

    /**
     * Displays confirmation after a task has been marked done.
     *
     * @param task Task that was marked done.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Displays confirmation after a task has been marked not done.
     *
     * @param task Task that was marked not done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Displays confirmation after a task has been deleted.
     *
     * @param task Task that was deleted.
     * @param numberOfTasks Number of tasks after the deletion.
     */
    public void showTaskDeleted(Task task, int numberOfTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list");
    }
}
