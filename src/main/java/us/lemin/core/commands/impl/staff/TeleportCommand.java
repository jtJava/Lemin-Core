package us.lemin.core.commands.impl.staff;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;

public class TeleportCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public TeleportCommand(CorePlugin plugin) {
        super("tp", Rank.TRIAL_MOD);
        this.plugin = plugin;
        setAliases("teleport");
        setUsage(CC.RED + "Usage: /teleport <player> [player]");
    }

    private static boolean isOffline(Player checker, Player target) {
        if (target == null) {
            checker.sendMessage(Messages.PLAYER_NOT_FOUND);
            return true;
        }

        return false;
    }

    private void teleport(Player to, Player from) {
        to.teleport(from);
        to.sendMessage(CC.GREEN + "You have been teleported to " + from.getName() + ".");

        final CoreProfile fromProfile = plugin.getProfileManager().getProfile(from.getUniqueId());

        if (fromProfile.hasStaff()) {
            from.sendMessage(CC.GREEN + to.getName() + " has been teleported to you.");
        }
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final Player target = plugin.getServer().getPlayer(args[0]);

        if (isOffline(player, target)) {
            return;
        }

        if (args.length < 2) {
            teleport(player, target);
        } else {
            final Player target2 = plugin.getServer().getPlayer(args[1]);

            if (isOffline(player, target2)) {
                return;
            }

            teleport(target, target2);

            player.sendMessage(CC.GREEN + "Teleported " + target.getName() + " to " + target2.getName() + ".");
        }
    }
}
