package tasque;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import tasque.task.Deadline;
import tasque.task.Event;
import tasque.task.Task;
import tasque.task.Todo;

/**
 * Loads and saves Tasque tasks using the application's storage format.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a storage handler backed by the specified file path.
     *
     * @param filePath Path of the task data file.
     */
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
        Path tasquePath = Path.of(this.filePath).toAbsolutePath();
        Path parentDirectory = tasquePath.getParent();
        Path temporaryPath = null;
        try {
            Files.createDirectories(parentDirectory);
            temporaryPath = Files.createTempFile(parentDirectory, ".tasque-", ".tmp");
            try (BufferedWriter taskWriter = Files.newBufferedWriter(
                    temporaryPath, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    taskWriter.write(task.toStorageString());
                    taskWriter.write(System.lineSeparator());
                }
            }
            replaceStorageFile(temporaryPath, tasquePath);
        } catch (IOException e) {
            throw new TasqueException("I couldn't save your tasks.");
        } finally {
            if (temporaryPath != null) {
                try {
                    Files.deleteIfExists(temporaryPath);
                } catch (IOException e) {
                    // The save result is known; leave cleanup to the operating system.
                }
            }
        }
    }

    private void replaceStorageFile(Path temporaryPath, Path tasquePath) throws IOException {
        try {
            Files.move(temporaryPath, tasquePath,
                    StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temporaryPath, tasquePath, StandardCopyOption.REPLACE_EXISTING);
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
        try (Scanner taskReader = new Scanner(tasqueFile, StandardCharsets.UTF_8.name())) {
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
        String[] parts = storageString.split("\\|", -1);
        String taskType = parts[0].trim();
        boolean hasEncodedDescription = taskType.endsWith("2");
        if (hasEncodedDescription) {
            taskType = taskType.substring(0, taskType.length() - 1);
        }
        String description = hasEncodedDescription
                ? decodeDescription(parts[2].trim())
                : parts[2].trim();
        Task task;

        if (taskType.equals("T")) {
            task = new Todo(description);
        } else if (taskType.equals("D")) {
            task = new Deadline(description, parts[3].trim());
        } else if (taskType.equals("E")) {
            task = Event.fromStoredValues(description, parts[3].trim(), parts[4].trim());
        } else {
            return null;
        }

        if (parts[1].trim().equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private String decodeDescription(String encodedDescription) {
        byte[] descriptionBytes = Base64.getDecoder().decode(encodedDescription);
        return new String(descriptionBytes, StandardCharsets.UTF_8);
    }
}
