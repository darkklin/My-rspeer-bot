package construction;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.input.menu.ActionOpcodes;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

@ScriptMeta(name = "construction", version = 0.1, desc = "SMALT", developer = "darkklin", category = ScriptCategory.CONSTRUCTION)
public class main extends Script {


    @Override
    public int loop() {
        SceneObject larderSpace = SceneObjects.getNearest("Larder space");
        SceneObject larder = SceneObjects.getNearest("Larder");

        final SceneObject portal = SceneObjects.getNearest("Portal");
        final Npc phials = Npcs.getNearest("Phials");
        Item notePlank = Inventory.getLast("Oak plank");

        Area AREA_REMINNG = Area.rectangular(2958, 3226, 2944, 3209);
        Player me = Players.getLocal();
        InterfaceComponent menu = Interfaces.getComponent(458, 5);

        if (Inventory.getCount(8778) >= 4 && !AREA_REMINNG.contains(me)) {
            Log.info("1");
            if (larderSpace != null) {
                Log.info("should build Lader");
                larderSpace.interact("Build");
                Time.sleepUntil(() -> (menu != null), 4000);
                if (menu != null) {
                    menu.interact("Build");

                    Time.sleepUntil(() -> (me.isAnimating()), 2000);

                }
            } else {
                larder.interact("Remove");
                Time.sleepUntil(() -> Dialog.isOpen(), 2400);
                if (Dialog.isOpen()) {
                    Dialog.process(0);
                    Time.sleepUntil(() -> (!Dialog.isOpen()), 2500);
                }
            }

        } else {
            if (!Inventory.contains(8778) && !AREA_REMINNG.contains(me)) {
                portal.interact("Enter");
                Time.sleepUntil(() -> AREA_REMINNG.contains(me), 2500);
            }

            if (AREA_REMINNG.contains(me) && Inventory.getCount(8778) < 4) {
                Movement.walkToRandomized(phials.getPosition());
                Time.sleep(1200, 2500);
                notePlank.interact("Use");
                phials.interact(ActionOpcodes.ITEM_ON_NPC);
                Time.sleepUntil(() -> Dialog.isOpen(), 3000);
                if (Dialog.isOpen()) {
                    Dialog.process(2);
                    Time.sleepUntil(() -> Inventory.isFull(), 2500);
                }


            }

            if (Inventory.isFull() && AREA_REMINNG.contains(me)) {
                Movement.walkTo(portal.getPosition());
                Time.sleep(800, 2000);
                portal.interact("Build mode");
                Time.sleepUntil(() -> !AREA_REMINNG.contains(me), 2500);
            }

        }

        return 0;
    }
}
