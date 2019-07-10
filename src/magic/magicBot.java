package magic;

import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.tab.*;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(name = "magicBot", version = 0.1, desc = "SMALT", developer = "darkklin", category = ScriptCategory.MAGIC)

public class magicBot extends Script implements RenderListener {
    private StopWatch stopWatch;

    private static final int TOOL = 561;
    private static final int RESOURCE = 440;
    private static boolean crafting = false;
    private static final Skill skill = Skill.MAGIC;
    private static final int INIT_XP = Skills.getExperience(skill);
    Player me = Players.getLocal();

    private static String status = "Init";
    Area area = Area.rectangular(3167, 3492, 3161, 3487);

    @Override
    public void onStart() {
        stopWatch = StopWatch.start();
        Log.info("Started " + getMeta().name() + " script.");
    }
    @Override
    public int loop() {


        if (!Bank.isOpen() && !Inventory.contains(RESOURCE)) {
            status = "Banking";
            Time.sleep(300, 3000);
            if (Bank.open()) {
                Time.sleep(500, 1000);
                Bank.withdrawAll(TOOL);
                Time.sleepUntil(() -> Inventory.containsOnly(TOOL), 2000);
                Time.sleep(700, 1000);


            }
        }

        if (Bank.isOpen() && Inventory.containsOnly(TOOL)) {
            crafting = false;
            status = "Withdrawing";
            if (!Bank.contains(RESOURCE)) {
                status = "Out of resources";
                setStopping(true);
            }
            Time.sleep(500, 1300);
            Bank.withdrawAll(RESOURCE);
            Time.sleepUntil(() -> Inventory.containsOnly(RESOURCE, TOOL), 2000);
        }

        if (Inventory.contains(RESOURCE, TOOL) && !crafting) {
            status = "Crafting";
            Time.sleep(200, 600);
            Magic.cast(Spell.Modern.SUPERHEAT_ITEM, Inventory.getLast(440));
            Time.sleep(Random.nextInt(500, 1600));
            if (Random.nextInt(1,9)==4)
            {
                Movement.walkToRandomized(area.getCenter());
                Time.sleepUntil(()->area.contains(me),1400);
            }


        }
        if (!Inventory.contains(RESOURCE)) {
            Log.info("deposit iron bars ");
            status = "Banking";
            Time.sleep(300, 1500);
            if (Bank.open()) {
                Time.sleep(500, 1000);
                Bank.depositAll(2351);
                Time.sleep(700, 1000);

            }


        }



        if (Dialog.canContinue()) {
            status = "Level-up";
            Time.sleep(300, 800);
            if (Dialog.processContinue()) {
                crafting = false;
                Time.sleep(100, 150);
            }
        }


        return Random.nextInt(150, 300);
    }

    @Override
    public void onStop() {
        Log.severe("Stopped " + getMeta().name() + " script. Status = " + status);
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
