package us.lemin.core.commands.impl.staff;

import org.bukkit.command.CommandSender;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.event.server.ServerShutdownCancelEvent;
import us.lemin.core.event.server.ServerShutdownScheduleEvent;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.task.ShutdownTask;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.NumberUtil;

public class ShutdownCommand extends BaseCommand {

    public ShutdownCommand() {
        super("shutdown", Rank.ADMIN);
        setUsage(CC.RED + "Usage: /shutdown <seconds|cancel>");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(usageMessage);
            return;
        }

        final String arg = args[0];

        if (arg.equals("cancel")) {
            final ShutdownTask task = plugin.getServerSettings().getShutdownTask();

            if (task == null) {
                sender.sendMessage(CC.RED + "There is no shutdown in progress.");
            } else {
                plugin.getServer().getPluginManager().callEvent(new ServerShutdownCancelEvent());

                task.cancel();

                plugin.getServerSettings().setShutdownTask(null);
                plugin.getServer().broadcastMessage(CC.GREEN + "The shutdown in progress has been cancelled by " + sender.getName() + ".");
            }
            return;
        }

        final Integer seconds = NumberUtil.getInteger(arg);

        if (seconds == null) {
            sender.sendMessage(usageMessage);
        } else {
            if (seconds >= 5 && seconds <= 300) {
                plugin.getServer().getPluginManager().callEvent(new ServerShutdownScheduleEvent());

                final ShutdownTask task = new ShutdownTask(seconds);

                plugin.getServerSettings().setShutdownTask(task);
                task.runTaskTimer(plugin, 0L, 20L);
            } else {
                sender.sendMessage(CC.RED + "Please enter a time between 5 and 300 seconds.");
            }
        }
    }
}
