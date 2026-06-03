/**
 * Bookworm.java
 *
 * NPC subclass for the Bookworm, who lives in the Library.
 *
 * The Bookworm trades information for items.  Each item unlocks
 * a specific piece of advice:
 *
 *   Wrench  -> Learn about the Yard, Fence, and the riot requirement.
 *   Coin    -> Learn that the Scrawny Guy is VERY dangerous.
 *   Brick   -> Learn that the Warden's Office exists and that
 *              the Warden never leaves (needs a riot to distract him).
 *
 * Each item can only be traded once.  The Bookworm tracks which
 * items he has already received using three boolean flags.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends NPC)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - boolean state flags
 *   - String.equalsIgnoreCase()
 *   - if / else-if chain
 */
public class Bookworm extends NPC
{
    // -------------------------------------------------------
    // State flags — track which trades have occurred
    // -------------------------------------------------------

    /** True once the player has traded the Wrench. */
    private boolean hasWrench;

    /** True once the player has traded the Coin. */
    private boolean hasCoin;

    /** True once the player has traded the Red Painted Brick. */
    private boolean hasBrick;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Bookworm NPC in his initial state (no items received).
     */
    public Bookworm()
    {
        super("Bookworm",
              "A frail man surrounded by towering stacks of books. "
              + "He peers at you over wire-rimmed glasses.");
        hasWrench = false;
        hasCoin   = false;
        hasBrick  = false;
    }

    // -------------------------------------------------------
    // Overridden interact
    // -------------------------------------------------------

    /**
     * The Bookworm explains his trade system when the player talks to him.
     * He hints at what each item unlocks without giving it away for free.
     *
     * @param player the Player (not inspected directly here)
     * @param game   the Game (not modified here)
     */
    @Override
    public void interact(Player player, Game game)
    {
        System.out.println("Bookworm: \"Ah, a visitor. I trade knowledge for objects.\"");
        System.out.println("Bookworm: \"Bring me the right things and I'll share what I know.\"");
        System.out.println();
        System.out.println("  - A Wrench  -> I'll tell you about a way out.");
        System.out.println("  - A Coin    -> I'll warn you about a certain person.");
        System.out.println("  - A Brick   -> I'll tell you about a very important room.");
        System.out.println();
        System.out.println("(Use: trade Wrench / trade Coin / trade Red Painted Brick)");
    }

    // -------------------------------------------------------
    // Overridden trade
    // -------------------------------------------------------

    /**
     * Processes a trade offer.  Removes the matching item from the
     * player's inventory and reveals the corresponding information.
     *
     * @param player   the Player making the offer
     * @param game     the Game (not modified here)
     * @param itemName the name of the item being offered
     */
    @Override
    public void trade(Player player, Game game, String itemName)
    {
        if (itemName.equalsIgnoreCase("Wrench"))
        {
            if (hasWrench)
            {
                System.out.println("Bookworm: \"You already gave me a wrench. I told you everything I know about that.\"");
            }
            else if (!player.getInventory().hasItem("Wrench"))
            {
                System.out.println("Bookworm: \"You don't have a Wrench.\"");
            }
            else
            {
                // Accept the wrench
                player.getInventory().removeItem("Wrench");
                hasWrench = true;

                System.out.println("Bookworm accepts the wrench and nods slowly.");
                System.out.println("Bookworm: \"There is a Yard to the north of the cafeteria.");
                System.out.println("           A fence borders the perimeter.  If a riot were");
                System.out.println("           distracting the guards — and the Warden — a nimble");
                System.out.println("           person MIGHT be able to scale it.");
                System.out.println("           But carrying anything heavy would make that impossible.\"");
            }
        }
        else if (itemName.equalsIgnoreCase("Coin"))
        {
            if (hasCoin)
            {
                System.out.println("Bookworm: \"We already made that trade.\"");
            }
            else if (!player.getInventory().hasItem("Coin"))
            {
                System.out.println("Bookworm: \"You don't have a Coin.\"");
            }
            else
            {
                // Accept the coin — NOTE: the coin is retained after use in
                // most contexts, but the Bookworm keeps this one as payment.
                player.getInventory().removeItem("Coin");
                hasCoin = true;

                System.out.println("Bookworm pockets the coin and leans forward.");
                System.out.println("Bookworm: \"Listen carefully. There is a scrawny fellow in the Yard.");
                System.out.println("           Do NOT talk to him. Do NOT look at him.");
                System.out.println("           Anyone who approaches him... doesn't come back.\"");
            }
        }
        else if (itemName.equalsIgnoreCase("Red Painted Brick"))
        {
            if (hasBrick)
            {
                System.out.println("Bookworm: \"I already have your brick.\"");
            }
            else if (!player.getInventory().hasItem("Red Painted Brick"))
            {
                System.out.println("Bookworm: \"You don't have a Red Painted Brick.\"");
            }
            else
            {
                // Accept the brick
                player.getInventory().removeItem("Red Painted Brick");
                hasBrick = true;

                // Update the heavy-item penalty on the player
                // (the brick is now gone, so speed returns to normal)
                System.out.println("Bookworm hefts the brick with some effort.");
                System.out.println("Bookworm: \"Interesting. Now, the Warden's Office.");
                System.out.println("           Most prisoners don't know it exists.");
                System.out.println("           The Warden NEVER leaves — not for meals, not for anything.");
                System.out.println("           If you need to get in there, you'd need something very");
                System.out.println("           distracting... like, say, a full prison riot.\"");
            }
        }
        else
        {
            System.out.println("Bookworm: \"That's not something I'd find useful. Try something else.\"");
        }
    }
}
