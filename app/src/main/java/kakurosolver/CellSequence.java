package kakurosolver;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CellSequence {
    private final Collection<SolutionCell> cells;
    private final Set<Integer> possibilities;
    @Getter
    private boolean isComplete;

    public CellSequence(final Set<Integer> possibilities) {
        this.possibilities = new HashSet<>(possibilities);
        this.cells = new ArrayList<>();
    }

    public void registerCell(final SolutionCell cell) {
        cells.add(cell);
        cell.addSolutionEvent(this::updatePossibilities);
        cell.filterPossibilities(possibilities);
    }

    private void updatePossibilities(final int solution) {
        possibilities.remove(solution);

        cells.stream()
                .filter(cell -> !cell.isSolved())
                .forEach(cell -> {
                    cell.filterPossibilities(possibilities);
                });

        if (possibilities.size() == 1) {
            isComplete = true;
        }
    }
}
