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
