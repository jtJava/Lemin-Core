package us.lemin.core.commands.impl;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.timer.Timer;

public class HelpOpCommand extends PlayerCommand {
    private final Init init;

    public HelpOpCommand() {
        super("helpop");
        init = new Init(plugin);
        setUsage(CC.RED + "/helpop <help message>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final CoreProfile profile = init.getProfileManager().getProfile(player.getUniqueId());
        final Timer cooldownTimer = profile.getReportCooldownTimer();

        if (cooldownTimer.isActive()) {
            player.sendMessage(CC.RED + "You can't request assistance for another " + cooldownTimer.formattedExpiration() + ".");
            return;
        }

        final String request = StringUtil.buildString(args, 0);

        init.getStaffManager().messageStaff(CC.RED + "\n(HelpOp) " + CC.SECONDARY + player.getName()
                + CC.PRIMARY + " requested assistance: " + CC.SECONDARY + request + CC.PRIMARY + ".\n ");

        player.sendMessage(CC.GREEN + "Request sent: " + CC.R + request);
    }
}
