/**
 * Player.java
 *
 * Represents the human player character inside the game world.
 *
 * Responsibilities:
 *   - Tracks the player's current Room.
 *   - Owns the player's Inventory.
 *   - Exposes a speed variable that is reduced by heavy items
 *     (specifically the Red Painted Brick).
 *   - Provides helper methods used by the Game class.
 *
 * The speed variable feeds into the 2-minute in-game timer:
 *   - Normal speed  = 1  (no speed penalty)
 *   - Slowed speed  = 2  (carrying the Red Painted Brick)
 * The Game's timer loop advances faster when speed == 2,
 * meaning the player has less effective time.
 *
 * AP CSA Concepts Used:
 *   - Encapsulation (private fields, public getters/setters)
 *   - Object composition (Player HAS-A Inventory, HAS-A Room)
 *   - Constructor
 *   - int variable manipulation
 */
public class Player
{
    // -------------------------------------------------------
    // Instance variables
    // -------------------------------------------------------

    /** The room the player is currently standing in. */
    private Room currentRoom;

    /**
     * The player's movement speed multiplier.
     *   1 = normal speed (no penalty)
     *   2 = slowed (carrying a heavy item like the Red Painted Brick)
     * The Game class reads this to scale the timer penalty.
     */
    private int speed;

    /** The player's inventory (items they are carrying). */
    private Inventory inventory;

    /**
     * Whether the player is inside the ventilation system.
     * True when currentRoom.isVent() is true AND the player
     * has explicitly "entered" the vent, allowing for the vent
     * crawl movement sub-system.
     */
    private boolean inVent;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Player and places them in the starting room.
     * Speed begins at 1 (normal).  Inventory starts empty.
     *
     * @param startingRoom the room where the game begins (Prison Room)
     */
    public Player(Room startingRoom)
    {
        currentRoom = startingRoom;
        speed       = 1;
        inventory   = new Inventory();
        inVent      = false;
    }

    // -------------------------------------------------------
    // Room / location management
    // -------------------------------------------------------

    /**
     * Returns the player's current room.
     * @return currentRoom
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Moves the player to a new room.
     * Also updates the inVent flag based on whether the
     * new room is part of the ventilation system.
     *
     * @param newRoom the destination Room
     */
    public void setCurrentRoom(Room newRoom)
    {
        currentRoom = newRoom;
        inVent      = newRoom.isVent();
    }

    /**
     * Returns true if the player is currently inside a vent.
     * @return inVent
     */
    public boolean isInVent()
    {
        return inVent;
    }

    /**
     * Manually sets the inVent flag.
     * Used when the player crawls into / out of a vent entrance
     * that is inside their current room.
     *
     * @param inVent true if inside the ventilation system
     */
    public void setInVent(boolean inVent)
    {
        this.inVent = inVent;
    }

    // -------------------------------------------------------
    // Speed / heavy-item tracking
    // -------------------------------------------------------

    /**
     * Returns the player's current speed multiplier.
     * @return 1 (normal) or 2 (slowed by heavy item)
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * Updates the speed variable based on whether the player
     * is currently carrying any heavy items.
     * Called by the Game class whenever the player picks up
     * or drops an item.
     *
     * Rules:
     *   - If inventory contains a heavy item -> speed = 2
     *   - Otherwise                          -> speed = 1
     */
    public void updateSpeed()
    {
        if (inventory.hasHeavyItem())
        {
            speed = 2;
            System.out.println("[!] You are carrying something heavy. You feel sluggish.");
            System.out.println("[!] The clock is ticking faster...");
        }
        else
        {
            speed = 1;
        }
    }

    // -------------------------------------------------------
    // Inventory access
    // -------------------------------------------------------

    /**
     * Returns the player's Inventory object.
     * NPC subclasses and the Game class use this to add/remove
     * items and check item possession.
     *
     * @return inventory
     */
    public Inventory getInventory()
    {
        return inventory;
    }
}
