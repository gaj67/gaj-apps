package gaj.apps.simulator;

import java.util.Set;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Denotes a trainable model of a game player.
 * 
 * @param <M> - The type of game move.
 * @param <G> - The type of game.
 */
public interface Model<M extends Move, G extends Game<M>> {

    /**
     * Creates a new, unstarted game.
     * 
     * @return The game.
     */
    G newGame();

    /**
     * Obtains (possibly a subset of) the set of valid moves that may be made next in the game.
     * 
     * @param game - The current game.
     * @return A (possibly empty) set of moves.
     */
    Set<M> permissibleMoves(G game);

    /**
     * Updates the model based upon learning from the current game.
     * 
     * @param game - The training sample game.
     * @return An averaged error score for the model after training.
     */
    double train(G game);

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
