package kakurosolver;

import java.util.Collection;
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
     * Set the event to be executed when the potential solution is updated for the cell.
     * @param updateEvent the event to be executed, which contains the remaining possible solutions
     */
    void addUpdateEvent(Consumer<Collection<Integer>> updateEvent);

    /**
     * Determine whether a cell has been solved or not.
     * @return True if solved, False if not
     */
    boolean isSolved();

    /**
     * Get the remaining possibilities for the cell.
     * @return the remaining possibilities as a String.
     */
    String getPossibilities();

    /**
     * Get whether a given number is compatible within the potential cell solution.
     * @param number the number to test
     * @return True if compatible, False if not
     */
    boolean isCompatible(int number);
}
