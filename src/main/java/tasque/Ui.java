package tasque;

import java.util.List;
import java.util.Scanner;

import tasque.task.Task;

/**
 * Handles console input and output for Tasque.
 */
public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads and returns the next user command.
     *
     * @return Next user command.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the welcome message.
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
     * Shows an error message.
     *
     * @param errorMessage Error message to show.
     */
    public void showError(String errorMessage) {
        System.out.println("OOPS!!! " + errorMessage);
    }

    /**
     * Shows the exit message.
     */
    public void showExit() {
        System.out.print("\nGoodbye! See you again soon.");
    }

    /**
     * Shows all tasks in the task list.
     *
     * @param tasks Tasks to show.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Shows the added task and updated task count.
     *
     * @param task Added task.
     * @param numberOfTasks Updated number of tasks.
     */
    public void showTaskAdded(Task task, int numberOfTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list");
    }

    /**
     * Shows the task marked as done.
     *
     * @param task Marked task.
     */
    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    /**
     * Shows the task marked as not done.
     *
     * @param task Unmarked task.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    /**
     * Shows the deleted task and updated task count.
     *
     * @param task Deleted task.
     * @param numberOfTasks Updated number of tasks.
     */
    public void showTaskDeleted(Task task, int numberOfTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list");
    }
}
