package gaj.apps.simulator.nc;

import gaj.apps.simulator.Move;

/**
 * Specifies a single move in the game of Naughts-and-Crosses.
 */
public class NCMove implements Move {

    private final NCMoveType type;
    private final NCMovePosition pos;

    public NCMove(NCMoveType type, NCMovePosition pos) {
        this.type = type;
        this.pos = pos;
    }

    public NCMoveType getType() {
        return type;
    }

    public NCMovePosition getPosition() {
        return pos;
    }

}
