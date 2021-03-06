package miningGuildMember.base.tasks;

import miningGuildMember.base.Context;
import miningGuildMember.util.rocks.RockHandler;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;
import org.rspeer.ui.Log;

public class MineRock extends Task {
    @Override
    public boolean validate() {
        return Context.validateMineRock();
    }

    @Override
    public int execute() {


        SceneObject rock = RockHandler.getValidRock();


        if (rock != null && RockHandler.ROCK_AREA.contains(Players.getLocal())) {

            if(Combat.getSpecialEnergy() == 100) {
                Log.info("We have special attack. Using special attack...");
                Combat.toggleSpecial(true);
                Time.sleepUntil(() -> Combat.getSpecialEnergy() < 100, 1000);
            }

            Context.setMining(true);
            rock.interact("Mine");
            Time.sleepUntil(() -> !Context.isMining(), 5000);
            Time.sleep(680, 800); //little bit of randomization
        }

        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }


        return Random.nextInt(100, 400);
    }


}
