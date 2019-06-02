package woodcutting.task;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.script.task.Task;
import woodcutting.WuCutting;

import java.util.function.Predicate;


public class Banking extends Task {

    private static final Predicate<Item> AXE_PREDICATE = item -> item.getName().contains("axe");

    @Override
    public boolean validate() {
        return (WuCutting.tree != null && WuCutting.location != null) && Inventory.isFull() && WuCutting.location.getBankArea().contains(Players.getLocal());
    }

    @Override
    public int execute() {
        if (Bank.isOpen()) {
            System.out.println("The bank is open ");

            Bank.depositAllExcept(AXE_PREDICATE);
        } else{
            System.out.println("Opening bank");
            System.out.println("Deposit");
            Bank.open();
            Bank.depositAllExcept(AXE_PREDICATE);

        }
        return 0;
    }
}
