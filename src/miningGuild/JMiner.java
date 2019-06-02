package miningGuild;

import miningGuild.base.Context;
import miningGuild.base.tasks.MineRock;
import miningGuild.base.tasks.PowerDrop;
import miningGuild.util.AreaUtil;
import miningGuild.util.MiscUtil;
import miningGuild.util.rocks.Rock;
import miningGuild.util.rocks.RockHandler;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.listeners.SkillListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.SkillEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import org.rspeer.ui.Log;
;

import java.awt.*;

@ScriptMeta(name = "darkklin", version = 0.1, desc = "Power mines any iron rocks", developer = "JRTN", category = ScriptCategory.MINING)
public class JMiner extends TaskScript implements RenderListener, SkillListener {

    private static final Task[] TASKS = new Task[] { new MineRock(), new PowerDrop() };

    @Override
    public void onStart() {
        RockHandler.ROCK = Rock.COAL;
        RockHandler.ROCKtWO = Rock.MITHRIL;

//        RockHandler.ROCK_AREA = AreaUtil.surroundingPlayer(7);
        this.submit(TASKS);
        Context.startTimer();
    }

    @Override
    public void notify(RenderEvent e) {
        Graphics g = e.getSource();
        Graphics2D g2 = (Graphics2D)g;

        int y = 35;
        int x = 10;

        g2.setColor(Color.CYAN);
        g2.drawString("jPowerminer", x, y);
        g2.drawString("Runtime: " + MiscUtil.formatRuntime(Context.getRunTime() / 1000), x, y += 20);
        g2.drawString("Ore mined: " + Context.getOreMined(), x, y += 20);
        g2.drawString("Experience gained: " + Context.getExperienceGained(), x, y += 20);
        g2.drawString("Exp/hr: " + Context.getExperiencePerHour(), x, y += 20);
    }


    @Override
    public void notify(SkillEvent skillEvent) {
        Log.info("Experience drop received.");
        if(skillEvent.getType() == SkillEvent.TYPE_EXPERIENCE && skillEvent.getSource() == Skill.MINING) {
            Context.incrementOreMined();
            Context.setMining(false);
        }
    }

}
