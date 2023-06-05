package rankednetwork.NetworkLevelling.Boosters;

/**
 * The Pair class represents a simple generic pair of two items.
 *
 * @param <A> the type of the first item in the Pair
 * @param <B> the type of the second item in the Pair
 */
public class Pair<A, B> {

	/**
	 * The first item in the Pair.
	 */
	public final A first;
	/**
	 * The second item in the Pair.
	 */
	public final B second;

	/**
	 * Creates a new Pair with the given items.
	 *
	 * @param first  the first item in the Pair
	 * @param second the second item in the Pair
	 */
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
}
