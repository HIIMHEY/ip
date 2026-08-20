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
            } else if (userInput.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i].toString());
                }
            } else if (userInput.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(userInput.substring(5));
                tasks[taskNumber - 1].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println(tasks[taskNumber - 1].toString());
            } else if (userInput.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(userInput.substring(7));
                tasks[taskNumber - 1].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println(tasks[taskNumber - 1].toString());
            } else if (userInput.startsWith("todo ")) {
                System.out.println("Got it. I've added this task:");
                tasks[taskCount] = new Todo(userInput.substring(5));
                System.out.println(tasks[taskCount].toString());
                taskCount++;
                System.out.println("Now you have " + taskCount + " tasks in the list");
            } else if (userInput.startsWith("deadline ")) {
                int byIndex = userInput.indexOf(" /by ");
                String description = userInput.substring("deadline ".length(), byIndex);
                String by = userInput.substring(byIndex + " /by ".length());
                System.out.println("Got it. I've added this task:");
                tasks[taskCount] = new Deadline(description, by);
                System.out.println(tasks[taskCount].toString());
                taskCount++;
                System.out.println("Now you have " + taskCount + " tasks in the list");
            } else if (userInput.startsWith("event ")) {
                int fromIndex = userInput.indexOf(" /from ");
                int toIndex = userInput.indexOf(" /to ");
                String description = userInput.substring("event ".length(), fromIndex);
                String from = userInput.substring(fromIndex + " /from ".length(), toIndex);
                String to = userInput.substring(toIndex + " /to ".length());
                System.out.println("Got it. I've added this task:");
                tasks[taskCount] = new Event(description, from, to);
                System.out.println(tasks[taskCount].toString());
                taskCount++;
                System.out.println("Now you have " + taskCount + " tasks in the list");
            } else {
                tasks[taskCount] = new Task(userInput);
                System.out.println("added: " + tasks[taskCount].getDescription());
                taskCount++;
            }
        }
        System.out.print(exit);
    }
}
