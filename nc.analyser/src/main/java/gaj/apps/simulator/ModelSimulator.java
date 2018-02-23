package gaj.apps.simulator;

import java.util.Set;

/**
 * Implements a game simulator using a game model.
 * 
 * @param <M> - The type of game move to be simulated.
 * @param <G> - The type of game to be simulated.
 */
public class ModelSimulator<M extends Move, G extends Game<M>> implements Simulator<G> {

    private final Creator<G> creator;
    private final Model<M, G> model;
    private Mover<M>[] movers;

    public ModelSimulator(Creator<G> creator, Model<M, G> model, @SuppressWarnings("unchecked") Mover<M>... movers) {
        this.creator = creator;
        this.model = model;
        this.movers = movers;
        if (movers.length == 0) {
            throw new IllegalArgumentException("Require at least one game mover");
        }
    }

    @Override
    public G simulate() {
        int moverIdx = 0;
        G game = creator.newGame();
        while (!game.isComplete()) {
            Set<M> moves = model.permissibleMoves(game);
            if (!moves.isEmpty()) {
                Mover<M> mover = movers[moverIdx];
                moverIdx = (moverIdx + 1) % movers.length;
                game.makeMove(mover.selectMove(moves));
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
