package kakurosolver.processing;

import kakurosolver.SolutionCell;
import org.junit.Test;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SolutionResolverTest {

    /**
     * 7|,        6|
     * 124,       12
     * ^ must be 4
     */
    @Test
    public void filterInvalidSolutions() {
        //  7|,        6|
        //  124,       12
        //  ^ must be 4
        final var resolver = new SolutionResolverImpl();

        final var cell1 = mock(SolutionCell.class);
        final var cell2 = mock(SolutionCell.class);
        when(cell1.getPossibilities()).thenReturn("124");
        when(cell2.getPossibilities()).thenReturn("12");

        resolver.filterInvalidSolutions(Set.of(cell1, cell2));
    }
}