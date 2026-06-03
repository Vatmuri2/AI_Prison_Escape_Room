/**
 * Coin.java
 *
 * Subclass of Item representing the Coin found in the Prison Room.
 *
 * Uses:
 *   1. Unscrewing the ventilation duct to enter the Ventilation System.
 *   2. Convincing Buff Dude to start the prison riot.
 *   3. Given to the Bookworm in the Library to learn about Scrawny Guy.
 *
 * Important: The coin is RETAINED after use (it is never consumed).
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends Item)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - Polymorphism
 */
public class Coin extends Item
{
    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Coin item.
     * Passes appropriate name, description, and weight to the
     * parent Item constructor via super().
     * The coin is not heavy, so heavy = false.
     */
    public Coin()
    {
        super("Coin",
              "A battered old coin. Surprisingly useful in this dystopian prison.",
              false);    // not heavy — does not slow the player
    }

    // -------------------------------------------------------
    // Overridden interact method
    // -------------------------------------------------------

    /**
     * Overrides Item.interact() to describe the coin's uses
     * when the player examines it.
     */
    @Override
    public void interact()
    {
        System.out.println("You turn the coin over in your fingers.");
        System.out.println("It looks ordinary, but it fits perfectly in a vent screw.");
        System.out.println("Maybe someone around here would want it too...");
    }
}
