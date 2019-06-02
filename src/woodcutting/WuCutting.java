package woodcutting;

import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.task.Task;
import org.rspeer.script.task.TaskScript;
import woodcutting.data.Location;
import woodcutting.data.Tree;
import woodcutting.task.*;

import java.awt.*;


@ScriptMeta(name = "WuCutting", desc = "WoodCutting", developer = "darkklin", category = ScriptCategory.MONEY_MAKING)
public class WuCutting extends TaskScript implements RenderListener {

    private static final Task[] TASKS = {new Banking(), new Drop(), new Traverse(), new Woodcut()};


    public static boolean dropLogs = false;

    public static Location location;
    public static Tree tree;

    @Override
    public void onStart() {
        System.out.println("onStart");
        new WuCuttingGUI().setVisible(true);
         submit(TASKS);
    }


    @Override
    public void notify(RenderEvent renderEvent) {

        Graphics g = renderEvent.getSource();
        g.drawString("My animation: " + Players.getLocal().getAnimation(), 30, 30); //x and y are destinations the size of a pixel on the canvas, canvas is 503x765

    }
}
