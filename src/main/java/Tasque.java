import java.util.Scanner;

public class Tasque {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
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
            System.out.println(userInput);
        }
        System.out.print(exit);
    }
}
