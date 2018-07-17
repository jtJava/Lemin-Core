package us.lemin.core.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import us.lemin.core.storage.flatfile.Config;

public class ServerListener implements Listener {
    private final String motd;
    private final int maxPlayers;

    public ServerListener(Config config) {
        this.motd = ChatColor.translateAlternateColorCodes('&', config.getString("server.motd"));
        this.maxPlayers = config.getInt("server.max-players");
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMaxPlayers(maxPlayers);
        event.setMotd(motd);
    }
}
