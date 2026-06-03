/**
 * The Scrawny Guy is a lethal Yard NPC. Any attempt to interact with him
 * results in an immediate game over. He accepts no trades and cannot be reasoned with.
 */
public class ScrawnyGuy extends NPC {

    /**
     * Constructs the ScrawnyGuy NPC with appropriate aliases.
     */
    public ScrawnyGuy() {
        super("Scrawny Guy", "Leaning against the wall. Something feels deeply wrong.");
        addAlias("scrawny");
        addAlias("scrawny guy");
        addAlias("thin guy");
        addAlias("slim");
    }

    /**
     * Talking to the Scrawny Guy triggers an immediate game-over death sequence.
     *
     * @param player the Player instance
     * @param game   the current Game instance
     */
    @Override
    public void talkTo(Player player, Game game) {
        game.triggerScrawnyGuy();
    }

    /**
     * The Scrawny Guy doesn't trade. He kills. Triggers game over.
     *
     * @param player the Player instance
     * @param item   the Item being offered (ignored)
     * @param game   the current Game instance
     */
    @Override
    public void trade(Player player, Item item, Game game) {
        game.triggerScrawnyGuy();
    }
}
