package us.lemin.core.commands.impl.punish;


import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;

public class KickCommand extends BaseCommand {
	private final CorePlugin plugin;

	public KickCommand(CorePlugin plugin) {
		super("kick", Rank.TRIAL_MOD);
		this.plugin = plugin;
		this.setUsage(CC.RED + "/kick <player> [reason] [-s]");
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(usageMessage);
			return;
		}

		Player target = plugin.getServer().getPlayer(args[0]);

		if (target == null) {
			sender.sendMessage(Messages.PLAYER_NOT_FOUND);
			return;
		}

		boolean silent;
		String reason;

		if (args.length < 2) {
			reason = "Misconduct";
			silent = false;
		} else {
			String builtArgs = StringUtil.buildString(args, 1).trim();
			silent = builtArgs.contains("-s");

			if (silent) {
				if (builtArgs.equals("-s")) {
					reason = "Misconduct";
				} else {
					reason = builtArgs.replace("-s", "");
				}
			} else {
				reason = builtArgs;
			}
		}

		target.kickPlayer(CC.RED + "You were kicked: " + reason);

		String msg = CC.GREEN + target.getName() + " was kicked by " + sender.getName() + ".";

		if (silent) {
			String silentMsg = CC.GRAY + "(Silent) " + msg;

			plugin.getStaffManager().messageStaff(silentMsg);
			plugin.getLogger().info(silentMsg);
		} else {
			plugin.getServer().broadcastMessage(msg);
		}
	}
}
