package powerlevelMining.base.tasks;

import powerlevelMining.base.Context;
import powerlevelMining.util.rocks.RockHandler;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.script.task.Task;

public class MineRock extends Task {
    @Override
    public boolean validate() {
        return Context.validateMineRock();
    }

    @Override
    public int execute() {


        SceneObject rock = RockHandler.getValidRock();


        if (rock != null && RockHandler.ROCK_AREA.contains(Players.getLocal())) {
            Context.setMining(true);
            rock.interact("Mine");
            Time.sleepUntil(() -> !Context.isMining(), 5000);
            Time.sleep(400, 600); //little bit of randomization
        }

        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }


        return Random.nextInt(100, 400);
    }


}
