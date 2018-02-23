package gaj.apps.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Assumes that a model has scored each permissible move. This mover aims to
 * always play optimally-scored moves.
 * 
 * @param <M> - The type of game move.
 */
public abstract class MoverMethods {

    private MoverMethods() {}

    /**
     * Selects an optimally-scored move from amongst the permissible moves. If
     * there are multiple optimal moves, then one is selected at random.
     */
    public static @Nullable <M extends Move> M selectScoredMove(Set<M> moves) {
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
