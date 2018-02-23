package gaj.apps.simulator.nc;

import gaj.apps.simulator.Game;
import gaj.apps.simulator.ModelSimulator;
import gaj.apps.simulator.MoverMethods;
import gaj.apps.simulator.Simulator;

/**
 * Simulates two computers repeatedly playing Naughts and Crosses against each
 * other. The results are used to train a simple linear discriminator for
 * estimating the next optimal move.
 * 
 * Player 1 always plays an optimal move (according to the model), whereas
 * player 2 always plays a random move.
 */
public class NCOptRndSim 
{
    private static final int DEFAULT_NUM_SIMULATIONS = 100;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void main(String... args) {
        final int numSimulations = (args.length == 0) ? DEFAULT_NUM_SIMULATIONS : Integer.parseInt(args[0]);
        NCSimpleModel model = new NCSimpleModel(0.9, 0.1);
        Simulator simulator = new ModelSimulator<>(
            NCGame::new, model, 
            MoverMethods::selectScoredMove, MoverMethods::selectRandomMove
        );
        for (int i = 0; i < numSimulations; i++) {
            Game game = simulator.simulate();
            double score = simulator.train(game);
            System.out.printf("Game: %s, Score: %f%n", game, score);
        }
        System.out.printf("Model: %s%n", model);
    }

}
