package kakurosolver;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SolutionCellImplTest {

    @Test
    public void filterPossibilities() {
        final var cell = new SolutionCellImpl();
        final var solutions = new HashSet<Integer>();
        cell.addUpdateEvent(solutions::addAll);

        cell.filterPossibilities(Set.of(4,2,1));
        assertEquals(Set.of(4,2,1), solutions);
        
        assertFalse(cell.isSolved());

        solutions.clear();
        cell.filterPossibilities(Set.of(5,1));
        assertEquals(1, solutions.size());
        assertEquals(1, solutions.stream().findFirst().get().intValue());
        
        assertTrue(cell.isSolved());
    }


    @Test
    public void multipleSolutions() {
        final var cell = new SolutionCellImpl();
        final var event1 = new HashSet<Integer>();
        final var event2 = new HashSet<Integer>();
        cell.addUpdateEvent(event1::addAll);
        cell.addUpdateEvent(event2::addAll);

        cell.filterPossibilities(Set.of(1,2));

        assertEquals(Set.of(1,2), event1);
        assertEquals(Set.of(1,2), event2);

        event1.clear();
        event2.clear();
        cell.filterPossibilities(Set.of(1));

        assertEquals(1, event1.size());
        assertEquals(1, event2.size());
        assertEquals(1, event1.stream().findFirst().get().intValue());
        assertEquals(1, event2.stream().findFirst().get().intValue());
    }
}