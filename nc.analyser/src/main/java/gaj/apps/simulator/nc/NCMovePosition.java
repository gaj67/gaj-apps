package gaj.apps.simulator.nc;

/**
 * Specifies the allowable positions on a Naughts-and-Crosses board.
 */
public enum NCMovePosition {

    UpperLeft,
    UpperCentre,
    UpperRight,
    CentreLeft,
    Centre,
    CentreRight,
    LowerLeft, 
    LowerCentre,
    LowerRight;

    private final int row;
    private final int col;

    private NCMovePosition() {
        row = ordinal() / 3;
        col = ordinal() % 3;
    }

    /**
     * Obtains the row index of the position on the board.
     * 
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Obtains the column index of the position on the board.
     * 
     * @return The column index.
     */
    public int getColumn() {
        return col;
    }

    /**
     * Indicates whether or not the position is located on the major (top-left
     * to bottom-right) diagonal.
     * 
     * @return A value of true (or false) if the position is (or is not) on the
     *         major diagonal.
     */
    public boolean isOnMajorDiagonal() {
        return row == col;
    }

    /**
     * Indicates whether or not the position is located on the minor (top-right
     * to bottom-left) diagonal.
     * 
     * @return A value of true (or false) if the position is (or is not) on the
     *         minor diagonal.
     */
    public boolean isOnMinorDiagonal() {
        return row + col == 2;
    }
    
    /**
     * Obtains the position that is the specified number of moves to the right
     * and down from the current position. Negative numbers will move up and/or
     * left, respectively. The position wraps around the board, if necessary.
     * 
     * @param right - The number of places to move to the right.
     * @param down - The number of places to move down.
     * @return The new position.
     */
    public NCMovePosition move(int right, int down) {
        int row = (this.row + down % 3 + 3) % 3;
        int col = (this.col + right % 3 + 3) % 3;
        return values()[3 * row + col];
    }

}
