package woodcutting.data;

import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;

public enum Location {
    POWERCUT(Area.rectangular(1593, 3480, 1589, 3475),Area.rectangular(1599, 3501, 1583, 3481),Tree.values()),


    DRAYNOR(Area.rectangular(new Position(1591,3477,0),new Position(1599,3497,0)),
    (Area.rectangular(1599, 3497, 3110, 1583,0)),


    Tree.YEW);


    private final Area bankArea, treeArea;
    private final Tree[] trees;

    Location(final Area bankArea, final Area treeArea, final Tree... trees) {
        this.bankArea = bankArea;
        this.treeArea = treeArea;
        this.trees = trees;
    }

    public Area getBankArea() {
        return bankArea;
    }

    public Area getTreeArea() {
//        System.out.println("getTreeArea");

        return treeArea;
    }

    public Tree[] getTrees() {
        return trees;
    }
}
