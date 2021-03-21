package kakurosolver;

import java.util.Optional;
import java.util.Set;

/**
 * A cell that contains a solution in a row or column.
 */
public interface SolutionCell extends Cell {
    /**
     * Modify the list of cell possibilities so that it cannot contain any numbers
     * not in the given collection.
     * <p>
     * If only one possibility left then return it, otherwise an empty optional.
     *
     * @param possibilities the list of given possibilities
     * @return the last remaining possibility if none others exist, otherwise empty
     */
    Optional<Integer> filterPossibilities(Set<Integer> possibilities);
}
