package motherlodeMine;


import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;
import smlting.Context;

@ScriptMeta(version = 0.01, name = "motherlodeMine", category = ScriptCategory.MINING, developer = "darkklin", desc = "mining Dirt")
public class Main extends Script {

    public Area ORE_VEIN_AREA = Area.rectangular(3743, 5697, 3729, 5684);
    Area depositArea = Area.rectangular(3747, 5674, 3762, 5658);

    Area fallRockPosition = Area.rectangular(3730, 5683, 3733, 5679);
    Area area = Area.rectangular(3746, 5679, 3733, 5673);


    Player me = Players.getLocal();
    String action = "null";
    int depositCount = 3;
    int withdraw = 0;

    @Override
    public int loop() {
        final SceneObject oreVein = SceneObjects.getNearest(x -> x.getName().contains("Ore vein") && x.getY() != 5685 && x.getY() != 5684 && x.getY() != 5683 && x.getY() != 5682);
        SceneObject brokenStrut = SceneObjects.getNearest(x -> x.getName().contains("Broken strut") && x.getY() == 5669);
        SceneObject rockFallOne = SceneObjects.getNearest(x -> x.getId() == 26680 && x.getX() == 3731);
        SceneObject rockFallTwo = SceneObjects.getNearest(x -> x.getId() == 26679 && x.getX() == 3733);

        Position bankPosition = new Position(3759, 5666, 0);
        InterfaceComponent nmDirtInDeposit = Interfaces.getComponent(382, 4, 2);
        int nmDirt = Integer.parseInt(nmDirtInDeposit.getText());
        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }


        if (brokenStrut != null && Inventory.contains(2347)) {

            for (Item hammer : Inventory.getItems(item -> item.getName().equals("Hammer"))) {
                hammer.interact("Drop");
                Time.sleep(300);
            }

        }
        Log.info(depositCount + "<<< deposit count ");
        if (nmDirtInDeposit.getTextColor() == 16711680) {

            Log.info("test this while");
            while (nmDirt != 0) {
                SceneObject sack = SceneObjects.getNearest("Sack");
                sack.interact("search");
                Time.sleepUntil(() -> (Inventory.getCount() > 1), 8000);

                if (!Inventory.isEmpty()) {
                    depositBank();
                }

                nmDirt = Integer.parseInt(nmDirtInDeposit.getText());

            }


        } else {

            if (ORE_VEIN_AREA.contains(oreVein) && !Inventory.isFull()) {
                if (oreVein != null) {
                    Log.info("should mining dirt ");
                    String action = "mining";
                    oreVein.interact("Mine");
                    Time.sleepUntil(() -> me.isAnimating(), 3000);
                    if (oreVein.distance() == 1 && me.isAnimating()) {
                        Log.info("here ...");
                        Random.nextInt(600, 2000);
                        Time.sleepUntil(() -> oreVein.distance() > 1, Random.nextInt(3500, 6000));

                        if (Combat.getSpecialEnergy() == 100) {
                            Log.info("We have special attack. Using special attack...");
                            Combat.toggleSpecial(true);
                            Time.sleepUntil(() -> Combat.getSpecialEnergy() < 100, 1000);


                            if (Inventory.contains(2347)) {
                                for (Item hammer : Inventory.getItems(item -> item.getName().equals("Hammer"))) {
                                    hammer.interact("Drop");
                                    Time.sleep(300);
                                }
                            }
                        }
                    }
                }


            }

        }
        if (Inventory.isFull() && !depositArea.contains(me) && !area.contains(me)) {

            Log.info("if ");
            if (fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo)) {

                if (rockFallOne != null) {
                    rockFallOne.interact("Mine");
                    Time.sleepUntil(() -> (me.isAnimating()), 4000);
                    Random.nextInt(200, 500);
                }
                if (rockFallTwo != null) {
                    rockFallTwo.interact("Mine");
                    Time.sleepUntil(() -> (me.isAnimating()), 2000);
                    Random.nextInt(200, 500);
                }
            } else {
                Movement.walkToRandomized(depositArea.getCenter());
                Time.sleepUntil(() -> depositArea.contains(me), 5000);

            }


        } else if (!ORE_VEIN_AREA.contains(me) && !Inventory.isFull() && nmDirtInDeposit.getTextColor() != 16711680) {
            Log.info("Else if ");

            if (fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo)) {


                if (Inventory.contains(2347)) {
                    for (Item hammer : Inventory.getItems(item -> item.getName().equals("Hammer"))) {
                        hammer.interact("Drop");
                        Time.sleep(300);
                    }
                }

                Log.info("Should go to the mining area ");
                Log.info(rockFallTwo != null);

                if (rockFallTwo != null) {

                    Log.info("Rock fall Two");
                    rockFallTwo.interact("Mine");
                    Time.sleepUntil(() -> (me.isAnimating()), 2000);
                    Random.nextInt(200, 500);
                }

                Log.info(rockFallOne != null);
                if (rockFallOne != null) {
                    Log.info("Rock fall one");

                    rockFallOne.interact("Mine");
                    Time.sleepUntil(() -> (me.isAnimating()), 2000);
                    Random.nextInt(200, 500);
                }
            } else {
                Movement.setWalkFlag(ORE_VEIN_AREA.getCenter());
                Time.sleepUntil(() -> ORE_VEIN_AREA.contains(me), 7000);

            }
        }
        if (depositArea.contains(me) || area.contains(me) && Inventory.contains(12011) && Inventory.isFull()) {
            Log.info("should deposit the dirt");
            SceneObject hepper = SceneObjects.getNearest("Hopper");

            hepper.interact("Deposit");
            Time.sleepUntil(() -> !Inventory.containsAll(12011), 2500);

            if (brokenStrut != null && !Inventory.contains(12011)) {
                Log.info("should take hemmer");
                SceneObject crate = SceneObjects.getNearest(x -> x.getName().contains("Crate") && x.getY() == 5674);
                crate.interact("Search");
                Time.sleepUntil(() -> Inventory.contains("Hammer"), 4000);
                if (Inventory.contains("Hammer") && brokenStrut != null) {
                    Log.info("should fix");
                    brokenStrut.interact("Hammer");
                    Time.sleepUntil(() -> (brokenStrut == null), 6000);
                }

            }

        }

        return 0;
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
}
