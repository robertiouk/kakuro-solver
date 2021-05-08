package kakurosolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CellSequenceTest {
    private SolutionCell cell1;
    private SolutionCell cell2;
    private Set<Integer> possibilities;
    private CellSequence sequence;
    private Set<SolutionCell> solvedCells;

    @Before
    public void setup() {
        solvedCells = new HashSet<>();
        cell1 = mock(SolutionCell.class);
        when(cell1.getPossibilities()).thenReturn("123456789");
        cell2 = mock(SolutionCell.class);
        when(cell2.getPossibilities()).thenReturn("123456789");

        possibilities = Set.of(1, 2, 3);
        sequence = new CellSequence(List.of(possibilities), solvedCells::add);
    }

    @Test
    public void testMultipleSequences() {
        final var altPossibilities = Set.of(2,4,6);
        sequence = new CellSequence(List.of(possibilities, altPossibilities), a -> {});

        sequence.registerCell(cell1);

        verify(cell1).filterPossibilities(eq(Set.of(1,2,3,4,6)));
    }

    @Test
    public void registerCell() {
        sequence.registerCell(cell1);
        sequence.registerCell(cell2);

        verify(cell1, times(2)).addUpdateEvent(any());
        verify(cell1, times(2)).addUpdateEvent(any());

        verify(cell1).filterPossibilities(eq(possibilities));
        verify(cell2).filterPossibilities(eq(possibilities));
    }

    @Test
    public void isComplete() {
        final var cell3 = mock(SolutionCell.class);
        when(cell3.getPossibilities()).thenReturn("123456789");
        when(cell1.isCompatible(anyInt())).thenReturn(true);
        when(cell2.isCompatible(anyInt())).thenReturn(true);
        when(cell3.isCompatible(anyInt())).thenReturn(true);
        sequence.registerCell(cell1);
        sequence.registerCell(cell2);
        sequence.registerCell(cell3);

        final var captor1 = ArgumentCaptor.forClass(Consumer.class);
        verify(cell1, times(2)).addUpdateEvent(captor1.capture());
        final var captor2 = ArgumentCaptor.forClass(Consumer.class);
        verify(cell2, times(2)).addUpdateEvent(captor2.capture());
        final var captor3 = ArgumentCaptor.forClass(Consumer.class);
        verify(cell3, times(2)).addUpdateEvent(captor3.capture());

        assertFalse(sequence.isComplete());
        verify(cell1, times(1)).filterPossibilities(eq(possibilities));
        verify(cell2, times(1)).filterPossibilities(eq(possibilities));
        verify(cell3, times(1)).filterPossibilities(eq(possibilities));
        assertTrue(solvedCells.isEmpty());

        final var event1 = captor1.getAllValues();
        final var event2 = captor2.getAllValues();

        // Set solution for cell 1
        when(cell1.isSolved()).thenReturn(true);
        event1.forEach(c -> c.accept(Set.of(1)));
        assertFalse(sequence.isComplete());
        assertEquals(1, solvedCells.size());
        assertTrue(solvedCells.contains(cell1));
        verify(cell1, times(1)).filterPossibilities(eq(Set.of(2,3))); // Don't filter solved cell*
        verify(cell2, times(2)).filterPossibilities(eq(Set.of(2,3)));
        verify(cell3, times(2)).filterPossibilities(eq(Set.of(2,3)));
        //* the filter set reference is shared so will have the same size for each verify

        // Set solution for cell 2
        when(cell2.isSolved()).thenReturn(true);
        event2.forEach(e -> e.accept(Set.of(2)));
        assertEquals(2, solvedCells.size());
        assertTrue(solvedCells.contains(cell2));
        assertTrue(sequence.isComplete());
        verify(cell1, times(1)).filterPossibilities(eq(Set.of(3))); // Don't filter solved cell
        verify(cell2, times(2)).filterPossibilities(eq(Set.of(3))); // Don't filter solved cell
        verify(cell3, times(3)).filterPossibilities(eq(Set.of(3)));
    }

    /**
     * 35 = 56789
     * |5|
     * |6/7/8/9| <- Must be 7/9
     * |6/8|
     * |6/7/8/9| <- Must be 7/9
     * |6/8|
     */
    @Test
    public void testDeduction_matching_pair() {
        final var cell1 = mock(SolutionCell.class);
        when(cell1.getPossibilities()).thenReturn("5");
        when(cell1.isSolved()).thenReturn(true);
        final var cell2 = mock(SolutionCell.class);
        when(cell2.getPossibilities()).thenReturn("6789");
        final var cell3 = mock(SolutionCell.class);
        when(cell3.getPossibilities()).thenReturn("68");
        final var cell4 = mock(SolutionCell.class);
        when(cell4.getPossibilities()).thenReturn("6789");
        final var cell5 = mock(SolutionCell.class);
        when(cell5.getPossibilities()).thenReturn("68");
        sequence.registerCell(cell1);
        sequence.registerCell(cell2);
        sequence.registerCell(cell3);
        sequence.registerCell(cell4);
        sequence.registerCell(cell5);

        setupCell(cell1, Set.of(5));
        setupCell(cell2, Set.of(6,7,8,9));
        setupCell(cell3, Set.of(6,8));
        setupCell(cell4, Set.of(6,7,8,9));
        setupCell(cell5, Set.of(6,8));

        verify(cell2, atLeastOnce()).filterPossibilities(eq(Set.of(7,9)));
        verify(cell4, atLeastOnce()).filterPossibilities(eq(Set.of(7,9)));
        verify(cell1, times(0)).filterPossibilities(eq(Set.of(7,9)));
        verify(cell3, times(0)).filterPossibilities(eq(Set.of(7,9)));
        verify(cell5, times(0)).filterPossibilities(eq(Set.of(7,9)));
    }

    private void setupCell(final SolutionCell cell, final Set<Integer> options) {
        final var captor1 = ArgumentCaptor.forClass(Consumer.class);
        verify(cell, times(2)).addUpdateEvent(captor1.capture());
        final var event1 = captor1.getAllValues();
        event1.forEach(e -> e.accept(options));
    }

    /**
     * 20 = 389/479/569/578
     * |? !3| = 7
     * |6/8/9|
     * |1/2/4|
     * = 479
     */
    @Test
    public void testDeduction_exclude_incompatible_sequences() {
        final var cell1 = mock(SolutionCell.class);
        when(cell1.getPossibilities()).thenReturn("12456789");
        Set.of(1,2,3,4,5,6,7,8,9).forEach(i -> when(cell1.isCompatible(i)).thenReturn(true));
        final var cell2 = mock(SolutionCell.class);
        when(cell2.getPossibilities()).thenReturn("689");
        Set.of(6,8,9).forEach(i -> when(cell2.isCompatible(i)).thenReturn(true));
        final var cell3 = mock(SolutionCell.class);
        when(cell3.getPossibilities()).thenReturn("124");
        Set.of(1,2,4).forEach(i -> when(cell3.isCompatible(i)).thenReturn(true));

        sequence = new CellSequence(List.of(Set.of(3,8,9),
                Set.of(4,7,9),
                Set.of(5,6,9),
                Set.of(5,7,8)), solvedCells::add);

        sequence.registerCell(cell1);
        sequence.registerCell(cell2);
        sequence.registerCell(cell3);

        setupCell(cell1, Set.of(1,2,4,5,6,7,8,9));
        setupCell(cell2, Set.of(6,8,9));
        setupCell(cell3, Set.of(1,2,4));

        verify(cell1, atLeastOnce()).filterPossibilities(eq(Set.of(4,7,9)));
        verify(cell2, atLeastOnce()).filterPossibilities(eq(Set.of(4,7,9)));
        verify(cell3, atLeastOnce()).filterPossibilities(eq(Set.of(4,7,9)));
    }

    @Test
    public void testDeduction_exclude_incompatible_total() {
        //  7|,        6|
        //  124,       12
        //  ^ must be 4

        final var cell1 = mock(SolutionCell.class);
        when(cell1.getPossibilities()).thenReturn("124");
        final var cell2 = mock(SolutionCell.class);
        when(cell2.getPossibilities()).thenReturn("12");

        sequence = new CellSequence(List.of(Set.of(1,5), Set.of(2,4)), c -> {});
        sequence.filterCells();

        verify(cell1).filterPossibilities(Set.of(4));
        verify(cell2).filterPossibilities(Set.of(2));
    }
}