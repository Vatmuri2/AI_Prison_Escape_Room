/**
 * Jilly is the Letter Room NPC who provides the three possible Time Machine
 * timecodes when the player talks to her. No item is required.
 */
public class Jilly extends NPC {

    /** Whether Jilly has already given the player the timecodes. */
    private boolean sharedCodes = false;

    /**
     * Constructs the Jilly NPC with appropriate aliases.
     */
    public Jilly() {
        super("Jilly", "Sorts mail with mechanical precision. Knows things.");
        addAlias("jilly");
        addAlias("letter lady");
        addAlias("mail lady");
    }

    /**
     * Jilly's dialogue. She provides all three Time Machine timecodes.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (sharedCodes) {
            System.out.println("  Jilly: I already told you everything I know.");
            System.out.println("  Jilly: Remember, Freshman Orientation. Don't second-guess yourself.");
            return;
        }

        System.out.println("  Jilly: I sort the Warden's correspondence. I see everything that passes through.");
        System.out.println("  Jilly: I intercepted three destination timecodes from the Warden's files:");
        System.out.println();
        System.out.println("    Far Future                       -> 01382");
        System.out.println("    Day Your Crush Rejected You      -> 18572");
        System.out.println("    High School Freshman Orientation -> 90928");
        System.out.println();
        System.out.println("  Jilly: Think carefully about which one stops the problem at its source.");
        game.setKnowsCodes(true);
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
        System.out.println("  Jilly: I don't want things. I deal in information.");
        System.out.println("  (Try: talk to jilly)");
    }
}
