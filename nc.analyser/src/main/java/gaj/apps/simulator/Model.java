package gaj.apps.simulator;

import java.util.Set;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Denotes a trainable model of a game player.
 * 
 * @param <M>
 *            - The type of game move.
 */
public interface Model<M extends Move, G extends Game<M>> {

    /**
     * Creates a new, unstarted game.
     * 
     * @return The game.
     */
    G newGame();

    /**
     * Obtains the set of valid moves that may be made next in the game.
     * 
     * @return A (possibly empty) set of moves.
     */
    Set<M> permissibleMoves(G game);

    /**
     * Updates the model based upon learning from the current game.
     * 
     * @param game - The training sample game.
     */
    void train(G game);

    /**
     * Selects a move from the given set of moves according to inbuilt model
     * criteria.
     * 
     * @param moves - The set of permissible moves.
     * @return The selected move, or a value of null if there are no permissible
     *         moves.
     */
    @Nullable M selectMove(Set<M> moves);

}
