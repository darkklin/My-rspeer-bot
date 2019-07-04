package motherloderUPlevel;

import com.rabbitmq.client.Return;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Context {
    static  int countGold = 0;

    private static int toggleNextRun = 20;

    private static boolean isMining = false;

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
            toggleNextRun = ThreadLocalRandom.current().nextInt(52, 67 + 1);
        }
    }

    public static int getCountGoldold() {
        int gold = Inventory.getCount(444);

        countGold = countGold + gold;
    Log.info(countGold);
        return countGold;
    }
}
