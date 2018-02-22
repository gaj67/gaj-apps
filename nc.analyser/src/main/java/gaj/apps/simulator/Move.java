package gaj.apps.simulator;

/**
 * Denotes a move in a game.
 */
public interface Move {

    /**
     * Obtains the score of a move generated by a model.
     * 
     * @return The score of the move.
     */
    default double score() {
        return 0;
    }

}
