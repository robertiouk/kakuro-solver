package kakurosolver;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SolutionCellImpl implements SolutionCell {
    private final Set<Integer> possibilities =
            IntStream.range(1, 10).boxed().collect(Collectors.toSet());

    @Override
    public Optional<Integer> filterPossibilities(final Set<Integer> possibilities) {
        final var toRemove = this.possibilities.stream()
                .filter(n -> !possibilities.contains(n))
                .collect(Collectors.toSet());
        toRemove.forEach(this.possibilities::remove);

        return this.possibilities.size() == 1 ? this.possibilities.stream().findFirst()
                : Optional.empty();
    }
}
