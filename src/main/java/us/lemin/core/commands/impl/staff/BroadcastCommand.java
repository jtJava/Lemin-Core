package us.lemin.core.commands.impl.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;

public class BroadcastCommand extends BaseCommand {

    public BroadcastCommand() {
        super("broadcast", Rank.ADMIN);
        setAliases("bc");
        setUsage(CC.RED + "Usage: /broadcast <message> [-god]");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(usageMessage);
            return;
        }

        String message = CC.SECONDARY + "(Alert) " + CC.PRIMARY
                + ChatColor.translateAlternateColorCodes('&', StringUtil.buildString(args, 0)).trim();

        if (message.endsWith(" -god")) {
            message = message.substring(12, message.length() - 5).trim();
        }

        plugin.getServer().broadcastMessage(message);
    }
}
