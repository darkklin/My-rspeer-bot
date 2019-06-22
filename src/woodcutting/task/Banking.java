package woodcutting.task;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import woodcutting.WuCutting;

import java.util.function.Predicate;


public class Banking extends Task {

    private static final Predicate<Item> AXE_PREDICATE = item -> item.getName().contains("axe");

    @Override
    public boolean validate() {
        return (WuCutting.tree != null && WuCutting.location != null) && Inventory.isFull();
    }

    @Override
    public int execute() {

        final SceneObject bankChest = SceneObjects.getNearest("Bank Chest");
        Movement.walkTo(bankChest);
        Time.sleepUntil(() -> Players.getLocal().getPosition().equals(bankChest), Random.nextInt(2000, 4000));
        bankChest.interact("Use");
        Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(2500, 5600));
        if (Bank.isOpen()) {
            Time.sleep(1000, 1700);

            Bank.depositInventory();
            Time.sleep(1000);

            Bank.close();
            Time.sleep(500, 1000);


        }

        return 0;
    }
}
