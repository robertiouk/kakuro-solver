package kakurosolver;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CombinationResolverImplTest {
    private CombinationResolverImpl resolver;

    @Before
    public void setup() {
        resolver = new CombinationResolverImpl();
    }

    @Test
    public void testFourInTwo() {
        final var actual = resolver.getCombinations(4, 2);
        final var expected = List.of(Set.of(3,1));
        assertEquals(expected, actual);
    }

    @Test
    public void testSixInTwo() {
        final var actual = resolver.getCombinations(6, 2);
        final var expected = List.of(Set.of(1,5),
                Set.of(2,4));
        assertEquals(expected, actual);
    }

    @Test
    public void testSixInThree() {
        final var actual = resolver.getCombinations(6, 3);
        final var expected = List.of(Set.of(1,2,3));
        assertEquals(expected, actual);
    }

    @Test
    public void testMemoized() {
        var actual = resolver.getCombinations(6, 3);
        final var expected = List.of(Set.of(1,2,3));
        assertEquals(expected, actual);

        actual = resolver.getCombinations(6, 3);
        assertEquals(expected, actual);
    }

    @Test
    public void testSevenInThree() {
        final var actual = resolver.getCombinations(7, 3);
        final var expected = List.of(Set.of(1,2,4));
        assertEquals(expected, actual);
    }

    @Test
    public void testTwentyFourInThree() {
        final var actual = resolver.getCombinations(24, 3);
        var expected = List.of(Set.of(7,8,9));
        assertEquals(expected, actual);
    }

    @Test
    public void testTwentyTwoInThree() {
        // 22 in 3 = 589, 679
        final var actual = resolver.getCombinations(22, 3);
        var expected = List.of(Set.of(5, 8, 9),
                Set.of(6,7,9));
        assertEquals(expected, actual);
    }

    @Test
    public void testThirtyInSix() {
        // 123789 124689 125679 134589 134679 135678 234579 234678 30
        final var actual = resolver.getCombinations(30, 6);
        final var expected = List.of(
                Set.of(1,2,3,7,8,9),
                Set.of(1,2,4,6,8,9),
                Set.of(1,2,5,6,7,9),
                Set.of(1,3,4,5,8,9),
                Set.of(1,3,4,6,7,9),
                Set.of(1,3,5,6,7,8),
                Set.of(2,3,4,5,7,9),
                Set.of(2,3,4,6,7,8));

        //assertEquals(expected, actual);
        testCombinations(expected, actual);
    }

    private void testCombinations(final Collection<Set<Integer>> expected,
                                  final Collection<Set<Integer>> results) {
        assertEquals(expected.size(), results.size());
        expected.forEach(combo -> assertTrue(results.contains(combo)));
    }
}