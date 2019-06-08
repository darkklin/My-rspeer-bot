package miningGuildMember.base.tasks;

import miningGuildMember.base.Context;
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
                randomPath();

            }
        }


        return Random.nextInt(200, 500);
    }


    public void randomPath() {

        Position POS_ONE = new Position(3029, 9723, 0);
        Position POS_TWO = new Position(3037, 9714, 0);
        Position POS_THREE = new Position(3043, 9725, 0);
        Position POS_four = new Position(3033, 9721, 0);
        int position = Random.nextInt(1, 4);
        switch (position) {
            case 1:
                Log.info("Player Go to position 1");
                Movement.setWalkFlag(POS_ONE);
                Time.sleepUntil(() -> POS_ONE.equals(Players.getLocal()), Random.nextInt(5000, 7000));
                break;
            case 2:
                Log.info("Player Go to position 2");
                Movement.setWalkFlag(POS_TWO);
                Time.sleepUntil(() -> POS_TWO.equals(Players.getLocal()), Random.nextInt(5000, 7000));
                break;
            case 3:
                Log.info("Player Go to position 3");
                Movement.setWalkFlag(POS_THREE);
                Time.sleepUntil(() -> POS_THREE.equals(Players.getLocal()), Random.nextInt(10000, 15000));
                break;
            case 4:
                Log.info("Player Go to position 4");
                Movement.setWalkFlag(POS_four);
                Time.sleepUntil(() -> POS_four.equals(Players.getLocal()), Random.nextInt(5000, 7000));
                break;
        }
    }
}


