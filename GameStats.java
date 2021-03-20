import com.marshie.uno.GameManager;

public class GameStats {
    private final int[] winCounter;
    private final GameManager game;
    public GameStats (int numPlayers) {
        winCounter = new int[numPlayers];
        game = new GameManager(numPlayers);
    }
    public void test (int numTests) {
        long startTime = System.nanoTime();
        for (int i = 0; i < numTests; i++)
            winCounter[game.playGame()]++;
        double secondsTook = (System.nanoTime() - startTime) / Math.pow(10, 9);

        System.out.printf("%d GAMES PLAYED (%.4e):\n", numTests, secondsTook);
        for (int i = 0; i < winCounter.length; i++)
            System.out.printf("Player %d: %.2f%% (%dW/%dL)\n", i + 1, (double)winCounter[i] / numTests, winCounter[i], numTests - winCounter[i]);
    }

    public static void main(String[] args) {
        GameStats stats = new GameStats(2);
        stats.test(1);
    }
}