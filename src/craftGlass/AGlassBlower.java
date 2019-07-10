package craftGlass;


import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Production;
import org.rspeer.runetek.api.component.tab.*;
import org.rspeer.runetek.api.input.menu.ActionOpcodes;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;

@ScriptMeta(name = "AGlassBlower", version = 0.01, desc = "Blows", developer = "1234567Alex", category = ScriptCategory.CRAFTING)
public final class AGlassBlower extends Script implements RenderListener {

    //Timer
    private StopWatch stopWatch;

    private static final String TOOL = "Glassblowing pipe";
    private static final String TOOL_ACTION = "Use";
    private static final String RESOURCE = "Molten glass";

    private static final int BEER_PRODUCTION_INDEX = 1 - 1;
    private static final int CANDLE_PRODUCTION_INDEX = 2 - 1;
    private static final int OIL_LAMP_PRODUCTION_INDEX = 3 - 1;
    private static final int VIAL_PRODUCTION_INDEX = 4 - 1;
    private static final int FISHBOWL_PRODUCTION_INDEX = 5 - 1;
    private static final int ORB_PRODUCTION_INDEX = 6 - 1;
    private static final int LENS_PRODUCTION_INDEX = 7 - 1;
    private static final int LIGHT_PRODUCTION_INDEX = 8 - 1;


    private static final Skill skill = Skill.CRAFTING;
    private static final int INIT_XP = Skills.getExperience(skill);

    private static String status = "Init";

    private static boolean crafting = false;

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
                Bank.depositAllExcept(TOOL);
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
            if (!Tab.INVENTORY.isOpen()) {
                Tabs.open(Tab.INVENTORY);
                Time.sleep(100, 450);
            } else if (Inventory.getFirst(TOOL).interact(TOOL_ACTION)) {
                Time.sleep(200, 500);
                Inventory.getLast(RESOURCE).interact(ActionOpcodes.ITEM_ON_ITEM);
                Time.sleepUntil(Production::isOpen, 3000);
                if (Production.isOpen()) {
                    Time.sleep(100, 400);
                    if (Skills.getCurrentLevel(Skill.CRAFTING) < 4)
                        Production.initiate(BEER_PRODUCTION_INDEX);
                    else if (Skills.getCurrentLevel(Skill.CRAFTING) < 12)
                        Production.initiate(CANDLE_PRODUCTION_INDEX);
                    else if (Skills.getCurrentLevel(Skill.CRAFTING) < 33)
                        Production.initiate(OIL_LAMP_PRODUCTION_INDEX);
                    else if (Skills.getCurrentLevel(Skill.CRAFTING) < 42)
                        Production.initiate(VIAL_PRODUCTION_INDEX);
                    else if (Skills.getCurrentLevel(Skill.CRAFTING) < 46)
                        Production.initiate(FISHBOWL_PRODUCTION_INDEX);
                    else if (Skills.getCurrentLevel(Skill.CRAFTING) < 49)
                        Production.initiate(ORB_PRODUCTION_INDEX);
                    else if (Skills.getCurrentLevel(Skill.CRAFTING) < 87)
                        Production.initiate(ORB_PRODUCTION_INDEX);
                    else if (Skills.getCurrentLevel(Skill.CRAFTING) >= 87)
                        Production.initiate(ORB_PRODUCTION_INDEX);
                    Time.sleep(300, 600);
                    crafting = true;
                }
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
