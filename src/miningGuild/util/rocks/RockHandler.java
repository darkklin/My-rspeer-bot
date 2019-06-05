package miningGuild.util.rocks;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;

import java.util.Arrays;
import java.util.function.Predicate;

public class RockHandler {

    //    public static Area ROCK_AREA;
    public static Rock ROCK;
    public static Rock ROCK_MITHRIL;
    public static Rock ROCK_ADM;


    public static Area ROCK_AREA = Area.rectangular(3058, 9730, 3009, 9752);

    private static final Predicate<SceneObject> validRock = rock -> {
        if (rock == null) return false;
        if (!rock.getName().contains("Rocks")) return false;
        short[] rockColors = rock.getDefinition().getNewColors();
        return (rockColors != null && Arrays.equals(rockColors, ROCK.getColor()) || Arrays.equals(rockColors, ROCK_MITHRIL.getColor()) || Arrays.equals(rockColors, ROCK_ADM.getColor())
                && ROCK_AREA.contains(rock.getPosition()));

    };

    public static SceneObject getValidRock() {
        SceneObject rock = SceneObjects.getNearest(validRock);


        return rock;
    }
}
