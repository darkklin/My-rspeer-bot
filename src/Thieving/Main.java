package Thieving;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(name = "Thieving power", version = 0.1, desc = "fish", developer = "darkklin", category = ScriptCategory.THIEVING)
public class Main extends Script implements RenderListener {
    private StopWatch stopWatch;

    private static final Skill skill = Skill.THIEVING;
    private static final int INIT_XP = Skills.getExperience(skill);
    private static String status = "Init";

    Area Thieving_area = Area.rectangular(1803, 3614, 1792, 3606);
    Player me = Players.getLocal();

    @Override
    public void onStart() {
        stopWatch = StopWatch.start();
        Log.info("Started " + getMeta().name() + " script.");
    }

    @Override
    public int loop() {

        if (Thieving_area.contains(me) ) {
            SceneObject frutStall = SceneObjects.getNearest(x -> x.getName().contains("Fruit Stall") && x.getX() == 1795);

            if (!me.isAnimating()&& !Inventory.isFull()) {
//                frutStall.interact("Steal-from");
                frutStall.click();

            }
            if (Inventory.isFull())
            {//                Time.sleepUntil(() -> me.isAnimating(), 2500);

                Log.info("full invotory");
                for (Item fish : Inventory.getItems(item -> item.getId()==1955 || item.getId()==1963|| item.getId()==1951|| item.getId()==2102 || item.getId()==504 || item.getId()==247 || item.getId()==464|| item.getId()==5504 || item.getId()==2120|| item.getId()==19653|| item.getId()==2114|| item.getId()==5972) ) {
                    Time.sleep(50,150);
                    fish.interact("Drop");
                    Time.sleep(250,680);

                }
            }


        }


        return 0;
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
        g2.drawString("Runtime: " + stopWatch.toElapsedString() + " (" + (status != null ? status : "") + ")", x, y);
        g2.drawString("Crafting [" + Skills.getCurrentLevel(skill) + "]", x, y += 20);
        g2.drawString("XP to next lvl: " + Skills.getExperienceToNextLevel(skill), x, y += 20);

        //Xp gains
        int craftXp = Skills.getExperience(skill) - INIT_XP;
        if (craftXp > 0)
            g2.drawString("+ Crafting XP: " + craftXp + " (" + (int) stopWatch.getHourlyRate(craftXp) / 1000 + "k /h)", x, y += 20);
    }
}
