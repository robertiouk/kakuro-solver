package kakurosolver;

import org.junit.Before;
import org.junit.Test;

public class BoardLoaderImplTest {
    private BoardLoaderImpl loader;
    @Before
    public void setup() {
        loader = new BoardLoaderImpl();
    }

    @Test
    public void testGetSequences() {
        final var sequences = loader.getSequences();

        sequences.forEach(c -> c.isComplete());
        loader.printBoard();
    }
}