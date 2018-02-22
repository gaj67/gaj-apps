package gaj.apps.simulator;

import java.util.Set;

/**
 * Implements a game simulator using a game model.
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

    @Override
    public void train(G game) {
        model.train(game);
    }

}
