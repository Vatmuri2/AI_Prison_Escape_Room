import java.util.ArrayList;

/**
 * Inventory.java
 *
 * Manages the player's collection of Item objects.
 * Uses an ArrayList instead of a HashMap because Map structures
 * are outside the AP CSA exam subset.  Linear searches via
 * for-each loops are used throughout.
 *
 * AP CSA Concepts Used:
 *   - ArrayList (adding, removing, traversal)
 *   - for-each loop
 *   - index-based remove() for safe removal during iteration
 *   - String.equalsIgnoreCase() for case-insensitive look-up
 *   - Encapsulation (private list, public methods)
 */
public class Inventory
{
    // -------------------------------------------------------
    // Instance variable
    // -------------------------------------------------------

    /**
     * The list of items currently carried by the player.
     * ArrayList is used because the size changes dynamically
     * as items are picked up and dropped.
     */
    private ArrayList<Item> items;

    // -------------------------------------------------------
    // Constructor
    // -------------------------------------------------------

    /**
     * Creates an empty Inventory.
     */
    public Inventory()
    {
        items = new ArrayList<Item>();
    }

    // -------------------------------------------------------
    // Core operations
    // -------------------------------------------------------

    /**
     * Adds an item to the inventory.
     *
     * @param item the Item to add
     */
    public void addItem(Item item)
    {
        items.add(item);
        System.out.println("You picked up: " + item.getName());
    }

    /**
     * Removes the first item whose name matches (case-insensitive).
     * Uses index-based remove(i) to avoid index-shifting bugs —
     * a classic AP CSA exam concept.
     *
     * @param itemName the name of the item to remove
     * @return the removed Item, or null if not found
     */
    public Item removeItem(String itemName)
    {
        // Iterate by index so we can safely call remove(i)
        for (int i = 0; i < items.size(); i++)
        {
            if (items.get(i).getName().equalsIgnoreCase(itemName))
            {
                Item removed = items.get(i);
                items.remove(i);   // index-based remove — AP CSA best practice
                System.out.println("You dropped: " + removed.getName());
                return removed;
            }
        }
        System.out.println("You don't have an item called \"" + itemName + "\".");
        return null;
    }

    /**
     * Searches for an item by name (case-insensitive).
     * Does NOT remove the item.
     *
     * @param itemName the name to search for
     * @return the matching Item, or null if not found
     */
    public Item getItem(String itemName)
    {
        for (Item item : items)
        {
            if (item.getName().equalsIgnoreCase(itemName))
            {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns true if the inventory contains an item with the given name.
     *
     * @param itemName the name to check (case-insensitive)
     * @return true if found, false otherwise
     */
    public boolean hasItem(String itemName)
    {
        return getItem(itemName) != null;
    }

    /**
     * Returns true if ANY item in the inventory is heavy.
     * Used by the Game class to block the player from climbing
     * the fence while carrying the Red Painted Brick.
     *
     * @return true if a heavy item is present
     */
    public boolean hasHeavyItem()
    {
        for (Item item : items)
        {
            if (item.isHeavy())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Prints all items in the inventory to the console.
     * If the inventory is empty, a message is shown instead.
     */
    public void displayInventory()
    {
        if (items.size() == 0)
        {
            System.out.println("Your inventory is empty.");
            return;
        }
        System.out.println("=== INVENTORY ===");
        for (Item item : items)
        {
            System.out.println("  " + item.toString());
        }
        System.out.println("=================");
    }

    /**
     * Returns the number of items currently in the inventory.
     *
     * @return item count
     */
    public int size()
    {
        return items.size();
    }
}
