package kakurosolver;

import java.util.Set;
import java.util.function.Consumer;

/**
 * A cell that contains a solution in a row or column.
 */
public interface SolutionCell extends Cell {
    /**
     * Modify the list of cell possibilities so that it cannot contain any numbers
     * not in the given collection.
     * <p>
     * If only one possibility left then execute the solution event (if assigned).
     *
     * @param possibilities the list of given possibilities
     */
    void filterPossibilities(Set<Integer> possibilities);

    /**
     * Set the event to be executed when a solution is found for the cell.
     * @param solutionEvent the event to be executed, which contains the solution
     */
    void setSolutionEvent(Consumer<Integer> solutionEvent);
}
