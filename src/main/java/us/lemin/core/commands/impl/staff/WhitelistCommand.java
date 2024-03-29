package us.lemin.core.commands.impl.staff;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.server.ServerSettings;
import us.lemin.core.server.WhitelistMode;
import us.lemin.core.utils.message.CC;

public class WhitelistCommand extends BaseCommand {

    public WhitelistCommand() {
        super("whitelist", Rank.ADMIN);
        setAliases("wl");
        setUsage(CC.RED + "Usage: /whitelist <none|ranks|staff>");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(usageMessage);
            return;
        }

        final ServerSettings settings = plugin.getServerSettings();

        switch (args[0].toLowerCase()) {
            case "none":
            case "off":
                settings.setServerWhitelistMode(WhitelistMode.NONE);
                break;
            case "ranks":
            case "donors":
                settings.setServerWhitelistMode(WhitelistMode.RANKS);
                break;
            case "staff":
            case "on":
                settings.setServerWhitelistMode(WhitelistMode.STAFF);
                break;
            default:
                sender.sendMessage(CC.RED + "That's not a valid whitelist mode!");
                return;
        }

        final WhitelistMode whitelistMode = settings.getServerWhitelistMode();
        final Server server = plugin.getServer();

        if (whitelistMode == WhitelistMode.NONE) {
            server.broadcastMessage(CC.GREEN + "The server is no longer whitelisted!");
        } else {
            whitelistMode.activate();
            server.broadcastMessage(CC.RED + "The server is now whitelisted (Mode: " + whitelistMode + ").");
        }
    }
}
