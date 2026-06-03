/**
 * Represents the player character in the game.
 * Tracks the player's current room, their inventory, and whether
 * they are currently crawling through the ventilation system.
 */
public class Player {

    /** The room the player is currently occupying. */
    private Room currentRoom;

    /** The player's personal inventory of collected items. */
    private Inventory inventory;

    /** Whether the player is currently inside the ventilation duct. */
    private boolean inVent;

    /**
     * Constructs a new Player with an empty inventory and not in the vent.
     */
    public Player() {
        this.inventory = new Inventory();
        this.inVent = false;
    }

    /**
     * Returns the room the player is currently in.
     *
     * @return the current Room
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Moves the player to a new room.
     *
     * @param room the Room to move the player into
     */
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    /**
     * Returns the player's inventory.
     *
     * @return the player's Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Returns whether the player is currently inside the ventilation duct.
     *
     * @return true if in the vent, false otherwise
     */
    public boolean isInVent() {
        return inVent;
    }

    /**
     * Sets whether the player is inside the ventilation duct.
     *
     * @param inVent true if entering the vent, false if exiting
     */
    public void setInVent(boolean inVent) {
        this.inVent = inVent;
    }
}
