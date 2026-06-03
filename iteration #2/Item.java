/**
 * Item.java
 *
 * Base class for all items in the AI Prison Escape game.
 * Subclasses can override interact() to provide specialized behavior.
 *
 * AP CSA Concepts Used:
 *   - Encapsulation (private fields, public getters/setters)
 *   - Inheritance base class
 *   - Polymorphism via overrideable methods
 *   - Constructors
 *   - String methods
 */
public class Item
{
    // -------------------------------------------------------
    // Instance variables (private for encapsulation)
    // -------------------------------------------------------

    /** The display name of this item (e.g. "Coin", "Wrench"). */
    private String name;

    /** A short description shown when the player looks at the item. */
    private String description;

    /**
     * Whether this item slows the player down.
     * Only the Red Painted Brick sets this to true.
     * Affects the speed variable checked during the 2-minute timer.
     */
    private boolean heavy;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a new Item with the given name, description, and weight.
     *
     * @param name        the item's display name
     * @param description a short flavour description
     * @param heavy       true if the item slows the player (like the brick)
     */
    public Item(String name, String description, boolean heavy)
    {
        this.name        = name;
        this.description = description;
        this.heavy       = heavy;
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------

    /**
     * Returns the item's name.
     * @return name string
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the item's description.
     * @return description string
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns whether this item is heavy (slows the player).
     * @return true if heavy
     */
    public boolean isHeavy()
    {
        return heavy;
    }

    // -------------------------------------------------------
    // Polymorphic interaction hook
    // -------------------------------------------------------

    /**
     * Called when the player uses this item in the world.
     * Subclasses should override this to add special behaviour.
     * The base implementation just prints a generic message.
     */
    public void interact()
    {
        System.out.println("You examine the " + name + ". " + description);
    }

    // -------------------------------------------------------
    // toString
    // -------------------------------------------------------

    /**
     * Returns a readable representation of the item
     * in the format:  [ItemName] - description
     *
     * @return formatted string
     */
    @Override
    public String toString()
    {
        return "[" + name + "] - " + description;
    }
}
