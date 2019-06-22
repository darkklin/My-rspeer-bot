package woodcutting.base;

import woodcutting.data.Tree;

public class Context {
    private static int experienceGained;
    private static int logCutted;
    private static long startTime;
    private static boolean isCutting = false;
    private static int toggleNextRun = 20;


    public static void startTimer() {
        if(startTime == 0) {
            startTime = System.currentTimeMillis();
        }
    }
    public static int getExperienceGained() {
        return experienceGained;
    }
    public static void incrementLogCutted() {
        logCutted++;
        experienceGained += Tree.WILLOW.getExperience();
    }

    public static int getLogCutted() {
        return logCutted;
    }
    public static void setMining(boolean cutting) {
        isCutting = cutting;
    }
    public static long getRunTime() {
        return System.currentTimeMillis() - startTime;
    }
    public static int getExperiencePerHour() {
        double xphr = experienceGained / ((double) getRunTime() / 3600000D);
        return (int) xphr;
    }

}
