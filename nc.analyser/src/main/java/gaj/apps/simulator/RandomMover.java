package gaj.apps.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.jdt.annotation.Nullable;

/**
 * This mover aims to play random moves.
 * 
 * @param <M> - The type of game move.
 */
public class RandomMover<M extends Move> implements Mover<M> {

    /**
     * Selects an random move from amongst the permissible moves.
     */
    @Override
    public @Nullable M selectMove(Set<M> moves) {
        return selectRandomMove(moves);
    }

    /**
     * Selects an random move from amongst the permissible moves.
     */
    public static @Nullable <M extends Move> M selectRandomMove(Set<M> moves) {
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
