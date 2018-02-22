package gaj.apps.simulator;

/**
 * Describes a generic simulator for a game.
 * 
 * @param <M>
 *            - The type of game move to be simulated.
 */
public interface Simulator<M extends Move, G extends Game<M>> {

    /**
     * Simulates one complete game, from start to finish.
     * 
     * @return A complete game.
     */
    G simulate();

    /**
     * Retrains the simulator by learning from the given game.
     * 
     * @param game - The game to be used as a training example.
     */
    void train(G game);

}
