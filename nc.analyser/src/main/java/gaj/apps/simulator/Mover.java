package gaj.apps.simulator;

import java.util.Set;
import org.eclipse.jdt.annotation.Nullable;

/**
 * Denotes a game move selector.
 * 
 * @param <M> - The type of game move.
 */
public interface Mover<M extends Move> {

    /**
     * Selects a move from the given set of moves according to inbuilt model
     * criteria.
     * 
     * @param moves - The set of permissible moves.
     * @return The selected move, or a value of null if there are no permissible
     *         moves.
     */
    @Nullable M selectMove(Set<M> moves);

}
