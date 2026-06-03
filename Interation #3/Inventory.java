import java.util.ArrayList;

/**
 * Manages the collection of items the player is currently carrying.
 * Provides methods for adding, removing, querying, and displaying inventory contents.
 */
public class Inventory {

    /** The list of items the player is carrying. */
    private ArrayList<Item> items;

    /**
     * Constructs an empty Inventory.
     */
    public Inventory() {
        this.items = new ArrayList<Item>();
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
     * Removes the first item from the inventory whose name matches the given
     * name (case-insensitive, partial match).
     *
     * @param name the name of the item to remove
     */
    public void removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                items.remove(i);
                return;
            }
        }
    }

    /**
     * Returns whether the inventory contains an item matching the given name.
     * Uses case-insensitive, partial-string matching.
     *
     * @param name the name to search for
     * @return true if a matching item is found, false otherwise
     */
    public boolean hasItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
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
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                return items.get(i);
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
        System.out.println("-- INVENTORY --");
        if (items.size() == 0) {
            System.out.println("  Your pockets are empty.");
        } else {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                System.out.println("  - " + item.getName() + " : " + item.getShortDesc());
            }
        }
    }
}
