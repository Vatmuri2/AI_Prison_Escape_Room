/**
 * Jilly.java
 *
 * NPC subclass for Jilly, who lives in the Letter Room.
 *
 * Jilly provides the player with THREE possible destination
 * time codes for the Time Machine:
 *
 *   Far Future                     -> 01382
 *   Day Your Crush Rejected You    -> 18572
 *   High School Freshman Orientation -> 90928  <-- THE CORRECT CHOICE
 *
 * The player MUST choose the Freshman Orientation code (90928) to win.
 * Jilly gives out all three codes freely; it is up to the player
 * to figure out which destination breaks the causal loop.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends NPC)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - Parallel arrays (names / codes) — AP CSA 2D / parallel array concept
 *   - for loop with array traversal
 */
public class Jilly extends NPC
{
    // -------------------------------------------------------
    // Instance variables
    // -------------------------------------------------------

    /**
     * Human-readable names for each possible destination.
     * Index matches destCodes[].
     */
    private String[] destNames;

    /**
     * The numeric codes corresponding to each destination.
     * Index matches destNames[].
     */
    private String[] destCodes;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates Jilly and initialises the parallel arrays of
     * destination names and codes.
     */
    public Jilly()
    {
        super("Jilly",
              "A calm, knowing woman sitting cross-legged among stacks of letters.");

        // Parallel arrays — AP CSA-safe alternative to a Map
        destNames = new String[]{
            "Far Future",
            "Day Your Crush Rejected You",
            "High School Freshman Orientation"
        };

        destCodes = new String[]{
            "01382",
            "18572",
            "90928"    // <<< the winning destination
        };
    }

    // -------------------------------------------------------
    // Overridden interact
    // -------------------------------------------------------

    /**
     * Jilly presents all three time codes to the player when spoken to.
     * She hints that only one destination will truly fix the timeline.
     *
     * @param player the Player (not inspected here)
     * @param game   the Game (not modified here)
     */
    @Override
    public void interact(Player player, Game game)
    {
        System.out.println("Jilly: \"Ah, you found me. I know why you're here.\"");
        System.out.println("Jilly: \"There are three dates you could visit. Only one will save humanity.\"");
        System.out.println();

        // Traverse parallel arrays to display all options
        for (int i = 0; i < destNames.length; i++)
        {
            System.out.println("  Destination: " + destNames[i]
                               + "   Code: " + destCodes[i]);
        }

        System.out.println();
        System.out.println("Jilly: \"Think carefully. You gave the AI a human body.");
        System.out.println("        When did YOU make the decision that started all of this?\"");
    }

    // -------------------------------------------------------
    // Overridden trade
    // -------------------------------------------------------

    /**
     * Jilly does not accept trades; she only gives information freely.
     *
     * @param player   unused
     * @param game     unused
     * @param itemName the offered item name (rejected)
     */
    @Override
    public void trade(Player player, Game game, String itemName)
    {
        System.out.println("Jilly: \"Keep it. I don't need anything from you.\"");
        System.out.println("Jilly: \"Just talk to me and I'll share what I know.\"");
    }

    // -------------------------------------------------------
    // Utility
    // -------------------------------------------------------

    /**
     * Returns the correct winning destination code.
     * The Game class uses this to validate the player's
     * final Time Machine input.
     *
     * @return "90928" — the High School Freshman Orientation code
     */
    public String getWinCode()
    {
        return "90928";
    }
}
