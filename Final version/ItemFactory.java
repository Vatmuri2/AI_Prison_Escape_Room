/**
 * Factory class for constructing all Item instances used in the game.
 * Each method returns a new Item configured with the appropriate
 * description and flavor text.
 */
public class ItemFactory {

    /**
     * Creates the Coin item found in the Prison Room.
     * Used to unscrew the vent, start the riot with the Buff Dude,
     * and trade with the Bookworm. The coin is retained after unscrewing the vent.
     *
     * @return a new Coin item
     */
    public static Item coin() {
        return new Item(
            "Coin",
            "A dull metal coin. Warm to the touch.",
            "A small coin. It's retained after unscrewing the vent.");
    }

    /**
     * Creates the Wrench item found in the Prison Room.
     * Used as a trade item for the Bookworm in the Library.
     *
     * @return a new Wrench item
     */
    public static Item wrench() {
        return new Item(
            "Wrench",
            "A heavy steel wrench, slightly rusted.",
            "A solid wrench. Useful for trading, apparently.");
    }

    /**
     * Creates the Fork item found in the Prison Room.
     * Required by Billy in the Cafeteria in exchange for the Fortune Cookie.
     *
     * @return a new Fork item
     */
    public static Item fork() {
        return new Item(
            "Fork",
            "A bent prison cafeteria fork.",
            "Contraband? Maybe. Valuable? Definitely.");
    }

    /**
     * Creates the Red Painted Brick found in the Ventilation System.
     * Heavy enough to prevent fence climbing. Used as a trade item with the Bookworm.
     *
     * @return a new Red Painted Brick item
     */
    public static Item redPaintedBrick() {
        return new Item(
            "Red Painted Brick",
            "A brick, inexplicably painted red. Surprisingly heavy.",
            "Why is it red? You take it anyway. It's heavier than expected.");
    }

    /**
     * Creates the Fortune Cookie received from Billy in the Cafeteria.
     * Contains the Time Machine access code. Required to activate the Time Machine.
     *
     * @return a new Fortune Cookie item
     */
    public static Item fortuneCookie() {
        return new Item(
            "Fortune Cookie",
            "A plastic-wrapped fortune cookie. Something is written inside.",
            "You crack it open. The fortune reads: "
                + "\"Time Machine access password: FRESHMAN\"");
    }
}
