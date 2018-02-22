package apps.gaj.nc.analyser;

/**
 * Describes a generic simulator for a game.
 * 
 * @param <T> - The type of game to be simulated.
 */
public interface Simulator<T extends Game> {

    /**
     * Simulates one complete game, from start to finish.
     * 
     * @return A complete game.
     */
    T simulate();

    /**
     * Retrains the simulator by studying the given game.
     * 
     * @param game - The complete game to be used as a training example.
     */
    void train(T game);

}
