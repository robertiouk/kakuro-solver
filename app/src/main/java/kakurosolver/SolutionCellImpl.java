package kakurosolver;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionCellImpl implements SolutionCell {
    private final Set<Integer> possibilities =
            IntStream.range(1, 10).boxed().collect(Collectors.toSet());
    private Consumer<Integer> solutionEvent;

    @Override
    public void filterPossibilities(final Set<Integer> possibilities) {
        final var toRemove = this.possibilities.stream()
                .filter(n -> !possibilities.contains(n))
                .collect(Collectors.toSet());
        toRemove.forEach(this.possibilities::remove);

        if (this.possibilities.size() == 1) {
            solutionEvent.accept(this.possibilities.stream()
                    .findFirst()
                    .orElseThrow(IllegalStateException::new));
        }
    }

    @Override
    public void setSolutionEvent(final Consumer<Integer> solutionEvent) {
        this.solutionEvent = solutionEvent;
    }
}
