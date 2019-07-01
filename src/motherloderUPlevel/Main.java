package motherloderUPlevel;


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

@ScriptMeta(version = 0.01, name = "motherlodeMineUp", category = ScriptCategory.MINING, developer = "darkklin", desc = "mining Dirt")
public class Main extends Script {

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
    public int loop() {
        final SceneObject oreVein = SceneObjects.getNearest(x -> x.getName().contains("Ore vein") && x.getY() != 5678 && x.getY() != 5679 && x.getY() != 5676 && x.getY() != 5680 && x.getY() != 5675 && x.getY() != 5683 && x.getY() != 5682 && x.getY() != 5682 && x.getY() != 5686 && x.getY() != 5681 && x.getY() != 5684 && x.getY() != 5685);
        SceneObject brokenStrut = SceneObjects.getNearest(x -> x.getName().contains("Broken strut") && x.getY() == 5669);
        SceneObject ladderDown = SceneObjects.getNearest(x -> x.getId() == 19045 && x.getY() == 5674);
        SceneObject ladderUp = SceneObjects.getNearest(x -> x.getId() == 19044 && x.getY() == 5673);

        SceneObject rockFallOne = SceneObjects.getNearest(x -> x.getId() == 26680 && x.getX() == 3731);
        SceneObject rockFallTwo = SceneObjects.getNearest(x -> x.getId() == 26679 && x.getX() == 3733);

        Position bankPosition = new Position(3759, 5666, 0);
        InterfaceComponent nmDirtInDeposit = Interfaces.getComponent(382, 4, 2);
        int nmDirt = Integer.parseInt(nmDirtInDeposit.getText());
        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }



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


        }
        if (ORE_VEIN_AREATWO.contains(oreVein) && !Inventory.isFull() && ORE_VEIN_AREATWO.contains(me)) {
            Log.info(ORE_VEIN_AREATWO.contains(oreVein));
            if (oreVein != null) {
                Log.info("should mining dirt ");
                Log.info(oreVein.getPosition());
                Log.info(oreVein.getId());

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
                    }
                    if (Inventory.contains(2347)) {
                        Time.sleep(300);
                        for (Item hammer : Inventory.getItems(item -> item.getName().equals("Hammer"))) {
                            hammer.interact("Drop");
                            Time.sleep(300);
                        }
                    }
                }
            }

        }
        else{
            if (!ORE_VEIN_AREATWO.contains(oreVein)) {
                Movement.walkTo(ladderDown.getPosition());
                Time.sleepUntil(() -> ladderDown.getPosition() == me.getPosition(), 6000);
            }
        }




        if (Inventory.isFull() && !depositArea.contains(me)) {
            Log.info("Climb down ");
            ladderDown.interact("Climb");
            Time.sleepUntil(()->depositArea.contains(me),4000);
            Time.sleep(400, 600);
        }

            if (depositArea.contains(me) && Inventory.contains(12011) && Inventory.isFull()) {
                Log.info("should deposit the dirt");
                SceneObject hepper = SceneObjects.getNearest("Hopper");
                hepper.interact("Deposit");
                Time.sleepUntil(() -> !Inventory.containsAll(12011), 2500);
                if (nmDirt > 50&&brokenStrut == null) {
                    Log.info("should wait 2 secont");
                    Time.sleepUntil(()->(Integer.parseInt(nmDirtInDeposit.getText()) >70),10000);
                    Time.sleep(200,600);
                }

                if (brokenStrut != null && !Inventory.contains(12011) && depositArea.contains(me) || WHILS_aREA.contains(me)) {
                    Log.info("should take hemmer");
                    SceneObject crate = SceneObjects.getNearest(x -> x.getName().contains("Crate") && x.getY() == 5674);

                    while (!Inventory.contains("Hammer")) {
                        crate.interact("Search");
                        Time.sleepUntil(() -> Inventory.contains("Hammer"), 8000);


                    }

                    if (Inventory.contains("Hammer") && brokenStrut != null) {
                        boolean isbroken = true;


                            Log.info("should fix");
                            brokenStrut.interact("Hammer");
                            Log.info(brokenStrut == null);
                            Time.sleepUntil(() -> (brokenStrut == null), 10000);


                    }

                }



        }
        if (depositArea.contains(me) || WHILS_aREA.contains(me) && brokenStrut == null) {

            if (!Inventory.isEmpty())
            {
                depositBank();
            }
            Log.info("should go up ");
            ladderUp.interact("Climb");
            Time.sleep(700, 1000);
        }

        InterfaceComponent steel = Interfaces.getComponent(193, 2);

        Log.info((brokenStrut != null)+"<<<<<<<<<<<<<<<<");
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
