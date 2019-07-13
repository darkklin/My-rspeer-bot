package agility;

import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(name = "Canifis Agility", version = 0.1, desc = "Agility", developer = "darkklin", category = ScriptCategory.AGILITY)

public class Canifis extends Script implements RenderListener {
    private static final Skill skill = Skill.AGILITY;
    private static final int INIT_XP = Skills.getExperience(skill);
    private StopWatch stopWatch;

    @Override
    public void onStart() {
        stopWatch = StopWatch.start();
        Log.info("Started " + getMeta().name() + " script.");
    }

    @Override
    public int loop() {
        Player me = Players.getLocal();

        Area CANIFIS_AREA = Area.rectangular(3516, 3509, 3472, 3468);
        Area START_AREA = Area.rectangular(3510, 3490, 3503, 3487);
        Area AREA_NUM_1 = Area.rectangular(3511, 3499, 3503, 3491, 2);
        Area AREA_NUM_2 = Area.rectangular(3504, 3508, 3495, 3502, 2);
        Area AREA_NUM_3 = Area.rectangular(3493, 3506, 3484, 3497, 2);
        Area AREA_NUM_4 = Area.rectangular(3480, 3500, 3474, 3491,3);
        Area AREA_NUM_5 = Area.rectangular(3485, 3488, 3475, 3480, 2);
        Area AREA_NUM_6 = Area.rectangular(3487, 3480, 3504, 3467, 3);
        Area FINISH_AREA = Area.rectangular(3517, 3483, 3507, 3474, 2);

        SceneObject tallTree = SceneObjects.getNearest(14843);
        SceneObject gap = SceneObjects.getNearest("Gap");
        SceneObject gapArea3 = SceneObjects.getNearest(14848);
        SceneObject poleVault = SceneObjects.getNearest("Pole-vault");


        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }

        if (CANIFIS_AREA.contains(me)) {

            Log.info(tallTree!=null);
            if (START_AREA.contains(me)) {


                tallTree.interact("Climb");
                Time.sleepUntil(() -> !START_AREA.contains(me)&& !me.isAnimating(), Random.nextInt(2000, 2500));

            } else {
                Movement.walkToRandomized(START_AREA.getCenter());
                Time.sleepUntil(() -> START_AREA.contains(me), Random.nextInt(1000, 2500));
            }

        }
        if (AREA_NUM_1.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 1");
            Time.sleep(1000, 1550);
            Context.takeGrace(AREA_NUM_1);

            if (gap !=null)
            {
                gap.interact("Jump");
                Time.sleepUntil(() -> !AREA_NUM_1.contains(me), 8000);
            }



        }
        if (AREA_NUM_2.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 2");
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_2);

            gap.interact("Jump");
            Time.sleepUntil(() -> !AREA_NUM_2.contains(me), 8000);


        }
        if (AREA_NUM_3.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 3");
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_3);

            gapArea3.interact("Jump");
            Time.sleepUntil(() -> !AREA_NUM_3.contains(me), 8000);


        }
        if (AREA_NUM_4.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 4");
            Time.sleep(1200, 2000);
            Context.takeGrace(AREA_NUM_4);

            gap.interact("Jump");
            Time.sleepUntil(() -> !AREA_NUM_4.contains(me), 8000);


        }
        if (AREA_NUM_5.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 5");
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_5);

            poleVault.interact("Vault");
            Time.sleepUntil(() -> !AREA_NUM_5.contains(me), 8000);


        }
        if (AREA_NUM_6.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 6");
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_6);

            gap.interact("Jump");
            Time.sleepUntil(() -> !AREA_NUM_6.contains(me), 8000);


        }
        if (FINISH_AREA.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 7");
            Time.sleep(800, 1550);
            Context.takeGrace(FINISH_AREA);

            gap.interact("Jump");
            Time.sleepUntil(() -> !FINISH_AREA.contains(me), 8000);


        }


        return 0;
    }

    @Override
    public void onStop() {
        Log.severe("Stopped " + getMeta().name());
    }

    @Override
    public void notify(RenderEvent e) {
        long seconds = stopWatch.getElapsed().getSeconds();
        if (seconds <= 0)
            return;

        //Initialize our graphics
        Graphics g = e.getSource();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int y = 38;
        int x = 9;
        //Background semi-transparent fill
        g.setColor(new Color(0, 0, 0, 60));
        g.fillRect(x - 3, 38 - 13, 175, 100);

        g2.setColor(Color.white);
        g2.drawString("Runtime: " + stopWatch.toElapsedString(), x, y);
        g2.drawString("Crafting [" + Skills.getCurrentLevel(skill) + "]", x, y += 20);
        g2.drawString("XP to next lvl: " + Skills.getExperienceToNextLevel(skill), x, y += 20);

        //Xp gains
        int craftXp = Skills.getExperience(skill) - INIT_XP;
        if (craftXp > 0)
            g2.drawString("+ Crafting XP: " + craftXp + " (" + (int) stopWatch.getHourlyRate(craftXp) / 1000 + "k /h)", x, y += 20);
    }
}
