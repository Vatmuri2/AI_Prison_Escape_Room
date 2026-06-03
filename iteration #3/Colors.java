/**
 * Utility class containing ANSI escape codes for terminal text coloring and styling.
 * Used throughout the game to add color and emphasis to console output.
 * All fields are public static final constants.
 * 
 * Note: ANSI codes may not render on all terminals (e.g., older Windows CMD without
 * Virtual Terminal Processing enabled). The game is still fully playable without colors.
 */
public class Colors {

    /**
     * Private constructor — this is a static constants class and should not be instantiated.
     */
    private Colors() {}

    /** Resets all text formatting back to the terminal default. */
    public static final String RESET   = "\u001B[0m";

    /** Bold / bright text formatting. */
    public static final String BOLD    = "\u001B[1m";

    /** Black text color. */
    public static final String BLACK   = "\u001B[30m";

    /** Red text color — used for danger, death, and game-over events. */
    public static final String RED     = "\u001B[31m";

    /** Green text color — used for success, pickups, and the true ending. */
    public static final String GREEN   = "\u001B[32m";

    /** Yellow text color — used for items, warnings, and timecodes. */
    public static final String YELLOW  = "\u001B[33m";

    /** Blue text color. */
    public static final String BLUE    = "\u001B[34m";

    /** Magenta / purple text color. */
    public static final String MAGENTA = "\u001B[35m";

    /** Cyan text color — used for NPC names, headers, and the title screen. */
    public static final String CYAN    = "\u001B[36m";

    /** White text color — used for primary narrative text and item names. */
    public static final String WHITE   = "\u001B[37m";

    /** Dark gray text color — used for hints, secondary descriptions, and muted text. */
    public static final String GRAY    = "\u001B[90m";

    /** Bright red text color. */
    public static final String BRIGHT_RED   = "\u001B[91m";

    /** Bright green text color. */
    public static final String BRIGHT_GREEN = "\u001B[92m";

    /** Bright yellow text color. */
    public static final String BRIGHT_YELLOW = "\u001B[93m";

    /** Bright cyan text color. */
    public static final String BRIGHT_CYAN  = "\u001B[96m";
}
