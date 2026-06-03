import java.util.HashMap;
import java.util.Map;

/**
 * Factory class responsible for creating and caching all Room instances.
 * Rooms are created once and reused throughout the game to preserve their state
 * (items removed by the player remain removed). Call {@link #initRooms()} before
 * starting the game, then retrieve rooms with the individual getter methods.
 */
public class RoomFactory {

    /** Cache of all constructed Room instances keyed by room name. */
    private static final Map<String, Room> rooms = new HashMap<>();

    /**
     * Initializes all rooms, populating them with their items and NPCs.
     * Should be called once at game startup. Calling it again resets room state.
     */
    public static void initRooms() {
        rooms.clear();
        rooms.put("Prison Room",            buildPrisonRoom());
        rooms.put("Main Ventilation Duct",  buildVentilationSystem());
        rooms.put("Cafeteria",              buildCafeteria());
        rooms.put("Letter Room",            buildLetterRoom());
        rooms.put("Library",               buildLibrary());
        rooms.put("Yard",                  buildYard());
        rooms.put("Warden's Office",        buildWardensOffice());
    }

    /**
     * Retrieves a Room by its canonical name.
     *
     * @param name the canonical room name (e.g., "Prison Room")
     * @return the Room instance, or null if the name is unrecognized
     */
    public static Room getRoom(String name) {
        if (rooms.isEmpty()) initRooms();
        return rooms.get(name);
    }

    /**
     * Returns the Prison Room — the player's starting location.
     *
     * @return the Prison Room instance
     */
    public static Room getPrisonRoom() {
        if (rooms.isEmpty()) initRooms();
        return rooms.get("Prison Room");
    }

    /**
     * Returns the Ventilation System room.
     *
     * @return the Main Ventilation Duct instance
     */
    public static Room getVentilationSystem() {
        if (rooms.isEmpty()) initRooms();
        return rooms.get("Main Ventilation Duct");
    }

    /**
     * Returns the Cafeteria room.
     *
     * @return the Cafeteria instance
     */
    public static Room getCafeteria() {
        if (rooms.isEmpty()) initRooms();
        return rooms.get("Cafeteria");
    }

    /**
     * Returns the Letter Room.
     *
     * @return the Letter Room instance
     */
    public static Room getLetterRoom() {
        if (rooms.isEmpty()) initRooms();
        return rooms.get("Letter Room");
    }

    /**
     * Returns the Library room.
     *
     * @return the Library instance
     */
    public static Room getLibrary() {
        if (rooms.isEmpty()) initRooms();
        return rooms.get("Library");
    }

    /**
     * Returns the Yard room.
     *
     * @return the Yard instance
     */
    public static Room getYard() {
        if (rooms.isEmpty()) initRooms();
        return rooms.get("Yard");
    }

    /**
     * Returns the Warden's Office room.
     *
     * @return the Warden's Office instance
     */
    public static Room getWardensOffice() {
        if (rooms.isEmpty()) initRooms();
        return rooms.get("Warden's Office");
    }

    // ────────────────────────────────────────────────────────────
    //  Room builders
    // ────────────────────────────────────────────────────────────

    /**
     * Builds and returns a fully initialized Prison Room with items and walking paths.
     *
     * @return the configured Prison Room
     */
    private static Room buildPrisonRoom() {
        Room r = new Room("Prison Room",
            Colors.WHITE + "Your cell. Concrete walls, a rusted cot, and the faint smell of despair.\n" +
            "  A " + Colors.YELLOW + "ventilation duct" + Colors.WHITE + " is mounted on the far wall — screwed shut.\n" +
            "  This is where it all begins.") {
            @Override public boolean hasVentAccess() { return true; }
        };
        r.connectedRooms.add("Cafeteria");
        r.items.add(ItemFactory.coin());
        r.items.add(ItemFactory.wrench());
        r.items.add(ItemFactory.fork());
        return r;
    }

    /**
     * Builds and returns the Ventilation System room. This room has no walking paths
     * and cannot be exited by normal movement.
     *
     * @return the configured Ventilation System room
     */
    private static Room buildVentilationSystem() {
        Room r = new Room("Main Ventilation Duct",
            Colors.GRAY + "Cramped metal tunnels that snake through the prison walls.\n" +
            "  Faint sounds of movement echo from several directions.\n" +
            "  You can feel a " + Colors.RED + "painted brick" + Colors.GRAY + " sitting on a ledge nearby.\n" +
            "  Type " + Colors.WHITE + "vent" + Colors.GRAY + " to drop into a room, or " +
            Colors.WHITE + "paths" + Colors.GRAY + " to see your options.") {
            @Override public boolean hasVentAccess() { return false; }
        };
        r.items.add(ItemFactory.redPaintedBrick());
        return r;
    }

    /**
     * Builds and returns the Cafeteria room with Billy the NPC and its walking connections.
     *
     * @return the configured Cafeteria room
     */
    private static Room buildCafeteria() {
        Room r = new Room("Cafeteria",
            Colors.WHITE + "Rows of long metal tables bolted to the floor. The food smells synthetic.\n" +
            "  A figure in a chef's apron — " + Colors.CYAN + "Billy" + Colors.WHITE +
            " — watches you from behind the serving counter.\n" +
            "  " + Colors.RED + "⚠ DO NOT use the vents here." + Colors.WHITE +
            " Sora AI's sensors are all over this room.") {
            @Override public boolean hasVentAccess() { return false; }
        };
        r.connectedRooms.add("Prison Room");
        r.connectedRooms.add("Letter Room");
        r.connectedRooms.add("Yard");
        r.npcs.add(new Billy());
        return r;
    }

    /**
     * Builds and returns the Letter Room with Jilly the NPC and its connections.
     *
     * @return the configured Letter Room
     */
    private static Room buildLetterRoom() {
        Room r = new Room("Letter Room",
            Colors.WHITE + "A small room filled with stacked mail crates. Letters from the outside world\n" +
            "  sit unopened in bins. A woman — " + Colors.CYAN + "Jilly" + Colors.WHITE +
            " — sorts them with robotic efficiency.\n" +
            "  There is a vent grate on the wall. This room is your safest vent hub.") {
            @Override public boolean hasVentAccess() { return true; }
        };
        r.connectedRooms.add("Cafeteria");
        r.connectedRooms.add("Library");
        r.npcs.add(new Jilly());
        return r;
    }

    /**
     * Builds and returns the Library room with the Bookworm NPC.
     * The library cannot be entered from the vents.
     *
     * @return the configured Library room
     */
    private static Room buildLibrary() {
        Room r = new Room("Library",
            Colors.WHITE + "Shelves of decaying books line the walls. The AI deemed knowledge\n" +
            "  a threat but kept this place as a trophy room.\n" +
            "  A hunched figure — the " + Colors.CYAN + "Bookworm" + Colors.WHITE +
            " — reads in the corner, ignoring everything.\n" +
            "  " + Colors.GRAY + "(You can exit through the vent, but cannot enter from it.)") {
            @Override public boolean hasVentAccess() { return true; }
        };
        r.connectedRooms.add("Yard");
        r.connectedRooms.add("Letter Room");
        r.npcs.add(new Bookworm());
        return r;
    }

    /**
     * Builds and returns the Yard room with the Buff Dude, Scrawny Guy, and the Fence.
     * The yard has no vent access at all.
     *
     * @return the configured Yard room
     */
    private static Room buildYard() {
        Room r = new Room("Yard",
            Colors.WHITE + "A concrete exercise yard under a simulated sky. Three things catch your eye:\n" +
            "  " + Colors.CYAN + "Big Buff Dude" + Colors.WHITE +
            " — lifting weights in the corner, seems approachable.\n" +
            "  " + Colors.RED + "Scrawny Guy" + Colors.WHITE +
            " — leaning against the wall. Something feels very wrong about him.\n" +
            "  A tall " + Colors.YELLOW + "Fence" + Colors.WHITE +
            " — topped with razor wire, the obvious escape route. Maybe too obvious.") {
            @Override public boolean hasVentAccess() { return false; }
        };
        r.connectedRooms.add("Cafeteria");
        r.connectedRooms.add("Library");
        r.npcs.add(new BuffDude());
        r.npcs.add(new ScrawnyGuy());
        return r;
    }

    /**
     * Builds and returns the Warden's Office room. It can only be safely entered
     * via the vents during an active prison riot.
     *
     * @return the configured Warden's Office room
     */
    private static Room buildWardensOffice() {
        Room r = new Room("Warden's Office",
            Colors.WHITE + "A sterile, minimalist office. Screens cover every wall, displaying\n" +
            "  prison-wide surveillance feeds. In the corner, barely visible beneath\n" +
            "  a holographic tarp, sits " + Colors.GREEN + Colors.BOLD + "THE TIME MACHINE" +
            Colors.WHITE + ".\n" +
            "  The keypad glows. Type " + Colors.GREEN + "use time machine" + Colors.WHITE + " to interact with it.") {
            @Override public boolean hasVentAccess() { return false; }
        };
        r.npcs.add(new Warden());
        return r;
    }
}