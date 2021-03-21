package kakurosolver;

import lombok.Setter;

import java.util.Optional;


public class HeaderCellImpl implements HeaderCell {
    @Setter
    private int rowTotal;
    @Setter
    private int columnTotal;

    @Setter private int a;

    @Override
    public Optional<Integer> getRowTotal() {
        return rowTotal == 0 ? Optional.empty() : Optional.of(rowTotal);
    }

    @Override
    public Optional<Integer> getColumnTotal() {
        return columnTotal == 0 ? Optional.empty() : Optional.of(columnTotal);
    }
}
