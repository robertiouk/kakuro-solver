package kakurosolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionCellImpl implements SolutionCell {
    private final Set<Integer> possibilities =
            IntStream.range(1, 10).boxed().collect(Collectors.toSet());
    private final Collection<Consumer<Integer>> solutionEvents = new ArrayList<>();

    @Override
    public void filterPossibilities(final Set<Integer> possibilities) {
        final var toRemove = this.possibilities.stream()
                .filter(n -> !possibilities.contains(n))
                .collect(Collectors.toSet());
        toRemove.forEach(this.possibilities::remove);

        if (this.possibilities.size() == 1) {
            solutionEvents.forEach(s -> s.accept(this.possibilities.stream()
                    .findFirst()
                    .orElseThrow(IllegalStateException::new)));
        }
    }

    @Override
    public void addSolutionEvent(final Consumer<Integer> solutionEvent) {
        solutionEvents.add(solutionEvent);
    }

    @Override
    public boolean isSolved() {
        return possibilities.size() == 1;
    }
}
