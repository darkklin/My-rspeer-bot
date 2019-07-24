package smlting;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.input.menu.ActionOpcodes;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.ui.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Context {
    private static int toggleNextRun = 20;
    private static boolean isEmpty ;

    public static void checkRunEnergy(){
        if(Movement.getRunEnergy() > toggleNextRun && !Movement.isRunEnabled()){
            Movement.toggleRun(true);
            Log.info("should run");
            //Will toggle the run energy when it is between 20 and 30
            toggleNextRun = ThreadLocalRandom.current().nextInt(52, 67 + 1);
        }
    }
    public static void widrowCoalFromBank(int bagCoal,int coal,int iron ){
        Bank.withdrawAll(bagCoal);
        Time.sleep(500, 1000);
        Bank.withdrawAll(coal);
        Time.sleepUntil(()->Inventory.isFull(),2500);

        if (Inventory.contains(bagCoal) && Inventory.contains(coal)) {
            Bank.close();
            Time.sleepUntil(() -> !Bank.isOpen(), 2000);
            if (Inventory.getFirst(coal).interact("Use")) {
                Time.sleep(200, 500);
                Inventory.getLast(bagCoal).interact(ActionOpcodes.ITEM_ON_ITEM);
                Time.sleepUntil(()->!Inventory.contains(coal),2500);
            }
            if (Inventory.contains(bagCoal) && !Inventory.contains(coal)) {
                Bank.open();
                Time.sleep(500, 1500);
                Bank.withdraw(iron,18);
                Time.sleep(400, 800);
                Bank.withdrawAll(coal);
            }
        }
    }

    public static Boolean isBagEmpty(String coalBag){
        Item bagCoal = Inventory.getFirst(coalBag);
        InterfaceComponent dialog = Interfaces.getComponent(193, 2);

        if (Inventory.contains(coalBag))
        {
            bagCoal.interact("Check");
            if (dialog.getText() == "The coal bag is empty")
            {
                isEmpty = true;
            }
            else{
                isEmpty = false;
            }

        }


        return isEmpty;
    }
}
