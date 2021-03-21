package kakurosolver;

import java.util.Optional;

/**
 * Cell that determines column or row totals.
 */
public interface HeaderCell extends Cell {
    Optional<Integer> getRowTotal();
    Optional<Integer> getColumnTotal();
}
