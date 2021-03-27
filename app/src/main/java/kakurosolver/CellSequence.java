package kakurosolver;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class CellSequence {
    private final Collection<SolutionCell> cells;
    private final Set<Integer> possibilities;
    private final Consumer<SolutionCell> solutionEvent;
    @Getter
    private boolean isComplete;

    public CellSequence(final Set<Integer> possibilities, final Consumer<SolutionCell> solutionEvent) {
        this.possibilities = new HashSet<>(possibilities);
        this.cells = new ArrayList<>();
        this.solutionEvent = solutionEvent;
    }

    public void registerCell(final SolutionCell cell) {
        cells.add(cell);
        cell.addUpdateEvent(solution -> solutionEvent.accept(cell));
        cell.addUpdateEvent(this::updatePossibilities);
        cell.filterPossibilities(possibilities);
    }

    private void updatePossibilities(final Collection<Integer> solutions) {
        if (solutions.size() == 1) {
            // A solution has been found so update remaining possibilities
            solutions.stream()
                    .findFirst()
                    .ifPresent(possibilities::remove);
        }

        // Check for matching groups

        // Check for invalid sequences

        // Todo: only do this if possibilities have changed
        cells.stream()
                .filter(cell -> !cell.isSolved())
                .forEach(cell -> cell.filterPossibilities(possibilities));


        if (possibilities.size() == 1) {
            isComplete = true;
        }
    }
}
