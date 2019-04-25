package us.lemin.core.commands.impl;

import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;

public class IgnoreCommand extends PlayerCommand {

    public IgnoreCommand() {
        super("ignore");
        setAliases("unignore");
        setUsage(CC.RED + "Usage: /ignore <player>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }

        if (target.getName().equals(player.getName())) {
            player.sendMessage(CC.RED + "You can't ignore yourself!");
            return;
        }

        final CoreProfile targetProfile = plugin.getProfileManager().getProfile(target.getUniqueId());

        if (targetProfile.hasStaff()) {
            player.sendMessage(CC.RED + "You can't ignore a staff member. If this staff member is harrassing you " +
                    "or engaging in other abusive manners, please report this or contact a higher staff member.");
            return;
        }

        final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile.hasPlayerIgnored(target.getUniqueId())) {
            profile.unignore(target.getUniqueId());
            player.sendMessage(CC.GREEN + "No longer ignoring " + target.getName() + ".");
        } else {
            profile.ignore(target.getUniqueId());
            player.sendMessage(CC.GREEN + "Now ignoring " + target.getName() + ".");
        }
    }
}
