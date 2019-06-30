package woodcutting.task;

import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import woodcutting.WuCutting;
import woodcutting.data.Location;

public class Traverse extends Task {


    @Override
    public boolean validate() {
        System.out.println("traverseToTrees");

       return Inventory.isFull() && !WuCutting.location.equals((Location.POWERCUT));
//        return (WuCutting.tree != null && WuCutting.location != null) && traverseToBank() || traverseToTrees() ;
    }

    @Override
    public int execute() {
        System.out.println(" execute traverseToTrees");

        Movement.walkTo(traverseToBank() ? WuCutting.location.getBankArea().getCenter() :   WuCutting.location.getTreeArea().getCenter());

        return 300;
    }

    private boolean traverseToBank() {
        return Inventory.isFull() && !WuCutting.location.getBankArea().contains(Players.getLocal()) && !WuCutting.location.equals(Location.POWERCUT);
    }

    private boolean traverseToTrees() {
        return (WuCutting.tree != null && WuCutting.location != null) && !Inventory.isFull() && !WuCutting.location.getTreeArea().contains(Players.getLocal());
    }
}
//