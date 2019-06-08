package miningGuild;

import miningGuild.base.Context;
import miningGuild.base.tasks.MineRock;
import miningGuild.base.tasks.PowerDrop;
import miningGuild.util.MiscUtil;
import miningGuild.util.rocks.Rock;
import miningGuild.util.rocks.RockHandler;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.listeners.SkillListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.runetek.event.types.SkillEvent;
import org.rspeer.script.ScriptBlockingEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
;

import java.awt.*;

@ScriptMeta(name = "Mining guild", version = 0.1, desc = "mining at mining guild", developer = "darkklin", category = ScriptCategory.MINING)
public class JMiner extends TaskScript implements RenderListener, SkillListener {

    private static final Task[] TASKS = new Task[] { new MineRock(), new PowerDrop() };

    @Override
    public void onStart() {
        RockHandler.ROCK = Rock.COAL;
        RockHandler.ROCK_MITHRIL = Rock.MITHRIL;
        RockHandler.ROCK_ADM = Rock.ADAMANTITE;
        ScriptBlockingEvent RANDOMS = new ScriptBlockingEvent(this) {
            @Override
            public boolean validate() {
                return Npcs.getNearest(npc -> npc.containsAction("Dismiss")
                        && npc.getTarget() != null
                        && npc.getTarget().equals(Players.getLocal())) != null;
            }
            @Override
            public void process() {
                Npc random = Npcs.getNearest(npc -> npc.containsAction("Dismiss")
                        && npc.getTarget() != null
                        && npc.getTarget().equals(Players.getLocal()));
                if(random != null){
                    Time.sleep(1250, 3000);
                    random.interact("Dismiss");
                }
            }
        };
        addBlockingEvent(RANDOMS);

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
        if(skillEvent.getType() == SkillEvent.TYPE_EXPERIENCE && skillEvent.getSource() == Skill.MINING) {
            Context.incrementOreMined();
            Context.setMining(false);
        }
    }

}
