package kakurosolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

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

    @Before
    public void setup() {
        cell1 = mock(SolutionCell.class);
        cell2 = mock(SolutionCell.class);

        possibilities = Set.of(1, 2, 3);
        sequence = new CellSequence(possibilities);
    }

    @Test
    public void registerCell() {
        sequence.registerCell(cell1);
        sequence.registerCell(cell2);

        verify(cell1).addSolutionEvent(any());
        verify(cell1).addSolutionEvent(any());

        verify(cell1).filterPossibilities(eq(possibilities));
        verify(cell2).filterPossibilities(eq(possibilities));
    }

    @Test
    public void isComplete() {
        final var cell3 = mock(SolutionCell.class);
        sequence.registerCell(cell1);
        sequence.registerCell(cell2);
        sequence.registerCell(cell3);

        final var captor1 = ArgumentCaptor.forClass(Consumer.class);
        verify(cell1).addSolutionEvent(captor1.capture());
        final var captor2 = ArgumentCaptor.forClass(Consumer.class);
        verify(cell2).addSolutionEvent(captor2.capture());
        final var captor3 = ArgumentCaptor.forClass(Consumer.class);
        verify(cell3).addSolutionEvent(captor3.capture());

        assertFalse(sequence.isComplete());
        verify(cell1, times(1)).filterPossibilities(eq(possibilities));
        verify(cell2, times(1)).filterPossibilities(eq(possibilities));
        verify(cell3, times(1)).filterPossibilities(eq(possibilities));

        final var event1 = captor1.getValue();
        final var event2 = captor2.getValue();

        // Set solution for cell 1
        when(cell1.isSolved()).thenReturn(true);
        event1.accept(1);
        assertFalse(sequence.isComplete());
        verify(cell1, times(1)).filterPossibilities(eq(Set.of(2,3))); // Don't filter solved cell*
        verify(cell2, times(2)).filterPossibilities(eq(Set.of(2,3)));
        verify(cell3, times(2)).filterPossibilities(eq(Set.of(2,3)));
        //* the filter set reference is shared so will have the same size for each verify

        // Set solution for cell 2
        when(cell2.isSolved()).thenReturn(true);
        event2.accept(2);
        assertTrue(sequence.isComplete());
        verify(cell1, times(1)).filterPossibilities(eq(Set.of(3))); // Don't filter solved cell
        verify(cell2, times(2)).filterPossibilities(eq(Set.of(3))); // Don't filter solved cell
        verify(cell3, times(3)).filterPossibilities(eq(Set.of(3)));
    }
}