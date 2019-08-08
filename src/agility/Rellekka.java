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
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(name = "Agility Rellekka", version = 0.1, desc = "Agility", developer = "darkklin", category = ScriptCategory.AGILITY)

public class Rellekka extends Script implements RenderListener {
    private static final Skill skill = Skill.AGILITY;
    private static final Skill skillMagic = Skill.MAGIC;

    private static final int INIT_XP = Skills.getExperience(skill);
    private static final int INIT_XP_MAGIC = Skills.getExperience(skillMagic);

    private static final int TOOL = 561;
    private static final int RESOURCE = 856;
    private StopWatch stopWatch;

    @Override
    public void onStart() {
        stopWatch = StopWatch.start();
        Log.info("Started " + getMeta().name() + " script.");
    }

    @Override
    public int loop() {
        Player me = Players.getLocal();
        Position STOCK_POSITION = new Position(3505, 3489, 2);
        Area Relleka_AREA = Area.rectangular(2676, 3707, 2610, 3647);
        Area START_AREA = Area.rectangular(2627, 3680, 2620, 3677);
        Area AREA_NUM_1 = Area.rectangular(2626, 3676, 2622, 3672,3);
        Area AREA_NUM_2 = Area.rectangular(2622, 3669, 2615, 3657,3);
        Area AREA_NUM_3 =Area.rectangular(2630, 3655, 2626, 3651,3);
        Area AREA_NUM_4 = Area.rectangular(2644, 3653, 2639, 3649,3);

        Area AREA_NUM_5 = Area.rectangular(2650, 3662, 2643, 3657,3);
        Area FINISH_AREA = Area.rectangular(2662, 3681, 2655, 3665,3);

        SceneObject wall = SceneObjects.getNearest(14946);
        SceneObject gap = SceneObjects.getNearest("Gap");
        SceneObject tighrope = SceneObjects.getNearest("Tightrope");
        SceneObject pileOfFish = SceneObjects.getNearest("Pile of fish");
        SceneObject edge = SceneObjects.getNearest(14931);
        SceneObject gap_4 = SceneObjects.getNearest(14930);




        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }

        if (Relleka_AREA.contains(me)) {


            if (START_AREA.contains(me)&& !me.isAnimating()) {


                wall.interact("Climb");
                Time.sleepUntil(() -> !me.isAnimating(), Random.nextInt(2000, 2500));

            } else {
                Context.alchemy(me, TOOL, RESOURCE);
                Movement.walkTo(START_AREA.getCenter());
                Time.sleepUntil(() -> START_AREA.contains(me), Random.nextInt(4000, 6500));
            }

        }
        if (AREA_NUM_1.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 1");
            Time.sleep(500, 1000);
            Context.alchemy(me, TOOL, RESOURCE);
            Context.takeGrace(AREA_NUM_1);
            if (gap != null) {
                gap.interact("Leap");
                Time.sleepUntil(() -> !AREA_NUM_1.contains(me), 8000);
            }


        }
        if (AREA_NUM_2.contains(me) && !me.isAnimating()) {
            Context.alchemy(me, TOOL, RESOURCE);

            Log.info("AREA NM 2");
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_2);

            tighrope.interact("Cross");

            Time.sleepUntil(() -> !AREA_NUM_2.contains(me), 8000);


        }
        if (AREA_NUM_3.contains(me) && !me.isAnimating()) {
            Context.alchemy(me, TOOL, RESOURCE);

            Log.info("AREA NM 3");
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_3);

            if (gap != null) {
                gap.interact("Leap");
                Time.sleepUntil(() -> !AREA_NUM_3.contains(me), 4500, 8000);
            }
            Context.alchemy(me, TOOL, RESOURCE);


        }
        if (AREA_NUM_4.contains(me) && !me.isAnimating()) {

            Log.info("AREA NM 4");
            Time.sleep(500, 1550);
            Context.alchemy(me, TOOL, RESOURCE);
            Context.takeGrace(AREA_NUM_4);
            if (gap != null) {
                gap.interact("Hurdle");
                Time.sleepUntil(() -> !AREA_NUM_4.contains(me), 4500, 8000);

            }

        }
        if (AREA_NUM_5.contains(me) && !me.isAnimating()) {
            Log.info("FINISH AREA");
            Context.alchemy(me, TOOL, RESOURCE);
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_5);
            if (tighrope != null) {
                tighrope.interact("Cross");
                Time.sleepUntil(() -> !AREA_NUM_5.contains(me), 1000, 4000);
            }

        }
        if (FINISH_AREA.contains(me) && !me.isAnimating()) {
            Log.info("FINISH AREA");
            Context.alchemy(me, TOOL, RESOURCE);
            Time.sleep(800, 1550);
            Context.takeGrace(FINISH_AREA);
            if (pileOfFish != null) {
                pileOfFish.interact("Jump-in");
                Time.sleepUntil(() -> !FINISH_AREA.contains(me), 1000, 4000);
            }

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
        g2.drawString("Agility [" + Skills.getCurrentLevel(skill) + "]", x, y += 20);
        g2.drawString("Magic [" + Skills.getCurrentLevel(skillMagic) + "]", x, y += 20);
        g2.drawString("XP to next lvl Agility: " + Skills.getExperienceToNextLevel(skill), x, y += 20);
        g2.drawString("XP to next lvl Magic: " + Skills.getExperienceToNextLevel(skillMagic), x, y += 20);

        //Xp gains
        int agilityXp = Skills.getExperience(skill) - INIT_XP;
        int magicXp = Skills.getExperience(skillMagic) - INIT_XP_MAGIC;

        if (agilityXp > 0) {
            g2.drawString("+ Agility XP: " + agilityXp + " (" + (int) stopWatch.getHourlyRate(agilityXp) / 1000 + "k /h)", x, y += 20);
        }
        if (magicXp > 0) {
            g2.drawString("+ Magic XP: " + magicXp + " (" + (int) stopWatch.getHourlyRate(magicXp) / 1000 + "k /h)", x, y += 20);
        }
    }
}

