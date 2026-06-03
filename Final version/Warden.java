/**
 * The Warden (running Claude Opus 9.2) is found in his office. The Time Machine
 * is accessed through this NPC. If the player arrives without an active riot,
 * interacting triggers a game over. The Time Machine prompts for the access
 * password (from the Fortune Cookie), then a destination timecode (from Jilly).
 */
public class Warden extends NPC {

    /** Whether the player has successfully entered the access password. */
    private boolean passwordEntered = false;

    /**
     * Constructs the Warden / Time Machine NPC with appropriate aliases.
     */
    public Warden() {
        super("Time Machine", "A hulking device under a holographic tarp. The keypad glows.");
        addAlias("time machine");
        addAlias("machine");
        addAlias("warden");
        addAlias("keypad");
        addAlias("device");
    }

    /**
     * Interacting with the Time Machine prompts the player for the access password
     * and the destination timecode. Validates both inputs and triggers the
     * true ending or a bad ending depending on the selected code.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (!game.isRiotStarted()) {
            game.triggerWardenCatch();
            return;
        }

        System.out.println("  The Time Machine pulses with a soft glow.");

        // Step 1: password check (loop until correct or player has no cookie)
        if (!passwordEntered) {
            if (!player.getInventory().hasItem("Fortune Cookie")) {
                System.out.println("  The keypad displays: \"ENTER ACCESS PASSWORD\"");
                System.out.println("  It's locked with a password. You'll need to find it somewhere in the prison.");
                return;
            }
            while (!passwordEntered) {
                System.out.println("  The keypad displays: \"ENTER ACCESS PASSWORD\"");
                System.out.println("  (The fortune cookie told you the password.)");
                System.out.print("  Enter password > ");
                String pw = Game.getScanner().nextLine().trim().toUpperCase();
                if (pw.equals("FRESHMAN")) {
                    passwordEntered = true;
                    System.out.println("  Access granted. The machine hums to life.");
                } else {
                    System.out.println("  Incorrect password. The machine stays locked. Try again.");
                }
            }
        }

        // Step 2: destination timecode
        System.out.println("  The keypad displays: \"ENTER DESTINATION CODE\"");
        System.out.println("  (Jilly gave you three codes. Choose wisely.)");
        System.out.print("  Enter code > ");
        String code = Game.getScanner().nextLine().trim();

        if (code.equals("90928")) {
            game.triggerTrueEnding();
        } else if (code.equals("01382")) {
            System.out.println("  The machine whirs. You arrive in the Far Future.");
            System.out.println("  It's somehow worse here. More AIs. Bigger ones.");
            game.triggerGameOver("ENDING - FAR FUTURE (BAD ENDING)\n  Far Future. Not better. Much worse.");
        } else if (code.equals("18572")) {
            System.out.println("  The machine whirs. You land on the day your crush rejected you.");
            System.out.println("  It hurts just as much the second time. Also, the AI still takes over.");
            game.triggerGameOver("ENDING - REJECTED (BAD ENDING)\n  That was a bad day then AND now.");
        } else {
            System.out.println("  The keypad buzzes. Invalid code.");
            System.out.println("  (Talk to Jilly in the Letter Room for the correct timecodes.)");
        }
    }

    /**
     * The Time Machine does not accept trades. It requires a code input.
     *
     * @param player the Player instance
     * @param item   the Item being offered
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        System.out.println("  The Time Machine doesn't accept items. Try 'use time machine'.");
    }
}
