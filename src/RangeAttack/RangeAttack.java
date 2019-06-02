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
import org.rspeer.runetek.event.listeners.SkillListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.SkillEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(version = 0.01, name = "RANGE", category = ScriptCategory.COMBAT, developer = "darkklin", desc = "Traning range")
public class RangeAttack extends Script implements RenderListener {

    private static int experienceGained;

    private static long startTime;
    private static long startXp;
    private static int startLvl;

    String action = "Idle";
    StopWatch time;

    @Override
    public void onStart() {
        time = StopWatch.start();

        startTime = System.currentTimeMillis();
        startXp = Skills.getExperience(Skill.RANGED);
        startLvl = Skills.getLevel(Skill.RANGED);

    }

    @Override
    public int loop() {
        Player me = Players.getLocal();
        Npc enemy = Npcs.getNearest(x -> x.getName().equals("Wolf") && (x.getTargetIndex() == -1 || x.getTarget().equals(me)) && x.getHealthPercent() > 0);
        Pickable groundBones = Pickables.getNearest(x -> x.getName().equals("Bones") && x.distance(me) < 20 && x.distance(enemy) < 20);
        Pickable groudArrow = Pickables.getNearest(x -> x.getName().contains("Iron arrow") && x.distance(me) < 20 && x.distance(enemy) < 20);

        Item invBones = Inventory.getFirst("Bones");
        Item invArrow = Inventory.getFirst("Iron arrow");

        if (!Movement.isRunEnabled() && Movement.getRunEnergy() > Random.nextInt(5, 15))
            Movement.toggleRun(true);

        if (Dialog.canContinue())
            Dialog.processContinue();

        if (groudArrow != null && enemy.getTargetIndex() == -1 && groudArrow.distance(me)<10) {
            action = "TakeArrows";
            groudArrow.interact("Take");
            Log.info("Player Takes arrow from the ground");
            Time.sleepUntil(() -> (groudArrow != null), Random.nextInt(1000, 5000));

        }
        int randomStocksize =  Random.nextInt(27, 150);
        if (invArrow != null&& invArrow.getStackSize() > randomStocksize) {
            Log.info("String random Stock size = "+randomStocksize);
            invArrow.interact("Wield");

        } else {
            Log.info(me.getTargetIndex());
            if (me.getTargetIndex() == -1 && !action.equals("TakeArrows")) {
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
                        Log.info(action);

                        Movement.walkToRandomized(enemy);
                    } else {
                        action = "Waiting for spawn";
                        Log.info(action);
                    }
                }
            } else {
                Log.info(action + "<<<");
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

        g.drawString("Current Level: " + Skills.getLevel(Skill.RANGED) + ("(" + experience.gainedLvl(Skill.RANGED, startLvl) + ")"), 5, 280);
        g.drawString("XP Gained: " + experience.gainedXp(Skill.RANGED, startXp), 5, 295);
        g.drawString("XP To Level: " + experience.xpToLvl(Skill.RANGED), 5, 310);
        g.drawString("XP/HR: " + experience.xpHour(Skill.RANGED, startXp, upTime), 5, 325);
        g.drawString("% To Level: " + experience.percentToLvl(Skill.RANGED), 5, 340);

    }
//
//


}
