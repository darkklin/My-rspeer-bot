package motherlodeMine;


import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
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

    public Area ORE_VEIN_AREA = Area.rectangular(3744, 5688, 3729, 5693);



    Player me = Players.getLocal();
    String action = "null";


    @Override
    public int loop() {
        final SceneObject oreVein = SceneObjects.getNearest("Ore vein");
        Position position = new Position(3734, 5688, 0);
        Area stocAreaOne = Area.rectangular(3736, 5688, 3732, 5685);
        Area StockareTwo = Area.rectangular(3743, 5688, 3741, 5687);
        Position positionWalk = new Position(3737, 5690, 0);

        Log.info(oreVein.distance());
        if (ORE_VEIN_AREA.contains(me) && !Inventory.isFull()) {


            if (oreVein != null) {
                Log.info(action);
                if ((oreVein.distance() == 3 && stocAreaOne.contains(me))|| (StockareTwo.contains(me) && oreVein.distance() ==5)) {
                    action = "stock";
                    Log.info("should walk");
                    Movement.walkToRandomized(positionWalk.getPosition());
                    Time.sleepUntil(() -> positionWalk.equals(me), 2500);

                }
                else{
                    action = "not stock";

                }
                if (action != "stock") {

                    String action = "mining";
                    oreVein.interact("Mine");
                    Time.sleepUntil(() -> (oreVein == null), 5000);

                }
            }
        }
        if (Inventory.isFull()&& ORE_VEIN_AREA.contains(me))
        {
            SceneObject rockFallOne = SceneObjects.getNearest(26680);
            SceneObject rockFallTwo = SceneObjects.getNearest(26679);


            Movement.walkToRandomized(new Position(3730, 5684, 0));
            if (rockFallOne !=null)
            {
                rockFallOne.interact("Mine");
                Time.sleepUntil(()->(rockFallOne ==null),4000);
            }
            if (rockFallTwo !=null)
            {
                rockFallTwo.interact("Mine");
                Time.sleepUntil(()->(rockFallTwo ==null),4000);
            }


        }


        return 0;
    }
}
