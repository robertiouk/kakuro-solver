package kakurosolver;

import org.junit.Test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

public class SolutionCellImplTest {

    @Test
    public void filterPossibilities() {
        final var cell = new SolutionCellImpl();
        final var solution = new AtomicInteger();
        cell.addSolutionEvent(solution::set);

        cell.filterPossibilities(Set.of(4,2,1));
        assertEquals(0, solution.get());

        cell.filterPossibilities(Set.of(5,1));
        assertEquals(1, solution.get());
    }

    @Test
    public void multipleSolutions() {
        final var cell = new SolutionCellImpl();
        final var event1 = new AtomicInteger();
        final var event2 = new AtomicInteger();
        cell.addSolutionEvent(event1::set);
        cell.addSolutionEvent(event2::set);

        cell.filterPossibilities(Set.of(1,2));

        assertEquals(0, event1.get());
        assertEquals(0, event2.get());

        cell.filterPossibilities(Set.of(1));

        assertEquals(1, event1.get());
        assertEquals(1, event2.get());
    }
}