package us.lemin.core.commands.impl;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.timer.Timer;

public class HelpOpCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public HelpOpCommand(CorePlugin plugin) {
        super("helpop");
        this.plugin = plugin;
        setUsage(CC.RED + "/helpop <help message>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        final Timer cooldownTimer = profile.getReportCooldownTimer();

        if (cooldownTimer.isActive()) {
            player.sendMessage(CC.RED + "You can't request assistance for another " + cooldownTimer.formattedExpiration() + ".");
            return;
        }

        final String request = StringUtil.buildString(args, 0);

        plugin.getStaffManager().messageStaff(CC.RED + "\n(HelpOp) " + CC.SECONDARY + player.getName()
                + CC.PRIMARY + " requested assistance: " + CC.SECONDARY + request + CC.PRIMARY + ".\n ");

        player.sendMessage(CC.GREEN + "Request sent: " + CC.R + request);
    }
}
