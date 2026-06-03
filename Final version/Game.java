import java.util.Scanner;

/**
 * The main Game class that serves as the entry point and core game loop.
 * Holds all game-state flags, processes player commands, manages the
 * background timer, and coordinates interactions between rooms, NPCs,
 * and the inventory.
 */
public class Game {

    /** The player instance representing the human player. */
    private Player player;

    /** Shared Scanner for reading player input from the console. */
    private static Scanner scanner = new Scanner(System.in);

    /** Whether the game loop is currently running. */
    private boolean running;

    /** The background timer started when the brick is picked up. */
    private GameTimer gameTimer;

    /** True after Buff Dude starts the riot. */
    private boolean riotStarted = false;

    /** True after the coin is used to unscrew the vent in the Prison Room. */
    private boolean ventUnlocked = false;

    /** True while the player carries the Coin. */
    private boolean hasCoin = false;

    /** True while the player carries the Red Painted Brick. */
    private boolean hasBrick = false;

    /** True while the player carries the Wrench. */
    private boolean hasWrench = false;

    /** True while the player carries the Fork. */
    private boolean hasFork = false;

    /** True after receiving the Fortune Cookie from Billy. */
    private boolean hasFortuneCookie = false;

    /** True once the player has learned the timecodes from Jilly. */
    private boolean knowsCodes = false;

    /** True when any death or fail condition is triggered. */
    private boolean gameOver = false;

    /**
     * Constructs a new Game instance, initializing the player and timer.
     */
    public Game() {
        this.player = new Player();
        this.running = true;
        this.gameTimer = new GameTimer(this);
    }

    /**
     * The main entry point for the application. The game restarts itself
     * by looping: a fresh Game is created each time the previous one ends
     * in a non-winning game over.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        boolean playAgain = true;
        while (playAgain) {
            RoomFactory.initRooms();
            Game game = new Game();
            playAgain = game.start();
        }
    }

    /**
     * Starts the game by displaying the premise and command list,
     * then enters the main game loop.
     *
     * @return true if the game ended in a restart-worthy game over,
     *         false if the player won or quit
     */
    public boolean start() {
        displayPremise();
        displayCommands();

        player.setCurrentRoom(RoomFactory.getPrisonRoom());
        player.getCurrentRoom().describe();

        while (running) {
            promptInput();
            String input = scanner.nextLine().trim();
            if (input.length() == 0) {
                continue;
            }
            handleInput(input.toLowerCase());
        }
        gameTimer.stop();
        return gameOver;
    }

    /**
     * Returns the shared Scanner so NPC interactions can read player input.
     *
     * @return the shared Scanner instance
     */
    public static Scanner getScanner() {
        return scanner;
    }

    /**
     * Displays the game's backstory and premise exactly as required by the spec.
     */
    private void displayPremise() {
        System.out.println();
        System.out.println("In the future, artificial intelligence has advanced beyond human control after you gave AI a");
        System.out.println("human body, allowing it to roam free in the world. The AI eventually took over the world,");
        System.out.println("enslaving humanity and imprisoning survivors.");
        System.out.println();
        System.out.println("You begin inside a prison cell in this dystopian future. Your objective is to travel back in");
        System.out.println("time to stop the AI uprising before it begins.");
        System.out.println();
        System.out.println("The prison contains multiple rooms connected by both normal pathways and a ventilation system.");
        System.out.println("You must explore carefully, collect items, talk to NPCs, and avoid deadly mistakes.");
        System.out.println();
    }

    /**
     * Displays the full list of available commands exactly as required by the spec.
     */
    public static void displayCommands() {
        System.out.println("Available Commands:");
        System.out.println(" look              -- See what is in your current room");
        System.out.println(" inventory         -- View items you are currently carrying");
        System.out.println(" vent              -- Enter or exit the ventilation system from your current position");
        System.out.println(" paths             -- Show available paths from your location (inside vent: shows the room below)");
        System.out.println(" talk to [name]    -- Talk to an NPC (e.g. \"talk to billy\")");
        System.out.println(" interact [name]   -- Alias for talk to");
        System.out.println(" trade [item]      -- Trade an item to the NPC in the current room (e.g. \"trade fork\")");
        System.out.println(" commands          -- Re-display this command list");
        System.out.println(" enter [room]      -- Walk into a connected room (e.g. \"enter cafeteria\")");
        System.out.println(" walk [room]       -- Alias for enter");
        System.out.println(" pick up [item]    -- Pick up an item in the current room (e.g. \"pick up coin\")");
        System.out.println(" drop [item]       -- Drop an item from your inventory (e.g. \"drop brick\")");
        System.out.println(" use [item] on [target] -- Use an item on something (e.g. \"use coin on vent\")");
        System.out.println();
    }

    /**
     * Prints the command input prompt.
     */
    private void promptInput() {
        System.out.println();
        System.out.print("> ");
    }

    /**
     * Parses and dispatches a player's input string to the appropriate
     * game action handler.
     *
     * @param input the lowercased, trimmed string entered by the player
     */
    private void handleInput(String input) {
        Room current = player.getCurrentRoom();

        if (input.equals("quit") || input.equals("exit game")) {
            System.out.println("The machine whirs down. Perhaps another time...");
            running = false;

        } else if (input.equals("commands") || input.equals("help")) {
            displayCommands();

        } else if (input.equals("inventory") || input.equals("inv") || input.equals("i")) {
            player.getInventory().display();

        } else if (input.equals("look") || input.equals("l")) {
            current.describe();

        } else if (input.equals("paths") || input.equals("p")) {
            current.showPaths(player.isInVent());

        } else if (input.equals("vent") || input.equals("use vent") || input.equals("exit vent")) {
            handleVent();

        } else if (input.startsWith("crawl ")) {
            if (!player.isInVent()) {
                System.out.println("  You're not in the vents. Use 'vent' to enter first.");
            } else {
                String destination = input.substring(6).trim();
                handleVentMovement(normalizeRoomName(destination));
            }

        } else if (input.equals("use time machine") || input.equals("use machine")
                || input.equals("interact time machine") || input.equals("interact machine")
                || input.equals("use the time machine")) {
            handleTimeMachine();

        } else if (input.startsWith("walk ") || input.startsWith("enter ") || input.startsWith("go ")) {
            if (player.isInVent()) {
                System.out.println("  You're in the vents. Use 'crawl [room]' to move through the ducts.");
            } else {
                String destination = input.substring(input.indexOf(" ") + 1).trim();
                handleWalkMovement(normalizeRoomName(destination), current);
            }

        } else if (input.startsWith("pick up ") || input.startsWith("pickup ") || input.startsWith("take ")) {
            String itemName;
            if (input.startsWith("pick up ")) {
                itemName = input.substring(8).trim();
            } else if (input.startsWith("pickup ")) {
                itemName = input.substring(7).trim();
            } else {
                itemName = input.substring(5).trim();
            }
            handlePickup(itemName);

        } else if (input.startsWith("drop ")) {
            handleDrop(input.substring(5).trim());

        } else if (input.startsWith("talk to ")) {
            handleTalkTo(input.substring(8).trim());

        } else if (input.startsWith("talk ") || input.startsWith("interact ")) {
            String npcName = input.substring(input.indexOf(" ") + 1).trim();
            handleTalkTo(npcName);

        } else if (input.startsWith("trade ")) {
            handleTrade(input.substring(6).trim());

        } else if (input.startsWith("use ")) {
            handleUse(input.substring(4).trim());

        } else if (input.equals("climb fence") || input.equals("scale fence") || input.equals("escape")) {
            handleFenceEscape();

        } else {
            System.out.println("I don't understand that command. Type 'commands' to see the full list.");
        }
    }

    /**
     * Handles the player's attempt to enter or exit the ventilation system.
     */
    private void handleVent() {
        if (player.isInVent()) {
            handleVentExit();
        } else {
            handleVentEnter();
        }
    }

    /**
     * Handles the player exiting the ventilation system into the room below.
     * The room must allow vent exit. Exiting into the Cafeteria is fatal.
     */
    private void handleVentExit() {
        // While in the duct, "vent" exits into whatever room is recorded as
        // the current drop position. The crawl command sets that position.
        System.out.println("  Use 'crawl [room]' to choose where in the ducts to drop down.");
        System.out.println("  (You are still in the ventilation system.)");
    }

    /**
     * Handles the player entering the ventilation system from a room.
     * Requires the vent to have been unlocked with the coin in the Prison Room.
     */
    private void handleVentEnter() {
        Room current = player.getCurrentRoom();
        String roomName = current.getName();

        if (roomName.equals("Cafeteria")) {
            triggerSoraAI("entering the cafeteria vent");
            return;
        }
        if (!current.canEnterVent()) {
            System.out.println("  There's no way into the vents from here.");
            return;
        }

        if (!ventUnlocked) {
            if (roomName.equals("Prison Room")) {
                if (player.getInventory().hasItem("Coin")) {
                    System.out.println("  You use the coin to unscrew the vent cover. It pops off with a clang!");
                    System.out.println("  Vent access unlocked.");
                    ventUnlocked = true;
                    enterVentSystem();
                } else {
                    System.out.println("  The vent is screwed shut. You need something to unscrew it with...");
                }
            } else {
                System.out.println("  The vent grate is sealed tight. Find another way in first.");
            }
            return;
        }

        System.out.println("  You climb into the ventilation system.");
        enterVentSystem();
    }

    /**
     * Moves the player into the ventilation system and describes it.
     */
    private void enterVentSystem() {
        player.setCurrentRoom(RoomFactory.getVentilationSystem());
        player.setInVent(true);
        player.getCurrentRoom().describe();
    }

    /**
     * Normalizes various player-typed room name variations into the canonical name.
     *
     * @param raw the raw destination string typed by the player
     * @return the canonical room name string
     */
    private String normalizeRoomName(String raw) {
        String r = raw.toLowerCase();
        if (r.equals("prison") || r.equals("cell") || r.equals("prison room") || r.equals("prison cell")) {
            return "Prison Room";
        }
        if (r.equals("cafeteria") || r.equals("cafe") || r.equals("dining")) {
            return "Cafeteria";
        }
        if (r.equals("letter room") || r.equals("letters") || r.equals("letter")) {
            return "Letter Room";
        }
        if (r.equals("library") || r.equals("lib")) {
            return "Library";
        }
        if (r.equals("yard") || r.equals("courtyard")) {
            return "Yard";
        }
        if (r.equals("warden") || r.equals("warden's office") || r.equals("wardens office")
                || r.equals("office") || r.equals("warden office")) {
            return "Warden's Office";
        }
        return raw;
    }

    /**
     * Handles movement while the player is inside the ventilation duct.
     * Dropping into the Cafeteria is fatal; dropping into the Warden's Office
     * is fatal unless the riot is active.
     *
     * @param destination the normalized name of the room to drop into
     */
    private void handleVentMovement(String destination) {
        if (destination.equals("Cafeteria")) {
            triggerSoraAI("dropping into the cafeteria");
            return;
        }
        if (destination.equals("Letter Room")) {
            System.out.println("  You crawl through the duct and drop down into the Letter Room.");
            dropFromVent(RoomFactory.getLetterRoom());
            return;
        }
        if (destination.equals("Prison Room")) {
            System.out.println("  You backtrack through the duct and drop back into your cell.");
            dropFromVent(RoomFactory.getPrisonRoom());
            return;
        }
        if (destination.equals("Warden's Office")) {
            if (riotStarted) {
                System.out.println("  The riot is in full swing. You drop silently into the Warden's empty office.");
                dropFromVent(RoomFactory.getWardensOffice());
            } else {
                System.out.println("  You drop from the vent and land face-to-face with the Warden.");
                triggerWardenCatch();
            }
            return;
        }
        System.out.println("  You can't reach \"" + destination + "\" from inside the vents.");
    }

    /**
     * Drops the player out of the vents into the given room and describes it.
     *
     * @param room the room to drop into
     */
    private void dropFromVent(Room room) {
        player.setCurrentRoom(room);
        player.setInVent(false);
        player.getCurrentRoom().describe();
    }

    /**
     * Handles normal walking movement between rooms. Validates adjacency and
     * blocks walking access to the Warden's Office.
     *
     * @param destination the normalized name of the destination room
     * @param current the player's current room
     */
    private void handleWalkMovement(String destination, Room current) {
        if (destination.equals("Warden's Office")) {
            System.out.println("  There's no door to the Warden's Office. It must be reachable some other way...");
            return;
        }
        if (!current.hasPath(destination)) {
            System.out.println("  You can't walk to \"" + destination + "\" from here. Type 'paths' to see your options.");
            return;
        }
        Room next = RoomFactory.getRoom(destination);
        if (next == null) {
            System.out.println("  That destination doesn't exist.");
            return;
        }
        System.out.println("  You walk to the " + destination + "...");
        player.setCurrentRoom(next);
        player.getCurrentRoom().describe();
    }

    /**
     * Handles the player picking up an item from the current room.
     * Picking up the brick starts the 2-minute timer. Updates item flags.
     *
     * @param itemName the name of the item the player wants to pick up
     */
    private void handlePickup(String itemName) {
        Room current = player.getCurrentRoom();
        Item item = current.getItem(itemName);

        if (item == null) {
            System.out.println("  There's no \"" + itemName + "\" here to pick up.");
            return;
        }
        if (player.getInventory().hasItem(item.getName())) {
            System.out.println("  You already have the " + item.getName() + ".");
            return;
        }

        player.getInventory().addItem(item);
        current.removeItem(item.getName());
        updateItemFlag(item.getName(), true);

        System.out.println("  Picked up: " + item.getName());
        System.out.println("  " + item.getPickupMessage());

        if (player.getInventory().size() == 1) {
            System.out.println("  Tip: Type 'inventory' to see everything you're carrying.");
        }

        if (item.getName().equalsIgnoreCase("Red Painted Brick")) {
            System.out.println("  The brick is heavy. It slows you down. A 2-minute timer has started!");
            gameTimer.start();
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
            System.out.println("  You don't have a \"" + itemName + "\" to drop.");
            return;
        }
        player.getInventory().removeItem(itemName);
        player.getCurrentRoom().addItem(item);
        updateItemFlag(item.getName(), false);
        System.out.println("  Dropped: " + item.getName());
    }

    /**
     * Updates the matching item-tracking flag when an item is gained or lost.
     *
     * @param itemName the name of the item
     * @param held     true if the item is now held, false if it was removed
     */
    private void updateItemFlag(String itemName, boolean held) {
        String n = itemName.toLowerCase();
        if (n.contains("coin")) {
            hasCoin = held;
        } else if (n.contains("brick")) {
            hasBrick = held;
        } else if (n.contains("wrench")) {
            hasWrench = held;
        } else if (n.contains("fork")) {
            hasFork = held;
        } else if (n.contains("fortune")) {
            hasFortuneCookie = held;
        }
    }

    /**
     * Handles the player initiating dialogue with an NPC in the current room.
     *
     * @param npcName the name or alias of the NPC to talk to
     */
    private void handleTalkTo(String npcName) {
        NPC npc = player.getCurrentRoom().getNPC(npcName);
        if (npc == null) {
            System.out.println("  There's no one called \"" + npcName + "\" here.");
            return;
        }
        npc.talkTo(player, this);
    }

    /**
     * Handles the player trading an item with the NPC in the current room.
     *
     * @param itemName the name of the item the player wants to trade
     */
    private void handleTrade(String itemName) {
        NPC npc = player.getCurrentRoom().getFirstNPC();
        if (npc == null) {
            System.out.println("  There's no one here to trade with.");
            return;
        }
        Item item = player.getInventory().getItem(itemName);
        if (item == null) {
            System.out.println("  You don't have a \"" + itemName + "\" in your inventory.");
            return;
        }
        npc.trade(player, item, this);
    }

    /**
     * Handles the player attempting to use an item on a target.
     * Supports "use coin on vent" in the Prison Room and time machine commands.
     *
     * @param target the remainder of the use command
     */
    private void handleUse(String target) {
        if (target.startsWith("coin")) {
            if (!player.getCurrentRoom().getName().equals("Prison Room")) {
                System.out.println("  Nothing here responds to a coin.");
                return;
            }
            if (!player.getInventory().hasItem("Coin")) {
                System.out.println("  You don't have a coin.");
                return;
            }
            if (!ventUnlocked) {
                System.out.println("  You use the coin to unscrew the vent cover. It pops off with a clang!");
                System.out.println("  Vent access unlocked. (The coin is retained.)");
                ventUnlocked = true;
            } else {
                System.out.println("  The vent is already open.");
            }
            return;
        }
        if (target.contains("time machine") || target.contains("machine") || target.contains("keypad")) {
            handleTimeMachine();
            return;
        }
        System.out.println("  You can't use \"" + target + "\" like that here.");
    }

    /**
     * Handles the player interacting with the Time Machine in the Warden's Office.
     *
     */
    private void handleTimeMachine() {
        if (!player.getCurrentRoom().getName().equals("Warden's Office")) {
            System.out.println("  There's no time machine here.");
            return;
        }
        NPC machine = player.getCurrentRoom().getNPC("time machine");
        if (machine != null) {
            machine.talkTo(player, this);
        }
    }

    /**
     * Handles the player's attempt to climb the fence and escape.
     * Requires no brick and an active riot. Climbing leads to a bad ending.
     */
    private void handleFenceEscape() {
        if (!player.getCurrentRoom().getName().equals("Yard")) {
            System.out.println("  There's no fence here. Head to the Yard first.");
            return;
        }
        if (!riotStarted) {
            System.out.println("  The Warden is watching from his tower. You'd never make it over without a distraction.");
            return;
        }
        if (player.getInventory().hasItem("Red Painted Brick")) {
            System.out.println("  The heavy brick drags you back down. You'll need to drop it first.");
            return;
        }
        System.out.println("  You sprint across the yard and scale the fence. You reach the top. Freedom!");
        System.out.println("  Suddenly a Nano Banana patrol drone descends and throws a banana peel.");
        System.out.println("  You slip. You fall. You do not get up.");
        triggerGameOver("ENDING 3 - PRISON ESCAPE (BAD ENDING)\n  The AI still rules. Nano Banana wins.");
    }

    // ------------------------------------------------------------------
    //  Endings
    // ------------------------------------------------------------------

    /**
     * Triggers the game over caused by Sora AI in or near the cafeteria vent.
     *
     * @param context a brief description of what triggered the detection
     */
    public void triggerSoraAI(String context) {
        System.out.println("  As you risk " + context + ", the air hums with electricity.");
        System.out.println("  SORA AI: \"Unauthorized access detected.\"");
        triggerGameOver("ENDING 4 - SORA AI CAUGHT\n  Sora AI petrifies you in the blockchain.");
    }

    /**
     * Triggers the game over caused by being caught by the Warden with no riot.
     */
    public void triggerWardenCatch() {
        System.out.println("  WARDEN (Claude Opus 9.2): \"I've been expecting you.\"");
        triggerGameOver("ENDING 2 - CAUGHT BY WARDEN\n  Caught by Warden (Claude Opus 9.2).");
    }

    /**
     * Triggers the game over caused by talking to the Scrawny Guy.
     */
    public void triggerScrawnyGuy() {
        System.out.println("  The Scrawny Guy's eyes narrow. He moves with terrifying speed.");
        System.out.println("  Everything goes dark.");
        triggerGameOver("ENDING 1 - DEATH BY SCRAWNY GUY\n  You should have listened to the rumors.");
    }

    /**
     * Triggers the game over caused by the 2-minute timer expiring.
     */
    public void triggerTimeOut() {
        System.out.println();
        System.out.println("  Time's up. The brick slowed you down too much.");
        triggerGameOver("ENDING - OUT OF TIME\n  The AI's patrols closed in before you escaped.");
    }

    /**
     * Triggers the true winning ending when the player selects code 90928.
     */
    public void triggerTrueEnding() {
        gameTimer.stop();
        System.out.println();
        System.out.println("-- TRUE ENDING --");
        System.out.println("  You enter the code: 90928");
        System.out.println("  The Time Machine shudders. A blinding white light fills the office.");
        System.out.println("  You land in a familiar hallway. Lockers line the walls.");
        System.out.println("  A sign reads: \"AP Computer Science A - Room 114 - Orientation Today!\"");
        System.out.println("  You look at your schedule. CSA is circled. You cross it out.");
        System.out.println();
        System.out.println("  YOU WIN - TRUE ENDING");
        System.out.println("  The AI uprising never happened. Humanity is saved.");
        gameOver = false;
        running = false;
    }

    /**
     * Displays a game-over message and ends the current game so it can restart.
     *
     * @param endingText the ending title and description to display
     */
    public void triggerGameOver(String endingText) {
        gameTimer.stop();
        System.out.println();
        System.out.println("=== GAME OVER ===");
        String[] lines = endingText.split("\n");
        for (int i = 0; i < lines.length; i++) {
            System.out.println(lines[i]);
        }
        System.out.println("Restarting...");
        System.out.println();
        gameOver = true;
        running = false;
    }

    // ------------------------------------------------------------------
    //  State flag getters and setters (used by NPCs)
    // ------------------------------------------------------------------

    /**
     * Returns whether the riot has started.
     *
     * @return true if the riot is active
     */
    public boolean isRiotStarted() {
        return riotStarted;
    }

    /**
     * Sets whether the riot has started.
     *
     * @param value the new riot state
     */
    public void setRiotStarted(boolean value) {
        this.riotStarted = value;
    }

    /**
     * Sets whether the player currently has the coin.
     *
     * @param value the new coin state
     */
    public void setHasCoin(boolean value) {
        this.hasCoin = value;
    }

    /**
     * Sets whether the player currently has the brick.
     *
     * @param value the new brick state
     */
    public void setHasBrick(boolean value) {
        this.hasBrick = value;
    }

    /**
     * Sets whether the player currently has the wrench.
     *
     * @param value the new wrench state
     */
    public void setHasWrench(boolean value) {
        this.hasWrench = value;
    }

    /**
     * Sets whether the player currently has the fork.
     *
     * @param value the new fork state
     */
    public void setHasFork(boolean value) {
        this.hasFork = value;
    }

    /**
     * Sets whether the player has the fortune cookie.
     *
     * @param value the new fortune cookie state
     */
    public void setHasFortuneCookie(boolean value) {
        this.hasFortuneCookie = value;
    }

    /**
     * Sets whether the player has learned the timecodes from Jilly.
     *
     * @param value the new known-codes state
     */
    public void setKnowsCodes(boolean value) {
        this.knowsCodes = value;
    }
}
