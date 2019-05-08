package us.lemin.core.api.tablistapi.tab;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;
import us.lemin.core.api.tablistapi.api.TabTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TabHandler implements Listener {
    private final Map<UUID, TabList> tabLists = new HashMap<>();
    @Getter
    private final JavaPlugin plugin;

    public TabHandler(JavaPlugin plugin, int updateInterval) {
        if (plugin.getServer().getMaxPlayers() < 60) {
            throw new IllegalStateException("Max players must be greater than or equal to 60!");
        }

        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this::updateAllTabs, 0L, updateInterval);
    }

    private void updateAllTabs() {
        for (TabList tabList : tabLists.values()) {
            tabList.update();
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();

        if (player.getScoreboard().equals(scoreboardManager.getMainScoreboard())) {
            player.setScoreboard(scoreboardManager.getNewScoreboard());
        }

        tabLists.put(player.getUniqueId(), new TabList(player, this));
        updateTab(player);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        tabLists.remove(player.getUniqueId());
    }

    private TabList getTabList(Player player) {
        return tabLists.get(player.getUniqueId());
    }

    public void setDefaultTabTemplate(TabTemplate lines) {
    }

    public void setTabTemplate(Player player, TabTemplate lines) {
        getTabList(player).setTemplate(lines);
        updateTab(player);
    }

    private void updateTab(Player player) {
        getTabList(player).update();
    }
}
