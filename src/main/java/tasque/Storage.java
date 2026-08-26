package tasque;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import tasque.task.Deadline;
import tasque.task.Event;
import tasque.task.Task;
import tasque.task.Todo;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the current tasks to the data file.
     *
     * @param tasks Tasks to save.
     * @throws TasqueException If the tasks cannot be saved.
     */
    public void saveTasks(List<Task> tasks) throws TasqueException {
        File tasqueFile = new File(this.filePath);
        File parentDirectory = tasqueFile.getParentFile();
        if (parentDirectory != null && !parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
        try (FileWriter taskWriter = new FileWriter(tasqueFile)) {
            for (Task task : tasks) {
                taskWriter.write(task.toStorageString());
                taskWriter.write(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new TasqueException("I couldn't save your tasks.");
        }
    }

    /**
     * Loads saved tasks from the data file.
     *
     * @return Saved tasks, or an empty list if no save file exists.
     */
    public List<Task> loadTasks() {
        File tasqueFile = new File(this.filePath);
        ArrayList<Task> tasks = new ArrayList<>();
        try (Scanner taskReader = new Scanner(tasqueFile)) {
            while (taskReader.hasNextLine()) {
                Task task = parseStoredTask(taskReader.nextLine());
                if (task != null) {
                    tasks.add(task);
                }
            }
            return tasks;
        } catch (FileNotFoundException e) {
            return tasks;
        }
    }

    private Task parseStoredTask(String storageString) {
        String[] parts = storageString.split("\\|");
        String taskType = parts[0].trim();
        Task task;

        if (taskType.equals("T")) {
            task = new Todo(parts[2].trim());
        } else if (taskType.equals("D")) {
            task = new Deadline(parts[2].trim(), parts[3].trim());
        } else if (taskType.equals("E")) {
            task = new Event(parts[2].trim(), parts[3].trim(), parts[4].trim());
        } else {
            return null;
        }

        if (parts[1].trim().equals("1")) {
            task.markAsDone();
        }
        return task;
    }
}
