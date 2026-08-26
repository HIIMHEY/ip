import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeParseException;

public class Tasque {
    /**
     * Saves the current tasks to the data file.
     *
     * @param tasks takes an ArrayList of tasks.
     */
    private static void saveTasks(ArrayList<Task> tasks) {
        File dataDirectory = new File("data");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        File tasqueFile = new File(dataDirectory, "tasque.txt");
        try {
            FileWriter taskWriter = new FileWriter(tasqueFile);
            for (Task task : tasks) {
                taskWriter.write(task.toStorageString());
                taskWriter.write(System.lineSeparator());
            }
            taskWriter.close();
        } catch (IOException e) {
            System.out.println("OOPS!!! I couldn't save your tasks.");
        }
    }

    /**
     * Loads saved tasks from the data file.
     *
     * @return Saved tasks, or an empty list if no save file exists.
     */
    private static ArrayList<Task> loadTasks() {
        File dataDirectory = new File("data");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        File tasqueFile = new File(dataDirectory, "tasque.txt");
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            Scanner taskReader = new Scanner(tasqueFile);
            while (taskReader.hasNextLine()) {
                String storageString = taskReader.nextLine();
                String[] parts = storageString.split("\\|");
                if (parts[0].trim().equals("T")) {
                    if (parts[1].trim().equals("1")) {
                        Todo todo = new Todo(parts[2].trim());
                        todo.markAsDone();
                        tasks.add(todo);
                    } else {
                        tasks.add(new Todo(parts[2].trim()));
                    }
                } else if (parts[0].trim().equals("D")) {
                    if (parts[1].trim().equals("1")) {
                        Deadline deadline = new Deadline(parts[2].trim(), parts[3].trim());
                        deadline.markAsDone();
                        tasks.add(deadline);
                    } else {
                        tasks.add(new Deadline(parts[2].trim(), parts[3].trim()));
                    }
                } else if (parts[0].trim().equals("E")) {
                    if (parts[1].trim().equals("1")) {
                        Event event = new Event(parts[2].trim(), parts[3].trim(), parts[4].trim());
                        event.markAsDone();
                        tasks.add(event);
                    } else {
                        tasks.add(new Event(parts[2].trim(), parts[3].trim(), parts[4].trim()));
                    }
                }
            }
            taskReader.close();
            return tasks;
        } catch (FileNotFoundException e) {
            return tasks;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = loadTasks();
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
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i).toString());
                    }
                } else if (userInput.equals("mark") || userInput.startsWith("mark ")) {
                    int taskNumber = getTaskNumber(userInput, "mark", tasks.size());
                    tasks.get(taskNumber - 1).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(tasks.get(taskNumber - 1).toString());
                    saveTasks(tasks);
                } else if (userInput.equals("unmark") || userInput.startsWith("unmark ")) {
                    int taskNumber = getTaskNumber(userInput, "unmark", tasks.size());
                    tasks.get(taskNumber - 1).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(tasks.get(taskNumber - 1).toString());
                    saveTasks(tasks);
                } else if (userInput.equals("todo") || userInput.startsWith("todo ")) {
                    String description = userInput.substring("todo".length()).trim();
                    if (description.isEmpty()) {
                        throw new TasqueException("The description of a todo cannot be empty.");
                    }
                    System.out.println("Got it. I've added this task:");
                    Task task = new Todo(description);
                    tasks.add(task);
                    System.out.println(task.toString());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list");
                    saveTasks(tasks);
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
                    try {
                        Task task = new Deadline(description, by);
                        tasks.add(task);
                        System.out.println("Got it. I've added this task:");
                        System.out.println(task.toString());
                        System.out.println("Now you have " + tasks.size() + " tasks in the list");
                        saveTasks(tasks);
                    } catch (DateTimeParseException e) {
                        throw new TasqueException("Please enter the date as yyyy-MM-dd.");
                    }
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
                    Task task = new Event(description, from, to);
                    tasks.add(task);
                    System.out.println(task.toString());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list");
                    saveTasks(tasks);
                } else if (userInput.equals("delete") || userInput.startsWith("delete ")) {
                    int taskNumber = getTaskNumber(userInput, "delete", tasks.size());
                    int taskIndex = taskNumber - 1;
                    Task deletedTask = tasks.remove(taskIndex);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println(deletedTask.toString());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list");
                    saveTasks(tasks);
                } else {
                    throw new TasqueException("I do not recognize that command.");
                }
            } catch (TasqueException e) {
                System.out.println("OOPS!!! " + e.getMessage());
            }
        }
        System.out.print(exit);
    }

    private static int getTaskNumber(String userInput, String command, int numberOfTasks)
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
        if (taskNumber > numberOfTasks) {
            throw new TasqueException("Task " + taskNumber + " does not exist in the list.");
        }
        return taskNumber;
    }
}
