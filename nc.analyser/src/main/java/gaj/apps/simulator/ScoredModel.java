package gaj.apps.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Assumes that the model can score each move as it is played in a game.
 * The model aims to always play optimally-score moves.
 * 
 * @param <M> - The type of game move.
 * @param <G> - The type of game.
 */
public abstract class ScoredModel<M extends Move, G extends Game<M>> implements Model<M, G> {

    /**
     * Selects an optimally-scored move from amongst the permissible moves. If
     * there are multiple optimal moves, then one is selected at random.
     */
    @Override
    public @Nullable M selectMove(Set<M> moves) {
        if (moves.isEmpty()) return null;
        double score = Double.NEGATIVE_INFINITY;
        List<M> optimalMoves = new ArrayList<>(moves.size());
        for (M move : moves) {
            if (move.getScore() > score) {
                score = move.getScore();
                optimalMoves.clear();
            }
            if (move.getScore() >= score) {
                optimalMoves.add(move);
            }
        }
        if (optimalMoves.size() == 1) return optimalMoves.get(0);
        return optimalMoves.get((int) (Math.random() * optimalMoves.size()));
    }

}
