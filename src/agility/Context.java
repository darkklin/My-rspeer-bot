package agility;

import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Pickables;
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

    public static void takeGrace(Area place) {
        Pickable markOfGrace = Pickables.getNearest(11849);
        Inventory.getCount(11849);
//        if (place.contains(markOfGrace))
//        {
//            Log.info("should take mark of grace");
//            markOfGrace.interact("Take");
//            Log.info(markOfGrace!=null);
//            Time.sleepUntil(() -> (markOfGrace ==null),  Random.nextInt(8000,15000));
//
//        }

        while(place.contains(markOfGrace))
        {
            Log.info("should take mark of grace");
            markOfGrace.interact("Take");
            Time.sleep(500,1000);
            markOfGrace = Pickables.getNearest(11849);

        }

    }
}
