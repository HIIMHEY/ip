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
    private static final int TASK_MARKER_INDEX = 0;
    private static final int COMPLETION_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int DEADLINE_DATE_INDEX = 3;
    private static final int EVENT_START_INDEX = 3;
    private static final int EVENT_END_INDEX = 4;
    // The suffix distinguishes Base64 descriptions from legacy plain-text records.
    private static final String ENCODED_DESCRIPTION_SUFFIX = "2";
    private static final String COMPLETED_MARKER = "1";

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
            writeTasksToFile(temporaryPath, tasks);
            replaceStorageFile(temporaryPath, tasquePath);
        } catch (IOException e) {
            throw new TasqueException("I couldn't save your tasks.");
        } finally {
            cleanupTemporaryFile(temporaryPath);
        }
    }

    private void writeTasksToFile(Path temporaryPath, List<Task> tasks) throws IOException {
        try (BufferedWriter taskWriter = Files.newBufferedWriter(
                temporaryPath, StandardCharsets.UTF_8)) {
            for (Task task : tasks) {
                taskWriter.write(task.toStorageString());
                taskWriter.write(System.lineSeparator());
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

    private void cleanupTemporaryFile(Path temporaryPath) {
        if (temporaryPath == null) {
            return;
        }
        try {
            Files.deleteIfExists(temporaryPath);
        } catch (IOException e) {
            // Cleanup failure must not replace the original save outcome.
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
        String storedTaskMarker = parts[TASK_MARKER_INDEX].trim();
        boolean hasEncodedDescription = storedTaskMarker.endsWith(ENCODED_DESCRIPTION_SUFFIX);
        String taskTypeSymbol = hasEncodedDescription
                ? storedTaskMarker.substring(0, storedTaskMarker.length() - ENCODED_DESCRIPTION_SUFFIX.length())
                : storedTaskMarker;
        String description = hasEncodedDescription
                ? decodeDescription(parts[DESCRIPTION_INDEX].trim())
                : parts[DESCRIPTION_INDEX].trim();
        Task task;

        if (taskTypeSymbol.equals("T")) {
            task = new Todo(description);
        } else if (taskTypeSymbol.equals("D")) {
            task = new Deadline(description, parts[DEADLINE_DATE_INDEX].trim());
        } else if (taskTypeSymbol.equals("E")) {
            task = Event.fromStoredValues(description,
                    parts[EVENT_START_INDEX].trim(), parts[EVENT_END_INDEX].trim());
        } else {
            return null;
        }

        if (parts[COMPLETION_INDEX].trim().equals(COMPLETED_MARKER)) {
            task.markAsDone();
        }
        return task;
    }

    private String decodeDescription(String encodedDescription) {
        byte[] descriptionBytes = Base64.getDecoder().decode(encodedDescription);
        return new String(descriptionBytes, StandardCharsets.UTF_8);
    }
}
