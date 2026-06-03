/**
 * RedPaintedBrick.java
 *
 * Subclass of Item representing the Red Painted Brick found
 * inside the Ventilation System.
 *
 * Uses:
 *   1. Traded to Billy for the Fortune Cookie (alternative to fork — but
 *      per the spec the fork is the correct trade; the brick is traded to
 *      the Bookworm).
 *   2. Given to the Bookworm to learn about the Warden's Office.
 *   3. MUST be dropped before the player can climb the Fence in the Yard
 *      (the brick is too heavy to carry while scaling).
 *
 * Special mechanic:
 *   This item is HEAVY (heavy = true).  When it is in the player's
 *   inventory the player's speed variable is reduced, contributing to the
 *   2-minute built-in game timer.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends Item)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - boolean flag usage
 */
public class RedPaintedBrick extends Item
{
    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Red Painted Brick item.
     * heavy = true because this item slows down the player.
     */
    public RedPaintedBrick()
    {
        super("Red Painted Brick",
              "A cinder block painted bright red. It weighs a ton and slows you down.",
              true);     // heavy — slows the player (affects speed variable)
    }

    // -------------------------------------------------------
    // Overridden interact method
    // -------------------------------------------------------

    /**
     * Overrides Item.interact() to warn the player about the
     * brick's weight penalty.
     */
    @Override
    public void interact()
    {
        System.out.println("You pick up the Red Painted Brick. It's surprisingly heavy.");
        System.out.println("WARNING: Carrying this brick will slow you down.");
        System.out.println("You CANNOT climb the fence while holding it.");
        System.out.println("Maybe a bookworm would find it interesting...");
    }
}
