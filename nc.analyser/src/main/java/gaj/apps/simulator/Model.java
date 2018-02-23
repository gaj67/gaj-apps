package gaj.apps.simulator;

import java.util.Set;

/**
 * Denotes a trainable model of a game.
 * 
 * @param <M> - The type of game move.
 * @param <G> - The type of game.
 */
public interface Model<M extends Move, G extends Game<M>> {

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

}
