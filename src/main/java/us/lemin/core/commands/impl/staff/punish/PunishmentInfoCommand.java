package us.lemin.core.commands.impl.staff.punish;


import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.core.utils.time.TimeUtil;

import java.util.UUID;

import static us.lemin.core.utils.misc.StringUtil.IP_REGEX;

// TODO: cleanup
public class PunishmentInfoCommand extends BaseCommand {


	public PunishmentInfoCommand() {
		super("punishmentinfo", Rank.ADMIN);
		this.setAliases("baninfo", "muteinfo", "playerinfo", "checkban", "checkmute");
		this.setUsage(CC.RED + "/punishmentinfo <player|ip>");
	}


    @Override
	protected void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(usageMessage);
			return;
		}

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			String arg = args[0];
			final Document document;

			if (IP_REGEX.matcher(arg).matches()) {
				document = plugin.getMongoStorage().getDocument("punished_addresses", arg);
			} else {
				final UUID id;
				final String name;
				final Player player = plugin.getServer().getPlayer(arg);

				if (player == null) {
					final ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(arg);

					if (profile == null) {
						sender.sendMessage(Messages.PLAYER_NOT_FOUND);
						return;
					}

					id = profile.getId();
					name = profile.getName();
				} else {
					id = player.getUniqueId();
					name = player.getName();
				}

				arg = name;
				document = plugin.getMongoStorage().getDocument("punished_ids", id);
			}

			if (document == null) {
				sender.sendMessage(CC.RED + "No punishment info was found for " + arg + ".");
				return;
			}

			sender.sendMessage(CC.PRIMARY + "Punishment Information for " + arg);

			Boolean punished = document.getBoolean("banned");
			boolean actuallyPunished = punished != null && punished && (document.getLong("ban_expiry") == -1L || System.currentTimeMillis() < document.getLong("ban_expiry"));
			sender.sendMessage(CC.PRIMARY + "Banned: " + CC.SECONDARY + actuallyPunished);

			if (actuallyPunished) {
				final long expiry = document.getLong("ban_expiry");
				final String punisher = document.getString("ban_punisher");
				final String reason = document.getString("ban_reason");

				sender.sendMessage(CC.PRIMARY + "Expiry: " + CC.SECONDARY
						+ (expiry == -1L ? "never" : TimeUtil.formatTimeMillis(expiry - System.currentTimeMillis())));
				sender.sendMessage(CC.PRIMARY + "Punisher: " + CC.SECONDARY + punisher);
				sender.sendMessage(CC.PRIMARY + "Reason: " + CC.SECONDARY + reason);

			}

			sender.sendMessage("");

			punished = document.getBoolean("muted");
			actuallyPunished = punished != null && punished && (document.getLong("mute_expiry") == -1L || System.currentTimeMillis() < document.getLong("mute_expiry"));

			sender.sendMessage(CC.PRIMARY + "Muted: " + CC.SECONDARY + actuallyPunished);

			if (actuallyPunished) {
				final long expiry = document.getLong("mute_expiry");
				final String punisher = document.getString("mute_punisher");
				final String reason = document.getString("mute_reason");

				sender.sendMessage(CC.PRIMARY + "Expiry: " + CC.SECONDARY
						+ (expiry == -1L ? "never" : TimeUtil.formatTimeMillis(expiry - System.currentTimeMillis())));
				sender.sendMessage(CC.PRIMARY + "Punisher: " + CC.SECONDARY + punisher);
				sender.sendMessage(CC.PRIMARY + "Reason: " + CC.SECONDARY + reason);
			}
		});
	}
}
