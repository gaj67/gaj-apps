package gaj.apps.simulator.nc;

import java.util.Iterator;
import java.util.LinkedList;
import org.eclipse.jdt.annotation.Nullable;
import gaj.apps.simulator.Game;

/**
 * Specifies the sequence of moves in a game of Naughts-and-Crosses.
 */
public class NCGame implements Game<NCMove> {

    private static final int MAX_MOVES = NCMovePosition.values().length;

    private final LinkedList<NCMove> moves = new LinkedList<>();
    private final NCMoveType[] board = new NCMoveType[MAX_MOVES];

    private boolean won = false;
    private boolean complete = false;

    /**
     * Initialises a new (empty) game of Naughts-and-Crosses.
     */
    public NCGame() {}

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public boolean isWon() {
        return won;
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
    public @Nullable NCMove getFirstMove() {
        return moves.isEmpty() ? null : moves.getFirst();
    }

    @Override
    public @Nullable NCMove getLastMove() {
        return moves.isEmpty() ? null : moves.getLast();
    }

    @Override
    public Iterator<NCMove> iterator() {
        return moves.iterator();
    }

    @Override
    public void makeMove(NCMove move) throws IllegalArgumentException {
        if (isComplete()) {
            throw new IllegalArgumentException("The game is already complete");
        }
        final int pos = move.getPosition().ordinal();
        if (board[pos] != null) {
            throw new IllegalArgumentException("Position " + move.getPosition() + " is occupied");
        }
        NCMove lastMove = getLastMove();
        if (lastMove != null && lastMove.getType() == move.getType()) {
            throw new IllegalArgumentException("Type " + move.getType() + " was played last turn");
        }
        board[pos] = move.getType();
        moves.add(move);
        checkStatus(move);
    }

    @Override
    public NCMove retractLastMove() {
        if (moves.isEmpty()) return null;
        won = false;
        complete = false;
        NCMove lastMove = moves.removeLast();
        board[lastMove.getPosition().ordinal()] = null;
        return lastMove;
    }

    private void checkStatus(NCMove lastMove) {
        won = getRowCount(lastMove) == 3
            || getColumnCount(lastMove) == 3
            || getMajorDiagCount(lastMove) == 3
            || getMinorDiagCount(lastMove) == 3;
        complete = won || moves.size() >= MAX_MOVES;
    }

    private int getRowCount(NCMove lastMove) {
        return getLineCount(lastMove, 1, 0);
    }

    private int getColumnCount(NCMove lastMove) {
        return getLineCount(lastMove, 0, 1);
    }

    private int getMajorDiagCount(NCMove lastMove) {
        return lastMove.getPosition().isOnMajorDiagonal() ? getLineCount(lastMove, 1, 1) : 0;
    }

    private int getMinorDiagCount(NCMove lastMove) {
        return lastMove.getPosition().isOnMinorDiagonal() ? getLineCount(lastMove, -1, 1) : 0;
    }

    private int getLineCount(NCMove lastMove, int right, int down) {
        int count = 1;
        NCMovePosition pos = lastMove.getPosition().move(right, down);
        if (lastMove.getType() == board[pos.ordinal()]) count++;
        pos = pos.move(right, down);
        if (lastMove.getType() == board[pos.ordinal()]) count++;
        return count;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (NCMoveType type : board) {
            buf.append((type == null) ? "." : (type == NCMoveType.Cross) ? "x" : "o");
        }
        buf.append(" [");
        for (NCMove move : moves) {
            buf.append(move.getPosition().ordinal());
        }
        buf.append("]");
        buf.append(won ? "*" : "");
        return buf.toString();
    }

}
