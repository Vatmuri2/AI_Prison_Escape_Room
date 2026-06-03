/**
 * Utility class for all console output formatting in the game.
 * Provides methods for typewriter-style text animation, section headers,
 * NPC dialogue formatting, warning messages, and timed pauses.
 * All methods are static and this class should not be instantiated.
 */
public class UI {

    /**
     * Private constructor — this is a static utility class.
     */
    private UI() {}

    /**
     * Prints text to the console one character at a time with a delay between
     * each character, creating a typewriter animation effect.
     *
     * @param text        the text to print
     * @param delayMillis the delay in milliseconds between each character
     */
    public static void typewrite(String text, int delayMillis) {
        // Strip ANSI codes from delay calculation, but print them instantly
        boolean inEscape = false;
        for (char c : text.toCharArray()) {
            if (c == '\u001B') {
                inEscape = true;
            }
            System.out.print(c);
            System.out.flush();
            if (inEscape) {
                if (c == 'm') inEscape = false;
                continue;
            }
            if (c != '\n' && c != '\r' && c != ' ') {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println(Colors.RESET);
    }

    /**
     * Pauses execution for the specified number of milliseconds.
     * Used to create dramatic timing between typewritten lines.
     *
     * @param millis the number of milliseconds to pause
     */
    public static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Prints a section divider header with the given title, formatted
     * with horizontal rules and consistent color.
     *
     * @param title the title text to display inside the header
     */
    public static void sectionHeader(String title) {
        System.out.println(Colors.CYAN + Colors.BOLD +
            "  ─────────────────────────────────────────");
        System.out.println("  " + title);
        System.out.println("  ─────────────────────────────────────────" + Colors.RESET);
    }

    /**
     * Displays an NPC's dialogue line in a consistent, visually distinct format.
     * The NPC's name is shown in cyan, followed by their spoken text in white.
     *
     * @param npcName the name of the speaking NPC
     * @param text    the dialogue text to display
     */
    public static void npcSay(String npcName, String text) {
        pause(200);
        System.out.print(Colors.CYAN + Colors.BOLD + "  " + npcName + ": " + Colors.RESET + Colors.WHITE);
        // Typewrite NPC dialogue slightly slower for dramatic effect
        for (char c : text.toCharArray()) {
            System.out.print(c);
            System.out.flush();
            if (c != ' ' && c != '\n') {
                try {
                    Thread.sleep(14);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        System.out.println(Colors.RESET);
    }

    /**
     * Prints a warning or error message to indicate invalid input or
     * a blocked action. Displayed in gray with a consistent prefix.
     *
     * @param message the warning text to display
     */
    public static void warn(String message) {
        System.out.println(Colors.GRAY + "  ✗ " + message + Colors.RESET);
    }

    /**
     * Displays the command input prompt on a new line, signaling to the player
     * that the game is awaiting their next instruction.
     */
    public static void prompt() {
        System.out.println();
        System.out.print(Colors.GREEN + Colors.BOLD + "  > " + Colors.RESET + Colors.WHITE);
    }
}
