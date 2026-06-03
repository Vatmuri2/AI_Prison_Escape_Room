import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Abstract base class for all Non-Player Characters (NPCs) in the game.
 * Each NPC has a name, a short description, and a list of name aliases
 * for flexible player input matching. Subclasses implement specific
 * dialogue and trade interactions.
 */
public abstract class NPC {

    /** The display name of this NPC. */
    protected String name;

    /** A brief description shown in room listings. */
    protected String shortDesc;

    /** Aliases the player can use to reference this NPC (e.g., "buff", "dude"). */
    protected List<String> aliases;

    /**
     * Constructs an NPC with a name, description, and name aliases.
     *
     * @param name      the display name
     * @param shortDesc brief room-listing description
     * @param aliases   alternative names the player may type
     */
    public NPC(String name, String shortDesc, String... aliases) {
        this.name = name;
        this.shortDesc = shortDesc;
        this.aliases = Arrays.asList(aliases);
    }

    /**
     * Returns the display name of this NPC.
     *
     * @return the NPC name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the brief description of this NPC shown in room listings.
     *
     * @return the short description
     */
    public String getShortDesc() {
        return shortDesc;
    }

    /**
     * Returns whether the given input matches this NPC's name or any of its aliases.
     * Matching is case-insensitive and checks for partial containment.
     *
     * @param input the player's typed NPC name
     * @return true if this NPC matches the input
     */
    public boolean matchesName(String input) {
        if (name.toLowerCase().contains(input.toLowerCase())) return true;
        for (String alias : aliases) {
            if (alias.toLowerCase().contains(input.toLowerCase()) ||
                input.toLowerCase().contains(alias.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the player initiating dialogue with this NPC.
     *
     * @param player the Player instance
     * @param game   the current Game instance (for triggering game-over states)
     */
    public abstract void talkTo(Player player, Game game);

    /**
     * Handles the player attempting to trade an item with this NPC.
     *
     * @param player the Player instance
     * @param item   the Item the player is offering
     * @param game   the current Game instance
     */
    public abstract void trade(Player player, Item item, Game game);
}


// ────────────────────────────────────────────────────────────────────────────
//  BILLY — Cafeteria NPC
// ────────────────────────────────────────────────────────────────────────────

/**
 * Billy is the cafeteria manager NPC. He trades a Fortune Cookie
 * (containing the Time Machine code) for a Fork. He will not deal
 * with any other items.
 */
class Billy extends NPC {

    /** Whether Billy has already made his trade with the player. */
    private boolean traded = false;

    /**
     * Constructs the Billy NPC with appropriate aliases.
     */
    public Billy() {
        super("Billy", "Cafeteria manager. Wiry, watching you carefully.",
              "billy", "chef", "cafeteria guy", "cook");
    }

    /**
     * Billy's dialogue. He hints that he has something worth trading
     * and asks what the player has to offer.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (traded) {
            UI.npcSay("Billy", "We already did business. Enjoy the cookie.");
            return;
        }
        UI.npcSay("Billy", "I don't talk for free, friend.");
        UI.npcSay("Billy", "But I might have something useful for someone willing to trade.");
        UI.npcSay("Billy", "I could use a proper utensil. Something... forked.");
        UI.typewrite(Colors.GRAY +
            "  (Hint: try " + Colors.WHITE + "trade fork" + Colors.GRAY + ")", 20);
    }

    /**
     * Handles a trade offer to Billy. Accepts only a Fork and returns a Fortune Cookie.
     * Rejects all other items with a dismissive comment.
     *
     * @param player the Player instance
     * @param item   the Item the player is offering
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        if (traded) {
            UI.npcSay("Billy", "We already made our deal. Move along.");
            return;
        }
        if (item.getName().equalsIgnoreCase("Fork")) {
            player.getInventory().removeItem("Fork");
            Item cookie = ItemFactory.fortuneCookie();
            player.getInventory().addItem(cookie);
            traded = true;
            UI.npcSay("Billy", "A fork. Perfect.");
            UI.pause(400);
            UI.npcSay("Billy", "Take this fortune cookie. The message inside... opens something important.");
            UI.typewrite(Colors.GREEN + "  Received: Fortune Cookie", 20);
            UI.pause(300);
            UI.typewrite(Colors.GRAY + "  You crack it open. The fortune reads:", 20);
            UI.typewrite(Colors.WHITE + "  \"Time Machine access password: FRESHMAN\"", 20);
            UI.typewrite(Colors.GRAY +
                "  That gets you in. You'll need someone else for the destination timecode.", 22);
        } else {
            UI.npcSay("Billy", "That's not what I want. I said something forked.");
        }
    }
}


// ────────────────────────────────────────────────────────────────────────────
//  JILLY — Letter Room NPC
// ────────────────────────────────────────────────────────────────────────────

/**
 * Jilly is the Letter Room NPC who provides the three possible Time Machine
 * timecodes when the player shows her the Fortune Cookie. She is the key
 * to knowing which date to select.
 */
class Jilly extends NPC {

    /** Whether Jilly has already given the player the timecodes. */
    private boolean sharedCodes = false;

    /**
     * Constructs the Jilly NPC with appropriate aliases.
     */
    public Jilly() {
        super("Jilly", "Sorts mail with mechanical precision. Knows things.",
              "jilly", "letter lady", "mail lady");
    }

    /**
     * Jilly's dialogue. If the player has the Fortune Cookie, she provides
     * all three Time Machine timecodes. Otherwise she hints at what she needs to see.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (sharedCodes) {
            UI.npcSay("Jilly", "I already told you everything I know.");
            UI.npcSay("Jilly", "Remember: Freshman Orientation. Don't second-guess yourself.");
            return;
        }

        if (!player.getInventory().hasItem("Fortune Cookie")) {
            UI.npcSay("Jilly", "I sort the Warden's correspondence. I see everything that passes through.");
            UI.pause(300);
            UI.npcSay("Jilly", "But I don't share information with just anyone.");
            UI.npcSay("Jilly", "Come back when you have proof you've been doing your homework.");
            UI.npcSay("Jilly", "Billy in the cafeteria owes me a favor. Start there.");
            return;
        }

        UI.npcSay("Jilly", "The fortune cookie. You spoke to Billy.");
        UI.pause(400);
        UI.npcSay("Jilly", "Good. Then you know what that password opens.");
        UI.pause(300);
        UI.npcSay("Jilly", "I intercepted three destination timecodes from the Warden's files:");
        System.out.println();
        System.out.println(Colors.YELLOW + "  ┌─────────────────────────────────────────────────────┐");
        System.out.println("  │              TIME MACHINE DESTINATION CODES          │");
        System.out.println("  ├─────────────────────────────────────────────────────┤");
        System.out.println("  │  Far Future                   ─────  " + Colors.WHITE + "01382" + Colors.YELLOW + "           │");
        System.out.println("  │  Day Your Crush Rejected You  ─────  " + Colors.WHITE + "18572" + Colors.YELLOW + "           │");
        System.out.println("  │  High School Freshman Orient. ─────  " + Colors.WHITE + "90928" + Colors.YELLOW + "           │");
        System.out.println("  └─────────────────────────────────────────────────────┘");
        System.out.println(Colors.RESET);
        UI.pause(400);
        UI.npcSay("Jilly", "Think carefully about which one stops the problem at its source.");
        UI.npcSay("Jilly", "You already have the password. Now you just need to get into that office.");
        sharedCodes = true;
    }

    /**
     * Jilly does not accept trades. She redirects the player to talk to her instead.
     *
     * @param player the Player instance
     * @param item   the Item being offered
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        UI.npcSay("Jilly", "I don't want things. I deal in information.");
        UI.typewrite(Colors.GRAY + "  (Try: " + Colors.WHITE + "talk to jilly" + Colors.GRAY + ")", 22);
    }
}


// ────────────────────────────────────────────────────────────────────────────
//  BOOKWORM — Library NPC
// ────────────────────────────────────────────────────────────────────────────

/**
 * The Bookworm is the Library NPC who trades information for items.
 * Each item traded reveals a different useful hint about the game world.
 * Trades are one-time — the same item cannot be traded twice.
 */
class Bookworm extends NPC {

    /** Whether the Bookworm has already received the wrench. */
    private boolean gotWrench = false;

    /** Whether the Bookworm has already received the coin. */
    private boolean gotCoin = false;

    /** Whether the Bookworm has already received the brick. */
    private boolean gotBrick = false;

    /**
     * Constructs the Bookworm NPC with appropriate aliases.
     */
    public Bookworm() {
        super("Bookworm", "Absorbed in a decaying novel. Notices more than they let on.",
              "bookworm", "reader", "book guy", "book woman", "librarian");
    }

    /**
     * The Bookworm's opening dialogue, hinting at their trade system.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        UI.npcSay("Bookworm", "...");
        UI.pause(400);
        UI.npcSay("Bookworm", "Information has a price. Everything in here does.");
        UI.npcSay("Bookworm", "Bring me something and I'll tell you what I know.");
        UI.typewrite(Colors.GRAY + "  (Trade items to learn information about the prison.)", 22);
        System.out.println(Colors.GRAY +
            "  Try: " + Colors.WHITE + "trade wrench" + Colors.GRAY + " / " +
            Colors.WHITE + "trade coin" + Colors.GRAY + " / " +
            Colors.WHITE + "trade brick" + Colors.RESET);
    }

    /**
     * Handles trade offers from the player. Each unique item reveals different information.
     * Wrench → Yard/Fence info, Coin → Scrawny Guy warning, Brick → Warden's Office hint.
     *
     * @param player the Player instance
     * @param item   the Item being offered
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        String itemName = item.getName().toLowerCase();

        if (itemName.contains("wrench")) {
            if (gotWrench) {
                UI.npcSay("Bookworm", "I already told you about the fence. Pay attention.");
                return;
            }
            player.getInventory().removeItem("Wrench");
            gotWrench = true;
            UI.npcSay("Bookworm", "A wrench. Practical.");
            UI.pause(300);
            UI.npcSay("Bookworm", "The yard has a fence. Tall. Razor-tipped.");
            UI.npcSay("Bookworm", "I've seen people try to climb it. They don't get far.");
            UI.npcSay("Bookworm", "You'd need a distraction. Something big enough to pull the Warden's attention.");
            UI.npcSay("Bookworm", "And you'd need to be... unencumbered. Nothing heavy.");

        } else if (itemName.contains("coin")) {
            if (gotCoin) {
                UI.npcSay("Bookworm", "I told you about the Scrawny Guy. Don't engage him. Ever.");
                return;
            }
            player.getInventory().removeItem("Coin");
            gotCoin = true;
            UI.npcSay("Bookworm", "A coin. Hmm.");
            UI.pause(300);
            UI.npcSay("Bookworm", "There's a man in the yard. Thin. Looks harmless.");
            UI.npcSay("Bookworm", "He is not.");
            UI.pause(400);
            UI.npcSay("Bookworm", "Whatever you do — do NOT talk to him.");
            UI.npcSay("Bookworm", "I've seen what he does. This library has a lot of empty chairs.");

        } else if (itemName.contains("brick")) {
            if (gotBrick) {
                UI.npcSay("Bookworm", "I told you about the Warden's Office. Go find the riot first.");
                return;
            }
            player.getInventory().removeItem("Red Painted Brick");
            gotBrick = true;
            UI.npcSay("Bookworm", "A painted brick. You've been in the vents.");
            UI.pause(300);
            UI.npcSay("Bookworm", "There's an office. The Warden never leaves it.");
            UI.npcSay("Bookworm", "It's not on any map. No door from the hallways.");
            UI.npcSay("Bookworm", "But the walls are thin. The vents run everywhere.");
            UI.npcSay("Bookworm", "And if the Warden were... distracted... his office would be empty.");
            UI.npcSay("Bookworm", "Something in there. Something important. That's all I know.");

        } else {
            UI.npcSay("Bookworm", "I have no use for that.");
            UI.npcSay("Bookworm", "Wrench, coin, or the brick — those I can work with.");
        }
    }
}


// ────────────────────────────────────────────────────────────────────────────
//  BUFF DUDE — Yard NPC
// ────────────────────────────────────────────────────────────────────────────

/**
 * The Big Buff Dude is a friendly Yard NPC who will start a prison riot
 * in exchange for a Coin. The riot distracts the Warden, enabling safe
 * access to the Warden's Office and the fence escape.
 */
class BuffDude extends NPC {

    /** Whether the Buff Dude has already agreed to start the riot. */
    private boolean riotAgreed = false;

    /**
     * Constructs the BuffDude NPC with appropriate aliases.
     */
    public BuffDude() {
        super("Big Buff Dude", "Enormous. Friendly. Currently doing 300 reps.",
              "buff dude", "buff", "dude", "big buff", "buff guy", "buff man", "big guy");
    }

    /**
     * The Buff Dude's dialogue. He offers to start a riot for a coin.
     * If already agreed, confirms the riot is active.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (riotAgreed) {
            UI.npcSay("Big Buff Dude", "*still causing chaos* I got you, bro! \\o/");
            return;
        }
        UI.npcSay("Big Buff Dude", "Yo! You look like you got a plan.");
        UI.pause(300);
        UI.npcSay("Big Buff Dude", "I've been WAITING to cause some mayhem in this place.");
        UI.npcSay("Big Buff Dude", "Give me a coin — just a little something to make it official —");
        UI.npcSay("Big Buff Dude", "and I'll start a riot so loud the Warden won't know which way is up.");
        UI.typewrite(Colors.GRAY + "  (Try: " + Colors.WHITE + "trade coin" + Colors.GRAY + ")", 22);
    }

    /**
     * Handles the trade. If the player offers a Coin, the Buff Dude starts the riot
     * and sets the global riotStarted flag. Rejects all other items.
     *
     * @param player the Player instance
     * @param item   the Item being offered
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        if (riotAgreed) {
            UI.npcSay("Big Buff Dude", "We're already going! WOOO!");
            return;
        }
        if (item.getName().equalsIgnoreCase("Coin")) {
            player.getInventory().addItem(item); // coin is retained
            riotAgreed = true;
            Game.riotStarted = true;
            UI.npcSay("Big Buff Dude", "OH. IT. IS. ON.");
            UI.pause(500);
            UI.typewrite(Colors.RED + Colors.BOLD +
                "  The Buff Dude throws a table. Then another. Chaos erupts across the yard.", 16);
            UI.pause(600);
            UI.typewrite(Colors.RED + "  Guards flood the yard. The Warden abandons his office.", 18);
            UI.pause(400);
            UI.typewrite(Colors.GREEN + Colors.BOLD +
                "  ★ Prison riot active — the Warden's Office is now unguarded.", 20);
            UI.typewrite(Colors.GREEN +
                "  ★ The fence is now climbable (if you're not carrying that heavy brick).", 20);
            UI.typewrite(Colors.GRAY + "  (Remember: the vents connect to the Warden's Office.)", 22);
        } else {
            UI.npcSay("Big Buff Dude", "Nah bro, I need something rounder. A coin. Classic.");
        }
    }
}


// ────────────────────────────────────────────────────────────────────────────
//  SCRAWNY GUY — Yard NPC
// ────────────────────────────────────────────────────────────────────────────

/**
 * The Scrawny Guy is a lethal Yard NPC. Any attempt to interact with him
 * results in an immediate game over. He accepts no trades and cannot be reasoned with.
 */
class ScrawnyGuy extends NPC {

    /**
     * Constructs the ScrawnyGuy NPC with appropriate aliases.
     */
    public ScrawnyGuy() {
        super("Scrawny Guy", "Leaning against the wall. Something feels deeply wrong.",
              "scrawny", "scrawny guy", "thin guy", "slim", "guy against the wall");
    }

    /**
     * Talking to the Scrawny Guy triggers an immediate game-over death sequence.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        game.triggerScrawnyGuy();
    }

    /**
     * The Scrawny Guy doesn't trade — he kills. Triggers game over.
     *
     * @param player the Player instance
     * @param item   the Item being offered (ignored)
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        game.triggerScrawnyGuy();
    }
}


// ────────────────────────────────────────────────────────────────────────────
//  WARDEN — Warden's Office NPC
// ────────────────────────────────────────────────────────────────────────────

/**
 * The Warden (running Claude Opus 9.2) is found in his office.
 * If the player arrives without an active riot, interacting with him triggers
 * a game over. If the riot is active, the office is empty and the Warden
 * NPC behaves as a dormant presence. The Time Machine is accessed here.
 */
class Warden extends NPC {

    /** Whether the player has successfully entered the access password. */
    private boolean passwordEntered = false;

    /** Whether the player has already entered the correct destination code. */
    private boolean codeEntered = false;

    /**
     * Constructs the Warden NPC with appropriate aliases.
     */
    public Warden() {
        super("Time Machine", "A hulking device under a holographic tarp. The keypad glows.",
              "time machine", "machine", "warden", "keypad", "device", "tarp");
    }

    /**
     * Interacting with the Time Machine prompts the player for the access code
     * and the destination timecode. Validates both inputs and triggers the
     * true ending or provides helpful error feedback.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (!Game.riotStarted) {
            game.triggerWardenCatch();
            return;
        }

        if (codeEntered) {
            UI.typewrite("The Time Machine hums. You've already made your trip.", 20);
            return;
        }

        UI.typewrite(Colors.GREEN + Colors.BOLD +
            "The Time Machine pulses with a soft golden glow.", 18);
        UI.pause(400);

        // Step 1: password check
        if (!passwordEntered) {
            UI.typewrite("The keypad displays: " + Colors.WHITE + "\"ENTER ACCESS PASSWORD\"", 20);
            UI.pause(300);
            if (!player.getInventory().hasItem("Fortune Cookie")) {
                UI.typewrite(Colors.GRAY +
                    "It's locked with a password. You'll need to find it somewhere in the prison.", 22);
                return;
            }
            UI.typewrite(Colors.GRAY + "  (You have the fortune cookie — check the password inside.)", 22);
            System.out.println();
            System.out.print(Colors.WHITE + "  Enter password > " + Colors.RESET);
            Scanner sc1 = new Scanner(System.in);
            String pw = sc1.nextLine().trim().toUpperCase();
            if (pw.equals("FRESHMAN")) {
                passwordEntered = true;
                UI.typewrite(Colors.GREEN + "  Access granted. The machine hums to life.", 20);
                UI.pause(500);
                UI.typewrite("The keypad now displays: " + Colors.WHITE + "\"ENTER DESTINATION CODE\"", 20);
                UI.typewrite(Colors.GRAY + "  (Jilly in the Letter Room has the timecodes.)", 22);
            } else {
                UI.typewrite(Colors.RED + "  Incorrect password. The machine stays locked.", 20);
                UI.typewrite(Colors.GRAY + "  (Find the password from someone who knows the cafeteria well.)", 22);
            }
            return;
        }

        // Step 2: destination timecode
        UI.typewrite("The keypad displays: " + Colors.WHITE + "\"ENTER DESTINATION CODE\"", 20);
        UI.pause(300);
        UI.typewrite(Colors.GRAY + "  (Jilly gave you three codes. Choose wisely.)", 22);
        System.out.println();
        System.out.print(Colors.WHITE + "  Enter code > " + Colors.RESET);

        Scanner sc = new Scanner(System.in);
        String code = sc.nextLine().trim();

        switch (code) {
            case "90928":
                codeEntered = true;
                game.triggerTrueEnding();
                break;
            case "01382":
                UI.typewrite(Colors.RED + "The machine whirrs. You arrive in the Far Future.", 18);
                UI.pause(600);
                UI.typewrite(Colors.RED +
                    "It's somehow worse here. More AIs. Bigger ones. With hats.", 20);
                UI.pause(500);
                game.triggerGameOver("WRONG CODE — 01382\n  Far Future. Not better. Much worse.");
                break;
            case "18572":
                UI.typewrite(Colors.YELLOW +
                    "The machine whirrs. You land on the day your crush rejected you.", 18);
                UI.pause(600);
                UI.typewrite(Colors.YELLOW + "It hurts just as much the second time.", 22);
                UI.pause(500);
                UI.typewrite(Colors.YELLOW +
                    "Also, the AI still takes over. You forgot to stop that part.", 20);
                UI.pause(400);
                game.triggerGameOver("WRONG CODE — 18572\n  That was a bad day then AND now.");
                break;
            default:
                UI.typewrite(Colors.RED + "The keypad buzzes. Invalid code.", 20);
                UI.typewrite(Colors.GRAY +
                    "  (Talk to Jilly in the Letter Room for the correct timecodes.)", 22);
                break;
        }
    }

    /**
     * The Time Machine does not accept trades — it requires a code input.
     *
     * @param player the Player instance
     * @param item   the Item being offered
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        UI.typewrite("The Time Machine doesn't accept items. Try " +
            Colors.WHITE + "interact time machine" + Colors.RESET + Colors.WHITE +
            " or " + Colors.WHITE + "talk to machine" + Colors.RESET + ".", 20);
    }
}