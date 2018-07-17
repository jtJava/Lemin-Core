package us.lemin.core.commands.impl;

import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.inventory.menu.impl.ReportMenu;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.timer.Timer;

public class ReportCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public ReportCommand(CorePlugin plugin) {
        super("report");
        this.plugin = plugin;
        setUsage(CC.RED + "Usage: /report <player> <reason>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(usageMessage);
            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }

        if (player == target) {
            player.sendMessage(CC.RED + "You can't report yourself!");
            return;
        }

        CoreProfile targetProfile = plugin.getProfileManager().getProfile(target.getUniqueId());

        if (targetProfile.hasStaff()) {
            player.sendMessage(CC.RED + "You can't report a staff member. If this staff member is harassing you or" +
                    " engaging in other abusive manners, please report this or contact a higher staff member.");
            return;
        }

        CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

        if (profile.isMuted()) {
            profile.setReportingPlayerName(target.getName());
            plugin.getMenuManager().getMenu(ReportMenu.class).open(player);
        } else {
            Timer cooldownTimer = profile.getReportCooldownTimer();

            if (cooldownTimer.isActive()) {
                player.sendMessage(CC.RED + "You can't report a player for another " + cooldownTimer.formattedExpiration() + ".");
                return;
            }

            String report = StringUtil.buildString(args, 1);

            plugin.getStaffManager().messageStaff("");
            plugin.getStaffManager().messageStaff(CC.RED + "(Report) " + CC.SECONDARY + player.getName() + CC.PRIMARY
                    + " reported " + CC.SECONDARY + target.getName() + CC.PRIMARY + " for " + CC.SECONDARY + report + CC.PRIMARY + ".");
            plugin.getStaffManager().messageStaff("");

            player.sendMessage(CC.GREEN + "Report sent for " + target.getDisplayName() + CC.GREEN + ": " + CC.R + report);
        }
    }
}
