package gaj.apps.simulator.nc;

/**
 * Specifies the types of moves allowed in a game of Naughts-and-Crosses.
 */
public enum NCMoveType {

    Naught,
    Cross;
    
    /**
     * Obtains the complementary type to the current move type.
     * 
     * @return The complementary type.
     */
    public NCMoveType swapType() {
        return values()[(ordinal() + 1) % 2];
    }
}
