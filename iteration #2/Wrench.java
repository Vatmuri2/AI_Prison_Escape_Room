/**
 * Wrench.java
 *
 * Subclass of Item representing the Wrench found in the Prison Room.
 *
 * Uses:
 *   1. Given to the Bookworm in the Library to learn about the
 *      Yard, the Fence, and how to escape while the Warden is distracted.
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends Item)
 *   - Method overriding (@Override)
 *   - super() constructor call
 */
public class Wrench extends Item
{
    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Wrench item.
     * The wrench is not heavy, so heavy = false.
     */
    public Wrench()
    {
        super("Wrench",
              "A rusty wrench. Probably left behind by a careless maintenance bot.",
              false);    // not heavy
    }

    // -------------------------------------------------------
    // Overridden interact method
    // -------------------------------------------------------

    /**
     * Overrides Item.interact() to describe the wrench
     * when the player examines it.
     */
    @Override
    public void interact()
    {
        System.out.println("You heft the wrench. It's heavy enough to do some damage.");
        System.out.println("Or... maybe trade it to someone who knows this place better.");
    }
}
