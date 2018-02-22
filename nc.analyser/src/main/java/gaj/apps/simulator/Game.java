package gaj.apps.simulator;

import org.eclipse.jdt.annotation.Nullable;

/**
 * Specifies a single game of zero, one or more moves.
 * 
 * @param <M>
 *            - The type of move.
 */
public interface Game<M extends Move> extends Iterable<M> {

    /**
     * Indicates whether or not the game is complete.
     * 
     * @return A value of true (or false) if the game is complete (or
     *         incomplete).
     */
    boolean isComplete();

    /**
     * Indicates whether or not the last move (if any) was the winning move.
     * 
     * @return A value of true (or false) if the game has (or has not) been won.
     */
    boolean isWon();

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
    M getMove(int i);

    /**
     * Obtains the first move played in the game (if any).
     * 
     * @return The first move, or a value of null if no moves have been played.
     */
    @Nullable M getFirstMove();

    /**
     * Obtains the last move played in the game (if any).
     * 
     * @return The last move, or a value of null if no moves have been played.
     */
    @Nullable M getLastMove();

    /**
     * Makes the given move in the game.
     * 
     * @param move
     *            - The new move.
     * @throws IllegalArgumentException
     *             if the game is already complete or if the move is not valid.
     */
    void makeMove(M move);

    /**
     * Retracts the last move made in the game, if any.
     * 
     * @return The retracted move, or a value of null if no moves have been
     *         played.
     */
    @Nullable M retractLastMove();

}
