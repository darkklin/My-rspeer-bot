package killCowTakeHide;

import miningGuild.base.Context;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import  org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;


@ScriptMeta(name = "SkuuSonius Cowhide+bones ShekelShuffler", desc = "95% of this code is Logans", developer = "Skuusonius", category = ScriptCategory.MONEY_MAKING)
public class BoneLooter extends Script implements RenderListener {


    public static final Area COW_AREA = Area.rectangular(3241, 3298, 3265, 3258);
    public static final Position GATE_POSITION = new Position(3253, 3270, 0);
    public static final Area BANK = Area.rectangular(3210, 3220, 3208, 3218,2);
    Player me = Players.getLocal();
    String action = "Idle";

    private StopWatch stopWatch = StopWatch.start();

    private long totalHides;
    private long previousHides;


    public  void checkRunEnergy(){
        int toggleNextRun = 20;
        if(Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()){
            Movement.toggleRun(true);
            Log.info("should run");
            //Will toggle the run energy when it is between 20 and 30
            toggleNextRun = ThreadLocalRandom.current().nextInt(20, 40 + 1);
        }
    }
    private void bank() {

        if (!Bank.isOpen()) {
            Log.info("player opens the bank");
            Bank.open();
            Time.sleep(230, 600);
            Bank.depositInventory();
            Time.sleepUntil(() -> Inventory.isEmpty(), 10000);
            Bank.close();
        }
    }

    @Override
    public void onStart() {
        previousHides = Inventory.getCount(1739);
    }

    @Override
    public int loop() {
        Player local = Players.getLocal();
        int floorLevel = local.getFloorLevel();
        Area BANK_AREA = Area.rectangular(3200, 3208, 3215, 3228, floorLevel);
        long inventoryCount = Inventory.getCount(526);

        if (inventoryCount > previousHides) {
            totalHides += (inventoryCount - previousHides);
        }
        previousHides = inventoryCount;


        if(Movement.isDestinationSet()){
            Context.checkRunEnergy();
        }

        if (Inventory.isFull()) {
            if (BANK_AREA.contains(local)) {
                if (((local.getFloorLevel() == 0)) || (local.getFloorLevel() == 1)) {
                    final SceneObject staircase = SceneObjects.getNearest(sceneObject -> sceneObject.getName().equalsIgnoreCase("Staircase"));
                    if (staircase != null) {
                        Log.info("Attempting to walk upstairs..");
                        Time.sleepUntil(()-> staircase.interact("Climb-up"),5000);
                        Time.sleepUntil(()-> (local.getFloorLevel() == 2),Random.nextInt(4000,7000));

                    }
                } else if (local.getFloorLevel() == 2) {
                    Log.info("Attempting to bank inventory..");
                    Movement.setWalkFlag(BANK.getCenter());
                    Time.sleepUntil(()->BANK.contains(local),Random.nextInt(5000,8000));
                    bank();
                }
            } else {
                if (COW_AREA.contains(local)) {
                    if (local.distance(COW_AREA.getCenter()) > 30) {
                        Movement.setWalkFlag(COW_AREA.getCenter());
                        Time.sleepUntil(()->COW_AREA.contains(local), Random.nextInt(8000,16000));


                    }
                    final SceneObject gate = SceneObjects.getNearest(1558);
                    Log.info(gate != null);
                    if (gate != null && gate.containsAction("Open") && COW_AREA.contains(gate)) {
                        Movement.setWalkFlag(gate);
                        Log.info("Opening gate");
                        Time.sleep(300);
                        gate.interact("Open");
                    } else {
                        Log.info("Walking to bank..");
                        Movement.walkToRandomized(BANK_AREA.getCenter());
                        Time.sleepUntil(()->BANK_AREA.contains(local),Random.nextInt(3500,8000));
                    }

                } else {
                    Log.info("Walking to bank.. else");
                    Movement.setWalkFlag(BANK_AREA.getCenter());
                    Time.sleepUntil(()->BANK_AREA.contains(local),Random.nextInt(8000,16000));

                }

            }
        } else {
            if (local.getFloorLevel() == 0) {
                Npc enemy = Npcs.getNearest(x -> x.getName().toLowerCase().equalsIgnoreCase("cow")&& (x.getTargetIndex() == -1 || x.getTarget().equals(me)) && x.getHealthPercent() > 0);

                if (COW_AREA.contains(local)) {
                    final SceneObject gate = SceneObjects.getNearest(1558);
                    if (gate != null && gate.containsAction("Open") && COW_AREA.contains(gate)) {
                        Movement.setWalkFlag(gate);
                        Log.info("Opening gate");
                        Time.sleep(300);
                        gate.interact("Open");
                    } else {
                        final Pickable cowhide = Pickables.getNearest("Cowhide");

                        if (local.getAnimation() == -1 && !local.isMoving() && cowhide.getStackSize() > 3) {
                            Log.info("Looking for cowHide..");
                            Position bonesPos;
                            if (cowhide != null) {
                                if (Movement.isWalkable(cowhide))
                                    Random.nextInt(250,400);
                                        cowhide.interact("Take");

                            }

                            else {
                                Log.info("12");
                                Movement.setWalkFlag(GATE_POSITION);

                            }
                        }
                        else
                            if(me.getTargetIndex() == -1 ) {
                            action = "Attacking";

                            if (enemy != null) {
                                Log.info(action);
                                if (!me.isMoving()) {
                                    enemy.interact("Attack");
                                    Time.sleepUntil(me::isAnimating, Random.nextInt(4000, 5000));

                                }
                            } else {
                                if (me.getPosition().distance(enemy) > 5) {
                                    action = "Walking";
                                    Movement.walkToRandomized(enemy);
                                } else {
                                    action = "Waiting for spawn";
                                }
                            }
                        } else {
                            action = "Attacking";

//                    action = "Waiting/In combat";
                        }
                    }
                } else {
                    Log.info("Walking to cows..");
                    Movement.setWalkFlag(COW_AREA.getCenter());
                    Time.sleepUntil(()->COW_AREA.contains(local),Random.nextInt(8000,16000));
                }
            } else {
                if (BANK_AREA.contains(local)) {
                    if (local.getFloorLevel() == 1) {
                        Log.info("Attempting to walk downstairs..");
                        final SceneObject staircase = SceneObjects.getNearest(sceneObject -> sceneObject.getName().equalsIgnoreCase("Staircase"));
                        if (staircase != null) {
                            Time.sleepUntil(()->staircase.interact("Climb-Down"),5000);
                            Time.sleepUntil(()->(local.getFloorLevel()==0),Random.nextInt(4000,6800));
                        }
                    } else if (local.getFloorLevel() == 2) {
                        Log.info("Attempting to walk downstairs..");
                        final SceneObject staircase = SceneObjects.getNearest(sceneObject -> sceneObject.getName().equalsIgnoreCase("Staircase"));
                        if (staircase != null) {
                            staircase.interact("Climb-Down");
                            Time.sleepUntil(()->staircase.interact("Climb-Down"),5000);
                            Time.sleepUntil(()->(local.getFloorLevel()==1),Random.nextInt(4000,6000));


                        }

                    }
                }
            }
        }
        return 600;
    }

    @Override
    public void onStop() {
        Log.info("Script Stopping");
    }

    @Override
    public void notify(RenderEvent renderEvent) {
        final Font font1 = new Font("Arial  Black", 0, 15);
        final Color color1 = new Color(0, 0, 0, 100);
        final Color color2 = new Color(0, 0, 0);
        Graphics g = renderEvent.getSource();
        g.setColor(color1);
        g.fillRect(8, 25, 170, 50);
        g.setColor(color2);
        g.drawRect(8, 25, 170, 50);
        g.setFont(font1);
        g.setColor(Color.RED);
        g.drawString("Running for: " + stopWatch.toElapsedString(), 20, 50);
        g.drawString("Total Bones: " + totalHides, 20, 64);
    }
}