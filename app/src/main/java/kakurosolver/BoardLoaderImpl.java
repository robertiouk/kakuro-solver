package kakurosolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class BoardLoaderImpl implements BoardLoader {
    public static final String HEADER_PATTERN = "(\\d*)\\|(\\d*)";
    private final CombinationResolver resolver;
    private final Function<Integer, Function<Integer, SolutionCell>> memoizedFunction;
    private final Collection<CellSequence> sequences;
    private String[][] board;

    public BoardLoaderImpl() {
        sequences = new ArrayList<>();
        resolver = new CombinationResolverImpl();
        memoizedFunction = Memoizer.memoize(x ->
                Memoizer.memoize(y -> new SolutionCellImpl()));

        buildTemplateBoard();
    }

    private void buildTemplateBoard() {
        final var rows = 11;
        final var columns = 11;

        board = new String[][]{
                {"|", "23|", "26|", "|", "7|", "6|", "|", "22|", "3|", "18|", "|"},
                {"|16", "", "", "20|6", "", "", "|6", "", "", "", "11|"},
                {"|26", "", "", "", "", "", "11|12", "", "", "", ""},
                {"|34", "", "", "", "", "", "", "", "|10", "", "", ""},
                {"|", "|3", "", "", "39|", "15|4", "", "", "|10", "", ""},
                {"|", "30|", "34|17", "", "", "", "", "", "19|4", "", ""},
                {"|12", "", "", "|35", "", "", "", "", "", "14|", "|"},
                {"|13", "", "", "|6", "", "", "23|", "20|16", "", "", "24|"},
                {"|17", "", "", "17|36", "", "", "", "", "", "", ""},
                {"|28", "", "", "", "", "|22", "", "", "", "", ""},
                {"|", "|23", "", "", "", "|16", "", "", "|12", "", ""}
        };

        final var pattern = Pattern.compile(HEADER_PATTERN);
        for (int r = 0; r < rows; r++) {
            final var row = board[r];
            for (int c = 0; c < columns; c++) {
                final var cell = row[c];
                final var matcher = pattern.matcher(cell);
                if (matcher.find()) {
                    final var colTotal = matcher.group(1);
                    final var rowTotal = matcher.group(2);
                    final var final_r = r;
                    final var final_c = c;
                    final Supplier<SolutionCell> supplier = () -> {
                        System.out.println(final_r + " " + final_c);
                        return memoizedFunction.apply(final_r).apply(final_c);
                    };
                    if (!colTotal.isEmpty()) {
                        final var sequence =
                                buildSequence(r, c,true, board.length, Integer.parseInt(colTotal), supplier);
                        sequences.add(sequence);
                    }
                    if (!rowTotal.isEmpty()) {
                        final var sequence =
                                buildSequence(c, r,false, board[0].length, Integer.parseInt(rowTotal), supplier);
                        sequences.add(sequence);
                    }
                }
            }
        }
    }

    private CellSequence buildSequence(final int startIndex,
                                       final int secondIndex,
                                       final boolean vertical,
                                       final int maxLength,
                                       final int targetTotal,
                                       final Supplier<SolutionCell> cellSupplier) {
        var finished = false;
        var currentIndex = startIndex;

        while (!finished) {
            currentIndex++;
            var currentCell = vertical ? board[currentIndex][secondIndex] : board[secondIndex][currentIndex];
            finished = !currentCell.isEmpty() || currentIndex+1 == maxLength;
        }

        var sequenceTotal = currentIndex - startIndex;
        // Unless the sequence hit the end of the board, subtract 1
        if (currentIndex+1 < maxLength) {
            sequenceTotal--;
        }
        final var possibilities = resolver.getCombinations(
                targetTotal, sequenceTotal);
        final var sequence = new CellSequence(possibilities, e -> {});
        IntStream.range(0, sequenceTotal)
                .mapToObj(i -> {
                    final var row = vertical ? startIndex + i + 1 : secondIndex;
                    final var col = vertical ? secondIndex : i + startIndex + 1;
                    return memoizedFunction.apply(row).apply(col);
                })
                .forEach(sequence::registerCell);

        return sequence;
    }

    @Override
    public Collection<CellSequence> getSequences() {
        return Collections.unmodifiableCollection(sequences);
    }

    public void printBoard() {
        final var pattern = Pattern.compile(HEADER_PATTERN);
        for (int r = 0; r < 11; r++) {
            final var row = board[r];
            StringBuilder rowString = new StringBuilder();
            for (int c = 0; c < 11; c++) {
                final var cell = row[c];
                final var matcher = pattern.matcher(cell);
                if (matcher.find()) {
                    rowString.append(String.format("%1$5s", cell));
                } else if (memoizedFunction.apply(r).apply(c).isSolved()) {
                    rowString.append(String.format("%1$5s", memoizedFunction.apply(r).apply(c).getPossibilities()));
                } else {
                    rowString.append("     ");
                }
                if (c < 10) {
                    rowString.append(", ");
                }
            }
            System.out.println(rowString);
        }
    }
}