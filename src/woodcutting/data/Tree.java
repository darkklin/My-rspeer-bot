package woodcutting.data;

import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;

public enum Tree {

    REGULAR("Tree","Logs",1,0),
    OAK("Oak","Oak Logs",15,0),
    WILLOW("Willow","Willow logs",30,67),
    YEW("Yew","Yew logs",60,175);


    private final String name, logName;
    private final int requiredLevel;
    private int experience;

    Tree(final String name, final String logName, final int requiredLevel,int experience) {
        this.name = name;
        this.logName = logName;
        this.requiredLevel = requiredLevel;
        this.experience = experience;


    }

    public String getName() {
        return name;
    }

    public String getLogName() {
        return logName;
    }
    public int getExperience() {
        return experience;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public boolean isAccessible() {
        return Skills.getCurrentLevel(Skill.WOODCUTTING) >= requiredLevel;
    }
}
