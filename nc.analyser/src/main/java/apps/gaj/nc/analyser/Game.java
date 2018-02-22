package apps.gaj.nc.analyser;

/**
 * Specifies a single game of zero, one or more moves.
 * 
 * @param <T> - The type of move.
 */
public interface Game<T> extends Iterable<T> {

    /**
     * Indicates whether or not the game is complete.
     * 
     * @return A value of true (or false) if the game is complete (or
     *         incomplete).
     */
    boolean isComplete();

    /**
     * Obtains the number of moves that have been made in the game.
     * 
     * @return The number of moves.
     */
    int numMoves();

    /**
     * Obtains the i-th move, counting from zero.
     * 
     * @param i - The index of the move.
     * @return The i-th move.
     * @throws IllegalArgumentException if the index i is invalid.
     */
    T getMove(int i);

    /**
     * Makes the given move in the game.
     * 
     * @param move - The new move.
     * @throws IllegalArgumentException if the game is already complete or if the move is not valid.
     */
    void makeMove(T move);

}
