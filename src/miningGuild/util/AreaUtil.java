package miningGuild.util;

import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.movement.position.Position;

public class AreaUtil {

    /*
        Creates a rectangular area around the player. This gives a little bit more customization over Area.surround().
     */
    public static Area rectangleAround(Position position, int Xsize, int Ysize) {
        return Area.rectangular(position.getX() - Xsize, position.getY() - Ysize,
                                position.getX() + Xsize, position.getY() + Ysize);
    }

    public static Area rectangleAroundPlayer(int Xsize, int Ysize) {
        return rectangleAround(Players.getLocal().getPosition(), Xsize, Ysize);
    }

    /*
        This creates a simple surrounding area around the player.
     */
    public static Area surroundingPlayer(int distance) {
        return Area.surrounding(Players.getLocal().getPosition(), distance);
    }
}
