package RangeAttack;


import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Pickable;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(version = 0.01, name = "RANGE", category = ScriptCategory.COMBAT, developer = "darkklin", desc = "Traning range")
public class RangeAttack extends Script implements RenderListener {


    private static long startTime;
    private static long startXp;
    private static int startLvl;
    private static Foodtype FOOD_TYPE = Foodtype.MONKFISH;
    String action = "Idle";
    StopWatch time;

    public static int getHealthPercent() {
        float curr = Skills.getCurrentLevel(Skill.HITPOINTS);
        float total = Skills.getLevel(Skill.HITPOINTS);
        float percent = curr / total;
        return (int) (percent * 100);
    }

    public static boolean isDead(Npc npc) {
        return npc == null || Npcs.getAt(npc.getIndex()) == null || npc.getHealthPercent() == 0;
    }

    @Override
    public void onStart() {
        time = StopWatch.start();

        startTime = System.currentTimeMillis();
        startXp = Skills.getExperience(Skill.ATTACK);
        startLvl = Skills.getLevel(Skill.ATTACK);

    }
//&& (x.getTargetIndex() == -1 || x.getTarget().equals(me)) && x.getHealthPercent() > 0)
    @Override
    public int loop() {
        Player me = Players.getLocal();
        Npc enemy = Npcs.getNearest(x -> x.getName().toLowerCase().contains("black") && (x.getTargetIndex() == -1 || x.getTarget().equals(me)) && x.getHealthPercent() > 0);
        Pickable groundBones = Pickables.getNearest(x -> x.getName().replaceAll(" ","").toLowerCase().contains("big") && x.distance(me) < 20 && x.distance(enemy) < 20);

        Pickable groudArrow = Pickables.getNearest(x -> x.getName().toLowerCase().contains("rune") && x.distance(me) < 20 && x.distance(enemy) < 20);
        Item invBones = Inventory.getLast("Big bones");
        Item invArrow = Inventory.getFirst("Iron arrow");

        if (!Movement.isRunEnabled() && Movement.getRunEnergy() > Random.nextInt(5, 15))
            Movement.toggleRun(true);

        if (Dialog.canContinue())
            Dialog.processContinue();

        // Eat food
        if (getHealthPercent() < 40 && Inventory.contains(FOOD_TYPE.getName())) {
            Log.info("Player Eat Food");
            Inventory.getFirst(FOOD_TYPE.getName()).interact("Eat");
            Time.sleep(200, 500);
            if (getHealthPercent() < 50) {
                Inventory.getFirst(FOOD_TYPE.getName()).interact("Eat");
            }
        }
        //Take arrows
        if (groudArrow != null && enemy.getTargetIndex() == -1 && groudArrow.distance(me) < 10 && !Inventory.isFull()) {

                action = "TakeArrows";
                Time.sleep(250, 450);
                groudArrow.interact("Take");
                Log.info("Player Takes arrow from the ground");
                Time.sleepUntil(() -> (groudArrow != null), Random.nextInt(3000, 5000));

        }
        //Wield arrow random amount
        int randomStockSize = Random.nextInt(32, 150);
        if ((invArrow != null && invArrow.getStackSize() > randomStockSize) || (invArrow != null && Inventory.isFull()) ) {
            Log.info("String random Stock size = " + randomStockSize);
            invArrow.interact("Wield");

        }
        if (groundBones != null && enemy.getTargetIndex() == -1 && groundBones.distance(me) < 6 && !Inventory.isFull() ) {
            action = "TakeBones";
            Time.sleep(250, 450);
            groundBones.interact("Take");
            Log.info("Player Takes Bones from the ground");
            Time.sleepUntil(() -> (groundBones != null), Random.nextInt(3000, 5000));

        }
        if (invBones != null && Inventory.isFull() && enemy.getTargetIndex() == -1) {
            Log.info("if  <<<<<<<<<<<<<<<<<<<<<<<<<<<");
            for (Item bones : Inventory.getItems(item -> item.getName().toLowerCase().contains("bones"))) {
                Log.info("should Bury bones ");

                Time.sleepUntil(()->bones.interact("Bury"),5000);
                Time.sleep(1000,1500);

            }
        }
        else {
            // attack
            //&& Inventory.contains(FOOD_TYPE.getName())
            if (me.getTargetIndex() == -1 && !action.equals("TakeArrows")  && !action.equals("TakeBones")&& Inventory.contains(FOOD_TYPE.getName()))  {
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

        return Random.nextInt(500, 800);
    }

    @Override
    public void notify(RenderEvent renderEvent) {

        Experience experience = new Experience();
        Player me = Players.getLocal();
        Graphics g = renderEvent.getSource();

        if (me.getTargetIndex() != -1)
            me.getTarget().getPosition().outline(g);

        int x = 372;
        int y = 19;

        g.setColor(new Color(0, 0, 0, 0.5f));
        g.fillRect(x - 5, y - 15, 150, 100);
        g.setColor(Color.red);

        g.setColor(Color.decode("#B63EA3"));
        g.drawString("SeagullKiller", x, y);
        g.setColor(Color.GREEN);

        g.drawString("Runtime: " + time.toElapsedString(), x, y += 11);
        g.drawString("Status: " + action, x, y += 11);


        final long upTime = System.currentTimeMillis() - startTime;
        g.drawString("Runtime: " + time.toElapsedString(), 5, 270);
        g.drawString("Current Level: " + Skills.getLevel(Skill.ATTACK) + ("(" + experience.gainedLvl(Skill.ATTACK, startLvl) + ")"), 5, 280);
        g.drawString("XP Gained: " + experience.gainedXp(Skill.ATTACK, startXp), 5, 295);
        g.drawString("XP To Level: " + experience.xpToLvl(Skill.ATTACK), 5, 310);
        g.drawString("XP/HR: " + experience.xpHour(Skill.ATTACK, startXp, upTime), 5, 325);
        g.drawString("% To Level: " + experience.percentToLvl(Skill.ATTACK), 5, 340);

    }

}
