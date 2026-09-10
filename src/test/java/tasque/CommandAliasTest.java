package tasque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CommandAliasTest {
    @TempDir
    Path tempDirectory;

    @Test
    public void isExitCommand_onlyExactExitCommands_returnsTrue() {
        Tasque tasque = new Tasque(this.tempDirectory.resolve("tasks.txt").toString());
        assertTrue(tasque.isExitCommand("bye"));
        assertTrue(tasque.isExitCommand("b"));
        for (String input : new String[] {"banana", "b extra", "b ", "B", "bye extra", "l"}) {
            assertFalse(tasque.isExitCommand(input), input);
        }
    }

    @Test
    public void getResponse_aliasSequence_matchesCanonicalResponsesAndStorage() throws Exception {
        Path canonicalPath = this.tempDirectory.resolve("canonical.txt");
        Path aliasPath = this.tempDirectory.resolve("aliases.txt");
        Tasque canonical = new Tasque(canonicalPath.toString());
        Tasque aliases = new Tasque(aliasPath.toString());
        String[][] commands = {
            {"todo   read t book", "t   read t book"},
            {"deadline submit report /by 2026-09-20", "d submit report /by 2026-09-20"},
            {"event meeting /from 2026-09-20 /to 2026-09-21", "e meeting /from 2026-09-20 /to 2026-09-21"},
            {"list", "l"}, {"mark 2", "m 2"}, {"unmark 2", "u 2"},
            {"find book", "f book"}, {"delete 1", "del 1"}, {"bye", "b"}
        };
        for (String[] pair : commands) {
            assertEquals(canonical.getResponse(pair[0]), aliases.getResponse(pair[1]), pair[1]);
            assertEquals(canonical.getResponse("list"), aliases.getResponse("list"), pair[1]);
            assertEquals(Files.readString(canonicalPath), Files.readString(aliasPath), pair[1]);
            assertEquals(canonical.getResponse("list"), new Tasque(aliasPath.toString()).getResponse("l"));
        }
    }

    @Test
    public void getResponse_invalidAliasArguments_matchesCanonicalErrors() {
        Tasque tasque = new Tasque(this.tempDirectory.resolve("tasks.txt").toString());
        String[][] commands = {
            {"todo", "t"}, {"deadline report", "d report"},
            {"deadline report /by tomorrow", "d report /by tomorrow"},
            {"event meeting /from 2026-09-20", "e meeting /from 2026-09-20"},
            {"mark", "m"}, {"unmark abc", "u abc"}, {"delete 1", "del 1"},
            {"find", "f"}, {"list extra", "l extra"}, {"bye extra", "b extra"},
            {"list ", "l "}, {"bye ", "b "}
        };
        for (String[] pair : commands) {
            assertEquals(tasque.getResponse(pair[0]), tasque.getResponse(pair[1]), pair[1]);
        }
        assertEquals("Here are the tasks in your list:", tasque.getResponse("list"));
    }

    @Test
    public void getResponse_nearMatches_remainUnknownCommands() {
        Tasque tasque = new Tasque(this.tempDirectory.resolve("tasks.txt").toString());
        String[] inputs = {"tree book", "dreport", "echo", "later", "m1", "update", "del1",
            "foo", "banana", "T book", "B", "t\tbook", " t book"};
        for (String input : inputs) {
            assertEquals("OOPS!!! I do not recognize that command.", tasque.getResponse(input), input);
        }
    }
}
