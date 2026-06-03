/**
 * ScrawnyGuy.java
 *
 * NPC subclass for the Scrawny Guy in the Yard.
 *
 * ANY attempt to interact with the Scrawny Guy results in
 * immediate death and a game restart.  He is a trap NPC
 * whose sole purpose is to punish players who ignore
 * the Bookworm's warning.
 *
 * There is no trade mechanic for this NPC; interact() is
 * the only relevant method and it always ends the game.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends NPC)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - Calling game.triggerGameOver() — method calls across objects
 */
public class ScrawnyGuy extends NPC
{
    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Scrawny Guy NPC.
     * His description intentionally gives nothing away to
     * discourage the player from avoiding him prematurely.
     */
    public ScrawnyGuy()
    {
        super("Scrawny Guy",
              "A thin, twitchy man leaning against the wall. "
              + "He makes eye contact and holds it uncomfortably long.");
    }

    // -------------------------------------------------------
    // Overridden interact — ALWAYS triggers game over
    // -------------------------------------------------------

    /**
     * Immediately kills the player and restarts the game.
     * This is Ending 1: Death by Scrawny Guy.
     *
     * The Scrawny Guy produces a hidden knife the instant the
     * player approaches him.
     *
     * @param player the Player (not inspected — nothing can save them)
     * @param game   the Game (triggerGameOver is called to restart)
     */
    @Override
    public void interact(Player player, Game game)
    {
        System.out.println("You walk toward the Scrawny Guy...");
        System.out.println("He turns, and in one blinding motion produces a shiv.");
        System.out.println();
        System.out.println("*** ENDING 1: DEATH BY SCRAWNY GUY ***");
        System.out.println("You didn't listen.  The Bookworm tried to warn you.");
        System.out.println();

        // Trigger game over and restart
        game.triggerGameOver("The Scrawny Guy shanks you before you can react. GAME OVER.");
    }

    // -------------------------------------------------------
    // Overridden trade — also triggers game over
    // -------------------------------------------------------

    /**
     * The Scrawny Guy does not trade.
     * Attempting to trade also counts as approaching him,
     * which results in the same lethal outcome.
     *
     * @param player   the Player
     * @param game     the Game
     * @param itemName the offered item (ignored)
     */
    @Override
    public void trade(Player player, Game game, String itemName)
    {
        // Even attempting a trade means you got too close
        interact(player, game);
    }
}
