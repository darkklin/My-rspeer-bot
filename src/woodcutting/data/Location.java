package woodcutting.data;

import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;

public enum Location {
    POWERCUT(null,null,Tree.values()),
    DRAYNOR(Area.rectangular(new Position(3097,3246,0),new Position(3092,3240,0)),
    (Area.rectangular(3100, 3233, 3110, 3225,0)),


    Tree.REGULAR);


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
