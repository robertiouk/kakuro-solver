package kakurosolver.processing;

import kakurosolver.SolutionCell;

import java.util.Set;

/**
 * Defines a class that determines potential solutions and filters out invalid ones.
 */
public interface SolutionResolver {
    /**
     * Filter out invalid solutions E.g.,
     *   7|,        6|
     *   124,       12
     *   ^ must be 4
     * @param cells the collection of cells
     */
    void filterInvalidSolutions(Set<SolutionCell> cells);
}
