package gaj.apps.simulator;

import java.util.Set;

/**
 * Implements a game simulator using a game model.
 * 
 * @param <M> - The type of game move to be simulated.
 * @param <G> - The type of game to be simulated.
 */
public class ModelSimulator<M extends Move, G extends Game<M>> implements Simulator<M, G> {

    private final Model<M, G> model;

    public ModelSimulator(Model<M, G> model) {
        this.model = model;
    }

    @Override
    public G simulate() {
        G game = model.newGame();
        while (!game.isComplete()) {
            Set<M> moves = model.permissibleMoves(game);
            if (!moves.isEmpty()) {
                game.makeMove(model.selectMove(moves));
            }
        }
        return game;
    }

    /**
     * Retrains the model by learning from the given game.
     * 
     * @return An averaged error score for the model after training.
     */
    @Override
    public double train(G game) {
        return model.train(game);
    }

}
