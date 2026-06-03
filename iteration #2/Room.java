import java.util.ArrayList;

/**
 * Room.java
 *
 * Represents a single location in the AI Prison Escape game world.
 *
 * Exits are stored as Strings formatted as "Direction:Destination"
 * (e.g. "north:Cafeteria").  This data-driven design means the Game
 * class can parse movement dynamically — adding new rooms never
 * requires changing the movement code.
 *
 * Items present in the room are stored in an ArrayList<Item>.
 * NPCs present in the room are stored in an ArrayList<NPC>.
 *
 * AP CSA Concepts Used:
 *   - ArrayList (adding, traversal, index-based remove)
 *   - for-each loop
 *   - String.split() and String.equalsIgnoreCase()
 *   - Encapsulation (private fields, public methods)
 *   - Constructor
 */
public class Room
{
    // -------------------------------------------------------
    // Instance variables
    // -------------------------------------------------------

    /** The display name of this room (e.g. "Prison Room"). */
    private String name;

    /** A description shown when the player uses the "look" command. */
    private String description;

    /**
     * Exit list.  Each entry is "Direction:Destination"
     * e.g. "north:Cafeteria", "vent:VentilationSystem"
     * Using a String ArrayList keeps this within AP CSA scope
     * while still supporting data-driven movement parsing.
     */
    private ArrayList<String> exits;

    /** Items currently on the floor of this room. */
    private ArrayList<Item> items;

    /** NPCs currently present in this room. */
    private ArrayList<NPC> npcs;

    /**
     * True if this room is part of the ventilation system.
     * Used by the Game class to decide how movement rules apply.
     */
    private boolean isVent;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates a Room with the given name and description.
     * All lists start empty; use addExit / addItem / addNPC to populate.
     *
     * @param name        the room's display name
     * @param description the room's look description
     * @param isVent      true if this room is inside the ventilation system
     */
    public Room(String name, String description, boolean isVent)
    {
        this.name        = name;
        this.description = description;
        this.isVent      = isVent;
        exits            = new ArrayList<String>();
        items            = new ArrayList<Item>();
        npcs             = new ArrayList<NPC>();
    }

    // -------------------------------------------------------
    // Exit management
    // -------------------------------------------------------

    /**
     * Adds a directional exit in "Direction:Destination" format.
     * Example: addExit("north:Cafeteria")
     *
     * @param exitString the exit string (case-insensitive direction)
     */
    public void addExit(String exitString)
    {
        exits.add(exitString);
    }

    /**
     * Given a direction string (e.g. "north"), returns the
     * destination room name, or null if no such exit exists.
     *
     * Parsing logic uses String.split(":") — AP CSA String method.
     *
     * @param direction the travel direction typed by the player
     * @return destination room name, or null
     */
    public String getDestination(String direction)
    {
        for (String exit : exits)
        {
            // Split "Direction:Destination" on the colon
            String[] parts = exit.split(":");
            if (parts[0].equalsIgnoreCase(direction))
            {
                return parts[1];   // return the destination name
            }
        }
        return null;   // no exit in that direction
    }

    /**
     * Prints all available exits in this room to the console.
     * If no exits exist, a message is shown instead.
     */
    public void displayExits()
    {
        if (exits.size() == 0)
        {
            System.out.println("There are no obvious exits.");
            return;
        }
        System.out.println("Available paths:");
        for (String exit : exits)
        {
            String[] parts = exit.split(":");
            System.out.println("  " + parts[0] + "  ->  " + parts[1]);
        }
    }

    // -------------------------------------------------------
    // Item management
    // -------------------------------------------------------

    /**
     * Places an item on the floor of this room.
     *
     * @param item the Item to add
     */
    public void addItem(Item item)
    {
        items.add(item);
    }

    /**
     * Removes and returns the first item whose name matches.
     * Uses index-based remove(i) — avoids index-shifting bugs
     * (an AP CSA exam trap).
     *
     * @param itemName the item name to search for (case-insensitive)
     * @return the removed Item, or null if not found
     */
    public Item removeItem(String itemName)
    {
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).getName().equalsIgnoreCase(itemName))
            {
                Item found = items.get(i);
                items.remove(i);
                return found;
            }
        }
        return null;
    }

    /**
     * Returns the item with the given name without removing it, or null.
     *
     * @param itemName item name (case-insensitive)
     * @return matching Item or null
     */
    public Item getItem(String itemName)
    {
        for (Item item : items)
        {
            if (item.getName().equalsIgnoreCase(itemName))
            {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns true if this room contains the named item.
     *
     * @param itemName name to check
     * @return true if found
     */
    public boolean hasItem(String itemName)
    {
        return getItem(itemName) != null;
    }

    // -------------------------------------------------------
    // NPC management
    // -------------------------------------------------------

    /**
     * Adds an NPC to this room.
     *
     * @param npc the NPC to add
     */
    public void addNPC(NPC npc)
    {
        npcs.add(npc);
    }

    /**
     * Returns the NPC with the given name (case-insensitive), or null.
     *
     * @param npcName the NPC's name
     * @return matching NPC or null
     */
    public NPC getNPC(String npcName)
    {
        for (NPC npc : npcs)
        {
            if (npc.getName().equalsIgnoreCase(npcName))
            {
                return npc;
            }
        }
        return null;
    }

    // -------------------------------------------------------
    // Look / display
    // -------------------------------------------------------

    /**
     * Prints the full room description, its items, and its NPCs.
     * Called when the player types "look".
     */
    public void look()
    {
        System.out.println("=== " + name.toUpperCase() + " ===");
        System.out.println(description);

        // List items on the floor
        if (items.size() > 0)
        {
            System.out.println("\nYou see the following items:");
            for (Item item : items)
            {
                System.out.println("  - " + item.getName());
            }
        }

        // List NPCs present
        if (npcs.size() > 0)
        {
            System.out.println("\nPeople here:");
            for (NPC npc : npcs)
            {
                System.out.println("  - " + npc.getName());
            }
        }
    }

    // -------------------------------------------------------
    // Getters
    // -------------------------------------------------------

    /**
     * Returns the room's display name.
     * @return name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the room's description.
     * @return description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Returns true if this room is inside the ventilation system.
     * @return isVent
     */
    public boolean isVent()
    {
        return isVent;
    }

    /**
     * Returns the list of items currently in this room.
     * Used by the Game class when the player picks something up.
     * @return items ArrayList
     */
    public ArrayList<Item> getItems()
    {
        return items;
    }

    /**
     * Returns the list of NPCs currently in this room.
     * @return npcs ArrayList
     */
    public ArrayList<NPC> getNpcs()
    {
        return npcs;
    }
}
