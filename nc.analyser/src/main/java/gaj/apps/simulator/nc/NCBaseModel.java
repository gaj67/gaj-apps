package gaj.apps.simulator.nc;

import java.util.HashSet;
import java.util.Set;
import gaj.apps.simulator.Model;

/**
 * Evaluates the score of each move by playing the move and then scoring the
 * game.
 */
public abstract class NCBaseModel implements Model<NCMove, NCGame> {

    /**
     * Computes a score for the given game, from the viewpoint of the last
     * player to make a move. This should be positive if the last player has the
     * advantage, negative if the last player is at a disadvantage, or zero if
     * there is no advantage or disadvantage.
     * 
     * @param game - The current game.
     * @return The score of the game.
     */
    protected abstract double scoreGame(NCGame game);

    /**
     * Obtains the set of valid moves that may be made next in the game. Scores
     * each move by first playing the move in the game, and then scoring the
     * resulting modified game.
     */
    @Override
    public Set<NCMove> permissibleMoves(NCGame game) {
        Set<NCMove> moves = new HashSet<>();
        if (!game.isComplete()) {
            NCMoveType nextType = (game.numMoves() == 0)
                    ? NCMoveType.values()[(int) (Math.random() * 2)]
                    : game.getLastMove().getType().swapType();
            for (NCMovePosition pos : getAvailablePositions(game)) {
                NCMove move = new NCMove(nextType, pos);
                try {
                    game.makeMove(move);
                    move.setScore(scoreGame(game));
                    game.retractLastMove();
                    moves.add(move);
                } catch (IllegalArgumentException e) {
                    // That move was not valid in the context of the game.
                }
            }
        }
        return moves;
    }

    private Set<NCMovePosition> getAvailablePositions(NCGame game) {
        Set<NCMovePosition> availablePositions = new HashSet<>();
        for (NCMovePosition pos : NCMovePosition.values()) {
            availablePositions.add(pos);
        }
        for (NCMove move : game) {
            availablePositions.remove(move.getPosition());
        }
        return availablePositions;
    }

}
