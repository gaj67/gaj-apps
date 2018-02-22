package apps.gaj.nc.analyser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Specifies the sequence of moves in a game of Naughts-and-Crosses.
 */
public class NCGame implements Game<NCMove> {

    private final List<NCMove> moves = new ArrayList<>();
    private final NCMoveType[] board;
    private boolean isComplete = false;

    /**
     * Initialises a new (empty) game of Naughts-and-Crosses.
     */
    public NCGame() {
        board = new NCMoveType[9];
        for (int i = 0; i < 9; i++) {
            board[i] = NCMoveType.Empty;
        }
    }

    @Override
    public Iterator<NCMove> iterator() {
        return moves.iterator();
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }

    @Override
    public int numMoves() {
        return moves.size();
    }

    @Override
    public NCMove getMove(int i) {
        if (i < 0 || i >= moves.size()) {
            throw new IllegalArgumentException("Invalid move index: " + i);
        }
        return moves.get(i);
    }

    @Override
    public void makeMove(NCMove move) {
        // TODO Auto-generated method stub

    }

}
