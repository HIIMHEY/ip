package tasque;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void parseTaskNumber_validTaskNumber_returnsTaskNumber() throws TasqueException {
        Parser parser = new Parser();
        int actual = parser.parseTaskNumber("delete 2", "delete", 5);
        assertEquals(2, actual);
    }

    @Test
    public void parseTaskNumber_nonNumericTaskNumber_throwsTasqueException() {
        Parser parser = new Parser();
        assertThrows(TasqueException.class, () -> parser.parseTaskNumber("delete abc", "delete", 5));
    }

    @Test
    public void parseTaskNumber_negativeTaskNumber_throwsTasqueException() {
        Parser parser = new Parser();
        assertThrows(TasqueException.class, () -> parser.parseTaskNumber("delete -1", "delete", 5));
    }

    @Test
    public void parseTaskNumber_missingTaskNumber_throwsTasqueException() {
        Parser parser = new Parser();
        TasqueException exception = assertThrows(TasqueException.class,
                () -> parser.parseTaskNumber("delete", "delete", 5));
        assertEquals("The delete command needs a task number.", exception.getMessage());
    }

    @Test
    public void parseTaskNumber_zeroTaskNumber_throwsTasqueException() {
        Parser parser = new Parser();
        TasqueException exception = assertThrows(TasqueException.class,
                () -> parser.parseTaskNumber("delete 0", "delete", 5));
        assertEquals("The task number must be a positive whole number.", exception.getMessage());
    }

    @Test
    public void parseTaskNumber_taskNumberPastEnd_throwsTasqueException() {
        Parser parser = new Parser();
        TasqueException exception = assertThrows(TasqueException.class,
                () -> parser.parseTaskNumber("delete 6", "delete", 5));
        assertEquals("Task 6 does not exist in the list.", exception.getMessage());
    }

    @Test
    public void parseTaskNumber_lastTaskNumber_returnsTaskNumber() throws TasqueException {
        Parser parser = new Parser();
        int actual = parser.parseTaskNumber("delete 5", "delete", 5);
        assertEquals(5, actual);
    }

    @Test
    public void parseFindKeyword_keywordProvided_returnsKeyword() throws TasqueException {
        Parser parser = new Parser();
        String actual = parser.parseFindKeyword("find book");
        assertEquals("book", actual);
    }

    @Test
    public void parseFindKeyword_missingKeyword_throwsTasqueException() {
        Parser parser = new Parser();
        TasqueException exception = assertThrows(TasqueException.class,
                () -> parser.parseFindKeyword("find   "));
        assertEquals("The find command needs a keyword.", exception.getMessage());
    }
}
