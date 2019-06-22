package miningPower.base;

import miningPower.util.rocks.RockHandler;
import miningPower.base.tasks.PowerDrop;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Context {


    public static boolean validatePowerDrop() {
        return  Inventory.isFull() || Inventory.isEmpty() ;
    }
    public static boolean validateMineRock() {
        return inMiningArea() && !Inventory.isFull() && !Players.getLocal().isAnimating();
    }

    public static boolean inMiningArea() {
        return RockHandler.ROCK_AREA.contains(Players.getLocal().getPosition());
    }

    private static int experienceGained;
    private static int oreMined;
    private static long startTime;
    private static boolean isMining = false;
    private static int toggleNextRun = 20;

    public static void startTimer() {
        if(startTime == 0) {
            startTime = System.currentTimeMillis();
        }
    }

    public static int getExperienceGained() {
        return experienceGained;
    }

    public static int getOreMined() {
        return oreMined;
    }

    public static void incrementOreMined() {
        oreMined++;
        experienceGained += RockHandler.ROCK_ADM.getExperience();
    }

    public static long getRunTime() {
        return System.currentTimeMillis() - startTime;
    }

    public static int getExperiencePerHour() {
        double xphr = experienceGained / ((double) getRunTime() / 3600000D);
        return (int) xphr;
    }

    public static boolean isMining() {
        return isMining;
    }

    public static void setMining(boolean mining) {
        isMining = mining;
    }

    public static void checkRunEnergy(){
        if(Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()){
            Movement.toggleRun(true);
            Log.info("should run");
            //Will toggle the run energy when it is between 20 and 30
            toggleNextRun = ThreadLocalRandom.current().nextInt(40, 60 + 1);
        }
    }
    
}
