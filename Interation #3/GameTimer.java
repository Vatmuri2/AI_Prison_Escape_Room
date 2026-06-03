import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages the 2-minute background countdown that starts when the player
 * picks up the Red Painted Brick. If the timer expires before the player
 * wins, the game ends.
 */
public class GameTimer {

    /** The number of milliseconds in the countdown (2 minutes). */
    private static final long TWO_MINUTES = 2 * 60 * 1000;

    /** The underlying Java timer running on a background thread. */
    private Timer timer;

    /** The game this timer belongs to, used to trigger the time-out ending. */
    private Game game;

    /**
     * Constructs a GameTimer tied to the given game.
     *
     * @param game the Game instance to notify when time expires
     */
    public GameTimer(Game game) {
        this.game = game;
    }

    /**
     * Starts the 2-minute countdown on a background thread. When the time
     * expires, the game's time-out ending is triggered.
     */
    public void start() {
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            /**
             * Runs when the countdown reaches zero, triggering the time-out ending.
             */
            @Override
            public void run() {
                game.triggerTimeOut();
            }
        }, TWO_MINUTES);
    }

    /**
     * Stops the countdown, cancelling the background timer if it is running.
     */
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
