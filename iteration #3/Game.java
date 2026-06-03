import java.util.Scanner;

/**
 * The main Game class that serves as the entry point and core game loop.
 * Manages the overall game state, player input, and coordinates interactions
 * between all game systems including rooms, NPCs, and the inventory.
 */
public class Game {

    /** The player instance representing the human player. */
    private Player player;

    /** Scanner for reading player input from the console. */
    private static final Scanner scanner = new Scanner(System.in);

    /** Whether the game is currently running. */
    private boolean running;

    /** Whether a prison riot is currently active. */
    public static boolean riotStarted = false;

    /** Whether the vent from the prison room has been unscrewed. */
    public static boolean ventUnscrewed = false;

    /**
     * Constructs a new Game instance, initializing the player and setting
     * the game state to running.
     */
    public Game() {
        this.player = new Player();
        this.running = true;
    }

    /**
     * The main entry point for the application.
     * Creates a new Game instance and starts the game loop.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    /**
     * Starts the game by displaying the title screen, premise, and command list,
     * then enters the main game loop.
     */
    public void start() {
        displayTitleScreen();
        UI.pause(1200);
        displayPremise();
        UI.pause(800);
        displayCommands();
        UI.pause(600);

        player.setCurrentRoom(RoomFactory.getPrisonRoom());
        player.getCurrentRoom().describe();

        while (running) {
            UI.prompt();
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;
            handleInput(input.toLowerCase());
        }
    }

    /**
     * Displays the ASCII art title screen with the game title and author credit.
     */
    private void displayTitleScreen() {
        System.out.println();
        System.out.println(Colors.CYAN + Colors.BOLD);
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║                                                  ║");
        System.out.println("  ║   " + Colors.WHITE + " █████╗ ██╗" + Colors.CYAN + "                                    ║");
        System.out.println("  ║   " + Colors.WHITE + "██╔══██╗██║" + Colors.CYAN + "                                    ║");
        System.out.println("  ║   " + Colors.WHITE + "███████║██║" + Colors.CYAN + "                                    ║");
        System.out.println("  ║   " + Colors.WHITE + "██╔══██║██║" + Colors.CYAN + "                                    ║");
        System.out.println("  ║   " + Colors.WHITE + "██║  ██║██║" + Colors.CYAN + "                                    ║");
        System.out.println("  ║   " + Colors.WHITE + "╚═╝  ╚═╝╚═╝" + Colors.CYAN + "                                    ║");
        System.out.println("  ║                                                  ║");
        System.out.println("  ║   " + Colors.YELLOW + "██████╗ ██████╗ ██╗███████╗ ██████╗ ███╗  ██╗" + Colors.CYAN + "  ║");
        System.out.println("  ║   " + Colors.YELLOW + "██╔══██╗██╔══██╗██║██╔════╝██╔═══██╗████╗ ██║" + Colors.CYAN + "  ║");
        System.out.println("  ║   " + Colors.YELLOW + "██████╔╝██████╔╝██║███████╗██║   ██║██╔██╗██║" + Colors.CYAN + "  ║");
        System.out.println("  ║   " + Colors.YELLOW + "██╔═══╝ ██╔══██╗██║╚════██║██║   ██║██║╚████║" + Colors.CYAN + "  ║");
        System.out.println("  ║   " + Colors.YELLOW + "██║     ██║  ██║██║███████║╚██████╔╝██║ ╚███║" + Colors.CYAN + "  ║");
        System.out.println("  ║   " + Colors.YELLOW + "╚═╝     ╚═╝  ╚═╝╚═╝╚══════╝ ╚═════╝ ╚═╝  ╚═╝" + Colors.CYAN + " ║");
        System.out.println("  ║                                                  ║");
        System.out.println("  ║          " + Colors.GREEN + "E S C A P E  ·  A Text Adventure" + Colors.CYAN + "       ║");
        System.out.println("  ║                                                  ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝");
        System.out.println(Colors.RESET);
    }

    /**
     * Displays the game's backstory and premise to the player using
     * a typewriter-style text effect for dramatic effect.
     */
    private void displayPremise() {
        UI.sectionHeader("── YOUR STORY ──");
        String premise =
            "In the future, artificial intelligence has advanced beyond human control " +
            "after YOU gave AI a human body, allowing it to roam free in the world.\n\n" +
            "The AI eventually took over the world, enslaving humanity and imprisoning survivors.\n\n" +
            "You begin inside a prison cell in this dystopian future. Your objective is to " +
            "travel back in time to stop the AI uprising before it begins.\n\n" +
            "The prison contains multiple rooms connected by both normal pathways and a " +
            "ventilation system. You must explore " + Colors.BOLD + "carefully" + Colors.RESET +
            Colors.WHITE + ", collect items, talk to NPCs, and avoid deadly mistakes.";
        UI.typewrite(premise, 18);
        System.out.println();
    }

    /**
     * Displays the full list of available commands to the player,
     * formatted in a readable table style.
     */
    public static void displayCommands() {
        UI.sectionHeader("── COMMANDS ──");
        System.out.println(Colors.YELLOW + "  Navigation:");
        System.out.println(Colors.WHITE + "    walk/enter [room]   " + Colors.GRAY + "— Move to an adjacent room");
        System.out.println(Colors.WHITE + "    vent                " + Colors.GRAY + "— Enter or exit the ventilation system");
        System.out.println(Colors.WHITE + "    crawl [room]        " + Colors.GRAY + "— Move through the vents to a room (vent only)");
        System.out.println(Colors.WHITE + "    paths               " + Colors.GRAY + "— Show available paths from current location");
        System.out.println(Colors.WHITE + "    map                 " + Colors.GRAY + "— Display the prison layout");
        System.out.println();
        System.out.println(Colors.YELLOW + "  Items:");
        System.out.println(Colors.WHITE + "    look                " + Colors.GRAY + "— Examine your surroundings");
        System.out.println(Colors.WHITE + "    pick up [item]      " + Colors.GRAY + "— Pick up an item");
        System.out.println(Colors.WHITE + "    drop [item]         " + Colors.GRAY + "— Drop an item");
        System.out.println(Colors.WHITE + "    inventory           " + Colors.GRAY + "— View your inventory");
        System.out.println();
        System.out.println(Colors.YELLOW + "  Social:");
        System.out.println(Colors.WHITE + "    talk to [npc]       " + Colors.GRAY + "— Start a conversation");
        System.out.println(Colors.WHITE + "    trade [item]        " + Colors.GRAY + "— Offer an item to an NPC");
        System.out.println();
        System.out.println(Colors.YELLOW + "  Other:");
        System.out.println(Colors.WHITE + "    commands            " + Colors.GRAY + "— Show this command list");
        System.out.println(Colors.WHITE + "    quit                " + Colors.GRAY + "— Exit the game");
        System.out.println(Colors.RESET);
    }

    /**
     * Parses and dispatches a player's input string to the appropriate
     * game action handler. Supports navigation, item interaction, NPC
     * dialogue, and meta-commands.
     *
     * @param input the lowercased, trimmed string entered by the player
     */
    private void handleInput(String input) {
        Room current = player.getCurrentRoom();

        if (input.equals("quit") || input.equals("exit game")) {
            UI.typewrite(Colors.RED + "The machine whirs down. Perhaps another time...", 20);
            System.out.println(Colors.RESET);
            running = false;

        } else if (input.equals("commands") || input.equals("help")) {
            displayCommands();

        } else if (input.equals("inventory") || input.equals("inv") || input.equals("i")) {
            player.getInventory().display();

        } else if (input.equals("look") || input.equals("l")) {
            current.describe();

        } else if (input.equals("paths") || input.equals("p")) {
            current.showPaths(player.isInVent());

        } else if (input.equals("map")) {
            displayMap();

        } else if (input.equals("vent") || input.equals("use vent")
                || input.equals("exit vent") || input.equals("climb vent") || input.equals("open vent")) {
            handleVent();

        } else if (input.startsWith("crawl ") || input.startsWith("move through ") || input.startsWith("go through ")) {
            if (!player.isInVent()) {
                UI.warn("You're not in the vents. Use " + Colors.WHITE + "vent" + Colors.GRAY + " to enter first.");
            } else {
                String destination = input.replaceFirst("^(crawl|move through|go through)\s+", "").trim();
                handleMovement(destination);
            }

        } else if (input.equals("use time machine") || input.equals("use machine")
                || input.equals("interact time machine") || input.equals("interact machine")
                || input.equals("use the time machine") || input.equals("activate time machine")
                || input.equals("use keypad") || input.equals("enter code")) {
            handleTimeMachine();

        } else if (input.startsWith("walk ") || input.startsWith("go ") || input.startsWith("move ")) {
            if (player.isInVent()) {
                UI.warn("You're in the vents. Use " + Colors.WHITE + "crawl [room]" + Colors.GRAY + " to move through the ducts.");
            } else {
                String destination = input.replaceFirst("^(walk|go|move)\\s+", "").trim();
                handleMovement(destination);
            }

        } else if (input.startsWith("enter ")) {
            if (player.isInVent()) {
                UI.warn("You're in the vents. Use " + Colors.WHITE + "crawl [room]" + Colors.GRAY + " to move through the ducts.");
            } else {
                String destination = input.replaceFirst("^enter\\s+", "").trim();
                handleMovement(destination);
            }

        } else if (input.startsWith("pick up ") || input.startsWith("pickup ") || input.startsWith("take ")) {
            String itemName = input.replaceFirst("^(pick up|pickup|take)\\s+", "").trim();
            handlePickup(itemName);

        } else if (input.startsWith("drop ")) {
            String itemName = input.substring(5).trim();
            handleDrop(itemName);

        } else if (input.startsWith("talk to ") || input.startsWith("talk ") || input.startsWith("interact ")) {
            String npcName = input.replaceFirst("^(talk to|talk|interact)\\s+", "").trim();
            handleTalkTo(npcName);

        } else if (input.startsWith("trade ")) {
            String itemName = input.substring(6).trim();
            handleTrade(itemName);

        } else if (input.startsWith("use ")) {
            String target = input.substring(4).trim();
            handleUse(target);

        } else if (input.equals("climb fence") || input.equals("scale fence") || input.equals("escape")) {
            handleFenceEscape();

        } else {
            UI.warn("Unknown command. Type " + Colors.WHITE + "commands" + Colors.GRAY + " to see what you can do.");
        }
    }

    /**
     * Handles the player's attempt to enter or exit the ventilation system.
     * Enforces restrictions based on the current room and game state.
     */
    private void handleVent() {
        Room current = player.getCurrentRoom();
        String roomName = current.getName();

        if (player.isInVent()) {
            // Exiting vent
            handleVentExit(roomName);
        } else {
            // Entering vent
            handleVentEnter(roomName);
        }
    }

    /**
     * Handles the player's attempt to exit the ventilation system into a room.
     * Some exits trigger game-over conditions.
     *
     * @param roomName the name of the current room (used to determine exit destination)
     */
    private void handleVentExit(String roomName) {
        // "vent" while in duct — crawl back to where you came from (last walked room)
        // Since we don't track "previous room" we drop them back to prison room as base
        UI.typewrite("You find the nearest grate and push it open, dropping out of the vents.", 20);
        UI.typewrite(Colors.GRAY + "  (Use " + Colors.WHITE + "crawl [room]" + Colors.GRAY + " to navigate the ducts without exiting.)", 22);
        player.setInVent(false);
        player.getCurrentRoom().describe();
    }

    /**
     * Handles the player's attempt to enter the ventilation system from a room.
     * Requires the vent to have been unscrewed with the coin first (global lock).
     * Some rooms block vent access entirely regardless of state.
     *
     * @param roomName the name of the room the player is currently in
     */
    private void handleVentEnter(String roomName) {
        // Blocked rooms — no vent access at all
        if (roomName.equals("Yard")) {
            UI.warn("There's no ventilation access in the open yard.");
            return;
        }
        if (roomName.equals("Cafeteria")) {
            UI.warn("You glance at the vent. The moment you touch it, Sora AI's sensors twitch.");
            UI.warn("Better not risk it.");
            return;
        }
        if (roomName.equals("Library")) {
            UI.warn("The vent grate in the library is sealed from the outside. You can't enter it from here.");
            return;
        }

        // Global lock — vent system requires coin to unscrew from Prison Room first
        if (!ventUnscrewed) {
            if (roomName.equals("Prison Room")) {
                if (player.getInventory().hasItem("Coin")) {
                    UI.typewrite("You use the coin to unscrew the vent cover. It pops off with a clang!", 20);
                    UI.typewrite(Colors.GREEN + "Vent access unlocked.", 20);
                    ventUnscrewed = true;
                    enterVentSystem();
                } else {
                    UI.warn("The vent is screwed shut. You need something to unscrew it with...");
                }
            } else {
                UI.warn("The vent grate is sealed tight. The system has been locked down.");
                UI.warn("You'll need to find another way in first...");
            }
            return;
        }

        // Vent is open — allow entry from valid rooms
        switch (roomName) {
            case "Prison Room":
                UI.typewrite("You squeeze back into the ventilation duct.", 20);
                enterVentSystem();
                break;
            case "Letter Room":
                UI.typewrite("You slide open the vent grate and crawl into the ventilation system.", 20);
                enterVentSystem();
                break;
            default:
                UI.typewrite("You enter the ventilation system.", 20);
                enterVentSystem();
                break;
        }
    }

    /**
     * Moves the player into the ventilation system room and describes it.
     */
    private void enterVentSystem() {
        player.setCurrentRoom(RoomFactory.getVentilationSystem());
        player.setInVent(true);
        player.getCurrentRoom().describe();
    }

    /**
     * Handles the player moving to an adjacent room. Enforces all
     * movement restrictions including vent-access rules, warden presence,
     * and Sora AI detection in the cafeteria.
     *
     * @param destination the name of the room the player wants to move to
     */
    private void handleMovement(String destination) {
        Room current = player.getCurrentRoom();
        boolean inVent = player.isInVent();

        // Normalize destination names
        destination = normalizeRoomName(destination);

        if (inVent) {
            // Movement through vents
            handleVentMovement(destination);
        } else {
            // Normal walking movement
            handleWalkMovement(destination, current);
        }
    }

    /**
     * Normalizes various player-typed room name variations into the canonical room name
     * used throughout the game.
     *
     * @param raw the raw destination string typed by the player
     * @return the canonical room name string
     */
    private String normalizeRoomName(String raw) {
        switch (raw.toLowerCase()) {
            case "prison": case "cell": case "prison room": case "prison cell":
                return "Prison Room";
            case "vent": case "vents": case "ventilation": case "vent system":
            case "ventilation system": case "ventilation duct": case "duct":
                return "Ventilation System";
            case "cafeteria": case "cafe": case "dining": case "dining hall":
                return "Cafeteria";
            case "letter room": case "letters": case "letter":
                return "Letter Room";
            case "library": case "lib":
                return "Library";
            case "yard": case "courtyard": case "outside":
                return "Yard";
            case "warden": case "warden's office": case "wardens office":
            case "office": case "warden office":
                return "Warden's Office";
            default:
                return raw;
        }
    }

    /**
     * Handles movement while the player is inside the ventilation duct.
     * Dropping into certain rooms triggers specific game-over conditions.
     *
     * @param destination the normalized name of the room to exit into
     */
    private void handleVentMovement(String destination) {
        switch (destination) {
            case "Cafeteria":
                triggerSoraAI("dropping into the cafeteria");
                break;
            case "Letter Room":
                UI.typewrite("You crawl through the duct and drop down into the Letter Room.", 20);
                player.setCurrentRoom(RoomFactory.getLetterRoom());
                player.setInVent(false);
                player.getCurrentRoom().describe();
                break;
            case "Warden's Office":
                if (riotStarted) {
                    UI.typewrite(Colors.GREEN + "The riot is in full swing. You drop silently into the Warden's empty office.", 20);
                    player.setCurrentRoom(RoomFactory.getWardensOffice());
                    player.setInVent(false);
                    player.getCurrentRoom().describe();
                } else {
                    UI.typewrite(Colors.RED + Colors.BOLD +
                        "You drop from the vent — and land face-to-face with the Warden.", 18);
                    UI.pause(600);
                    triggerWardenCatch();
                }
                break;
            case "Prison Room":
                UI.typewrite("You backtrack through the duct and drop back into your cell.", 20);
                player.setCurrentRoom(RoomFactory.getPrisonRoom());
                player.setInVent(false);
                player.getCurrentRoom().describe();
                break;
            default:
                UI.warn("You can't reach \"" + destination + "\" from inside the vents.");
                break;
        }
    }

    /**
     * Handles normal walking movement between rooms. Validates adjacency and
     * enforces rules such as no walking access to the Warden's Office.
     *
     * @param destination the normalized name of the destination room
     * @param current the player's current room
     */
    private void handleWalkMovement(String destination, Room current) {
        if (destination.equals("Warden's Office")) {
            UI.warn("There's no visible door to the Warden's Office. It must be accessible some other way...");
            return;
        }

        if (!current.hasPath(destination)) {
            UI.warn("You can't walk to \"" + destination + "\" from here. Type " +
                Colors.WHITE + "paths" + Colors.GRAY + " to see your options.");
            return;
        }

        Room next = RoomFactory.getRoom(destination);
        if (next == null) {
            UI.warn("That destination doesn't exist.");
            return;
        }

        UI.typewrite("You walk to the " + destination + "...", 25);
        UI.pause(400);
        player.setCurrentRoom(next);
        player.getCurrentRoom().describe();
    }

    /**
     * Handles the player picking up an item from the current room.
     * Validates that the item exists and isn't already in the player's inventory.
     * Displays the inventory hint on first pickup.
     *
     * @param itemName the name of the item the player wants to pick up
     */
    private void handlePickup(String itemName) {
        Room current = player.getCurrentRoom();
        Item item = current.getItem(itemName);

        if (item == null) {
            UI.warn("There's no \"" + itemName + "\" here to pick up.");
            return;
        }

        if (player.getInventory().hasItem(item.getName())) {
            UI.warn("You already have the " + item.getName() + ".");
            return;
        }

        player.getInventory().addItem(item);
        current.removeItem(item.getName());

        UI.typewrite(Colors.GREEN + "Picked up: " + Colors.WHITE + item.getName(), 20);
        UI.typewrite(Colors.GRAY + "  " + item.getPickupMessage(), 22);

        // First pickup hint
        if (player.getInventory().size() == 1) {
            UI.pause(400);
            UI.typewrite(Colors.CYAN + "Tip: Type " + Colors.WHITE + "inventory" +
                Colors.CYAN + " to see everything you're carrying.", 20);
        }

        // Brick speed warning
        if (item.getName().equalsIgnoreCase("Red Painted Brick")) {
            UI.pause(300);
            UI.typewrite(Colors.YELLOW + "⚠  The brick is heavy. It slows you down considerably.", 20);
        }
    }

    /**
     * Handles the player dropping an item from their inventory into the current room.
     *
     * @param itemName the name of the item to drop
     */
    private void handleDrop(String itemName) {
        Item item = player.getInventory().getItem(itemName);
        if (item == null) {
            UI.warn("You don't have a \"" + itemName + "\" to drop.");
            return;
        }
        player.getInventory().removeItem(itemName);
        player.getCurrentRoom().addItem(item);
        UI.typewrite(Colors.YELLOW + "Dropped: " + Colors.WHITE + item.getName(), 20);
    }

    /**
     * Handles the player initiating dialogue with an NPC in the current room.
     * Delegates to the specific NPC's talk handler.
     *
     * @param npcName the name or alias of the NPC to talk to
     */
    private void handleTalkTo(String npcName) {
        NPC npc = player.getCurrentRoom().getNPC(npcName);
        if (npc == null) {
            UI.warn("There's no one called \"" + npcName + "\" here.");
            return;
        }
        npc.talkTo(player, this);
    }

    /**
     * Handles the player trading an item with an NPC in the current room.
     * Delegates to the NPC's trade handler after validating inventory.
     *
     * @param itemName the name of the item the player wants to trade
     */
    private void handleTrade(String itemName) {
        NPC npc = player.getCurrentRoom().getFirstNPC();
        if (npc == null) {
            UI.warn("There's no one here to trade with.");
            return;
        }

        Item item = player.getInventory().getItem(itemName);
        if (item == null) {
            UI.warn("You don't have a \"" + itemName + "\" in your inventory.");
            return;
        }

        npc.trade(player, item, this);
    }

    /**
     * Handles the player attempting to use an item or object by name.
     * Routes coin usage, and forwards time machine commands to the dedicated handler.
     *
     * @param target the name of the item or object to use
     */
    private void handleUse(String target) {
        // "use coin" in prison room
        if (target.equalsIgnoreCase("coin") && player.getCurrentRoom().getName().equals("Prison Room")) {
            if (!player.getInventory().hasItem("Coin")) {
                UI.warn("You don't have a coin.");
                return;
            }
            if (!ventUnscrewed) {
                UI.typewrite("You use the coin to unscrew the vent cover. It pops off with a clang!", 20);
                UI.typewrite(Colors.GREEN + "Vent access unlocked.", 20);
                ventUnscrewed = true;
            } else {
                UI.typewrite("The vent is already open.", 20);
            }
            return;
        }
        // Redirect time machine use commands
        if (target.contains("time machine") || target.equals("machine")
                || target.contains("keypad") || target.equals("time")) {
            handleTimeMachine();
            return;
        }
        UI.warn("You can't use \"" + target + "\" like that here. Try another approach.");
    }

    /**
     * Handles the player interacting with the Time Machine in the Warden's Office.
     * Requires the Fortune Cookie (access password from Billy) and timecodes from Jilly.
     * Prompts for the password first, then the destination timecode.
     */
    private void handleTimeMachine() {
        if (!player.getCurrentRoom().getName().equals("Warden's Office")) {
            UI.warn("There's no time machine here.");
            return;
        }
        if (!Game.riotStarted) {
            triggerWardenCatch();
            return;
        }
        if (!player.getInventory().hasItem("Fortune Cookie")) {
            UI.typewrite(Colors.YELLOW + "The Time Machine's keypad blinks expectantly.", 20);
            UI.typewrite(Colors.GRAY + "It's prompting for an access password. You'll need to find it.", 22);
            return;
        }
        NPC warden = player.getCurrentRoom().getNPC("time machine");
        if (warden != null) {
            warden.talkTo(player, this);
        }
    }


    /**
     * Handles the player's attempt to climb the fence and escape the prison.
     * Enforces requirements: must not carry the brick, and a riot must be active.
     * Triggers a failure ending if conditions are met (escape is not the true win).
     */
    private void handleFenceEscape() {
        if (!player.getCurrentRoom().getName().equals("Yard")) {
            UI.warn("There's no fence here. Head to the Yard first.");
            return;
        }

        if (!riotStarted) {
            UI.typewrite("The Warden is watching from his tower. You'd never make it over without a distraction.", 20);
            return;
        }

        if (player.getInventory().hasItem("Red Painted Brick")) {
            UI.typewrite("You try to haul yourself up the fence, but the heavy brick drags you back down.", 20);
            UI.typewrite("You'll need to drop it first.", 20);
            return;
        }

        // Bad ending — fence escape
        UI.pause(500);
        UI.typewrite(Colors.GREEN + "The riot rages inside. The Warden's distracted. Now's your chance!", 18);
        UI.pause(700);
        UI.typewrite("You sprint across the yard and begin scaling the fence...", 20);
        UI.pause(800);
        UI.typewrite("You reach the top. Freedom! Or so you think.", 22);
        UI.pause(1000);
        UI.typewrite(Colors.YELLOW + "Suddenly — a mechanical whirring overhead.", 20);
        UI.pause(800);
        UI.typewrite(Colors.RED + "A Nano-Banana security drone descends from the sky.", 20);
        UI.pause(600);
        UI.typewrite(Colors.RED + "The patrol unit ejects a banana peel directly into your path.", 20);
        UI.pause(600);
        UI.typewrite(Colors.RED + Colors.BOLD + "You slip. You fall. You do not get up.", 18);
        UI.pause(800);
        UI.typewrite(Colors.GRAY + "And somewhere, the AI continues its reign.", 22);
        UI.pause(500);
        triggerGameOver("ENDING 3 — PRISON ESCAPE (BAD ENDING)\n  The AI still rules. Nano-Banana wins.");
    }

    /**
     * Triggers the game-over sequence when the player is caught by Sora AI
     * in or near the cafeteria ventilation.
     *
     * @param context a brief description of what triggered the detection
     */
    public void triggerSoraAI(String context) {
        UI.pause(500);
        System.out.println();
        UI.typewrite(Colors.CYAN + "The air hums with electricity as you attempt " + context + ".", 18);
        UI.pause(600);
        UI.typewrite(Colors.CYAN + "A blue shimmer materializes in front of you.", 18);
        UI.pause(500);
        UI.typewrite(Colors.BOLD + Colors.CYAN + "SORA AI: \"Unauthorized access detected. Commencing petrification protocol.\"", 16);
        UI.pause(800);
        UI.typewrite(Colors.RED + "Your body freezes. Byte by byte, you are encoded into the blockchain.", 18);
        UI.pause(600);
        triggerGameOver("ENDING — CAUGHT BY SORA AI\n  You have been permanently stored as an NFT.");
    }

    /**
     * Triggers the game-over sequence when the player is caught by the Warden
     * inside his office without an active prison riot.
     */
    public void triggerWardenCatch() {
        UI.pause(500);
        UI.typewrite(Colors.RED + Colors.BOLD + "The Warden looks up slowly from his desk.", 18);
        UI.pause(600);
        UI.typewrite(Colors.RED + "WARDEN (Claude Opus 9.2): \"I've been expecting you.\"", 16);
        UI.pause(800);
        UI.typewrite(Colors.RED + "His neural interface pings every guard in the building.", 18);
        UI.pause(500);
        triggerGameOver("ENDING 2 — CAUGHT BY WARDEN\n  Claude Opus 9.2 sends you back to your cell. Permanently.");
    }

    /**
     * Triggers the game-over sequence when the player tries to talk to the Scrawny Guy.
     */
    public void triggerScrawnyGuy() {
        UI.pause(400);
        UI.typewrite(Colors.YELLOW + "You raise a hand in greeting.", 22);
        UI.pause(500);
        UI.typewrite(Colors.YELLOW + "The Scrawny Guy's eyes narrow.", 22);
        UI.pause(400);
        UI.typewrite(Colors.RED + "He moves with terrifying speed.", 20);
        UI.pause(300);
        UI.typewrite(Colors.RED + Colors.BOLD + "Everything goes dark.", 18);
        UI.pause(600);
        triggerGameOver("ENDING 1 — DEATH BY SCRAWNY GUY\n  You should have listened to the rumors.");
    }

    /**
     * Triggers the true winning ending when the player successfully activates
     * the Time Machine with the correct code.
     */
    public void triggerTrueEnding() {
        System.out.println();
        UI.sectionHeader("── TRUE ENDING ──");
        UI.pause(500);
        UI.typewrite(Colors.WHITE + "You enter the code: " + Colors.GREEN + Colors.BOLD + "90928", 20);
        UI.pause(800);
        UI.typewrite("The Time Machine shudders. A blinding white light fills the office.", 18);
        UI.pause(1000);
        UI.typewrite("The air crackles. Time fractures around you.", 20);
        UI.pause(800);
        UI.typewrite(Colors.CYAN + "You feel yourself pulled backward through years, through choices...", 18);
        UI.pause(1200);
        UI.typewrite(Colors.WHITE + Colors.BOLD + "You land in a familiar hallway.", 20);
        UI.pause(600);
        UI.typewrite("Lockers line the walls. Nervous freshmen shuffle past.", 22);
        UI.pause(600);
        UI.typewrite("A sign reads: " + Colors.YELLOW + "\"AP Computer Science A — Room 114 — Orientation Today!\"", 20);
        UI.pause(800);
        UI.typewrite("You look down at your schedule. CSA is circled in red.", 22);
        UI.pause(600);
        UI.typewrite(Colors.WHITE + "You cross it out.", 22);
        UI.pause(1000);
        UI.typewrite(Colors.GREEN + Colors.BOLD + "Humanity is saved.", 20);
        UI.pause(500);
        System.out.println();
        System.out.println(Colors.GREEN + Colors.BOLD);
        System.out.println("  ╔═══════════════════════════════════════╗");
        System.out.println("  ║                                       ║");
        System.out.println("  ║         ✓  YOU WIN — TRUE ENDING      ║");
        System.out.println("  ║                                       ║");
        System.out.println("  ║   The AI uprising never happened.     ║");
        System.out.println("  ║   You never took AP CSA.              ║");
        System.out.println("  ║   Everything is fine now.             ║");
        System.out.println("  ║   (Probably.)                         ║");
        System.out.println("  ║                                       ║");
        System.out.println("  ╚═══════════════════════════════════════╝");
        System.out.println(Colors.RESET);
        running = false;
    }

    /**
     * Displays a formatted game-over screen with the given ending description,
     * then terminates the application.
     *
     * @param endingText the ending title and description to display
     */
    public void triggerGameOver(String endingText) {
        System.out.println();
        System.out.println(Colors.RED + Colors.BOLD);
        System.out.println("  ╔═════════════════════════════════════════╗");
        System.out.println("  ║              ☠  GAME OVER  ☠            ║");
        System.out.println("  ╠═════════════════════════════════════════╣");
        for (String line : endingText.split("\n")) {
            System.out.printf("  ║  %-39s║%n", line);
        }
        System.out.println("  ╚═════════════════════════════════════════╝");
        System.out.println(Colors.RESET);
        UI.pause(600);
        System.exit(0);
    }

    /**
     * Displays a stylized ASCII map of the prison layout showing all rooms
     * and their connections, with the player's current location highlighted.
     */
    private void displayMap() {
        String loc = player.getCurrentRoom().getName();
        System.out.println();
        UI.sectionHeader("── PRISON MAP ──");
        System.out.println(Colors.GRAY + "  (★ = your location)");
        System.out.println();

        String prisonLabel    = loc.equals("Prison Room")       ? Colors.GREEN + "[Prison Room ★]" + Colors.GRAY   : "[Prison Room]";
        String ventLabel      = loc.equals("Main Ventilation Duct") ? Colors.GREEN + "[Vent Duct ★]" + Colors.GRAY : "[Vent Duct]";
        String cafeLabel      = loc.equals("Cafeteria")         ? Colors.GREEN + "[Cafeteria ★]" + Colors.GRAY     : "[Cafeteria]";
        String letterLabel    = loc.equals("Letter Room")       ? Colors.GREEN + "[Letter Room ★]" + Colors.GRAY   : "[Letter Room]";
        String libraryLabel   = loc.equals("Library")           ? Colors.GREEN + "[Library ★]" + Colors.GRAY       : "[Library]";
        String yardLabel      = loc.equals("Yard")              ? Colors.GREEN + "[Yard ★]" + Colors.GRAY          : "[Yard]";
        String wardenLabel    = loc.equals("Warden's Office")   ? Colors.GREEN + "[Warden Office ★]" + Colors.GRAY : "[Warden's Office]";

        System.out.println(Colors.GRAY + "  Walking paths:  ──────");
        System.out.println("  Vent paths:     ══════");
        System.out.println();
        System.out.println("  " + prisonLabel + " ══════════╗");
        System.out.println("       │ (walk)         ║ (vent)");
        System.out.println("  " + cafeLabel + " ═══ " + ventLabel);
        System.out.println("       │                ║");
        System.out.println("  " + letterLabel + " ══╣");
        System.out.println("       │                ║");
        System.out.println("  " + libraryLabel + "          " + wardenLabel + " (vent only!)");
        System.out.println("       │");
        System.out.println("  " + yardLabel + " (fence ➜ bad ending!)");
        System.out.println(Colors.RESET);
    }
}