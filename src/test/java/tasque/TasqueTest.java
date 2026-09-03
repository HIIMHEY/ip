package tasque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TasqueTest {
    @TempDir
    Path tempDirectory;

    @Test
    public void getResponse_supportedCommands_returnsExpectedResponses() {
        Path storagePath = this.tempDirectory.resolve("data").resolve("tasque.txt");
        Tasque tasque = new Tasque(storagePath.toString());

        assertEquals("Hello! I'm Tasque.\nWhat can I do for you?", tasque.getWelcomeMessage());
        assertEquals("Got it. I've added this task:\n"
                + "[T][ ] read book\n"
                + "Now you have 1 tasks in the list", tasque.getResponse("todo read book"));
        assertEquals("Got it. I've added this task:\n"
                + "[D][ ] submit report (by: Aug 26 2026)\n"
                + "Now you have 2 tasks in the list",
                tasque.getResponse("deadline submit report /by 2026-08-26"));
        assertEquals("Got it. I've added this task:\n"
                + "[E][ ] project meeting (from: Aug 28 2026 to: Aug 29 2026)\n"
                + "Now you have 3 tasks in the list",
                tasque.getResponse("event project meeting /from 2026-08-28 /to 2026-08-29"));
        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] read book\n"
                + "2.[D][ ] submit report (by: Aug 26 2026)\n"
                + "3.[E][ ] project meeting (from: Aug 28 2026 to: Aug 29 2026)",
                tasque.getResponse("list"));
        assertEquals("Nice! I've marked this task as done:\n[T][X] read book",
                tasque.getResponse("mark 1"));
        assertEquals("OK, I've marked this task as not done yet:\n[T][ ] read book",
                tasque.getResponse("unmark 1"));
        assertEquals("Here are the matching tasks in your list:\n"
                + "1.[E][ ] project meeting (from: Aug 28 2026 to: Aug 29 2026)",
                tasque.getResponse("find meeting"));
        assertEquals("Noted. I've removed this task:\n"
                + "[D][ ] submit report (by: Aug 26 2026)\n"
                + "Now you have 2 tasks in the list", tasque.getResponse("delete 2"));
        assertEquals("Goodbye! See you again soon.", tasque.getResponse("bye"));
    }

    @Test
    public void getResponse_invalidInput_returnsErrorAndKeepsSessionUsable() {
        Path storagePath = this.tempDirectory.resolve("data").resolve("tasque.txt");
        Tasque tasque = new Tasque(storagePath.toString());

        assertEquals("OOPS!!! I do not recognize that command.", tasque.getResponse("unknown"));
        assertEquals("OOPS!!! Please enter the date as yyyy-MM-dd.",
                tasque.getResponse("deadline submit report /by tomorrow"));
        assertEquals("Got it. I've added this task:\n"
                + "[T][ ] session continues\n"
                + "Now you have 1 tasks in the list", tasque.getResponse("todo session continues"));
    }

    @Test
    public void getResponse_mutatingCommands_persistForNewSession() {
        Path storagePath = this.tempDirectory.resolve("data").resolve("tasque.txt");
        Tasque firstSession = new Tasque(storagePath.toString());
        firstSession.getResponse("todo persistent task");
        firstSession.getResponse("mark 1");

        Tasque secondSession = new Tasque(storagePath.toString());

        assertEquals("Here are the tasks in your list:\n1.[T][X] persistent task",
                secondSession.getResponse("list"));
    }

    @Test
    public void run_saveFails_doesNotReportSuccessOrKeepMutations() throws Exception {
        Path dataDirectory = Files.createDirectory(this.tempDirectory.resolve("data"));
        Path storagePath = dataDirectory.resolve("tasque.txt");
        Files.writeString(storagePath, "T | 0 | first task\nT | 1 | second task\n");

        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();
        String input = "mark 1\nunmark 2\ndelete 1\ntodo third task\nlist\nbye\n";
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(outputBytes, true, StandardCharsets.UTF_8));
            Tasque tasque = new Tasque(storagePath.toString());

            Files.delete(storagePath);
            Files.delete(dataDirectory);
            Files.writeString(dataDirectory, "blocks the storage directory");

            tasque.run();
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        String output = outputBytes.toString(StandardCharsets.UTF_8);
        assertEquals(4, countOccurrences(output, "OOPS!!! I couldn't save your tasks."));
        assertFalse(output.contains("Got it. I've added this task:"));
        assertFalse(output.contains("Nice! I've marked this task as done:"));
        assertFalse(output.contains("OK, I've marked this task as not done yet:"));
        assertFalse(output.contains("Noted. I've removed this task:"));
        assertTrue(output.contains("1.[T][ ] first task"));
        assertTrue(output.contains("2.[T][X] second task"));
        assertFalse(output.contains("third task"));
    }

    private int countOccurrences(String text, String target) {
        return text.split(java.util.regex.Pattern.quote(target), -1).length - 1;
    }
}
