import java.util.*;

/**
 * Abstract base class representing a location in the prison.
 * Each room has a name, description, items, NPCs, and connected paths.
 * Subclasses define specific room behavior and descriptions.
 */
public abstract class Room {

    /** The display name of this room. */
    protected String name;

    /** The narrative description shown when the player looks around. */
    protected String description;

    /** Items currently present in this room. */
    protected List<Item> items;

    /** NPCs currently present in this room. */
    protected List<NPC> npcs;

    /** Names of rooms reachable via normal walking from this room. */
    protected List<String> connectedRooms;

    /**
     * Constructs a Room with the given name and description.
     *
     * @param name        the display name of this room
     * @param description the narrative description of this room
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
        this.connectedRooms = new ArrayList<>();
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
     * Prints a full description of this room, including its narrative text,
     * any items visible, and any NPCs present.
     */
    public void describe() {
        System.out.println();
        UI.sectionHeader("── " + name.toUpperCase() + " ──");
        UI.typewrite(description, 16);

        if (!items.isEmpty()) {
            System.out.println();
            System.out.println(Colors.YELLOW + "  Items you can see:");
            for (Item item : items) {
                System.out.println(Colors.WHITE + "    • " + item.getName() +
                    Colors.GRAY + " — " + item.getShortDesc());
            }
        }

        if (!npcs.isEmpty()) {
            System.out.println();
            System.out.println(Colors.CYAN + "  People here:");
            for (NPC npc : npcs) {
                System.out.println(Colors.WHITE + "    • " + npc.getName() +
                    Colors.GRAY + " — " + npc.getShortDesc());
            }
        }

        System.out.println();
        System.out.print(Colors.GRAY + "  Type " + Colors.WHITE + "paths" +
            Colors.GRAY + " to see where you can go, or " +
            Colors.WHITE + "look" + Colors.GRAY + " to refresh." + Colors.RESET);
        System.out.println();
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
            System.out.println(Colors.CYAN + "  You are in the ventilation duct.");
            System.out.println(Colors.GRAY + "  Use " + Colors.WHITE + "crawl [room]" + Colors.GRAY + " to move to:");
            System.out.println(Colors.WHITE + "    • " + Colors.GRAY + "Letter Room (safe)");
            System.out.println(Colors.WHITE + "    • " + Colors.RED + "Cafeteria " +
                Colors.GRAY + "(⚠ Sora AI patrols — dangerous)");
            System.out.println(Colors.WHITE + "    • " + Colors.YELLOW + "Warden's Office " +
                Colors.GRAY + "(safe only during riot)");
            System.out.println(Colors.WHITE + "    • " + Colors.GRAY + "Prison Room (backtrack)");
            System.out.println(Colors.GRAY + "  Use " + Colors.WHITE + "vent" + Colors.GRAY + " to exit the ducts into the current room.");
        } else {
            if (connectedRooms.isEmpty()) {
                System.out.println(Colors.GRAY + "  No obvious walking exits from here.");
            } else {
                System.out.println(Colors.CYAN + "  You can walk to:");
                for (String room : connectedRooms) {
                    System.out.println(Colors.WHITE + "    • " + Colors.GRAY + room);
                }
            }
            if (hasVentAccess()) {
                System.out.println(Colors.WHITE + "    • " + Colors.GRAY +
                    "Ventilation System (type " + Colors.WHITE + "vent" + Colors.GRAY + ")");
            }
        }
        System.out.println(Colors.RESET);
    }

    /**
     * Returns whether this room has access to the ventilation system.
     * Override in subclasses to disable vent access.
     *
     * @return true by default; false for rooms without vent access
     */
    public boolean hasVentAccess() {
        return true;
    }

    /**
     * Returns whether this room has a walking path to the named destination.
     *
     * @param roomName the name of the destination room
     * @return true if connected via a normal path, false otherwise
     */
    public boolean hasPath(String roomName) {
        for (String r : connectedRooms) {
            if (r.equalsIgnoreCase(roomName)) return true;
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
        for (Item item : items) {
            if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Removes an item from this room by name.
     *
     * @param name the name of the item to remove
     */
    public void removeItem(String name) {
        items.removeIf(i -> i.getName().toLowerCase().contains(name.toLowerCase()));
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
     * Retrieves an NPC in this room by name or alias (case-insensitive).
     *
     * @param name the name or alias of the NPC to find
     * @return the matching NPC, or null if not found
     */
    public NPC getNPC(String name) {
        for (NPC npc : npcs) {
            if (npc.matchesName(name)) return npc;
        }
        return null;
    }

    /**
     * Returns the first NPC in this room, used for context-sensitive trading.
     *
     * @return the first NPC, or null if none are present
     */
    public NPC getFirstNPC() {
        return npcs.isEmpty() ? null : npcs.get(0);
    }
}