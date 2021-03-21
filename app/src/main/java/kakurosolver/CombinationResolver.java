package kakurosolver;

import java.util.Collection;
import java.util.Set;

public interface CombinationResolver {
    Collection<Set<Integer>> getCombinations(int total, int combinations);
}
