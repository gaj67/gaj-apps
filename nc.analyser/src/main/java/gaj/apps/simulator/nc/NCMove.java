package gaj.apps.simulator.nc;

import gaj.apps.simulator.Move;

/**
 * Specifies a single move in the game of Naughts-and-Crosses.
 */
public class NCMove implements Move {

    private final NCMoveType type;
    private final NCMovePosition pos;
    private double score = 0.0;

    /**
     * Creates a move of the given type at the given position on the board.
     * 
     * @param type - The move type.
     * @param pos - The move position.
     */
    public NCMove(NCMoveType type, NCMovePosition pos) {
        this.type = type;
        this.pos = pos;
    }

    /**
     * Obtains the type of the move.
     * 
     * @return The move type.
     */
    public NCMoveType getType() {
        return type;
    }

    /**
     * Obtains the position of the move.
     * 
     * @return The move position.
     */
    public NCMovePosition getPosition() {
        return pos;
    }

    /*package-private*/ void setScore(double score) {
        this.score = score;
    }
    
    @Override
    public double getScore() {
        return score;
    }

}
