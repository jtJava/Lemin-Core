package us.lemin.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public abstract class PlayerCommand extends BaseCommand {
    protected PlayerCommand(String name, Rank requiredRank) {
        super(name, requiredRank);
    }

    protected PlayerCommand(String name) {
        super(name, Rank.MEMBER);
    }

    @Override
    protected final void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            execute((Player) sender, args);
        } else {
            sender.sendMessage(CC.RED + "Only players can perform this command.");
        }
    }

    public abstract void execute(Player player, String[] args);
}
