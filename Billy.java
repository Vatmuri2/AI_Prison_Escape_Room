/**
 * Billy.java
 *
 * NPC subclass for Billy, the cafeteria vendor.
 *
 * Billy sells a Fortune Cookie that contains the Time Machine code.
 * The trade requirement is a Fork from the Prison Room.
 * Once the trade is made, Billy will not trade again.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends NPC)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - boolean state flag (tradeComplete)
 *   - String.equalsIgnoreCase()
 */
public class Billy extends NPC
{
    // -------------------------------------------------------
    // Instance variable
    // -------------------------------------------------------

    /**
     * Tracks whether the fork-for-fortune-cookie trade has already
     * happened.  Billy will not trade a second time.
     */
    private boolean tradeComplete;

    /**
     * The Time Machine code written on the fortune slip inside the cookie.
     * This value is set in the constructor so it can be passed to
     * FortuneCookie when the trade occurs.
     */
    private String timeMachineCode;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates Billy with his dialogue description and the
     * Time Machine code he encodes into the fortune cookie.
     *
     * @param timeMachineCode the code that will be placed in the cookie
     */
    public Billy(String timeMachineCode)
    {
        super("Billy",
              "A wiry cafeteria worker with shifty eyes. He seems to be selling something.");
        this.timeMachineCode = timeMachineCode;
        tradeComplete        = false;
    }

    // -------------------------------------------------------
    // Overridden interact
    // -------------------------------------------------------

    /**
     * Billy pitches his fortune cookie deal when the player talks to him.
     * If the trade is complete, he dismisses the player.
     *
     * @param player the Player (not used here but required by signature)
     * @param game   the Game (not used here)
     */
    @Override
    public void interact(Player player, Game game)
    {
        if (tradeComplete)
        {
            System.out.println("Billy: \"You already got what you paid for. Now scram.\"");
        }
        else
        {
            System.out.println("Billy: \"Psst. I've got something you need.\"");
            System.out.println("Billy: \"A fortune cookie. Very special. Has a code inside.\"");
            System.out.println("Billy: \"You bring me a Fork, and it's yours.\"");
            System.out.println("(Use: trade Fork)");
        }
    }

    // -------------------------------------------------------
    // Overridden trade
    // -------------------------------------------------------

    /**
     * Accepts a Fork from the player and gives back a FortuneCookie.
     * Removes the Fork from the player's inventory on success.
     * Adds the FortuneCookie to the player's inventory on success.
     *
     * @param player   the Player attempting the trade
     * @param game     the Game (hasFortuneCookie flag is set here)
     * @param itemName the name of the item being offered
     */
    @Override
    public void trade(Player player, Game game, String itemName)
    {
        // Check if trade is already done
        if (tradeComplete)
        {
            System.out.println("Billy: \"We already did business. One deal per customer.\"");
            return;
        }

        // Billy only wants a Fork
        if (itemName.equalsIgnoreCase("Fork"))
        {
            // Check the player actually has the fork
            if (player.getInventory().hasItem("Fork"))
            {
                // Remove the fork from player inventory
                player.getInventory().removeItem("Fork");

                // Create and give the fortune cookie
                FortuneCookie cookie = new FortuneCookie(timeMachineCode);
                player.getInventory().addItem(cookie);

                // Update game state
                game.setHasFortuneCookie(true);
                tradeComplete = true;

                System.out.println("Billy grins and slides a Fortune Cookie across the counter.");
                System.out.println("Billy: \"Crack it open when the time is right.\"");
            }
            else
            {
                System.out.println("Billy: \"You don't have a Fork. Come back when you do.\"");
            }
        }
        else
        {
            System.out.println("Billy: \"I don't want that. Bring me a Fork.\"");
        }
    }
}
