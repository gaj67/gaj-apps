package apps.gaj.nc.analyser;

/**
 * Simulates two computers repeatedly playing Naughts and Crosses against each
 * other. The results are used to train a simple linear discriminator for
 * estimating the next optimal move.
 *
 * This example has been deliberately chosen because it is simple enough for a
 * human to evaluate all game moves in advance, and formulate the optimal
 * strategies theoretically.
 */
public class App 
{
    private static final int DEFAULT_NUM_SIMULATIONS = 100;

    public static void main(String... args) {
        final int numSimulations = (args.length == 0) ? DEFAULT_NUM_SIMULATIONS : Integer.parseInt(args[0]);
        Simulator simulator = new Simulator();
        for (int i = 0; i < numSimulations; i++) {
            Game game = simulator.simulate();
            simulator.train(game);
        }
    }

}
