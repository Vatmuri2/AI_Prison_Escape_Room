/**
 * Warden.java
 *
 * NPC subclass for the Warden (Claude Opus 9.2), the antagonist
 * who guards the Warden's Office and the Time Machine.
 *
 * The Warden never leaves his office unless a riot is active.
 * If the player enters the Warden's Office through the vent
 * while no riot is happening, the Warden catches them — Game Over.
 *
 * The Warden has no dialogue and cannot be traded with.
 * His entire role is enforced by the Game class's movement logic;
 * this class exists to keep the NPC hierarchy consistent.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends NPC)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - Calling game.triggerGameOver() — method calls across objects
 */
public class Warden extends NPC
{
    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Warden NPC.
     * The Warden is named "Claude Opus 9.2" as per the game spec.
     */
    public Warden()
    {
        super("Warden",
              "Claude Opus 9.2 — a coldly efficient AI Warden who never abandons his post.");
    }

    // -------------------------------------------------------
    // Overridden interact — triggers game over
    // -------------------------------------------------------

    /**
     * Talking to the Warden when he is present in his office
     * immediately ends the game.
     *
     * In practice the Game class prevents entry to the Warden's
     * Office when no riot is active, so this method acts as a
     * secondary safety check.
     *
     * @param player the Player
     * @param game   the Game (triggerGameOver is called)
     */
    @Override
    public void interact(Player player, Game game)
    {
        System.out.println("The Warden's optical sensors lock onto you instantly.");
        System.out.println("Warden (Claude Opus 9.2): \"INTRUDER DETECTED. INITIATING CAPTURE PROTOCOL.\"");
        System.out.println();
        System.out.println("*** ENDING 2: CAUGHT BY THE WARDEN ***");

        game.triggerGameOver("GAME OVER — Caught by Warden (Claude Opus 9.2)");
    }

    // -------------------------------------------------------
    // Overridden trade — also triggers game over
    // -------------------------------------------------------

    /**
     * The Warden does not trade.
     * Any attempt to interact is treated as being caught.
     *
     * @param player   the Player
     * @param game     the Game
     * @param itemName the offered item (ignored)
     */
    @Override
    public void trade(Player player, Game game, String itemName)
    {
        interact(player, game);
    }
}
