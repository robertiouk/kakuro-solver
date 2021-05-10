package kakurosolver.processing;

import kakurosolver.SolutionCell;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SolutionResolverImpl implements SolutionResolver {
    @Override
    public void filterInvalidSolutions(final Set<SolutionCell> cells) {
        cells.forEach(c -> {
            final var combo = Arrays.stream(c.getPossibilities()
                    .split("(?!^)"))
                    .map(Integer::valueOf)
                    .collect(Collectors.toSet());
            System.out.println(combo);
        });
    }
}
