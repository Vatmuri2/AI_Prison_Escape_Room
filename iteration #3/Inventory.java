import java.util.ArrayList;
import java.util.List;

/**
 * Manages the collection of items the player is currently carrying.
 * Provides methods for adding, removing, querying, and displaying inventory contents.
 */
public class Inventory {

    /** The list of items the player is carrying. */
    private List<Item> items;

    /**
     * Constructs an empty Inventory.
     */
    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item the Item to add
     */
    public void addItem(Item item) {
        items.add(item);
    }

    /**
     * Removes an item from the inventory by name (case-insensitive, partial match).
     *
     * @param name the name of the item to remove
     */
    public void removeItem(String name) {
        items.removeIf(i -> i.getName().toLowerCase().contains(name.toLowerCase()));
    }

    /**
     * Returns whether the inventory contains an item matching the given name.
     * Uses case-insensitive, partial-string matching.
     *
     * @param name the name to search for
     * @return true if a matching item is found, false otherwise
     */
    public boolean hasItem(String name) {
        for (Item item : items) {
            if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves an item from the inventory by name (case-insensitive, partial match).
     *
     * @param name the name of the item to retrieve
     * @return the matching Item, or null if not found
     */
    public Item getItem(String name) {
        for (Item item : items) {
            if (item.getName().toLowerCase().contains(name.toLowerCase())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Returns the number of items currently in the inventory.
     *
     * @return the item count
     */
    public int size() {
        return items.size();
    }

    /**
     * Displays the inventory contents to the console in a formatted list.
     * Shows a message if the inventory is empty.
     */
    public void display() {
        System.out.println();
        UI.sectionHeader("── INVENTORY ──");
        if (items.isEmpty()) {
            System.out.println(Colors.GRAY + "  Your pockets are empty." + Colors.RESET);
        } else {
            for (Item item : items) {
                System.out.println(Colors.WHITE + "  • " + item.getName() +
                    Colors.GRAY + " — " + item.getShortDesc());
            }
        }
        System.out.println(Colors.RESET);
    }
}
