package gaj.apps.simulator;

/**
 * Denotes a game creator.
 * 
 * @param <G> - The type of game.
 */
public interface Creator<G extends Game<?>> {

    /**
     * Creates a new, unstarted game.
     * 
     * @return The game.
     */
    G newGame();

}
