package woodcutting.task;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.script.task.Task;
import woodcutting.WuCutting;

public class Drop extends Task {

    private static final String DROP_ACTION = "Drop";

    @Override
    public boolean validate() {
        return Inventory.isFull() && WuCutting.dropLogs;
    }

    @Override
    public int execute() {
        for(Item log :Inventory.getItems(item->item.getName().equals(WuCutting.tree.getLogName()))){
            log.interact(DROP_ACTION);
            Time.sleep(300);
        }

        return 300;
    }
}
