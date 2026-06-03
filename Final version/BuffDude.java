/**
 * The Big Buff Dude is a friendly Yard NPC who will start a prison riot
 * in exchange for a Coin. The riot distracts the Warden, enabling safe
 * access to the Warden's Office and the fence escape. The coin is consumed.
 */
public class BuffDude extends NPC {

    /** Whether the Buff Dude has already agreed to start the riot. */
    private boolean riotAgreed = false;

    /**
     * Constructs the BuffDude NPC with appropriate aliases.
     */
    public BuffDude() {
        super("Big Buff Dude", "Enormous. Friendly. Currently doing 300 reps.");
        addAlias("buff dude");
        addAlias("buff");
        addAlias("dude");
        addAlias("big guy");
    }

    /**
     * The Buff Dude's dialogue. He offers to start a riot for a coin.
     * If the player has a coin, talking to him starts the riot directly.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (riotAgreed) {
            System.out.println("  Big Buff Dude: We're already going! WOOO!");
            return;
        }
        if (player.getInventory().hasItem("Coin")) {
            startRiot(player, game);
        } else {
            System.out.println("  Big Buff Dude: Yo! You look like you got a plan.");
            System.out.println("  Big Buff Dude: Give me a coin and I'll start a riot so loud");
            System.out.println("  Big Buff Dude: the Warden won't know which way is up.");
            System.out.println("  (Try: trade coin)");
        }
    }

    /**
     * Handles the trade. If the player offers a Coin, the Buff Dude starts the riot.
     * Rejects all other items.
     *
     * @param player the Player instance
     * @param item   the Item being offered
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        if (riotAgreed) {
            System.out.println("  Big Buff Dude: We're already going! WOOO!");
            return;
        }
        if (item.getName().equalsIgnoreCase("Coin")) {
            startRiot(player, game);
        } else {
            System.out.println("  Big Buff Dude: Nah bro, I need something rounder. A coin. Classic.");
        }
    }

    /**
     * Consumes the player's coin and starts the riot, updating game state.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    private void startRiot(Player player, Game game) {
        player.getInventory().removeItem("Coin");
        game.setHasCoin(false);
        riotAgreed = true;
        game.setRiotStarted(true);
        System.out.println("  Big Buff Dude: OH. IT. IS. ON.");
        System.out.println("  The Buff Dude throws a table. Then another. Chaos erupts across the yard.");
        System.out.println("  Guards flood the yard. The Warden abandons his office.");
        System.out.println("  The Warden's Office is now unguarded (reachable through the vents).");
        System.out.println("  The fence is now climbable, if you're not carrying that heavy brick.");
    }
}
