package tasque;

import java.util.List;
import java.util.Scanner;

import tasque.task.Task;

public class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcome() {
        String banner = "========================================\n"
                + "                 TASQUE                 \n"
                + "========================================";
        String greet = "\nHello! I'm Tasque.\nWhat can I do for you?";
        System.out.println(banner);
        System.out.println(greet);
    }

    public void showError(String errorMessage) {
        System.out.println("OOPS!!! " + errorMessage);
    }

    public void showExit() {
        System.out.print("\nGoodbye! See you again soon.");
    }

    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays the tasks whose descriptions match a Find keyword.
     *
     * @param tasks Matching tasks to display.
     */
    public void showMatchingTasks(List<Task> tasks) {
        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    public void showTaskAdded(Task task, int numberOfTasks) {
        System.out.println("Got it. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list");
    }

    public void showTaskMarked(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
    }

    public void showTaskUnmarked(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println(task);
    }

    public void showTaskDeleted(Task task, int numberOfTasks) {
        System.out.println("Noted. I've removed this task:");
        System.out.println(task);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list");
    }
}
