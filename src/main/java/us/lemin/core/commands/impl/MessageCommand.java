package us.lemin.core.commands.impl;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.event.player.PlayerMessageEvent;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;

public class MessageCommand extends PlayerCommand {
    private final CorePlugin plugin;
    private final Init init;

    public MessageCommand(CorePlugin plugin) {
        super("message");
        this.plugin = plugin;
        init = new Init(plugin);
        setAliases("msg", "m", "whisper", "w", "tell", "t");
        setUsage(CC.RED + "Usage: /message <player> <message>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(usageMessage);
            return;
        }

        final CoreProfile profile = init.getProfileManager().getProfile(player.getUniqueId());

        if (profile.isMuted()) {
            if (profile.isTemporarilyMuted()) {
                player.sendMessage(CC.RED + "You're muted for another " + profile.getTimeMuted() + ".");
            } else if (profile.isPermanentlyMuted()) {
                player.sendMessage(CC.RED + "You're permanently muted.");
            }
            return;
        }

        final Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }

        final CoreProfile targetProfile = init.getProfileManager().getProfile(target.getUniqueId());

        if (targetProfile.hasPlayerIgnored(player.getUniqueId())) {
            player.sendMessage(CC.RED + "That player is ignoring you!");
            return;
        }

        plugin.getServer().getPluginManager().callEvent(new PlayerMessageEvent(player, target, StringUtil.buildString(args, 1)));
    }
}
