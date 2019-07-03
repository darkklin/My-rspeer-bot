package woodcutting;

import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.listeners.SkillListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.SkillEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import woodcutting.base.Context;
import woodcutting.base.MiscUtil;
import woodcutting.data.Location;
import woodcutting.data.Tree;
import woodcutting.task.*;

import java.awt.*;


@ScriptMeta(name = "WuCutting", desc = "WoodCutting", developer = "darkklin", category = ScriptCategory.MONEY_MAKING)
public class WuCutting extends TaskScript  implements RenderListener, SkillListener {

    private static final Task[] TASKS = { new Banking(), new Traverse(), new Woodcut()};


    public static boolean dropLogs = true;

    public static Location location;
    public static Tree tree;

    @Override
    public void onStart() {
        System.out.println("onStart");
        new WuCuttingGUI().setVisible(true);
         submit(TASKS);
        Context.startTimer();

    }


    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics g = renderEvent.getSource();
        Graphics2D g2 = (Graphics2D)g;

        int y = 35;
        int x = 10;

        g2.drawString("Runtime: " + MiscUtil.formatRuntime(Context.getRunTime() / 1000), x, y += 20);
        g2.drawString("Log cu: " + Context.getLogCutted(), x, y += 20);
        g2.drawString("Experience gained: " + Context.getExperienceGained(), x, y += 20);
        g2.drawString("Exp/hr: " + Context.getExperiencePerHour(), x, y += 20);
    }

    @Override
    public void notify(SkillEvent skillEvent) {
        if(skillEvent.getType() == SkillEvent.TYPE_EXPERIENCE && skillEvent.getSource() == Skill.WOODCUTTING) {
            Context.incrementLogCutted();
            Context.setMining(false);
        }
    }
}
