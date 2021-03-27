package kakurosolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class Board {
    private final Collection<CellSequence> cellSequences;
    private final Collection<SolutionCell> solutions;

    public Board() {
        cellSequences = new ArrayList<>();
        solutions = new LinkedList<>();
    }

}
