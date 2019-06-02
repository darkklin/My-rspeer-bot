package RangeAttack;
/*
 * Developed By Asta on 05/02/19 23:35.
 * Last Modified 06/02/19 00:18.
 * Copyright (c) 2019. All rights reserved.
 */

import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;

public class Experience {
    public final String xpHour(Skill skill, double startXp, long upTime) {
        double gainedXp = Skills.getExperience(skill) - startXp;
        return String.format("%.1f", (gainedXp * 3600000 / upTime) / 1000) + "K";
    }

    public final String xpToLvl(Skill skill) {
        return String.format("%.1f", ((double) Skills.getExperienceToNextLevel(skill) / 1000)) + "K";
    }

    public final String gainedXp(Skill skill, double startXp) {
        return String.format("%.1f", ((double) Skills.getExperience(skill) - startXp) / 1000) + "K";
    }

    public final int gainedLvl (Skill skill, int startLvl) {
        int currentSkillLevel = Skills.getLevel(skill);
        return currentSkillLevel - startLvl;
    }

    public final String percentToLvl(Skill skill) {

        long currentXp = Skills.getExperience(skill);

        int currentSkillLvl = Skills.getLevel(skill);

        long xpNextLvl = Skills.getExperienceAt(currentSkillLvl);

        long xpAtLvl = Skills.getExperienceAt(currentSkillLvl + 1);

        return (((xpNextLvl - currentXp) * 100) / (xpNextLvl - xpAtLvl)) + "% / 100%";

    }
}