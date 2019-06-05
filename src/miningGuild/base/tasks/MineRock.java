package miningGuild.base.tasks;

import miningGuild.base.Context;
import miningGuild.util.rocks.RockHandler;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
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
        Area LADDER_DOWN = Area.rectangular(3023, 9743, 3014, 9732);

        Position positionOne = new Position(3048, 9734, 0);
        Position positionTwo = new Position(3053, 9738, 0);
        Position positionThree = new Position(3030, 9738, 0);
        Position positionFour = new Position(3044, 9732, 0);

        if (LADDER_DOWN.contains(Players.getLocal())) {
            Log.info("Player Go to the coal area to start minig");

            Movement.setWalkFlag(positionOne);


            int position = Random.nextInt(1, 4);
            switch (position) {
                case 1:
                    Log.info("Player Go to position 1");
                    Movement.setWalkFlag(positionOne);
                    Time.sleepUntil(() -> positionOne.equals(Players.getLocal()), Random.nextInt(7000, 15000));
                    break;
                case 2:
                    Log.info("Player Go to position 2");
                    Movement.setWalkFlag(positionTwo);
                    Time.sleepUntil(() -> positionTwo.equals(Players.getLocal()), Random.nextInt(7000, 15000));
                    break;
                case 3:
                    Log.info("Player Go to position 3");
                    Movement.setWalkFlag(positionThree);
                    Time.sleepUntil(() -> positionThree.equals(Players.getLocal()), Random.nextInt(7000, 15000));
                    break;
                case 4:
                    Log.info("Player Go to position 4");
                    Movement.setWalkFlag(positionFour);
                    Time.sleepUntil(() -> positionFour.equals(Players.getLocal()), Random.nextInt(7000, 15000));
                    break;
            }


        }

        SceneObject rock = RockHandler.getValidRock();
        if (rock != null) {
            Context.setMining(true);
            rock.interact("Mine");
            Time.sleepUntil(() -> !Context.isMining(), 5000);
            Time.sleep(100, 500); //little bit of randomization
        }
        if (Movement.isDestinationSet()) {
            Context.checkRunEnergy();
        }


        return Random.nextInt(100, 400);
    }


}
