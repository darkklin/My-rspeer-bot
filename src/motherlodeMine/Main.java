package motherlodeMine;


import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
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
    int depositCount = 3;
    int withdraw = 0;


    @Override
    public int loop() {
        final SceneObject oreVein = SceneObjects.getNearest(x -> x.getName().contains("Ore vein") && x.getY() != 5685 && x.getY() != 5684 && x.getY() != 5683 && x.getY() != 5682);
        Position bankPosition = new Position(3759, 5666, 0);

        if (depositCount == 3) {
            SceneObject sack = SceneObjects.getNearest("Sack");
            sack.interact("search");
            if (Inventory.isFull())
            {
                Movement.walkToRandomized(bankPosition);
                Time.sleepUntil(()->bankPosition.equals(me),2500);
                if (Bank.isOpen())
                {
                    Bank.depositInventory();
                }
                else
                {
                    Bank.open();
                }
            }



        }
        else{
            if (ORE_VEIN_AREA.contains(oreVein) && !Inventory.isFull()) {


                if (oreVein != null) {

                    if (oreVein != null) {
                        Log.info(oreVein.distance());
                        Log.info("should mining dirt ");
                        String action = "mining";
                        oreVein.interact("Mine");
                        Time.sleepUntil(() -> (oreVein.distance() > 1), Random.nextInt(3500, 6000));

                    }
                } else {
                    Log.info("ddddddddd");

                }
            }

            Area fallRockPosition = Area.rectangular(3730, 5683, 3733, 5679);
            Area area = Area.rectangular(3746, 5679, 3733, 5673);
            SceneObject rockFallOne = SceneObjects.getNearest(x -> x.getId() == 26680 && x.getX() == 3731);
            SceneObject rockFallTwo = SceneObjects.getNearest(x -> x.getId() == 26679 && x.getX() == 3733);

            if (Inventory.isFull() && !depositArea.contains(me)) {


                Log.info("if ");
                Log.info(fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo) && !area.contains(me));
                if (fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo) && !area.contains(me)) {

                    if (rockFallOne != null) {
                        rockFallOne.interact("Mine");
                        Time.sleepUntil(() -> (rockFallOne == null), 6000);
                    }


                    if (rockFallTwo != null) {
                        rockFallTwo.interact("Mine");
                        Time.sleepUntil(() -> (rockFallTwo == null), 6000);
                    }
                } else {
                    Movement.walkToRandomized(depositArea.getCenter());
                    Time.sleepUntil(() -> depositArea.contains(me), 5000);

                }

                if (depositArea.contains(me)) {
                    Log.info("should deposit the dirt");
                    SceneObject hepper = SceneObjects.getNearest("Hopper");

                    hepper.interact("Deposit");
                    Time.sleepUntil(() -> Inventory.containsAll(12011), 2500);


                }

            } else if (!ORE_VEIN_AREA.contains(me) && !Inventory.isFull()) {
                Log.info("Else if ");

                if (fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo)) {

                    Log.info("Should go to the mining area ");

                    if (rockFallTwo != null) {
                        rockFallTwo.interact("Mine");
                        Time.sleepUntil(() -> (rockFallOne == null), 6000);
                    }


                    if (rockFallOne != null) {
                        rockFallOne.interact("Mine");
                        Time.sleepUntil(() -> (rockFallTwo == null), 6000);
                    }
                } else {
                    Movement.setWalkFlag(ORE_VEIN_AREA.getCenter());
                    Time.sleepUntil(() -> ORE_VEIN_AREA.contains(me), 7000);

                }


            }
        }


        return 0;
    }
}
