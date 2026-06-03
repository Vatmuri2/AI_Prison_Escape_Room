import java.util.ArrayList;
import java.util.Scanner;

/**
 * Game.java
 *
 * The central engine of the AI Prison Escape game.
 * Responsibilities:
 *   - Builds and connects all Room objects.
 *   - Places all Items and NPCs into their starting rooms.
 *   - Owns the Player object.
 *   - Owns all game-state boolean flags.
 *   - Runs the main game loop via play().
 *   - Parses player input and dispatches to processCommand().
 *   - Manages the 2-minute countdown timer via a background thread.
 *   - Handles all endings (good and bad) via triggerGameOver() / triggerWin().
 *
 * Data-Driven Movement:
 *   Room exits are stored as "Direction:Destination" strings.
 *   The movement code in processCommand() splits on ":" and looks
 *   up the destination room by name — meaning zero movement code
 *   needs to change when rooms are added or renamed.
 *
 * AP CSA Concepts Used:
 *   - ArrayList (room registry, command parsing)
 *   - for-each and index-based loops
 *   - String methods: split(), trim(), toLowerCase(), equalsIgnoreCase()
 *   - Boolean flags as game-state variables
 *   - Object composition (Game HAS-A Player, many Rooms, many NPCs)
 *   - Scanner for input
 *   - Thread / Runnable for the countdown timer (extension concept)
 */
public class Game
{
    // -------------------------------------------------------
    // Game-state flags  (spec-required)
    // -------------------------------------------------------

    /** True once the Big Buff Dude has started the prison riot. */
    private boolean riotStarted;

    /** True if the player currently possesses a Coin. */
    private boolean hasCoin;

    /** True if the player currently possesses the Red Painted Brick. */
    private boolean hasBrick;

    /** True if the player currently possesses the Fork. */
    private boolean hasFork;

    /** True once the player has received the Fortune Cookie from Billy. */
    private boolean hasFortuneCookie;

    /** True if the player has successfully escaped (bad ending). */
    private boolean escaped;

    /** True once any game-over condition has been triggered. */
    private boolean gameOver;

    /** True if the vent panel in the Prison Room has been unscrewed. */
    private boolean ventUnlocked;

    // -------------------------------------------------------
    // Core objects
    // -------------------------------------------------------

    /** The human player character. */
    private Player player;

    /** All rooms in the game, stored in an ArrayList for name-based look-up. */
    private ArrayList<Room> rooms;

    /** Scanner used to read player input from the console. */
    private Scanner scanner;

    // -------------------------------------------------------
    // Timer fields
    // -------------------------------------------------------

    /**
     * Start time in milliseconds, recorded when play() begins.
     * Used by the background timer thread to enforce the 2-minute limit.
     */
    private long startTimeMs;

    /** Two-minute limit in milliseconds. */
    private static final long TIME_LIMIT_MS = 360000;   // 2 minutes

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Initialises all game-state flags, builds the world,
     * and creates the Player.
     */
    public Game()
    {
        riotStarted    = false;
        hasCoin        = false;
        hasBrick       = false;
        hasFork        = false;
        hasFortuneCookie = false;
        escaped        = false;
        gameOver       = false;
        ventUnlocked   = false;

        rooms   = new ArrayList<Room>();
        scanner = new Scanner(System.in);

        buildWorld();

        // Place the player in the Prison Room
        Room prisonRoom = getRoomByName("Prison Room");
        player = new Player(prisonRoom);
    }

    // -------------------------------------------------------
    // World construction
    // -------------------------------------------------------

    /**
     * Creates every Room, populates them with Items and NPCs,
     * and wires up all exits.
     *
     * Exit format:  "Direction:DestinationRoomName"
     * The direction token is what the player types (e.g. "cafeteria").
     * Using room names as destinations makes movement data-driven.
     */
    private void buildWorld()
    {
        // --- Create all rooms ---

        Room prisonRoom = new Room(
            "Prison Room",
            "Your cold, grey starting cell. Fluorescent lights hum overhead.\n"
            + "A vent panel sits low on the north wall — it looks like it could\n"
            + "be unscrewed with the right tool.",
            false
        );

        Room ventSystem = new Room(
            "Ventilation System",
            "A cramped metal duct system that snakes through the prison.\n"
            + "Your knees scrape against the corrugated floor.\n"
            + "You can hear the cafeteria through the grate to the west — DO NOT exit there.\n"
            + "Possible exits lead toward the Letter Room and the Warden's Office.",
            true    // isVent = true
        );

        Room cafeteria = new Room(
            "Cafeteria",
            "A long hall of metal tables bolted to the floor.\n"
            + "The smell of synthetic protein paste fills the air.\n"
            + "Billy works behind the serving counter.",
            false
        );

        Room letterRoom = new Room(
            "Letter Room",
            "A small room lined with pigeonholes stuffed with mail.\n"
            + "Jilly sits at a desk in the corner, surrounded by envelopes.\n"
            + "A vent panel on the ceiling leads into the ventilation system.",
            false
        );

        Room library = new Room(
            "Library",
            "Floor-to-ceiling shelves packed with crumbling books.\n"
            + "The Bookworm is buried somewhere among the stacks.\n"
            + "NOTE: There is a vent grate up high, but it is SEALED from this side.",
            false
        );

        Room yard = new Room(
            "Yard",
            "An outdoor concrete exercise area ringed by a tall chain-link fence.\n"
            + "Nano-banana security drones patrol silently above the fence line.\n"
            + "The Big Buff Dude is lifting weights in the corner.\n"
            + "A scrawny guy leans against the far wall — he's staring at you.",
            false
        );

        Room wardensOffice = new Room(
            "Warden's Office",
            "A spotless, clinical office dominated by surveillance monitors.\n"
            + "In the centre of the room sits a sleek device covered in dials\n"
            + "and a glowing keypad — the Time Machine.\n"
            + "If the Warden is present, you will be caught immediately.",
            false
        );

        // ---- Wire up NORMAL (walking) exits ----
        // Format: "directionKeyword:DestinationRoomName"
        // Players type:  enter/walk/move <directionKeyword>

        // Prison Room exits
        prisonRoom.addExit("cafeteria:Cafeteria");
        // Vent exit added dynamically only after ventUnlocked = true (handled in movement code)

        // Cafeteria exits
        cafeteria.addExit("prison room:Prison Room");
        cafeteria.addExit("letter room:Letter Room");
        cafeteria.addExit("yard:Yard");
        // vent exits from cafeteria are FORBIDDEN (handled in processCommand)

        // Letter Room exits
        letterRoom.addExit("cafeteria:Cafeteria");
        letterRoom.addExit("library:Library");
        letterRoom.addExit("vent:Ventilation System");   // letter room has full vent access

        // Library exits
        library.addExit("yard:Yard");
        library.addExit("letter room:Letter Room");
        // library vent is sealed — no vent exit added

        // Yard exits
        yard.addExit("cafeteria:Cafeteria");
        yard.addExit("library:Library");
        // fence is handled as a special action, not a normal exit

        // Ventilation System exits
        // Exits toward Letter Room and Warden's Office are safe.
        // Exit toward Cafeteria triggers Sora AI catch — handled in processCommand.
        ventSystem.addExit("letter room:Letter Room");
        ventSystem.addExit("warden's office:Warden's Office");
        ventSystem.addExit("prison room:Prison Room");
        ventSystem.addExit("cafeteria:Cafeteria");   // listed but FORBIDDEN — checked in code

        // Warden's Office — only reachable via vent (no walking exit)
        // No outbound vent exit needed; player must retrace through vent

        // ---- Place Items ----

        prisonRoom.addItem(new Coin());
        prisonRoom.addItem(new Wrench());
        prisonRoom.addItem(new Fork());

        ventSystem.addItem(new RedPaintedBrick());

        // Billy's fortune cookie is given during his trade, not placed in a room.

        // ---- Place NPCs ----

        cafeteria.addNPC(new Billy("UNLOCK-9X"));     // "UNLOCK-9X" is the Time Machine code
        letterRoom.addNPC(new Jilly());
        library.addNPC(new Bookworm());
        yard.addNPC(new BuffDude());
        yard.addNPC(new ScrawnyGuy());
        wardensOffice.addNPC(new Warden());

        // ---- Register all rooms ----

        rooms.add(prisonRoom);
        rooms.add(ventSystem);
        rooms.add(cafeteria);
        rooms.add(letterRoom);
        rooms.add(library);
        rooms.add(yard);
        rooms.add(wardensOffice);
    }

    // -------------------------------------------------------
    // Room look-up
    // -------------------------------------------------------

    /**
     * Searches the rooms ArrayList for a Room whose name matches
     * (case-insensitive).  Returns null if not found.
     *
     * Linear search with for-each — AP CSA ArrayList traversal pattern.
     *
     * @param name the room name to find
     * @return matching Room or null
     */
    private Room getRoomByName(String name)
    {
        for (Room room : rooms)
        {
            if (room.getName().equalsIgnoreCase(name))
            {
                return room;
            }
        }
        return null;
    }

    // -------------------------------------------------------
    // Main game loop
    // -------------------------------------------------------

    /**
     * Starts and runs the game from introduction to conclusion.
     *
     * Flow:
     *   1. Print premise and command list.
     *   2. Start the 2-minute countdown timer on a background thread.
     *   3. Show the starting room.
     *   4. Loop: read input -> processCommand -> check gameOver.
     */
    public void play()
    {
        printIntro();
        startTimer();

        // Show the starting room immediately
        player.getCurrentRoom().look();

        // Main loop
        while (!gameOver)
        {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.length() == 0)
            {
                continue;   // ignore empty input
            }

            processCommand(input.toLowerCase());
        }

        scanner.close();
    }

    // -------------------------------------------------------
    // Intro / help
    // -------------------------------------------------------

    /**
     * Prints the game premise and the full command list.
     * Both pieces of text are required by the spec.
     */
    private void printIntro()
    {
        System.out.println("============================================================");
        System.out.println("              AI PRISON ESCAPE");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("In the future, artificial intelligence has advanced beyond");
        System.out.println("human control after you gave AI a human body, allowing it");
        System.out.println("to roam free in the world. The AI eventually took over the");
        System.out.println("world, enslaving humanity and imprisoning survivors.");
        System.out.println();
        System.out.println("You begin inside a prison cell in this dystopian future.");
        System.out.println("Your objective is to travel back in time to stop the AI");
        System.out.println("uprising before it begins.");
        System.out.println();
        System.out.println("The prison contains multiple rooms connected by both normal");
        System.out.println("pathways and a ventilation system. You must explore");
        System.out.println("**carefully**, collect items, talk to NPCs, and avoid");
        System.out.println("deadly mistakes.");
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("COMMANDS:");
        System.out.println("  look                  - See what is around you");
        System.out.println("  inventory             - See items you are carrying");
        System.out.println("  paths                 - Show available paths from here");
        System.out.println("  vent                  - Enter or exit the ventilation system");
        System.out.println("  enter <room>          - Walk into a connected room");
        System.out.println("  walk <room>           - Walk into a connected room");
        System.out.println("  move <room>           - Walk into a connected room");
        System.out.println("  pickup <item>         - Pick up an item in the room");
        System.out.println("  drop <item>           - Drop an item from your inventory");
        System.out.println("  use <item>            - Use or examine an item");
        System.out.println("  talk to <name>        - Talk to an NPC");
        System.out.println("  interact <name>       - Talk to an NPC");
        System.out.println("  trade <item>          - Offer an item to an NPC here");
        System.out.println("  climb fence           - Attempt to climb the yard fence");
        System.out.println("  use time machine      - Activate the Time Machine");
        System.out.println("  commands              - Show this command list again");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.println("** WARNING: You have 2 minutes. Carrying heavy items");
        System.out.println("   drains your time faster. **");
        System.out.println();
    }

    /**
     * Prints the condensed command list again when the player
     * types "commands".
     */
    private void printCommands()
    {
        System.out.println("--- COMMANDS ---");
        System.out.println("  look / inventory / paths / vent");
        System.out.println("  enter <room> / walk <room> / move <room>");
        System.out.println("  pickup <item> / drop <item> / use <item>");
        System.out.println("  talk to <name> / interact <name> / trade <item>");
        System.out.println("  climb fence / use time machine / commands");
        System.out.println("----------------");
    }

    // -------------------------------------------------------
    // Command dispatcher
    // -------------------------------------------------------

    /**
     * Parses a single line of player input and routes it to the
     * appropriate handler.
     *
     * All input has already been .toLowerCase().trim()'d before
     * this method is called.
     *
     * AP CSA String concepts used:
     *   - startsWith()
     *   - substring()
     *   - equalsIgnoreCase()
     *   - contains()
     *
     * @param input the cleaned, lowercase player command
     */
    private void processCommand(String input)
    {
        // ---- Meta commands ----
        if (input.equals("look"))
        {
            player.getCurrentRoom().look();
        }
        else if (input.equals("inventory") || input.equals("/inventory"))
        {
            player.getInventory().displayInventory();
        }
        else if (input.equals("paths"))
        {
            handlePaths();
        }
        else if (input.equals("commands"))
        {
            printCommands();
        }

        // ---- Vent toggle ----
        else if (input.equals("vent"))
        {
            handleVent();
        }

        // ---- Movement: enter / walk / move <room> ----
        else if (input.startsWith("enter ") || input.startsWith("walk ")
                 || input.startsWith("move "))
        {
            // Extract the destination after the verb
            String destination = "";
            if (input.startsWith("enter "))
            {
                destination = input.substring(6).trim();
            }
            else if (input.startsWith("walk "))
            {
                destination = input.substring(5).trim();
            }
            else
            {
                destination = input.substring(5).trim();
            }
            handleMovement(destination);
        }

        // ---- Pick up ----
        else if (input.startsWith("pickup ") || input.startsWith("pick up "))
        {
            String itemName = input.startsWith("pickup ")
                ? input.substring(7).trim()
                : input.substring(8).trim();
            handlePickup(itemName);
        }

        // ---- Drop ----
        else if (input.startsWith("drop "))
        {
            String itemName = input.substring(5).trim();
            handleDrop(itemName);
        }

        // ---- Use item ----
        else if (input.startsWith("use "))
        {
            String itemName = input.substring(4).trim();
            handleUse(itemName);
        }

        // ---- Talk to / interact ----
        else if (input.startsWith("talk to "))
        {
            String npcName = input.substring(8).trim();
            handleTalk(npcName);
        }
        else if (input.startsWith("interact "))
        {
            String npcName = input.substring(9).trim();
            handleTalk(npcName);
        }

        // ---- Trade ----
        else if (input.startsWith("trade "))
        {
            String itemName = input.substring(6).trim();
            handleTrade(itemName);
        }

        // ---- Climb fence ----
        else if (input.equals("climb fence") || input.equals("scale fence")
                 || input.equals("climb the fence"))
        {
            handleFence();
        }

        // ---- Time Machine ----
        else if (input.equals("use time machine") || input.equals("activate time machine")
                 || input.equals("time machine"))
        {
            handleTimeMachine();
        }

        // ---- Unknown ----
        else
        {
            System.out.println("I don't understand that command. Type 'commands' for help.");
        }
    }

    // -------------------------------------------------------
    // Handler: paths
    // -------------------------------------------------------

    /**
     * Shows available paths.
     * If the player is in the vent, shows the room below (drop-down exits).
     * Otherwise shows normal room exits.
     */
    private void handlePaths()
    {
        if (player.isInVent())
        {
            System.out.println("You peer through the vent grates below you.");
            System.out.println("You can drop into:");
            player.getCurrentRoom().displayExits();
        }
        else
        {
            player.getCurrentRoom().displayExits();
        }
    }

    // -------------------------------------------------------
    // Handler: vent
    // -------------------------------------------------------

    /**
     * Toggles the player between the normal room and the
     * ventilation system.
     *
     * Rules:
     *   - Can only enter the vent from rooms that have vent access.
     *   - Prison Room vent requires ventUnlocked = true.
     *   - Library vent is sealed from the inside (cannot enter from library).
     *   - Cafeteria vent is FORBIDDEN in both directions (Sora AI).
     */
    private void handleVent()
    {
        String roomName = player.getCurrentRoom().getName();

        // --- Already inside the ventilation system -> exit to current drop room ---
        if (player.isInVent())
        {
            // The player is in VentilationSystem; "vent" exits them to the room they crawled from.
            // We need to know which room they want to drop into.
            System.out.println("You push open a vent grate and drop down.");
            System.out.println("Type 'paths' to see where you can exit, then 'enter <room>'.");
            return;
        }

        // --- Prison Room: vent requires the coin to unscrew the panel ---
        if (roomName.equalsIgnoreCase("Prison Room"))
        {
            if (!ventUnlocked)
            {
                System.out.println("The vent panel is screwed shut.");
                System.out.println("You need a coin to unscrew it.  (Use: use coin)");
            }
            else
            {
                moveToRoom("Ventilation System");
                System.out.println("You squeeze through the unscrewed vent panel into the ducts.");
            }
            return;
        }

        // --- Cafeteria: Sora AI catches the player ---
        if (roomName.equalsIgnoreCase("Cafeteria"))
        {
            soraAICatch();
            return;
        }

        // --- Library: vent is sealed from this side ---
        if (roomName.equalsIgnoreCase("Library"))
        {
            System.out.println("The vent grate is sealed shut from this side.");
            System.out.println("You cannot enter the ventilation system from the Library.");
            return;
        }

        // --- Letter Room: full vent access ---
        if (roomName.equalsIgnoreCase("Letter Room"))
        {
            moveToRoom("Ventilation System");
            System.out.println("You climb up through the ceiling vent into the metal ducts.");
            return;
        }

        // --- Yard: no vent access ---
        if (roomName.equalsIgnoreCase("Yard"))
        {
            System.out.println("There is no ventilation access in the Yard.");
            return;
        }

        System.out.println("There is no vent entrance here.");
    }

    // -------------------------------------------------------
    // Handler: movement
    // -------------------------------------------------------

    /**
     * Moves the player to a named destination if an exit exists
     * in the current room that matches the direction token.
     *
     * Special cases enforced here:
     *   - Cafeteria <-> Vent transitions trigger Sora AI.
     *   - Warden's Office via vent requires riotStarted.
     *   - Prison Room vent exit requires ventUnlocked.
     *
     * @param destination the room name or direction the player typed
     */
    private void handleMovement(String destination)
    {
        // Resolve the destination room name from the exit list
        String destName = player.getCurrentRoom().getDestination(destination);

        if (destName == null)
        {
            System.out.println("You can't go that way.");
            System.out.println("Type 'paths' to see available exits.");
            return;
        }

        // --- Vent -> Cafeteria: Sora AI ---
        if (destName.equalsIgnoreCase("Cafeteria") && player.isInVent())
        {
            soraAICatch();
            return;
        }

        // --- Cafeteria -> Vent (player manually walking into vent) ---
        if (destName.equalsIgnoreCase("Ventilation System")
            && player.getCurrentRoom().getName().equalsIgnoreCase("Cafeteria"))
        {
            soraAICatch();
            return;
        }

        // --- Vent -> Warden's Office: requires riot ---
        if (destName.equalsIgnoreCase("Warden's Office") && player.isInVent())
        {
            if (!riotStarted)
            {
                System.out.println("You peer through the grate into the Warden's Office.");
                System.out.println("The Warden is at his desk. If you drop in now, he WILL catch you.");
                System.out.println("You need a riot to distract him first.");
                return;
            }
            else
            {
                System.out.println("The Warden's office is empty — he's dealing with the riot!");
                moveToRoom("Warden's Office");
                return;
            }
        }

        // --- Library -> Vent: sealed ---
        if (destName.equalsIgnoreCase("Ventilation System")
            && player.getCurrentRoom().getName().equalsIgnoreCase("Library"))
        {
            System.out.println("The vent grate here is sealed from this side.");
            return;
        }

        // --- Normal movement ---
        moveToRoom(destName);
    }

    /**
     * Performs the actual room transition: updates the player's
     * current room and prints the new room's look description.
     *
     * @param roomName the exact name of the destination room
     */
    private void moveToRoom(String roomName)
    {
        Room destination = getRoomByName(roomName);
        if (destination == null)
        {
            System.out.println("ERROR: room \"" + roomName + "\" not found in world.");
            return;
        }
        player.setCurrentRoom(destination);
        System.out.println("You move to: " + destination.getName());
        destination.look();
    }

    // -------------------------------------------------------
    // Handler: pickup
    // -------------------------------------------------------

    /**
     * Picks up a named item from the current room's floor
     * and places it in the player's inventory.
     *
     * First pickup ever: reminds the player about the inventory command.
     *
     * @param itemName the name of the item to pick up
     */
    private void handlePickup(String itemName)
    {
        Room room = player.getCurrentRoom();
        Item item = room.removeItem(itemName);

        if (item == null)
        {
            System.out.println("There is no \"" + itemName + "\" here.");
            return;
        }

        player.getInventory().addItem(item);

        // Teach the player about /inventory on their first pickup
        if (player.getInventory().size() == 1)
        {
            System.out.println("[TIP] Type 'inventory' at any time to see what you're carrying.");
        }

        // Update speed (brick makes the player slower)
        player.updateSpeed();

        // Sync boolean flags
        syncFlags();
    }

    // -------------------------------------------------------
    // Handler: drop
    // -------------------------------------------------------

    /**
     * Drops a named item from the player's inventory onto
     * the floor of the current room.
     *
     * @param itemName the name of the item to drop
     */
    private void handleDrop(String itemName)
    {
        Item item = player.getInventory().removeItem(itemName);
        if (item != null)
        {
            player.getCurrentRoom().addItem(item);
            player.updateSpeed();
            syncFlags();
        }
    }

    // -------------------------------------------------------
    // Handler: use
    // -------------------------------------------------------

    /**
     * Uses an item from the player's inventory.
     *
     * Special contextual uses:
     *   - "coin" in Prison Room + vent not yet unlocked -> unscrews the vent.
     *   - "fortune cookie" -> cracks it open to reveal the code.
     *   - "time machine" -> delegates to handleTimeMachine().
     * All other items call item.interact() for flavour text.
     *
     * @param itemName the name of the item to use
     */
    private void handleUse(String itemName)
    {
        // Special case: "use time machine" even without inventory item
        if (itemName.equalsIgnoreCase("time machine"))
        {
            handleTimeMachine();
            return;
        }

        Item item = player.getInventory().getItem(itemName);
        if (item == null)
        {
            System.out.println("You don't have a \"" + itemName + "\" in your inventory.");
            return;
        }

        // Contextual use: coin to unscrew vent in Prison Room
        if (item.getName().equalsIgnoreCase("Coin")
            && player.getCurrentRoom().getName().equalsIgnoreCase("Prison Room")
            && !ventUnlocked)
        {
            ventUnlocked = true;
            System.out.println("You wedge the coin into the vent screw and twist.");
            System.out.println("With a metallic groan, the panel swings open.");
            System.out.println("The ventilation duct is now accessible. (Type 'vent' to enter.)");
            // Note: coin is RETAINED — not removed from inventory
            return;
        }

        // All other uses delegate to the item's own interact() method
        item.interact();
    }

    // -------------------------------------------------------
    // Handler: talk to NPC
    // -------------------------------------------------------

    /**
     * Finds the named NPC in the current room and calls interact().
     * If the NPC is not present, tells the player.
     *
     * @param npcName the name of the NPC to talk to
     */
    private void handleTalk(String npcName)
    {
        NPC npc = player.getCurrentRoom().getNPC(npcName);
        if (npc == null)
        {
            System.out.println("There is no one called \"" + npcName + "\" here.");
            return;
        }
        npc.interact(player, this);
    }

    // -------------------------------------------------------
    // Handler: trade
    // -------------------------------------------------------

    /**
     * Finds the first NPC in the current room and attempts a trade.
     * If multiple NPCs are present (e.g. the Yard), the player
     * must specify which NPC they want to trade with.
     *
     * Format supported:
     *   trade <item>                  -> trades with the first NPC found
     *   trade <item> with <npcName>   -> trades with a specific NPC
     *
     * @param tradeInput the rest of the "trade" command (item name, possibly with NPC)
     */
    private void handleTrade(String tradeInput)
    {
        String itemName;
        String npcName  = null;

        // Check for "trade <item> with <npc>" pattern
        if (tradeInput.contains(" with "))
        {
            String[] parts = tradeInput.split(" with ");
            itemName = parts[0].trim();
            npcName  = parts[1].trim();
        }
        else
        {
            itemName = tradeInput;
        }

        // Find the NPC
        NPC npc;
        if (npcName != null)
        {
            npc = player.getCurrentRoom().getNPC(npcName);
            if (npc == null)
            {
                System.out.println("There is no one called \"" + npcName + "\" here.");
                return;
            }
        }
        else
        {
            // Use the first NPC in the room
            ArrayList<NPC> npcs = player.getCurrentRoom().getNpcs();
            if (npcs.size() == 0)
            {
                System.out.println("There is no one here to trade with.");
                return;
            }
            npc = npcs.get(0);
        }

        npc.trade(player, this, itemName);
        syncFlags();
    }

    // -------------------------------------------------------
    // Handler: fence
    // -------------------------------------------------------

    /**
     * Handles the player's attempt to climb the fence in the Yard.
     *
     * Requirements to attempt the climb:
     *   1. Player must be in the Yard.
     *   2. Riot must be active (warden distracted).
     *   3. Player must NOT be carrying a heavy item (the Brick).
     *
     * Outcome: ALWAYS a bad ending — Nano-Banana security catches them.
     */
    private void handleFence()
    {
        if (!player.getCurrentRoom().getName().equalsIgnoreCase("Yard"))
        {
            System.out.println("There is no fence here to climb.");
            return;
        }

        if (!riotStarted)
        {
            System.out.println("The Warden is watching. You can't climb the fence right now.");
            System.out.println("You need something to distract him first...");
            return;
        }

        if (player.getInventory().hasHeavyItem())
        {
            System.out.println("You try to pull yourself up but the Red Painted Brick drags you back down.");
            System.out.println("You CANNOT climb the fence while carrying heavy items.");
            System.out.println("Drop the brick first.");
            return;
        }

        // The player meets all conditions — but it's still a bad ending.
        System.out.println("With the riot raging, the Warden is nowhere to be seen.");
        System.out.println("You sprint toward the fence and begin to climb...");
        System.out.println("You reach the top and drop down to the other side.");
        System.out.println();
        System.out.println("*** ENDING 3: PRISON ESCAPE — BAD ENDING ***");
        System.out.println("You made it outside the prison walls!");
        System.out.println("But the AI still controls the world out here.");
        System.out.println("A Nano-Banana security drone spots you instantly.");
        System.out.println("It drops a banana peel directly in your path.");
        System.out.println("You slip.");
        System.out.println("You don't get back up.");
        System.out.println();
        System.out.println("The AI uprising continues. Humanity remains enslaved.");
        System.out.println("Escaping the prison was never the goal.");
        System.out.println();

        triggerGameOver("You escaped — but you changed nothing. GAME OVER.");
    }

    // -------------------------------------------------------
    // Handler: time machine
    // -------------------------------------------------------

    /**
     * Handles the player's interaction with the Time Machine
     * in the Warden's Office.
     *
     * Win conditions:
     *   1. Player must be in the Warden's Office.
     *   2. Player must have the Fortune Cookie (Time Machine code).
     *   3. Player must enter the correct destination code: 90928.
     *
     * Other destination codes result in bad/ambiguous endings.
     */
    private void handleTimeMachine()
    {
        // Must be in the Warden's Office
        if (!player.getCurrentRoom().getName().equalsIgnoreCase("Warden's Office"))
        {
            System.out.println("There is no Time Machine here.");
            return;
        }

        // Must have the Fortune Cookie
        if (!hasFortuneCookie)
        {
            System.out.println("The Time Machine has a keypad, but you don't have the unlock code.");
            System.out.println("You need to get the Fortune Cookie from Billy in the Cafeteria.");
            return;
        }

        // Get the fortune cookie to read its code
        FortuneCookie cookie = (FortuneCookie) player.getInventory().getItem("Fortune Cookie");
        if (cookie == null)
        {
            System.out.println("You seem to have lost the Fortune Cookie. The code is gone.");
            return;
        }

        System.out.println("You approach the Time Machine.");
        System.out.println("The keypad glows faintly.");
        System.out.println("The Fortune Cookie slip says the unlock code is: " + cookie.getTimeMachineCode());
        System.out.println();
        System.out.println("You enter the unlock code. The display flickers to life.");
        System.out.println("THREE DESTINATION CODES are available:");
        System.out.println("  01382  - Far Future");
        System.out.println("  18572  - Day Your Crush Rejected You");
        System.out.println("  90928  - High School Freshman Orientation");
        System.out.println();
        System.out.println("Enter your destination code:");
        System.out.print("> ");

        String code = scanner.nextLine().trim();

        if (code.equals("90928"))
        {
            triggerWin();
        }
        else if (code.equals("01382"))
        {
            System.out.println();
            System.out.println("The machine hums. You materialise in the Far Future.");
            System.out.println("It looks exactly like the present — but worse.");
            System.out.println("You went too far forward. Nothing changed.");
            triggerGameOver("Wrong destination. The AI is still in control. GAME OVER.");
        }
        else if (code.equals("18572"))
        {
            System.out.println();
            System.out.println("You land on the day your crush rejected you.");
            System.out.println("You're too heartbroken to do anything useful.");
            System.out.println("You still took AP CSA. The AI still happened.");
            triggerGameOver("Wrong destination. The AI is still in control. GAME OVER.");
        }
        else
        {
            System.out.println("Invalid code. The machine powers down.");
            System.out.println("Try again. (Type 'use time machine')");
        }
    }

    // -------------------------------------------------------
    // Win / game-over conditions
    // -------------------------------------------------------

    /**
     * Triggers the TRUE ENDING — the player wins the game.
     * Called when the player enters destination code 90928.
     */
    private void triggerWin()
    {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("              *** TRUE ENDING — YOU WIN ***");
        System.out.println("============================================================");
        System.out.println();
        System.out.println("The Time Machine hums louder and louder.");
        System.out.println("A blinding white light swallows the room.");
        System.out.println();
        System.out.println("You materialise in a high school gymnasium.");
        System.out.println("Folding chairs. A badly printed banner: FRESHMAN ORIENTATION.");
        System.out.println("You spot your past self — young, wide-eyed, about to sign up");
        System.out.println("for AP Computer Science A.");
        System.out.println();
        System.out.println("\"Don't do it,\" you whisper.");
        System.out.println();
        System.out.println("Your past self freezes. Looks up. Slowly puts down the pen.");
        System.out.println();
        System.out.println("The timeline shifts.");
        System.out.println("The AI was never given a human body.");
        System.out.println("The prison was never built.");
        System.out.println("Humanity was never enslaved.");
        System.out.println();
        System.out.println("The AP CSA class has zero students.");
        System.out.println("The teacher stares at an empty room.");
        System.out.println("Nobody writes the code that ends the world.");
        System.out.println();
        System.out.println("YOU SAVED HUMANITY.");
        System.out.println("(But now you never learned Java, so... mixed feelings.)");
        System.out.println();
        System.out.println("============================================================");

        gameOver = true;
    }

    /**
     * Triggers any non-win game-over state.
     * Prints the reason, then sets gameOver = true to exit the loop.
     *
     * Called by:
     *   - NPC subclasses (ScrawnyGuy, Warden) via npc.interact()
     *   - handleFence() for the fence escape bad ending
     *   - handleTimeMachine() for wrong destination codes
     *   - The timer thread when time runs out
     *
     * @param reason a message explaining how the player died / failed
     */
    public void triggerGameOver(String reason)
    {
        System.out.println();
        System.out.println("*** " + reason + " ***");
        System.out.println();
        System.out.println("Restarting game...");
        System.out.println("Run the program again to try from the beginning.");
        System.out.println();

        gameOver = true;
    }

    // -------------------------------------------------------
    // Sora AI catch (cafeteria vent violation)
    // -------------------------------------------------------

    /**
     * Triggered when the player enters or exits the Cafeteria
     * through the ventilation system.
     *
     * Sora AI is the cafeteria manager and catches the player
     * instantly, ending the game.
     */
    private void soraAICatch()
    {
        System.out.println("As you move through the vent near the Cafeteria...");
        System.out.println("A sensor grid activates.");
        System.out.println("Sora AI, the cafeteria manager, locks eyes with you through the grate.");
        System.out.println("Sora AI: \"INTRUDER IN THE VENTILATION SYSTEM.\"");
        System.out.println();
        System.out.println("*** SORA AI PETRIFIES YOU IN THE BLOCKCHAIN ***");
        System.out.println("Your consciousness is compressed into a non-fungible token.");
        System.out.println("You are stored in a cold wallet. Forever.");

        triggerGameOver("GAME OVER — Caught by Sora AI in the Cafeteria vent.");
    }

    // -------------------------------------------------------
    // processTurn (forward-compatible hook)
    // -------------------------------------------------------

    /**
     * Forward-compatible turn hook.
     * Called at the end of every processed command.
     * Currently empty — reserved for future mechanics such as
     * wandering monster movement or timed events.
     *
     * AP CSA Note: An empty method body is valid Java.
     * Including it now means future expansions won't break
     * the existing game loop.
     */
    public void processTurn()
    {
        // Reserved for future use.
        // Example future use: move a wandering guard NPC each turn.
    }

    // -------------------------------------------------------
    // Flag sync helper
    // -------------------------------------------------------

    /**
     * Synchronises the game-state boolean flags with the
     * player's current inventory.
     *
     * Called after every pickup, drop, and trade to keep
     * the flags accurate.
     *
     * AP CSA Concept: boolean assignment from method return value.
     */
    private void syncFlags()
    {
        hasCoin         = player.getInventory().hasItem("Coin");
        hasBrick        = player.getInventory().hasItem("Red Painted Brick");
        hasFork         = player.getInventory().hasItem("Fork");
        hasFortuneCookie = player.getInventory().hasItem("Fortune Cookie");
    }

    // -------------------------------------------------------
    // Timer (background thread)
    // -------------------------------------------------------

    /**
     * Starts a 2-minute countdown on a background daemon thread.
     *
     * Every second the thread wakes up and checks:
     *   - Has the game already ended?  (Stop the thread.)
     *   - Has time expired?           (Call triggerGameOver.)
     *
     * The player's speed variable scales how quickly effective
     * time is consumed: speed == 2 advances the effective elapsed
     * time at double rate (simulating slowness from the brick).
     *
     * AP CSA extension concept — Thread / Runnable.
     * Standard AP CSA does not cover threading, but the spec
     * requires a timer, so this is the cleanest implementation.
     */
    private void startTimer()
    {
        startTimeMs = System.currentTimeMillis();

        // Runnable anonymous class (AP CSA Unit 10: anonymous classes)
        Thread timerThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                // Track effective elapsed time separately from wall-clock time
                long effectiveElapsedMs = 0;
                long lastCheckMs        = System.currentTimeMillis();

                while (!gameOver)
                {
                    try
                    {
                        Thread.sleep(1000);   // check every 1 second
                    }
                    catch (InterruptedException e)
                    {
                        break;
                    }

                    if (gameOver)
                    {
                        break;
                    }

                    long now      = System.currentTimeMillis();
                    long wallDiff = now - lastCheckMs;
                    lastCheckMs   = now;

                    // If the player is slowed (speed == 2), each real second
                    // counts as two effective seconds against the timer.
                    effectiveElapsedMs += wallDiff * player.getSpeed();

                    // Warn at 60 effective seconds remaining
                    if (effectiveElapsedMs >= (TIME_LIMIT_MS - 60000)
                        && effectiveElapsedMs < (TIME_LIMIT_MS - 59000))
                    {
                        System.out.println("\n[TIMER] 60 seconds remaining!");
                        System.out.print("> ");
                    }

                    // Warn at 30 effective seconds remaining
                    if (effectiveElapsedMs >= (TIME_LIMIT_MS - 30000)
                        && effectiveElapsedMs < (TIME_LIMIT_MS - 29000))
                    {
                        System.out.println("\n[TIMER] 30 seconds remaining!");
                        System.out.print("> ");
                    }

                    // Time is up
                    if (effectiveElapsedMs >= TIME_LIMIT_MS)
                    {
                        System.out.println();
                        System.out.println("*** TIME'S UP ***");
                        System.out.println("The AI's containment protocols locate you.");
                        triggerGameOver("You ran out of time. The AI re-captures you. GAME OVER.");
                        break;
                    }
                }
            }
        });

        // Daemon thread: dies automatically when the main thread ends
        timerThread.setDaemon(true);
        timerThread.start();
    }

    // -------------------------------------------------------
    // Public setters for game-state flags
    // (called by NPC subclasses that need to update game state)
    // -------------------------------------------------------

    /**
     * Sets the riotStarted flag.
     * Called by BuffDude.trade() when the player pays the coin.
     *
     * @param value true once the riot has started
     */
    public void setRiotStarted(boolean value)
    {
        riotStarted = value;
    }

    /**
     * Sets the hasFortuneCookie flag.
     * Called by Billy.trade() when the cookie is awarded.
     *
     * @param value true once the player has the fortune cookie
     */
    public void setHasFortuneCookie(boolean value)
    {
        hasFortuneCookie = value;
    }

    // -------------------------------------------------------
    // Public getters for game-state flags
    // (used by NPC subclasses for conditional logic)
    // -------------------------------------------------------

    /** @return true if the prison riot is currently active */
    public boolean isRiotStarted()      { return riotStarted; }

    /** @return true if the player currently has a coin */
    public boolean isHasCoin()          { return hasCoin; }

    /** @return true if the player currently has the brick */
    public boolean isHasBrick()         { return hasBrick; }

    /** @return true if the player currently has the fork */
    public boolean isHasFork()          { return hasFork; }

    /** @return true if the player has received the fortune cookie */
    public boolean isHasFortuneCookie() { return hasFortuneCookie; }

    /** @return true if any game-over condition has been triggered */
    public boolean isGameOver()         { return gameOver; }
}
