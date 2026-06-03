/**
 * Billy is the cafeteria manager NPC. He trades a Fortune Cookie
 * (containing the Time Machine code) for a Fork. He will not deal
 * with any other items.
 */
public class Billy extends NPC {

    /** Whether Billy has already made his trade with the player. */
    private boolean traded = false;

    /**
     * Constructs the Billy NPC with appropriate aliases.
     */
    public Billy() {
        super("Billy", "Cafeteria manager. Wiry, watching you carefully.");
        addAlias("billy");
        addAlias("chef");
        addAlias("cook");
    }

    /**
     * Billy's dialogue. He hints that he has something worth trading
     * and asks what the player has to offer.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        if (traded) {
            System.out.println("  Billy: We already did business. Enjoy the cookie.");
            return;
        }
        System.out.println("  Billy: I don't talk for free, friend.");
        System.out.println("  Billy: But I might have something useful for someone willing to trade.");
        System.out.println("  Billy: I could use a proper utensil. Something forked.");
        System.out.println("  (Hint: try 'trade fork')");
    }

    /**
     * Handles a trade offer to Billy. Accepts only a Fork and returns a Fortune Cookie.
     * Rejects all other items with a dismissive comment.
     *
     * @param player the Player instance
     * @param item   the Item the player is offering
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        if (traded) {
            System.out.println("  Billy: We already made our deal. Move along.");
            return;
        }
        if (item.getName().equalsIgnoreCase("Fork")) {
            player.getInventory().removeItem("Fork");
            Item cookie = ItemFactory.fortuneCookie();
            player.getInventory().addItem(cookie);
            traded = true;
            game.setHasFork(false);
            game.setHasFortuneCookie(true);
            System.out.println("  Billy: A fork. Perfect.");
            System.out.println("  Billy: Take this fortune cookie. The message inside opens something important.");
            System.out.println("  Received: Fortune Cookie");
            System.out.println("  You crack it open. The fortune reads:");
            System.out.println("  \"Time Machine access password: FRESHMAN\"");
            System.out.println("  You'll need someone else for the destination timecode.");
        } else {
            System.out.println("  Billy: That's not what I want. I said something forked.");
        }
    }
}
