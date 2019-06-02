package miningGuild.util;

import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Inventory;

import java.util.function.Predicate;

public final class InventoryUtil {

    public static boolean columnInteract(Predicate<Item> condition, String action, int minSleep, int maxSleep) {
        int order[] = {
                0, 1, 4, 5, 8, 9, 12, 13, 16, 17, 20, 21, 24, 25,
                2, 3, 6, 7, 10, 11, 14, 15, 18, 19, 22, 23, 26, 27
        };
        for(int i : order) {
            Item item = Inventory.getItemAt(i);
            if(item != null && condition.test(item)) {
                item.interact(action);
                Time.sleep(minSleep, maxSleep);
            }
        }
        return true;
    }


    public static boolean columnDrop(Predicate<Item> condition) {
        return columnDrop(condition,140, 300);
    }

    public static boolean columnDrop(Predicate<Item> condition, int minSleep, int maxSleep) {
        return columnInteract(condition, "Drop", minSleep, maxSleep);
    }
}
