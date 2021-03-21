package kakurosolver;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SolutionCellImplTest {

    @Test
    public void filterPossibilities() {
        final var cell = new SolutionCellImpl();
        assertTrue(cell.filterPossibilities(Set.of(4,2,1)).isEmpty());

        assertEquals(1, cell.filterPossibilities(Set.of(5,1)).get().intValue());
    }
}