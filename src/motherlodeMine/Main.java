package motherlodeMine;


import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
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

@ScriptMeta(version = 0.01, name = "motherlodeMine", category = ScriptCategory.MINING, developer = "darkklin", desc = "mining Dirt")
public class Main extends Script {

    public Area ORE_VEIN_AREA = Area.rectangular(3743, 5697, 3729, 5684);
    Area depositArea = Area.rectangular(3747, 5674, 3762, 5658);


    Player me = Players.getLocal();
    String action = "null";
    int depositCount = 0;
    int withdraw = 0;

    @Override
    public int loop() {
        final SceneObject oreVein = SceneObjects.getNearest(x -> x.getName().contains("Ore vein") && x.getY() != 5685 && x.getY() != 5684 && x.getY() != 5683 && x.getY() != 5682);
        SceneObject brokenStrut = SceneObjects.getNearest("Broken strut");

        Position bankPosition = new Position(3759, 5666, 0);
        InterfaceComponent nmDirtInDeposit = Interfaces.getComponent(382, 4, 2);
        int nmDirt = Integer.parseInt(nmDirtInDeposit.getText());
        Log.info(nmDirt);
        if (nmDirt < 84 && nmDirt != 0 && depositCount == 3 && !Inventory.contains(12011) || nmDirtInDeposit.getTextColor() == 16711680) {
            Log.info("should take the ore ");
            if (!Inventory.isFull()) {
                SceneObject sack = SceneObjects.getNearest("Sack");
                sack.interact("search");
                Time.sleepUntil(() -> (Inventory.getCount() > 1), 5000);
            }

            if (Inventory.getCount() > 1) {
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
            if (nmDirt == 0) {
                depositCount = 0;
            }

        } else {
            Log.info("iam here");
            Log.info(depositCount);

            if (ORE_VEIN_AREA.contains(oreVein) && !Inventory.isFull()) {
                if (oreVein != null) {

                    Log.info(oreVein.distance());
                    Log.info("should mining dirt ");
                    String action = "mining";
                    oreVein.interact("Mine");
                    if (oreVein.distance() == 1 && me.isAnimating()) {
                        Log.info("here ...");
                        Random.nextInt(600, 2000);

                        Time.sleepUntil(() -> oreVein.distance() > 1, Random.nextInt(3500, 6000));

                    }


                }
                if (oreVein.distance() == 0) {
                    Position position = new Position(3739, 5689, 0);
                    Movement.walkToRandomized(position);

                }
            }

            Area fallRockPosition = Area.rectangular(3730, 5683, 3733, 5679);
            Area area = Area.rectangular(3746, 5679, 3733, 5673);
            SceneObject rockFallOne = SceneObjects.getNearest(x -> x.getId() == 26680 && x.getX() == 3731);
            SceneObject rockFallTwo = SceneObjects.getNearest(x -> x.getId() == 26679 && x.getX() == 3733);

            if (Inventory.isFull() && !depositArea.contains(me)) {


                Log.info("if ");
                if (fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo) && !area.contains(me)) {

                    if (rockFallOne != null) {
                        rockFallOne.interact("Mine");
                        Time.sleepUntil(() -> (rockFallOne == null), 4000);
                    }


                    if (rockFallTwo != null) {
                        rockFallTwo.interact("Mine");
                        Time.sleepUntil(() -> (rockFallTwo == null), 4000);
                    }
                } else {
                    Movement.walkToRandomized(depositArea.getCenter());
                    Time.sleepUntil(() -> depositArea.contains(me), 5000);

                }


            } else if (!ORE_VEIN_AREA.contains(me) && !Inventory.isFull() && nmDirtInDeposit.getTextColor() != 16711680) {
                Log.info("Else if ");

                if (fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo)) {

                    Log.info("Should go to the mining area ");

                    if (rockFallTwo != null) {
                        rockFallTwo.interact("Mine");
                        Time.sleepUntil(() -> (rockFallOne == null), 4000);
                    }


                    if (rockFallOne != null) {
                        rockFallOne.interact("Mine");
                        Time.sleepUntil(() -> (rockFallTwo == null), 4000);
                    }
                } else {
                    Movement.setWalkFlag(ORE_VEIN_AREA.getCenter());
                    Time.sleepUntil(() -> ORE_VEIN_AREA.contains(me), 7000);

                }


            }
            if (depositArea.contains(me) && Inventory.contains(12011) && Inventory.isFull()) {
                Log.info("should deposit the dirt");
                SceneObject hepper = SceneObjects.getNearest("Hopper");

                hepper.interact("Deposit");
                Time.sleepUntil(() -> !Inventory.containsAll(12011), 2500);
                if (!Inventory.contains(12011)) {
                    ++depositCount;
                }

            }
            if (brokenStrut != null && !Inventory.contains(12011) && nmDirtInDeposit.getTextColor() == 16711680) {
                Log.info("should take hemmer");
                SceneObject crate = SceneObjects.getNearest("Crate");
                crate.interact("Search");
                Time.sleepUntil(() -> Inventory.contains("Hammer"), 4000);
                if (Inventory.contains("Hammer") && brokenStrut != null) {
                    Log.info("should fix");
                    brokenStrut.interact("Hammer");
                    Time.sleepUntil(() -> (brokenStrut == null), 3500);
                } else {
                    for (Item hammer : Inventory.getItems(item -> item.getName().equals("Hammer"))) {
                        hammer.interact("Drop");
                        Time.sleep(300);
                    }
                }
            }


        }


        return 0;
    }
}
