package motherlodeMine;

public class Context {
    private static boolean isMining = false;

    public static boolean isMining() {
        return isMining;
    }

    public static void setMining(boolean mining) {
        isMining = mining;
    }
}
