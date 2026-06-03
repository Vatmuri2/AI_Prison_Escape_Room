/**
 * BuffDude.java
 *
 * NPC subclass for the Big Buff Dude found in the Yard.
 *
 * Despite his intimidating appearance, the Buff Dude is
 * a genuinely chill guy.  He will start a prison riot
 * in exchange for a Coin.
 *
 * The riot is critical because:
 *   1. It distracts the Warden, making it safe to enter
 *      the Warden's Office through the ventilation system.
 *   2. It also distracts the guards so the player can
 *      attempt to climb the fence (though that is a bad ending).
 *
 * Once the riot has started, the Buff Dude will not take
 * another coin — the riot is already going.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends NPC)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - boolean state flag (riotStarted)
 *   - String.equalsIgnoreCase()
 */
public class BuffDude extends NPC
{
    // -------------------------------------------------------
    // Instance variable
    // -------------------------------------------------------

    /**
     * Tracks whether Buff Dude has already started the riot.
     * Prevents the player from trading a second coin and
     * receiving duplicate confirmation messages.
     */
    private boolean riotStarted;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Buff Dude in his pre-riot state.
     */
    public BuffDude()
    {
        super("Big Buff Dude",
              "An absolutely enormous man lifting improvised weights. "
              + "He gives you a surprisingly friendly nod.");
        riotStarted = false;
    }

    // -------------------------------------------------------
    // Overridden interact
    // -------------------------------------------------------

    /**
     * The Buff Dude greets the player and makes his offer.
     * If the riot is already happening, he cheers them on.
     *
     * @param player the Player (not inspected here)
     * @param game   the Game (not modified here directly)
     */
    @Override
    public void interact(Player player, Game game)
    {
        if (riotStarted)
        {
            System.out.println("Big Buff Dude: \"RIOT'S GOING, BRO! GO DO YOUR THING!\"");
        }
        else
        {
            System.out.println("Big Buff Dude: \"Yo. This place stinks, doesn't it?\"");
            System.out.println("Big Buff Dude: \"I could start a riot — really shake things up.");
            System.out.println("                The Warden would HAVE to come deal with it.");
            System.out.println("                All I need is a coin.  Deal?\"");
            System.out.println("(Use: trade Coin)");
        }
    }

    // -------------------------------------------------------
    // Overridden trade
    // -------------------------------------------------------

    /**
     * Accepts a Coin and starts the prison riot.
     * Sets game.riotStarted = true so the Game class can
     * allow safe access to the Warden's Office.
     *
     * Note: the Coin is CONSUMED in this trade (unlike its
     * use with the vent screw, where it is retained).
     *
     * @param player   the Player making the trade
     * @param game     the Game object (riotStarted flag is set here)
     * @param itemName the name of the offered item
     */
    @Override
    public void trade(Player player, Game game, String itemName)
    {
        if (riotStarted)
        {
            System.out.println("Big Buff Dude: \"Already rioting, bro. We're good!\"");
            return;
        }

        if (itemName.equalsIgnoreCase("Coin"))
        {
            if (player.getInventory().hasItem("Coin"))
            {
                // Consume the coin
                player.getInventory().removeItem("Coin");

                // Trigger the riot
                riotStarted = true;
                game.setRiotStarted(true);

                System.out.println("Big Buff Dude snatches the coin and grins.");
                System.out.println("Big Buff Dude: \"ALRIGHT BOYS — IT'S TIME!\"");
                System.out.println();
                System.out.println("** PRISON RIOT HAS STARTED **");
                System.out.println("The Warden has left his desk to deal with the chaos.");
                System.out.println("The ventilation entrance to the Warden's Office is now safe.");
            }
            else
            {
                System.out.println("Big Buff Dude: \"You don't have a coin, bro. Come back when you do.\"");
            }
        }
        else
        {
            System.out.println("Big Buff Dude: \"Nah, I just want a coin. Simple.\"");
        }
    }

    // -------------------------------------------------------
    // Getter
    // -------------------------------------------------------

    /**
     * Returns whether the riot has been started by this NPC.
     * The Game class can also check game.isRiotStarted() directly.
     *
     * @return true if the riot is active
     */
    public boolean isRiotStarted()
    {
        return riotStarted;
    }
}
