package tasque;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import tasque.task.Deadline;
import tasque.task.Event;
import tasque.task.Task;
import tasque.task.Todo;

public class StorageTest {
    @TempDir
    Path tempDirectory;

    @Test
    public void saveAndLoadTasks_descriptionContainsPipe_preservesDescription() throws Exception {
        Path storagePath = this.tempDirectory.resolve("data").resolve("tasque.txt");
        Storage storage = new Storage(storagePath.toString());

        storage.saveTasks(List.of(new Todo("read | book")));
        List<Task> loadedTasks = storage.loadTasks();

        assertEquals(1, loadedTasks.size());
        assertEquals("read | book", loadedTasks.get(0).getDescription());
    }

    @Test
    public void loadTasks_legacyRecords_preservesExistingFormat() throws Exception {
        Path storagePath = this.tempDirectory.resolve("tasque.txt");
        Files.writeString(storagePath, "T | 0 | read book\n"
                + "D | 1 | submit report | 2026-08-28\n"
                + "E | 0 | team meeting | Monday 2pm | Monday 4pm\n");
        Storage storage = new Storage(storagePath.toString());

        List<Task> loadedTasks = storage.loadTasks();

        assertEquals(3, loadedTasks.size());
        assertEquals("[T][ ] read book", loadedTasks.get(0).toString());
        assertEquals("[D][X] submit report (by: Aug 28 2026)", loadedTasks.get(1).toString());
        assertEquals("[E][ ] team meeting (from: Monday 2pm to: Monday 4pm)",
                loadedTasks.get(2).toString());
    }

    @Test
    public void saveTasks_mixedTaskTypes_writesExactEncodedRecords() throws Exception {
        Path storagePath = this.tempDirectory.resolve("tasque.txt");
        Storage storage = new Storage(storagePath.toString());
        Task deadline = new Deadline("return book", "2026-08-26");
        deadline.markAsDone();

        storage.saveTasks(List.of(new Todo("borrow book"), deadline,
                new Event("project meeting", "2026-08-28", "2026-08-29")));

        String expected = String.join(System.lineSeparator(),
                "T2 | 0 | Ym9ycm93IGJvb2s=",
                "D2 | 1 | cmV0dXJuIGJvb2s= | 2026-08-26",
                "E2 | 0 | cHJvamVjdCBtZWV0aW5n | 2026-08-28 | 2026-08-29", "");
        assertEquals(expected, Files.readString(storagePath));
    }
}
