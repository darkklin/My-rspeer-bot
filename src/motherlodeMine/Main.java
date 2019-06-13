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


    Player me = Players.getLocal();
    String action = "null";


    @Override
    public int loop() {
        final SceneObject oreVein = SceneObjects.getNearest(x -> x.getName().contains("Ore vein") && x.getY() != 5685 && x.getY() != 5684 && x.getY() != 5683 && x.getY() != 5682);
        Position position = new Position(3734, 5688, 0);
        Area stocAreaOne = Area.rectangular(3736, 5688, 3732, 5685);
        Area StockareTwo = Area.rectangular(3743, 5688, 3741, 5687);
        Position positionWalk = new Position(3737, 5690, 0);

        Log.info(ORE_VEIN_AREA.contains(oreVein));

        if (ORE_VEIN_AREA.contains(oreVein) && !Inventory.isFull()) {


            if (oreVein != null) {

                if (oreVein != null) {
                    Log.info(oreVein.distance());
                    Log.info("should mining dirt ");
                    String action = "mining";
                    oreVein.interact("Mine");
                    Time.sleepUntil(() -> (oreVein.distance()>1), Random.nextInt(3500,6000));

                }
            } else {
                Log.info("ddddddddd");

            }
        }

        Area depositArea = Area.rectangular(3747, 5674, 3762, 5658);
        Area fallRockPosition = Area.rectangular(3729, 5683, 3736, 5679);
        SceneObject rockFallOne = SceneObjects.getNearest(x -> x.getId() == 26680 && x.getX() == 3731);
        SceneObject rockFallTwo = SceneObjects.getNearest(x -> x.getId() == 26679 && x.getX() == 3733);

        if (Inventory.isFull() && !depositArea.contains(me)) {


            Log.info("if ");

            if (fallRockPosition.contains(rockFallOne) || fallRockPosition.contains(rockFallTwo)) {

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
//            if (fallRockPosition.contains(rockFallTwo)) {
//                rockFallTwo.interact("Mine");
//                Time.sleepUntil(() -> (rockFallTwo == null), 6000);
//            }
//            if (!(fallRockPosition.contains(rockFallOne) && (fallRockPosition.contains(rockFallTwo)))) {
//                Movement.setWalkFlag(depositArea.getCenter());
//                Time.sleepUntil(() -> depositArea.contains(me), 5000);
//
//
//            }
            if (depositArea.contains(me)) {
                Log.info("should deposit the dirt");
                SceneObject hepper = SceneObjects.getNearest("Hopper");

                hepper.interact("Deposit");
                Time.sleepUntil(()->Inventory.containsAll(12011),2500);

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
                Time.sleepUntil(() -> ORE_VEIN_AREA.contains(me), 5000);

            }


//            if (fallRockPosition.contains(rockFallTwo)) {
//                rockFallTwo.interact("Mine");
//                Time.sleepUntil(() -> (rockFallTwo == null), 4000);
//            }
//
//            if (fallRockPosition.contains(rockFallOne)) {
//                rockFallOne.interact("Mine");
//                Time.sleepUntil(() -> (rockFallOne == null), 4000);
//            }
//
//            if (!(fallRockPosition.contains(rockFallOne) && (fallRockPosition.contains(rockFallTwo)))) {
//                Movement.setWalkFlag(ORE_VEIN_AREA.getCenter());
//                Time.sleepUntil(() -> ORE_VEIN_AREA.contains(me), 5000);
//
//
//            }
        }


        return 0;
    }
}
