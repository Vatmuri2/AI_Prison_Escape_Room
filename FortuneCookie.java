/**
 * FortuneCookie.java
 *
 * Subclass of Item representing the Fortune Cookie received from
 * Billy in the Cafeteria.
 *
 * The Fortune Cookie contains the Time Machine unlock code.
 * The player must show/use this code at the Time Machine in the
 * Warden's Office along with the Freshman Orientation time code
 * obtained from Jilly in the Letter Room.
 *
 * Correct time code to enter: 90928 (High School Freshman Orientation)
 *
 * AP CSA Concepts Used:
 *   - Inheritance (extends Item)
 *   - Method overriding (@Override)
 *   - super() constructor call
 *   - String literals / constants
 */
public class FortuneCookie extends Item
{
    // -------------------------------------------------------
    // Instance variable
    // -------------------------------------------------------

    /**
     * The Time Machine unlock code hidden inside this fortune cookie.
     * This is the code the player must enter at the Time Machine.
     * (The DESTINATION code 90928 is obtained from Jilly separately.)
     */
    private String timeMachineCode;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates the Fortune Cookie item.
     * Stores the Time Machine code provided by Billy.
     *
     * @param timeMachineCode the secret code written on the fortune slip
     */
    public FortuneCookie(String timeMachineCode)
    {
        super("Fortune Cookie",
              "A crispy fortune cookie from Billy. The slip inside holds a vital code.",
              false);    // not heavy
        this.timeMachineCode = timeMachineCode;
    }

    // -------------------------------------------------------
    // Getter
    // -------------------------------------------------------

    /**
     * Returns the Time Machine code stored inside this fortune cookie.
     * The Game class reads this when the player uses the Time Machine.
     *
     * @return the Time Machine unlock code string
     */
    public String getTimeMachineCode()
    {
        return timeMachineCode;
    }

    // -------------------------------------------------------
    // Overridden interact method
    // -------------------------------------------------------

    /**
     * Overrides Item.interact() to crack open the cookie and
     * reveal the code to the player.
     */
    @Override
    public void interact()
    {
        System.out.println("You crack open the Fortune Cookie.");
        System.out.println("Inside is a tiny slip of paper that reads:");
        System.out.println("  \"Time Machine Code: " + timeMachineCode + "\"");
        System.out.println("You'll need this — and the right destination code from Jilly.");
    }
}
