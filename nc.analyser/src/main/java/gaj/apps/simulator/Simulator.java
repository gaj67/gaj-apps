package gaj.apps.simulator;

/**
 * Denotes a generic simulator for a game.
 * 
 * @param <G> - The type of game to be simulated.
 */
public interface Simulator<G extends Game<?>> {

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
     * @return An averaged error score for the simulator after training.
     */
    double train(G game);

}
