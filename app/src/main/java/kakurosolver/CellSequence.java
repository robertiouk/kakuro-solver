package kakurosolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class CellSequence {
    private final Collection<SolutionCell> cells;
    private final Set<Integer> possibilities;

    public CellSequence(final Set<Integer> possibilities) {
        this.possibilities = possibilities;
        this.cells = new ArrayList<>();
    }

    public void registerCell(final SolutionCell cell) {
        cells.add(cell);
        cell.setSolutionEvent(this::updatePossibilities);
        cell.filterPossibilities(possibilities);
    }
    
    private void updatePossibilities(final int solution) {
        possibilities.remove(solution);
        cells.forEach(cell -> cell.filterPossibilities(possibilities));
    }
}
