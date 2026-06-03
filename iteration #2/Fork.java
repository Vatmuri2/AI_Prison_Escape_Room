/**
 * Fork.java
 *
 * Subclass of Item representing the Fork found in the Prison Room.
 *
 * Uses:
 *   1. Traded to Billy in the Cafeteria to receive a Fortune Cookie
 *      containing the Time Machine code.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends Item)
 *   - Method overriding (@Override)
 *   - super() constructor call
 */
public class Fork extends Item
{
    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Fork item.
     * The fork is not heavy, so heavy = false.
     */
    public Fork()
    {
        super("Fork",
              "A plastic cafeteria fork. Billy in the cafeteria might want this.",
              false);    // not heavy
    }

    // -------------------------------------------------------
    // Overridden interact method
    // -------------------------------------------------------

    /**
     * Overrides Item.interact() to describe the fork
     * when the player examines it.
     */
    @Override
    public void interact()
    {
        System.out.println("You inspect the fork. Four prongs, slightly bent.");
        System.out.println("Why would anyone trade something valuable for this?");
        System.out.println("...Only one way to find out.");
    }
}
