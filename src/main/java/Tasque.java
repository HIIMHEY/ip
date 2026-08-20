import java.util.Scanner;

public class Tasque {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] tasks = new String[100];
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
            if (userInput.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = userInput;
                taskCount++;
                System.out.println("added: " + userInput);
            }
        }
        System.out.print(exit);
    }
}
