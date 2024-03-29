package us.lemin.core.task;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import us.lemin.core.CorePlugin;
import us.lemin.core.utils.message.CC;

import java.util.ArrayList;
import java.util.List;

public class BroadcastTask implements Runnable {
    private final List<String> broadcasts;
    private final Server server;
    private int currentIndex;

    public BroadcastTask(Plugin plugin) {
        List<String> broadcasts = new ArrayList<>();
        CorePlugin.getInstance().getServerSettings().getCoreConfig().getStringList("server.broadcasts").forEach(s -> broadcasts.add(ChatColor.translateAlternateColorCodes('&', s)));
        this.broadcasts = broadcasts;
        this.server = plugin.getServer();
    }

    @Override
    public void run() {
        if (broadcasts.isEmpty()) {
            return;
        }

        String message = broadcasts.get(currentIndex);

        server.broadcastMessage("");
        server.broadcastMessage(CC.GRAY + "(" + CC.SECONDARY + "Lemin" + CC.GRAY + ") " + CC.PRIMARY + message);
        server.broadcastMessage("");

        if (++currentIndex >= broadcasts.size()) {
            currentIndex = 0;
        }
    }
}
