package kakurosolver;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CellSequence {
    private final Collection<SolutionCell> cells;
    private final Set<Integer> possibilities;
    private final Collection<Set<Integer>> allPossibilities;
    private final Consumer<SolutionCell> solutionEvent;
    @Getter
    private boolean isComplete;

    public CellSequence(final Collection<Set<Integer>> allPossibilities, final Consumer<SolutionCell> solutionEvent) {
        this.allPossibilities = new ArrayList<>(allPossibilities);
        this.possibilities = allPossibilities.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
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
        // 1) Get all cell combos,
        // 2) Count matching cell combos
        // 3) If n matches == n numbers in combo then remove match numbers from other cells
        final var cellPossibilities = new HashMap<String, Integer>();
        cells.forEach(c -> {
            final var stringified = c.getPossibilities();
            cellPossibilities.computeIfPresent(stringified, (s, i) -> i + 1);
            cellPossibilities.putIfAbsent(stringified, 1);
        });
        cellPossibilities.entrySet().stream()
                .filter(e -> e.getKey().length() == e.getValue() && e.getKey().length() > 1)
                .forEach(e -> cells.stream()
                        .filter(c -> !c.isSolved() && !c.getPossibilities().equals(e.getKey()))
                        .forEach(c -> {
                            // Remove matched combo from current cell possibilities
                            final var deduced = Arrays.stream(c.getPossibilities()
                                    .split("(?!^)"))
                                    .filter(n -> !e.getKey().contains(n))
                                    .map(Integer::valueOf)
                                    .collect(Collectors.toSet());
                            c.filterPossibilities(deduced);
                        }));


        // Check for invalid sequences
        // Todo: need to recursively solve this
        // Todo: Each combo number needs to be tried in each cell (recursively)
        // Todo: If a number can't fit in any cell then combo has failed

        // Todo: only do this if possibilities have changed
        cells.stream()
                .filter(cell -> !cell.isSolved())
                .forEach(cell -> cell.filterPossibilities(possibilities));

        if (possibilities.size() == 1) {
            isComplete = true;
        }
    }
}
