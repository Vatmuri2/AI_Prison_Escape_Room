/**
 * Main.java
 *
 * Entry point for the AI Prison Escape game.
 *
 * Responsibilities:
 *   - Creates a new Game instance (which builds the entire world).
 *   - Calls game.play() to start the main game loop.
 *
 * To run this game:
 *   1. Compile all .java files:
 *        javac *.java
 *   2. Run the Main class:
 *        java Main
 *
 * AP CSA Concepts Used:
 *   - main method signature: public static void main(String[] args)
 *   - Object instantiation
 *   - Method calls on objects
 */
public class Main
{
    /**
     * Program entry point.
     * Creates the Game and begins play.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args)
    {
        // Instantiate the game engine (builds all rooms, items, NPCs)
        Game game = new Game();

        // Start the game loop (runs until the player wins or loses)
        game.play();
    }
}
