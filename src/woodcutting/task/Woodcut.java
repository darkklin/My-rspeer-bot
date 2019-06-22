package woodcutting.task;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import woodcutting.WuCutting;

public class Woodcut extends Task {

    private static final String CUT_ACTION = "Chop down";

    @Override
    public boolean validate() {

        System.out.println("validate Woodcut ");



        return (WuCutting.tree != null && WuCutting.location != null) && !Inventory.isFull() && WuCutting.location.getTreeArea().contains(Players.getLocal());
    }

    @Override
    public int execute() {
        final SceneObject tree = SceneObjects.getNearest(x -> WuCutting.location.getTreeArea().contains(x) && x.getName().equals(WuCutting.tree.getName()));
        if (tree != null && (!Players.getLocal().isAnimating())) {
            tree.interact(CUT_ACTION);
            Time.sleep(500,2500);



        }
        return Random.nextInt(300,1000);
    }
}
