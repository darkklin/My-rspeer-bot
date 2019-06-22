package miningPower.util.rocks;

import miningPower.util.rocks.Rock;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.function.Predicate;

public class RockHandler {

    //    public static Area ROCK_AREA;
    public static miningGuildMember.util.rocks.Rock ROCK;
    public static miningGuildMember.util.rocks.Rock ROCK_MITHRIL;
    public static Rock ROCK_ADM;


    public static Area ROCK_AREA = Area.rectangular(3049, 9728, 3009, 9712);

    private static final Predicate<SceneObject> validRock = rock -> {
        if (rock == null) return false;
        if (!rock.getName().contains("Rocks")) return false;
        short[] rockColors = rock.getDefinition().getNewColors();
        if (rockColors == null || rockColors.length == 0)
            return false;
        return (rockColors != null && Arrays.equals(rockColors, ROCK_ADM.getColor())
                && ROCK_AREA.contains(rock.getPosition()));
    };
    //|| Arrays.equals(rockColors, ROCK_MITHRIL.getColor()
//&& Arrays.equals(rockColors, ROCK.getColor())

    public static SceneObject getValidRock() {
        SceneObject rock = SceneObjects.getNearest(validRock);


        return rock;
    }
}
