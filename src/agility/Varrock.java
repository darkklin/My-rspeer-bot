package agility;

import org.rspeer.runetek.adapter.scene.Pickable;
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
import org.rspeer.runetek.api.scene.Pickables;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(name = "Varrock Agility", version = 0.1, desc = "Agility", developer = "darkklin", category = ScriptCategory.AGILITY)
public class Varrock extends Script implements RenderListener {
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


        Area varrock = Area.rectangular(3253, 3448, 3177, 3381);
        Area START_Varock = Area.rectangular(3225, 3420, 3220, 3410);
        Area AREA_NUM_2 = Area.rectangular(3221, 3420, 3212, 3410, 3);
        Area AREA_NUM_3 = Area.rectangular(3210, 3420, 3201, 3410, 3);
        Area AREA_NUM_4 = Area.rectangular(3198, 3418, 3191, 3413, 1);
        Area AREA_NUM_5 = Area.rectangular(3191, 3406, 3199, 3401, 3);

        Area AREA_NUM_6 = Area.polygonal(
                new Position[]{
                        new Position(3209, 3404, 3),
                        new Position(3199, 3404, 3),
                        new Position(3199, 3399, 3),
                        new Position(3181, 3399, 3),
                        new Position(3181, 3388, 3),
                        new Position(3209, 3388, 3)
                }
        );
        Area AREA_NUM_7 = Area.rectangular(3213, 3404, 3234, 3392, 3);
        Area AREA_NUM_8 = Area.rectangular(3241, 3408, 3235, 3402, 3);
        Area FINISH_AREA = Area.rectangular(3243, 3416, 3233, 3409, 3);
        SceneObject wall_first = SceneObjects.getNearest(14412);
        SceneObject cloth_line = SceneObjects.getNearest(14413);
        SceneObject gap = SceneObjects.getNearest(14414);
        SceneObject wall = SceneObjects.getNearest(14832);
        SceneObject leap2 = SceneObjects.getNearest(14833);
        SceneObject leap3 = SceneObjects.getNearest(14834);
        SceneObject leap4 = SceneObjects.getNearest(14835);
        SceneObject ledge = SceneObjects.getNearest(14836);
        SceneObject jumpOf = SceneObjects.getNearest(14841);
        Pickable markOfGrace = Pickables.getNearest(11849);
        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }

        if (varrock.contains(me)) {

            if (START_Varock.contains(me)) {


                if (wall_first != null) {
                    wall_first.interact("Climb");
                    Time.sleepUntil(() -> AREA_NUM_2.contains(me), Random.nextInt(800, 2500));
                }
            } else {
                Movement.walkToRandomized(START_Varock.getCenter());
                Time.sleepUntil(() -> START_Varock.contains(me), Random.nextInt(800, 2500));
            }

        }

        if (AREA_NUM_2.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 2");
            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_2);

            cloth_line.interact("Cross");
            Time.sleepUntil(() -> AREA_NUM_3.contains(me), 8000);


        }
        if (AREA_NUM_3.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 3");

            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_3);

            Log.info("area 3 ");

            gap.interact("Leap");
            Time.sleepUntil(() -> !AREA_NUM_3.contains(me), 8000);

        }

        if (AREA_NUM_4.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 4");

            Time.sleep(1000, 1550);
            Context.takeGrace(AREA_NUM_4);

            wall.interact("Balance");
            Time.sleepUntil(() -> !AREA_NUM_4.contains(me), 8000);

        }
        if (AREA_NUM_5.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 5");

            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_5);
            leap2.interact("Leap");
            Time.sleepUntil(() -> !AREA_NUM_5.contains(me), 8000);

        }
        if (AREA_NUM_6.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 6");

            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_6);

            leap3.interact("Leap");
            Time.sleepUntil(() -> (!AREA_NUM_6.contains(me)), 8000);

        }
        if (AREA_NUM_7.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 7");

            Time.sleep(1000, 2000);
            Context.takeGrace(AREA_NUM_7);
            leap4.interact("Leap");
            Time.sleepUntil(() -> (!AREA_NUM_7.contains(me)), 8000);


        }
        if (AREA_NUM_8.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 8");

            Time.sleep(800, 1550);
            Context.takeGrace(AREA_NUM_8);
            ledge.interact("Hurdle");
            Time.sleepUntil(() -> (!AREA_NUM_8.contains(me)), 8000);


        }
        if (FINISH_AREA.contains(me) && !me.isAnimating()) {
            Log.info("AREA NM 9");
            Time.sleep(800, 1550);
            Context.takeGrace(FINISH_AREA);
            jumpOf.interact("Jump-off");
            Time.sleepUntil(() -> (!FINISH_AREA.contains(me)), 8000);


        }


        return 450;
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
