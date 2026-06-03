import java.util.HashMap;

/**
 * Factory class responsible for creating and caching all Room instances.
 * Rooms are created once and reused throughout the game to preserve their state
 * (items removed by the player remain removed). Call {@link #initRooms()} before
 * starting the game, then retrieve rooms with the getter methods.
 */
public class RoomFactory {

    /** Cache of all constructed Room instances keyed by room name. */
    private static HashMap<String, Room> rooms = new HashMap<String, Room>();

    /**
     * Initializes all rooms, populating them with their items and NPCs.
     * Should be called once at game startup. Calling it again resets room state.
     */
    public static void initRooms() {
        rooms.clear();
        rooms.put("Prison Room", buildPrisonRoom());
        rooms.put("Ventilation System", buildVentilationSystem());
        rooms.put("Cafeteria", buildCafeteria());
        rooms.put("Letter Room", buildLetterRoom());
        rooms.put("Library", buildLibrary());
        rooms.put("Yard", buildYard());
        rooms.put("Warden's Office", buildWardensOffice());
    }

    /**
     * Retrieves a Room by its canonical name.
     *
     * @param name the canonical room name (e.g., "Prison Room")
     * @return the Room instance, or null if the name is unrecognized
     */
    public static Room getRoom(String name) {
        if (rooms.isEmpty()) {
            initRooms();
        }
        return rooms.get(name);
    }

    /**
     * Returns the Prison Room, the player's starting location.
     *
     * @return the Prison Room instance
     */
    public static Room getPrisonRoom() {
        return getRoom("Prison Room");
    }

    /**
     * Returns the Ventilation System room.
     *
     * @return the Ventilation System instance
     */
    public static Room getVentilationSystem() {
        return getRoom("Ventilation System");
    }

    /**
     * Returns the Cafeteria room.
     *
     * @return the Cafeteria instance
     */
    public static Room getCafeteria() {
        return getRoom("Cafeteria");
    }

    /**
     * Returns the Letter Room.
     *
     * @return the Letter Room instance
     */
    public static Room getLetterRoom() {
        return getRoom("Letter Room");
    }

    /**
     * Returns the Library room.
     *
     * @return the Library instance
     */
    public static Room getLibrary() {
        return getRoom("Library");
    }

    /**
     * Returns the Yard room.
     *
     * @return the Yard instance
     */
    public static Room getYard() {
        return getRoom("Yard");
    }

    /**
     * Returns the Warden's Office room.
     *
     * @return the Warden's Office instance
     */
    public static Room getWardensOffice() {
        return getRoom("Warden's Office");
    }

    /**
     * Builds and returns a fully initialized Prison Room with items and walking paths.
     *
     * @return the configured Prison Room
     */
    private static Room buildPrisonRoom() {
        Room r = new Room("Prison Room",
            "Your cell. Concrete walls, a rusted cot, and the faint smell of despair.\n"
            + "  A ventilation duct is mounted on the far wall, screwed shut.\n"
            + "  This is where it all begins.");
        r.setVentAccess(true, true);
        r.addConnection("Cafeteria");
        r.addItem(ItemFactory.coin());
        r.addItem(ItemFactory.wrench());
        r.addItem(ItemFactory.fork());
        return r;
    }

    /**
     * Builds and returns the Ventilation System room. This room has no walking
     * paths and cannot be exited by normal movement.
     *
     * @return the configured Ventilation System room
     */
    private static Room buildVentilationSystem() {
        Room r = new Room("Ventilation System",
            "Cramped metal tunnels that snake through the prison walls.\n"
            + "  Faint sounds of movement echo from several directions.\n"
            + "  A red painted brick sits on a ledge nearby.\n"
            + "  Type 'paths' to see where you can crawl, or 'vent' to drop into the room below.");
        r.setVentAccess(false, false);
        r.addItem(ItemFactory.redPaintedBrick());
        return r;
    }

    /**
     * Builds and returns the Cafeteria room with Billy the NPC and its walking connections.
     *
     * @return the configured Cafeteria room
     */
    private static Room buildCafeteria() {
        Room r = new Room("Cafeteria",
            "Rows of long metal tables bolted to the floor. The food smells synthetic.\n"
            + "  A figure in a chef's apron, Billy, watches you from behind the counter.\n"
            + "  WARNING: do not use the vents here. Sora AI's sensors are everywhere.");
        r.setVentAccess(false, false);
        r.addConnection("Prison Room");
        r.addConnection("Letter Room");
        r.addConnection("Yard");
        r.addNPC(new Billy());
        return r;
    }

    /**
     * Builds and returns the Letter Room with Jilly the NPC and its connections.
     *
     * @return the configured Letter Room
     */
    private static Room buildLetterRoom() {
        Room r = new Room("Letter Room",
            "A small room filled with stacked mail crates. Letters from the outside\n"
            + "  world sit unopened in bins. A woman, Jilly, sorts them with mechanical\n"
            + "  efficiency. There is a vent grate on the wall. This is your safest vent hub.");
        r.setVentAccess(true, true);
        r.addConnection("Cafeteria");
        r.addConnection("Library");
        r.addNPC(new Jilly());
        return r;
    }

    /**
     * Builds and returns the Library room with the Bookworm NPC.
     * The library can be exited into from the vents, but not entered from it.
     *
     * @return the configured Library room
     */
    private static Room buildLibrary() {
        Room r = new Room("Library",
            "Shelves of decaying books line the walls. The AI deemed knowledge a\n"
            + "  threat but kept this place as a trophy room. A hunched figure, the\n"
            + "  Bookworm, reads in the corner, ignoring everything.\n"
            + "  (You can exit through the vent, but cannot enter the vent from here.)");
        r.setVentAccess(false, true);
        r.addConnection("Yard");
        r.addConnection("Letter Room");
        r.addNPC(new Bookworm());
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
            "A concrete exercise yard under a simulated sky. Three things catch your eye:\n"
            + "  Big Buff Dude, lifting weights in the corner, seems approachable.\n"
            + "  Scrawny Guy, leaning against the wall. Something feels very wrong about him.\n"
            + "  A tall Fence, topped with razor wire, the obvious escape route.\n"
            + "  (Type 'climb fence' to attempt an escape.)");
        r.setVentAccess(false, false);
        r.addConnection("Cafeteria");
        r.addConnection("Library");
        r.addNPC(new BuffDude());
        r.addNPC(new ScrawnyGuy());
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
            "A sterile, minimalist office. Screens cover every wall, displaying\n"
            + "  prison-wide surveillance feeds. In the corner, beneath a holographic\n"
            + "  tarp, sits THE TIME MACHINE.\n"
            + "  The keypad glows. Type 'use time machine' to interact with it.");
        r.setVentAccess(true, true);
        r.addNPC(new Warden());
        return r;
    }
}
