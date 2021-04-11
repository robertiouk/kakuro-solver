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
    private final int sequenceSize;
    @Getter
    private boolean isComplete;

    public CellSequence(final Collection<Set<Integer>> allPossibilities, final Consumer<SolutionCell> solutionEvent) {
        this.allPossibilities = new ArrayList<>(allPossibilities);
        this.possibilities = allPossibilities.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        this.cells = new ArrayList<>();
        this.solutionEvent = solutionEvent;

        sequenceSize = allPossibilities.stream().findFirst().map(Set::size).orElse(0);
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
        if (cells.size() == sequenceSize) {
            final var toRemove = allPossibilities.stream()
                    .filter(c -> {
                        var b = !testCombination(c);
                        if (b) System.out.println(c);
                        return b;
                    })
                    .collect(Collectors.toList());
            allPossibilities.removeAll(toRemove);
            //allPossibilities.forEach(combo -> cells.forEach(c -> c.filterPossibilities(combo)));
        }

        final var update = allPossibilities.stream().flatMap(Collection::stream).collect(Collectors.toSet());
        possibilities.removeIf(i -> !update.contains(i));

        // Todo: only do this if possibilities have changed
        cells.stream()
                .filter(cell -> !cell.isSolved())
                .forEach(cell -> cell.filterPossibilities(possibilities));

        if (possibilities.size() == 1) {
            isComplete = true;
        }
    }

    private boolean testCombination(final Set<Integer> combo) {
        // 389 x
        // 3 = 0 x
        // 8 = 2 c1, c2
        // 9 = 2 c1, c2
        // 569 x
        // 5 = 1 c1
        // 6 = 2 c1, c2
        // 9 = 2 c1, c2
        // 579 x
        // 5 = 1 c1
        // 7 = 1 c1
        // 9 = 2 c1, c2
        // 479 <-
        // 4 = 2 c1, c3
        // 7 = 1 c1
        // 9 = 2 c1, c2
        return combo.stream()
                .flatMap(i -> cells.stream()
                        .filter(c -> c.isCompatible(i)))
                .collect(Collectors.toSet()).size() == cells.size();
    }
}
