/**
 * The Bookworm is the Library NPC who trades information for items.
 * Each item traded reveals a different useful hint about the game world.
 * Trades are one-time. The same item cannot be traded twice.
 */
public class Bookworm extends NPC {

    /** Whether the Bookworm has already received the wrench. */
    private boolean gotWrench = false;

    /** Whether the Bookworm has already received the coin. */
    private boolean gotCoin = false;

    /** Whether the Bookworm has already received the brick. */
    private boolean gotBrick = false;

    /**
     * Constructs the Bookworm NPC with appropriate aliases.
     */
    public Bookworm() {
        super("Bookworm", "Absorbed in a decaying novel. Notices more than they let on.");
        addAlias("bookworm");
        addAlias("reader");
        addAlias("librarian");
    }

    /**
     * The Bookworm's opening dialogue, hinting at their trade system.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        System.out.println("  Bookworm: Information has a price. Everything in here does.");
        System.out.println("  Bookworm: Bring me something and I'll tell you what I know.");
        System.out.println("  (Try: trade wrench / trade coin / trade brick)");
    }

    /**
     * Handles trade offers from the player. Each unique item reveals different information.
     * Wrench gives a fence hint, Coin gives a Scrawny Guy warning, Brick gives a
     * Warden's Office hint. Each traded item is consumed.
     *
     * @param player the Player instance
     * @param item   the Item being offered
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        String itemName = item.getName().toLowerCase();

        if (itemName.contains("wrench")) {
            if (gotWrench) {
                System.out.println("  Bookworm: I already told you about the fence. Pay attention.");
                return;
            }
            player.getInventory().removeItem("Wrench");
            game.setHasWrench(false);
            gotWrench = true;
            System.out.println("  Bookworm: A wrench. Practical.");
            System.out.println("  Bookworm: There's a fence in the Yard. If the Warden were distracted...");
            System.out.println("  Bookworm: you might be able to climb it. But not weighed down by anything heavy.");

        } else if (itemName.contains("coin")) {
            if (gotCoin) {
                System.out.println("  Bookworm: I told you about the Scrawny Guy. Don't engage him. Ever.");
                return;
            }
            player.getInventory().removeItem("Coin");
            game.setHasCoin(false);
            gotCoin = true;
            System.out.println("  Bookworm: A coin. Hmm.");
            System.out.println("  Bookworm: Stay away from the Scrawny Guy. He's dangerous.");

        } else if (itemName.contains("brick")) {
            if (gotBrick) {
                System.out.println("  Bookworm: I told you about the Warden's Office. Go find the riot first.");
                return;
            }
            player.getInventory().removeItem("Red Painted Brick");
            game.setHasBrick(false);
            gotBrick = true;
            System.out.println("  Bookworm: A painted brick. You've been in the vents.");
            System.out.println("  Bookworm: The Warden never leaves his office. But maybe something could pull him away.");

        } else {
            System.out.println("  Bookworm: I have no use for that.");
            System.out.println("  Bookworm: Wrench, coin, or the brick. Those I can work with.");
        }
    }
}
