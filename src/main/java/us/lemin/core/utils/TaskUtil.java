package us.lemin.core.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.Plugin;

@UtilityClass
public class TaskUtil {
    public void runSync(Plugin plugin, Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public void runAsync(Plugin plugin, Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public void runSyncRepeating(Plugin plugin, Runnable runnable, int repeatTicks) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, 0L, repeatTicks);
    }

    public void runAsyncRepeating(Plugin plugin, Runnable runnable, int repeatTicks) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0L, repeatTicks);
    }

    public void runSyncLater(Plugin plugin, Runnable runnable, int ticksLater) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, ticksLater);
    }

    public void runAsyncLater(Plugin plugin, Runnable runnable, int ticksLater) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, ticksLater);
    }
}
