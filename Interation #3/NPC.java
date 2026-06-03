import java.util.ArrayList;

/**
 * Abstract base class for all Non-Player Characters (NPCs) in the game.
 * Each NPC has a name, a short description, and a list of name aliases
 * for flexible player input matching. Subclasses implement specific
 * dialogue and trade interactions.
 */
public abstract class NPC {

    /** The display name of this NPC. */
    protected String name;

    /** A brief description shown in room listings. */
    protected String shortDesc;

    /** Aliases the player can use to reference this NPC (e.g., "buff", "dude"). */
    protected ArrayList<String> aliases;

    /**
     * Constructs an NPC with a name and description.
     *
     * @param name      the display name
     * @param shortDesc brief room-listing description
     */
    public NPC(String name, String shortDesc) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.aliases = new ArrayList<String>();
    }

    /**
     * Adds an alias the player can type to refer to this NPC.
     *
     * @param alias the alternative name
     */
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * Returns the display name of this NPC.
     *
     * @return the NPC name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the brief description of this NPC shown in room listings.
     *
     * @return the short description
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Returns whether the given input matches this NPC's name or any of its aliases.
     * Matching is case-insensitive and checks for partial containment.
     *
     * @param input the player's typed NPC name
     * @return true if this NPC matches the input
     */
    public boolean matchesName(String input) {
        String low = input.toLowerCase();
        if (name.toLowerCase().contains(low)) {
            return true;
        }
        for (int i = 0; i < aliases.size(); i++) {
            String alias = aliases.get(i).toLowerCase();
            if (alias.contains(low) || low.contains(alias)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the player initiating dialogue with this NPC.
     *
     * @param player the Player instance
     * @param game   the current Game instance (for triggering game-over states)
     */
    public abstract void talkTo(Player player, Game game);

    /**
     * Handles the player attempting to trade an item with this NPC.
     *
     * @param player the Player instance
     * @param item   the Item the player is offering
     * @param game   the current Game instance
     */
    public abstract void trade(Player player, Item item, Game game);
}
