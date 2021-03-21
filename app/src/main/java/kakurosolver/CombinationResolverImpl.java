package kakurosolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class CombinationResolverImpl implements CombinationResolver {
    public static final int MAX_NUMBER = 9;
    private final Function<Integer, Function<Integer, Collection<Set<Integer>>>> memoizedFunction;

    public CombinationResolverImpl() {
        memoizedFunction = Memoizer.memoize(x ->
                Memoizer.memoize(y -> preparedFunction(x, y)));
    }

    /**
     * Recursively explore possible combinations from a given starting combination (working backwards from the highest
     * possible number).
     *
     * E.g., 7 in 3:
     * 6 x
     * 5,
     * - 2 x
     * - 1 x
     * 4,
     * - 3 x
     * - 2
     *   - 1 = (4,2,1)
     *
     * @param combo the starting combination
     * @param pivotIndex the current starting number to work down from
     * @param total the target total number
     * @param solutions the list of solutions
     * @param totalCombinations the total number of combinations required
     */
    private void findSolutions(final Set<Integer> combo,
                               int pivotIndex,
                               final int total,
                               final Collection<Set<Integer>> solutions,
                               final int totalCombinations) {
        var startIndex = pivotIndex;

        // Explore all combinations in descending order
        while (startIndex > 0 && comboSum(combo) < total) {
            var index = startIndex;
            final var currentCombo = new HashSet<>(combo);
            // Look for the next possible number to try
            while (comboSum(currentCombo) + index > total && index > 0) {
                index--;
            }
            if (index > 0) {
                currentCombo.add(index);
                startIndex--;

                // Found a possible number, search for sub-combinations
                findSolutions(new HashSet<>(currentCombo), startIndex, total, solutions, totalCombinations);
            }
        }

        // Check if solution is valid and add if so
        if (comboSum(combo) == total && combo.size() == totalCombinations && solutionIsUnique(combo, solutions)) {
            solutions.add(combo);
        }
    }

    private boolean solutionIsUnique(final Set<Integer> combo, final Collection<Set<Integer>> solutions) {
        return solutions.stream().noneMatch(c -> c.equals(combo));
    }

    private int comboSum(final Set<Integer> combo)
    {
        return combo.stream().mapToInt(i -> i).sum();
    }

    private Collection<Set<Integer>> preparedFunction(final int total, final int combinations) {
        final var result = new ArrayList<Set<Integer>>();

        var combo = new HashSet<Integer>();
        var startIndex = Math.min(total-1, MAX_NUMBER);
        findSolutions(combo, startIndex, total, result, combinations);

        return result;
    }

    @Override
    public Collection<Set<Integer>> getCombinations(final int total, final int combinations) {
        return memoizedFunction.apply(total).apply(combinations);
    }
}
