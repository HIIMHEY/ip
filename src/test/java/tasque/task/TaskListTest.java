package tasque.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void markAsDone_unmarkedTask_returnsMarkedTask() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        Task markedTask = tasks.markAsDone(1);
        assertEquals("X", markedTask.getStatusIcon());
    }

    @Test
    public void markAsDone_secondTask_marksOnlySecondTask() {
        TaskList tasks = new TaskList();
        Task firstTask = new Todo("read book");
        Task secondTask = new Todo("submit report");
        tasks.add(firstTask);
        tasks.add(secondTask);

        Task markedTask = tasks.markAsDone(2);

        assertSame(secondTask, markedTask);
        assertEquals(" ", firstTask.getStatusIcon());
        assertEquals("X", secondTask.getStatusIcon());
    }

    @Test
    public void markAsDone_doneTask_remainsMarked() {
        TaskList tasks = new TaskList();
        Task task = new Todo("read book");
        task.markAsDone();
        tasks.add(task);

        Task markedTask = tasks.markAsDone(1);

        assertSame(task, markedTask);
        assertEquals("X", task.getStatusIcon());
    }
}
