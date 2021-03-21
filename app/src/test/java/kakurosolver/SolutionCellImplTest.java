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
        cell.setSolutionEvent(solution::set);

        cell.filterPossibilities(Set.of(4,2,1));
        assertEquals(0, solution.get());

        cell.filterPossibilities(Set.of(5,1));
        assertEquals(1, solution.get());
    }
}