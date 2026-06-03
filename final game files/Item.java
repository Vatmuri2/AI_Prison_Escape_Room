/**
 * Represents a collectible item in the game world.
 * Items can be picked up, dropped, traded with NPCs, or used to
 * interact with the environment.
 */
public class Item {

    /** The display name of this item. */
    private String name;

    /** A brief one-line description shown when scanning a room. */
    private String shortDesc;

    /** A message displayed when the player picks up this item. */
    private String pickupMessage;

    /**
     * Constructs a new Item with all descriptive fields.
     *
     * @param name           the display name of the item
     * @param shortDesc      a brief description shown in room listings
     * @param pickupMessage  the message shown when the player picks this item up
     */
    public Item(String name, String shortDesc, String pickupMessage) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.pickupMessage = pickupMessage;
    }

    /**
     * Returns the display name of this item.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the brief room-listing description of this item.
     *
     * @return the short description
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Returns the message shown when the player picks up this item.
     *
     * @return the pickup message
     */
    public String getPickupMessage() {
        return pickupMessage;
    }
}

/**
 * Factory class for constructing all Item instances used in the game.
 * Each method returns a new Item configured with the appropriate
 * description and flavor text.
 */
class ItemFactory {

    /**
     * Creates the Coin item found in the Prison Room.
     * Used to unscrew the vent, bribe the Buff Dude, and trade with the Bookworm.
     * The coin is NOT consumed when used to unscrew the vent.
     *
     * @return a new Coin item
     */
    public static Item coin() {
        return new Item(
            "Coin",
            "A dull metal coin. Warm to the touch.",
            "A small coin. It's retained after use — you might need it again."
        );
    }

    /**
     * Creates the Wrench item found in the Prison Room.
     * Primarily used as a trade item for the Bookworm in the Library.
     *
     * @return a new Wrench item
     */
    public static Item wrench() {
        return new Item(
            "Wrench",
            "A heavy steel wrench, slightly rusted.",
            "A solid wrench. Useful for more than just bolts, apparently."
        );
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
            "Contraband? Maybe. Valuable? Definitely."
        );
    }

    /**
     * Creates the Red Painted Brick found in the Ventilation System.
     * Heavy — prevents fence climbing. Used as a trade item with the Bookworm.
     *
     * @return a new Red Painted Brick item
     */
    public static Item redPaintedBrick() {
        return new Item(
            "Red Painted Brick",
            "A brick, inexplicably painted red. Surprisingly heavy.",
            "Why is this here? Why is it red? You take it anyway. It's heavier than expected."
        );
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
            "You crack it open. The fortune reads: " +
            Colors.GREEN + "\"Time Machine access password: FRESHMAN\"" +
            Colors.RESET + Colors.GRAY +
            "\nThat unlocks the machine. You'll need Jilly in the Letter Room for the destination timecodes."
        );
    }
}