package motherloderUPlevel;


import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
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
import smlting.Context;

import java.awt.*;

@ScriptMeta(version = 0.01, name = "motherlodeMineUp", category = ScriptCategory.MINING, developer = "darkklin", desc = "mining Dirt")
public class Main extends Script implements RenderListener {

    private static final Skill skill = Skill.MINING;
    private static final int INIT_XP = Skills.getExperience(skill);

    private StopWatch stopWatch;

    private static String status = "Init";

    public Area ORE_VEIN_AREA = Area.rectangular(3749, 5685, 3763, 5669);
    Area WHILS_aREA = Area.rectangular(3743, 5672, 3739, 5660);
    Area ORE_VEIN_AREATWO = Area.polygonal(
            new Position[]{
                    new Position(3750, 5677, 0),
                    new Position(3762, 5669, 0),
                    new Position(3763, 5686, 0),
                    new Position(3750, 5687, 0),
                    new Position(3747, 5685, 0)
            }
    );

    Area depositArea = Area.polygonal(
            new Position[]{
                    new Position(3748, 5660, 0),
                    new Position(3761, 5660, 0),
                    new Position(3762, 5669, 0),
                    new Position(3748, 5679, 0)
            }
    );


    Player me = Players.getLocal();
    String action = "null";
    int depositCount = 3;
    int withdraw = 0;

    @Override
    public void onStart() {
        stopWatch = StopWatch.start();
        Log.info("Started " + getMeta().name() + " script.");
    }

    @Override
    public int loop() {
        Position STOCK_POSTION = new Position(3756, 5676, 0);
        final SceneObject oreVein = SceneObjects.getNearest(x -> x.getName().contains("Ore vein") && x.getY() != 5679 && x.getY() != 5676 && x.getY() != 5683 && x.getY() != 5682 && x.getY() != 5682 && x.getY() != 5686 && x.getY() != 5681 && x.getY() != 5684 && x.getY() != 5685);
        SceneObject brokenStrut = SceneObjects.getNearest(x -> x.getName().contains("Broken strut") && x.getY() == 5669);
        SceneObject brokenStrutTwo = SceneObjects.getNearest(x -> x.getName().contains("Broken strut"));
        SceneObject ladderDown = SceneObjects.getNearest(x -> x.getId() == 19045 && x.getY() == 5674);
        SceneObject ladderUp = SceneObjects.getNearest(x -> x.getId() == 19044 && x.getY() == 5673);
        SceneObject rockFall = SceneObjects.getNearest(x -> x.getId() == 26679 && x.getX() == 3757);


        InterfaceComponent nmDirtInDeposit = Interfaces.getComponent(382, 4, 2);
        int nmDirt = Integer.parseInt(nmDirtInDeposit.getText());
        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }


        if (nmDirtInDeposit.getTextColor() == 16711680 || nmDirt > 75 && !ORE_VEIN_AREATWO.contains(me)) {

            Log.info("test this while");
            while (nmDirt != 0) {

                if (ORE_VEIN_AREATWO.contains(me)) {
                    Log.info("Climb down ");
                    ladderDown.interact("Climb");
                    Time.sleepUntil(() -> depositArea.contains(me), 3500);
                }
                SceneObject sack = SceneObjects.getNearest("Sack");
                sack.interact("search");
                Time.sleepUntil(() -> (Inventory.getCount() > 1), 8000);
                countOra();


                if (Inventory.contains(444, 447, 451, 453, 449)) {
                    depositBank();
                }

                nmDirt = Integer.parseInt(nmDirtInDeposit.getText());

            }


        }
        if (rockFall != null && ORE_VEIN_AREATWO.contains(me) && status == "Climb down") {

            if (rockFall.distance() == 1) {
                rockFall.interact("Mine");

            }

        }
        if (ORE_VEIN_AREATWO.contains(oreVein) && !Inventory.isFull() && ORE_VEIN_AREATWO.contains(me) && nmDirtInDeposit.getTextColor() != 16711680) {

            if (STOCK_POSTION.equals(me.getPosition())&& (rockFall != null))
            {
                Log.info("player stock");

                if (rockFall.distance() == 2) {
                    Log.info("should mine the rock fall");
                    rockFall.interact("Mine");
                    Time.sleepUntil(()->rockFall==null,2500);

                }
            }
            Log.info(ORE_VEIN_AREATWO.contains(oreVein));

            if (oreVein != null) {
                Log.info("should mining dirt ");
                Log.info(oreVein.getPosition());
                Log.info(oreVein.getId());

                status = "mining";
                oreVein.interact("Mine");
                Time.sleepUntil(() -> me.isAnimating(), 3000);
                if (oreVein.distance() == 1 && me.isAnimating()) {
                    Log.info("here ...");
                    Random.nextInt(800, 2100);
                    Time.sleepUntil(() -> oreVein.distance() > 1, Random.nextInt(3500, 6000));

                    if (Combat.getSpecialEnergy() == 100) {
                        Time.sleep(1000,2000);
                        Log.info("We have special attack. Using special attack...");
                        Combat.toggleSpecial(true);
                        Time.sleepUntil(() -> Combat.getSpecialEnergy() < 100, 1000);
                    }

                }
            }

        } else {
            status = "Looking";

            if (!ORE_VEIN_AREATWO.contains(oreVein)) {
                Movement.walkTo(ladderDown.getPosition());
                Time.sleepUntil(() -> ladderDown.getPosition() == me.getPosition(), 6000);
            }
        }

        if (ORE_VEIN_AREATWO.contains(me) && nmDirtInDeposit.getTextColor() == 16711680) {
            status = "Climb down";

            Log.info("Climb down ");
            ladderDown.interact("Climb");
            Time.sleepUntil(() -> depositArea.contains(me), 4000);
        }


        if (Inventory.isFull() && !depositArea.contains(me)) {
            status = "Climb down";

            Log.info("Climb down ");
            ladderDown.interact("Climb");
            Time.sleepUntil(() -> depositArea.contains(me), 3500);
        }

        if (depositArea.contains(me) && Inventory.contains(12011) && Inventory.isFull()) {
            status = "deposit the dirt";

            Log.info("should deposit the dirt");
            SceneObject hepper = SceneObjects.getNearest("Hopper");

            hepper.interact("Deposit");
            Time.sleepUntil(() -> !Inventory.containsAll(12011), 6500);
            if (nmDirt > 50 && brokenStrut == null) {
                Log.info("should wait 2 secont");
                Time.sleepUntil(() -> (nmDirtInDeposit.getTextColor() == 16711680), 10000);
                Time.sleep(200, 600);
            }

            if (brokenStrut != null && !Inventory.contains(12011) && depositArea.contains(me) || WHILS_aREA.contains(me)) {
                status = "take hemmer";

                Log.info("should take hemmer");
                SceneObject crate = SceneObjects.getNearest(x -> x.getName().contains("Crate") && x.getY() == 5674);

                while (!Inventory.contains("Hammer")) {
                    crate.interact("Search");
                    Time.sleepUntil(() -> Inventory.contains("Hammer"), 8000);
                }

                if (Inventory.contains("Hammer") && brokenStrut != null) {
                    status = "fix the broken strut";

                    Log.info("should fix the broken strut");
                    brokenStrut.interact("Hammer");
                    Time.sleepUntil(() -> (brokenStrut == null), 10000);


                }

            }


        }
        if (depositArea.contains(me) || WHILS_aREA.contains(me) && brokenStrut == null) {


            if (!Inventory.isEmpty() && !Inventory.contains(12011) && !Inventory.contains("Hammer")) {
                status = " bank";
                depositBank();
            }
            if (Inventory.contains("Hammer")) {
                Log.info("should drop the Hammer");
                for (Item hammer : Inventory.getItems(item -> item.getName().equals("Hammer"))) {
                    Time.sleep(600);
                    hammer.interact("Drop");
                    Time.sleepUntil(() -> !Inventory.contains("Hammer"), 2500);
                }
            }
            if (nmDirtInDeposit.getTextColor() != 16711680 && nmDirt < 75) {
                Log.info("should go up ");
                ladderUp.interact("Climb");
                Time.sleepUntil(() -> ORE_VEIN_AREATWO.contains(me), 3500);
            }
        }


        Log.info((brokenStrut != null) + "<<<<<<<<<<<<<<<<");
        return 0;

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
        g2.drawString("Mining [" + Skills.getCurrentLevel(skill) + "]", x, y += 20);
        g2.drawString("XP to next lvl: " + Skills.getExperienceToNextLevel(skill), x, y += 20);

        //Xp gains
        int craftXp = Skills.getExperience(skill) - INIT_XP;
        if (craftXp > 0)
            g2.drawString("+ Crafting XP: " + craftXp + " (" + (int) stopWatch.getHourlyRate(craftXp) / 1000 + "k /h)", x, y += 20);
    }

    public void depositBank() {
        final SceneObject bankChest = SceneObjects.getNearest("Bank Chest");
        Movement.walkTo(bankChest);
        Time.sleepUntil(() -> me.getPosition().equals(bankChest), Random.nextInt(2000, 4000));
        bankChest.interact("Use");
        Time.sleepUntil(() -> Bank.isOpen(), Random.nextInt(2500, 5600));
        if (Bank.isOpen()) {
            Time.sleep(1000, 1700);
            Bank.depositInventory();
            Time.sleep(1000);
            Bank.close();
            Time.sleep(500, 1000);

        }
    }

    int countRune = 0;
    int countGold = 0;

    int countCoal = 0;
    int countAdam = 0;
    int countMithril = 0;

    public void countOra() {

        int rune = Inventory.getCount(451);
        int gold = Inventory.getCount(444);
        int coal = Inventory.getCount(453);
        int adam = Inventory.getCount(449);
        int mithril = Inventory.getCount(447);


        countRune = countRune + rune;
        countGold = countGold + gold;
        countCoal = countCoal + coal;
        countAdam = countAdam + adam;
        countMithril = countMithril + mithril;
        Log.info("rune = " + countRune, "Gold = " + countGold + " Coal = " + countCoal + " ADAM = " + countAdam + " Mithril = " + countMithril);


    }
}
