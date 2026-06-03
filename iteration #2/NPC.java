/**
 * NPC.java
 *
 * Base class for all Non-Player Characters (NPCs) in the game.
 * Each NPC subclass overrides interact() and trade() to implement
 * their unique dialogue and trading behaviour.
 *
 * AP CSA Concepts Used:
 *   - Encapsulation (private fields, public methods)
 *   - Inheritance base class
 *   - Polymorphism via overrideable methods
 *   - Constructor
 *   - String comparisons
 */
public class NPC
{
    // -------------------------------------------------------
    // Instance variables
    // -------------------------------------------------------

    /** The NPC's name, used to match "talk to <name>" commands. */
    private String name;

    /** A short description shown when the player first sees the NPC. */
    private String description;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a new NPC with the given name and description.
     *
     * @param name        the NPC's display name (e.g. "Billy")
     * @param description a brief description of the NPC
     */
    public NPC(String name, String description)
    {
        this.name        = name;
        this.description = description;
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------

    /**
     * Returns the NPC's name.
     * @return name string
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the NPC's description.
     * @return description string
     */
    public String getDescription()
    {
        return description;
    }

    // -------------------------------------------------------
    // Interaction hooks (overridden by subclasses)
    // -------------------------------------------------------

    /**
     * Called when the player uses "talk to <name>" or "interact <name>".
     * Subclasses MUST override this to provide unique dialogue.
     * The base version prints a generic greeting.
     *
     * @param player the Player object (subclasses may inspect inventory)
     * @param game   the Game object (subclasses may set game-state flags)
     */
    public void interact(Player player, Game game)
    {
        System.out.println(name + " doesn't seem interested in talking.");
    }

    /**
     * Called when the player uses "trade <itemName>" with this NPC.
     * Subclasses override this to handle their specific trade logic.
     * The base version rejects all trades.
     *
     * @param player   the Player attempting the trade
     * @param game     the Game object (for state changes)
     * @param itemName the name of the item the player is offering
     */
    public void trade(Player player, Game game, String itemName)
    {
        System.out.println(name + " doesn't want to trade for that.");
    }

    // -------------------------------------------------------
    // toString
    // -------------------------------------------------------

    /**
     * Returns a readable line describing the NPC.
     * @return "name - description"
     */
    @Override
    public String toString()
    {
        return name + " - " + description;
    }
}
