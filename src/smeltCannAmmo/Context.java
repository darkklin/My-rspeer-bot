package smeltCannAmmo;

import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Context {
    private static int toggleNextRun = 20;

    public static void checkRunEnergy(){
        if(Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()){
            Movement.toggleRun(true);
            Log.info("should run");
            //Will toggle the run energy when it is between 20 and 30
            toggleNextRun = ThreadLocalRandom.current().nextInt(52, 67 + 1);
        }
    }
}
