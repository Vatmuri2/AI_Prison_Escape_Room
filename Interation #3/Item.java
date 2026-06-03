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
