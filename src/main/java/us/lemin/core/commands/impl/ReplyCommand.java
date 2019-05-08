package us.lemin.core.commands.impl;

import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.event.player.PlayerMessageEvent;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.StringUtil;

public class ReplyCommand extends PlayerCommand {

    public ReplyCommand() {
        super("reply");
        setAliases("r");
        setUsage(CC.RED + "Usage: /reply <message>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile.isMuted()) {
            if (profile.isTemporarilyMuted()) {
                player.sendMessage(CC.RED + "You're muted for another " + profile.getTimeMuted() + ".");
            } else if (profile.isPermanentlyMuted()) {
                player.sendMessage(CC.RED + "You're permanently muted.");
            }
            return;
        }

        final Player target = plugin.getServer().getPlayer(profile.getConverser());

        if (target == null || !player.canSee(target)) {
            player.sendMessage(CC.RED + "You are not in a conversation.");
            return;
        }

        final CoreProfile targetProfile = plugin.getProfileManager().getProfile(target.getUniqueId());

        if (targetProfile.hasPlayerIgnored(player.getUniqueId())) {
            player.sendMessage(CC.RED + "That player is ignoring you!");
            return;
        }

        plugin.getServer().getPluginManager().callEvent(new PlayerMessageEvent(player, target, StringUtil.buildString(args, 0)));
    }
}
