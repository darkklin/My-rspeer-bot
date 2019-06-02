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
    public static Rock ROCKtWO;

    public static Area ROCK_AREA = Area.rectangular(3058, 9730, 3009, 9752);

    private static final Predicate<SceneObject> validRock = rock -> {
        if(rock == null) return false;
        if(!rock.getName().contains("Rocks")) return false;
        short[] rockColors = rock.getDefinition().getNewColors();
        return(rockColors != null && Arrays.equals(rockColors, ROCK.getColor())||Arrays.equals(rockColors,ROCKtWO.getColor())
                && ROCK_AREA.contains(rock.getPosition()));
    };

    public static SceneObject getValidRock() {
        SceneObject rock = SceneObjects.getNearest(validRock);

        return rock;
    }
}
