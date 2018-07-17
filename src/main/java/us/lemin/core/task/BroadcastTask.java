package us.lemin.core.task;

import java.util.List;
import org.bukkit.Server;
import us.lemin.core.CorePlugin;
import us.lemin.core.utils.message.CC;

public class BroadcastTask implements Runnable {
    private final List<String> broadcasts;
    private final Server server;
    private int currentIndex = -1;

    public BroadcastTask(CorePlugin plugin) {
        this.broadcasts = plugin.getServerSettings().getCoreConfig().getStringList("broadcasts");
        this.server = plugin.getServer();
    }

    @Override
    public void run() {
        if (broadcasts.isEmpty()) {
            return;
        }

        if (++currentIndex >= broadcasts.size()) {
            currentIndex = 0;
        }

        String message = broadcasts.get(currentIndex);

        server.broadcastMessage("");
        server.broadcastMessage(CC.SECONDARY + "[LeminBot] " + CC.PRIMARY + message);
        server.broadcastMessage("");
    }
}
