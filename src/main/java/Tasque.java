import java.util.Scanner;

public class Tasque {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;
        String banner = "========================================\n"
                + "                 TASQUE                 \n"
                + "========================================";
        String greet = "\nHello! I'm Tasque.\nWhat can I do for you?";
        String exit = "\nGoodbye! See you again soon.";
        System.out.println(banner);
        System.out.println(greet);
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput.equals("bye")) {
                break;
            }
            try {
                if (userInput.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i].toString());
                    }
                } else if (userInput.equals("mark") || userInput.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(userInput, "mark", taskCount);
                    tasks[taskNumber - 1].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(tasks[taskNumber - 1].toString());
                } else if (userInput.equals("unmark") || userInput.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(userInput, "unmark", taskCount);
                    tasks[taskNumber - 1].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(tasks[taskNumber - 1].toString());
                } else if (userInput.equals("todo") || userInput.startsWith("todo ")) {
                    String description = userInput.substring("todo".length()).trim();
                    if (description.isEmpty()) {
                        throw new TasqueException("The description of a todo cannot be empty.");
                    }
                    System.out.println("Got it. I've added this task:");
                    tasks[taskCount] = new Todo(description);
                    System.out.println(tasks[taskCount].toString());
                    taskCount++;
                    System.out.println("Now you have " + taskCount + " tasks in the list");
                } else if (userInput.equals("deadline") || userInput.startsWith("deadline ")) {
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
                    System.out.println("Got it. I've added this task:");
                    tasks[taskCount] = new Deadline(description, by);
                    System.out.println(tasks[taskCount].toString());
                    taskCount++;
                    System.out.println("Now you have " + taskCount + " tasks in the list");
                } else if (userInput.equals("event") || userInput.startsWith("event ")) {
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
                    System.out.println("Got it. I've added this task:");
                    tasks[taskCount] = new Event(description, from, to);
                    System.out.println(tasks[taskCount].toString());
                    taskCount++;
                    System.out.println("Now you have " + taskCount + " tasks in the list");
                } else {
                    throw new TasqueException("I do not recognize that command.");
                }
            } catch (TasqueException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }
        }
        System.out.print(exit);
    }

    private static int getTaskNumber(String userInput, String command, int taskCount)
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
        if (taskNumber > taskCount) {
            throw new TasqueException("Task " + taskNumber + " does not exist in the list.");
        }
        return taskNumber;
    }
}
