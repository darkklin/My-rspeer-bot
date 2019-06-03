package miningGuild.base.tasks;

import miningGuild.base.Context;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import  org.rspeer.runetek.api.commons.math.Random;

import org.rspeer.ui.Log;

import java.util.function.Predicate;

public class PowerDrop extends Task {

    public static Area BANK_AREA = Area.rectangular(3009, 3358, 3018, 3355);
    public static Area FALDOR = Area.rectangular(3032, 3358, 3008, 3330);
    public static Area COAL_AREA = Area.rectangular(3006, 9753, 3059, 9733);


    private final Predicate<Item> ore = item -> {
        if (item == null) return false;
        return item.getName().contains("Coal") || item.getName().toLowerCase().contains("uncut");
    };

    @Override
    public boolean validate() {
        return Context.validatePowerDrop();
    }

    @Override
    public int execute() {

        Player local = Players.getLocal();
        final SceneObject staircase = SceneObjects.getNearest(("Ladder"));
        if(Movement.isDestinationSet()){
            Context.checkRunEnergy();
        }

        if (Inventory.isFull() && COAL_AREA.contains(local) && !BANK_AREA.contains(local)) {
            staircase.interact("Climb-up");
            Time.sleepUntil(() -> Players.getLocal().isAnimating(),  Random.nextInt(7000,9500));
        } else {
            if (!BANK_AREA.contains(local) && Inventory.isFull()) {

                Movement.setWalkFlag(BANK_AREA.getCenter());
                Log.info("Player go to the Bank");
                Time.sleepUntil(() -> BANK_AREA.contains(local),  Random.nextInt(8000,15000));
            } else {
                if (!Bank.isOpen() && Inventory.contains("Coal")) {
                    Log.info("player opens the bank");
                    Bank.open();
                    Time.sleep(230, 600);
                    Bank.depositInventory();
                    Time.sleepUntil(() -> Inventory.isEmpty(), 10000);
                    Bank.close();
                }

            }
        }


        Area LADDER_UP = Area.rectangular(3028, 3343, 3013, 3335);
        Area LADDER_DOWN = Area.rectangular(3023, 9741, 3016, 9735);
        Area Mithril_AREA = Area.rectangular(3051, 9736, 3048, 9733);
        if (Inventory.isEmpty() && !COAL_AREA.contains(local) && FALDOR.contains(local)) {

            Log.info("Player Go to the guild mining near the Ladder");
            Movement.walkTo(LADDER_UP.getCenter());
            Log.info(LADDER_UP.contains(local));
            Time.sleepUntil(() -> LADDER_UP.contains(local),  Random.nextInt(5000,8900));

            if (LADDER_UP.contains(local)) {
                Log.info("Player Climb down");
                Log.info(staircase.interact("Climb-down"));
                Time.sleepUntil(() -> Players.getLocal().isAnimating(),  Random.nextInt(2000,4000));

            }
        }


        return Random.nextInt(200,500);
    }

}
//        Log.info("Dropping ores.");
//        Time.sleep(200, 400); //time between mining and dropping
//        InventoryUtil.columnDrop(ore);


