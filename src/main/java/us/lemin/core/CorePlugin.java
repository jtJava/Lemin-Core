package us.lemin.core;

import lombok.*;
import org.bukkit.plugin.java.*;
import us.lemin.core.task.*;
import us.lemin.core.utils.message.*;

public class CorePlugin extends JavaPlugin {
    @Getter
    private static CorePlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        new Init(this);
        getServer().getScheduler().runTaskTimerAsynchronously(this, new BroadcastTask(this), 20L * 60 * 2, 20L * 60L * 2);
    }

    @Override
    public void onDisable() {
        Init.getInstance().getProfileManager().saveProfiles();
        Init.getInstance().getServerSettings().saveConfig();

        getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(CC.RED + "The server is restarting."));
    }
}