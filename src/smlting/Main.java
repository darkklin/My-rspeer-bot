package smlting;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.input.menu.ActionOpcodes;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

@ScriptMeta(name = "smlting power", version = 0.1, desc = "SMALT", developer = "darkklin", category = ScriptCategory.SMITHING)
public class Main extends Script {


    @Override
    public int loop() {
        Area BANK_AREA = Area.rectangular(3098, 3499, 3090, 3494);
        Area SMALT_AREA = Area.rectangular(3110, 3501, 3105, 3496);
        int coal = 453;
        int iron = 440;
        int bagCoal = 12019;
        Player me = Players.getLocal();

        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }

        if (BANK_AREA.contains(me) && Inventory.isEmpty()) {

            if (Bank.isOpen()) {
                Time.sleep(500, 1500);
                Context.widrowCoalFromBank(bagCoal,coal,iron);

            }
            else {
                Bank.open();
                Time.sleep(500, 1500);
                Context.widrowCoalFromBank(bagCoal,coal,iron);
            }
        }

        if (Inventory.getCount(453) > 2 && Inventory.getCount(440) > 1) {
            Log.info("should smalte steel");
            Movement.walkToRandomized(SMALT_AREA.getCenter());
            Time.sleepUntil(() -> SMALT_AREA.contains(me), 5000);
            if (SMALT_AREA.contains(me) && Inventory.contains(453, 440)) ;
            {
                final SceneObject furnce = SceneObjects.getNearest("Furnace");
                if (furnce != null && Inventory.contains(453)) {
                    furnce.interact("Smelt");
                    Time.sleep(1000, 2000);
                    InterfaceComponent steel = Interfaces.getComponent(270, 18);
                    if (steel != null) {
                        Time.sleep(400, 1000);
                        steel.interact("Smelt");
                        Time.sleepUntil(() -> Inventory.getCount(440) == 1, Random.nextInt(27000, 29000));
                    }

                }

            }

        }
        if (Inventory.getCount(2353) == 18) {
            Log.info("Smelt steel finished");
            Log.info("Going to the bank to deposit steel error");
            Movement.walkToRandomized(BANK_AREA.getCenter());
            Time.sleepUntil(() -> BANK_AREA.contains(me), 5000);

            Bank.open();
            Time.sleep(1000);
            Bank.depositInventory();
            Time.sleepUntil(() -> Inventory.isEmpty(), 1000);

        }
        if (Inventory.isEmpty() && !BANK_AREA.contains(me)) {
            Movement.walkToRandomized(BANK_AREA.getCenter());
            Time.sleepUntil(() -> BANK_AREA.contains(me), 5000);
        }

        return 0;
    }
}
