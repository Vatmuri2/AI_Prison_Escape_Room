import java.util.ArrayList;

/**
 * Base class representing a location in the prison.
 * Each room has a name, description, items, NPCs, and connected paths.
 * Each room also records whether it allows entering and/or exiting the vents.
 */
public class Room {

    /** The display name of this room. */
    private String name;

    /** The narrative description shown when the player looks around. */
    private String description;

    /** Items currently present in this room. */
    private ArrayList<Item> items;

    /** NPCs currently present in this room. */
    private ArrayList<NPC> npcs;

    /** Names of rooms reachable via normal walking from this room. */
    private ArrayList<String> connectedRooms;

    /** Whether the player can enter the vents from this room. */
    private boolean canEnterVent;

    /** Whether the player can exit the vents into this room. */
    private boolean canExitVent;

    /**
     * Constructs a Room with the given name and description.
     *
     * @param name        the display name of this room
     * @param description the narrative description of this room
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<Item>();
        this.npcs = new ArrayList<NPC>();
        this.connectedRooms = new ArrayList<String>();
        this.canEnterVent = false;
        this.canExitVent = false;
    }

    /**
     * Returns the name of this room.
     *
     * @return the room name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a walking connection to another room by name.
     *
     * @param roomName the name of the connected room
     */
    public void addConnection(String roomName) {
        connectedRooms.add(roomName);
    }

    /**
     * Sets the vent access rules for this room.
     *
     * @param canEnter true if the player may enter the vents from this room
     * @param canExit  true if the player may exit the vents into this room
     */
    public void setVentAccess(boolean canEnter, boolean canExit) {
        this.canEnterVent = canEnter;
        this.canExitVent = canExit;
    }

    /**
     * Returns whether the player may enter the vents from this room.
     *
     * @return true if vent entry is allowed
     */
    public boolean canEnterVent() {
        return canEnterVent;
    }

    /**
     * Returns whether the player may exit the vents into this room.
     *
     * @return true if vent exit is allowed
     */
    public boolean canExitVent() {
        return canExitVent;
    }

    /**
     * Prints a full description of this room, including its narrative text,
     * any items visible, and any NPCs present.
     */
    public void describe() {
        System.out.println();
        System.out.println("-- " + name.toUpperCase() + " --");
        System.out.println(description);

        if (items.size() > 0) {
            System.out.println();
            System.out.println("  Items you can see:");
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.println("    - " + item.getName() + " : " + item.getShortDesc());
            }
        }

        if (npcs.size() > 0) {
            System.out.println();
            System.out.println("  People here:");
            for (int i = 0; i < npcs.size(); i++) {
                NPC npc = npcs.get(i);
                System.out.println("    - " + npc.getName() + " : " + npc.getShortDesc());
            }
        }

        System.out.println();
        System.out.println("  Type 'paths' to see where you can go, or 'look' to refresh.");
    }

    /**
     * Displays the paths available from this room, distinguishing between
     * walking paths and ventilation exits.
     *
     * @param inVent whether the player is currently inside the ventilation duct
     */
    public void showPaths(boolean inVent) {
        System.out.println();
        if (inVent) {
            System.out.println("  You are in the ventilation duct.");
            System.out.println("  Use 'crawl [room]' to move to:");
            System.out.println("    - Letter Room (safe)");
            System.out.println("    - Cafeteria (DANGER: Sora AI patrols)");
            System.out.println("    - Warden's Office (safe only during the riot)");
            System.out.println("    - Prison Room (backtrack)");
            System.out.println("  Use 'vent' to exit the ducts into the current room.");
        } else {
            if (connectedRooms.size() == 0) {
                System.out.println("  No obvious walking exits from here.");
            } else {
                System.out.println("  You can walk to:");
                for (int i = 0; i < connectedRooms.size(); i++) {
                    System.out.println("    - " + connectedRooms.get(i));
                }
            }
            if (canEnterVent) {
                System.out.println("    - Ventilation System (type 'vent')");
            }
        }
    }

    /**
     * Returns whether this room has a walking path to the named destination.
     *
     * @param roomName the name of the destination room
     * @return true if connected via a normal path, false otherwise
     */
    public boolean hasPath(String roomName) {
        for (int i = 0; i < connectedRooms.size(); i++) {
            if (connectedRooms.get(i).equalsIgnoreCase(roomName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves an item in this room by name (case-insensitive, partial match allowed).
     *
     * @param name the name of the item to find
     * @return the matching Item, or null if not found
     */
    public Item getItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                return items.get(i);
            }
        }
        return null;
    }

    /**
     * Removes the first matching item from this room by name.
     *
     * @param name the name of the item to remove
     */
    public void removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                items.remove(i);
                return;
            }
        }
    }

    /**
     * Adds an item to this room (e.g., when a player drops something).
     *
     * @param item the Item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Adds an NPC to this room.
     *
     * @param npc the NPC to add
     */
    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    /**
     * Retrieves an NPC in this room by name or alias (case-insensitive).
     *
     * @param name the name or alias of the NPC to find
     * @return the matching NPC, or null if not found
     */
    public NPC getNPC(String name) {
        for (int i = 0; i < npcs.size(); i++) {
            if (npcs.get(i).matchesName(name)) {
                return npcs.get(i);
            }
        }
        return null;
    }

    /**
     * Returns the first NPC in this room, used for context-sensitive trading.
     *
     * @return the first NPC, or null if none are present
     */
    public NPC getFirstNPC() {
        if (npcs.size() == 0) {
            return null;
        }
        return npcs.get(0);
    }
}
