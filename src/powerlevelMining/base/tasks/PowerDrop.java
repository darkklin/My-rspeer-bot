package powerlevelMining.base.tasks;

import powerlevelMining.base.Context;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class PowerDrop extends Task {


    public static Area COAL_AREA = Area.rectangular(3006, 9753, 3059, 9733);


    @Override
    public boolean validate() {
        return Context.validatePowerDrop();
    }

    @Override
    public int execute() {

        Player local = Players.getLocal();
        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }

        if (Inventory.isFull()) {
            final SceneObject bankChest = SceneObjects.getNearest("Bank Chest");
            Movement.walkTo(bankChest);
            Time.sleepUntil(() -> local.getPosition().equals(bankChest), Random.nextInt(2000, 4000));
            bankChest.interact("Use");
            Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(2500, 5600));
            if (Bank.isOpen()) {
                Time.sleep(1000, 1700);

                Bank.depositInventory();
                Time.sleep(1000);

                Bank.close();
                Time.sleep(500, 1000);


            }
            if (Inventory.isEmpty()&& Bank.isClosed()){

                Position position = new Position(3021, 9721, 0);
                Movement.walkTo(position);
                Time.sleepUntil(()->position.equals(local),4000);

            }
        }


        return Random.nextInt(200, 500);
    }



}


