package us.lemin.core.commands.impl.staff.punish;

import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.event.BanEvent;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.core.utils.time.TimeUtil;

import java.util.List;
import java.util.UUID;

public class PunishCommand extends BaseCommand {
	private final PunishType type;
	private final CorePlugin plugin;

	PunishCommand(Rank requiredRank, PunishType type, CorePlugin plugin) {
		super(type.getName(), requiredRank);
		this.type = type;
		this.plugin = plugin;
		this.setUsage(CC.RED + "Usage: /" + getName() + " <player> [time] [reason] [-s]");
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(usageMessage);
			return;
		}

		final boolean silent;
		final String reason;
		final long time;

		if (args.length < 2) {
			reason = type.getDefaultMessage();
			time = -1L;
			silent = false;
		} else {
			String builtArgs = StringUtil.buildString(args, 1).trim();

			time = TimeUtil.parseTime(args[1]);

			if (time != -1) {
				builtArgs = builtArgs.substring(args[1].length());
			}

			silent = builtArgs.endsWith("-s");

			if (silent) {
				reason = builtArgs.equals("-s") ? type.getDefaultMessage() : builtArgs.substring(0, builtArgs.length() - 2).trim();
			} else {
				reason = builtArgs;
			}
		}

		final boolean permanent = time == -1L;
		final long expiryTime = permanent ? -1L : System.currentTimeMillis() + time;

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			final UUID targetId;
			final String targetName;
			final Player targetPlayer = plugin.getServer().getPlayer(args[0]);
			CoreProfile targetProfile = null;

			if (targetPlayer == null) {
				final ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[0]);

				if (profile == null) {
					sender.sendMessage(Messages.PLAYER_NOT_FOUND);
					return;
				} else {
					targetId = profile.getId();
					targetName = profile.getName();
				}
			} else {
				targetId = targetPlayer.getUniqueId();
				targetName = targetPlayer.getName();
				targetProfile = plugin.getProfileManager().getProfile(targetId);

				if (type == PunishType.MUTE) {
					targetProfile.setMuteExpiryTime(expiryTime);
				}
			}

			if (sender instanceof Player && targetProfile != null) {
				final Player player = (Player) sender;
				final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());

				if (!profile.hasRank(targetProfile.getRank())) {
					player.sendMessage(CC.RED + "You can't punish someone with a higher rank than your own.");
					return;
				}
			}

			if (type == PunishType.BAN) {
				final BanEvent event = new BanEvent(sender, targetId);

				plugin.getServer().getPluginManager().callEvent(event);

				if (event.isCancelled()) {
					return;
				}
			}

			punish(sender.getName(), targetId, reason, expiryTime);

			final String diff = TimeUtil.formatTimeMillis(expiryTime - System.currentTimeMillis());
			final String msg = permanent ? CC.GREEN + targetName + " was permanently " + type.getPastTense() + " by " + getSenderName(sender) + "."
					: CC.GREEN + targetName + " was temporarily " + type.getPastTense() + " by " + getSenderName(sender)
					+ " for " + diff + ".";

			if (silent) {
				final String silentMsg = CC.GRAY + "(Silent) " + msg;

				plugin.getStaffManager().messageStaff(silentMsg);
				plugin.getLogger().info(silentMsg);
			} else {
				plugin.getServer().broadcastMessage(msg);
			}

			if (type == PunishType.BAN && targetPlayer != null && targetPlayer.isOnline()) {
				plugin.getServer().getScheduler().runTask(plugin, () -> targetPlayer.kickPlayer(permanent
						? Messages.BANNED_PERMANENTLY
						: String.format(Messages.BANNED_PERMANENTLY, diff)));
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void punish(String punisher, UUID punished, String reason, long expiry) {
		final Document document = plugin.getMongoStorage().getDocument("players", punished);

		if (document != null) {
			final List<String> knownAddresses = (List<String>) document.get("known_addresses");

			if (knownAddresses != null) {
				knownAddresses.forEach(address -> plugin.getMongoStorage().getOrCreateDocument("punished_addresses", address, (doc, found) ->
						MongoRequest.newRequest("punished_addresses", address)
								.put(type.getPastTense(), true)
								.put(type.getName() + "_expiry", expiry)
								.put(type.getName() + "_reason", reason)
								.put(type.getName() + "_punisher", punisher)
								.run()));
			}
		}

		plugin.getMongoStorage().getOrCreateDocument("punished_ids", punished, (doc, found) ->
				MongoRequest.newRequest("punished_ids", punished)
						.put(type.getPastTense(), true)
						.put(type.getName() + "_expiry", expiry)
						.put(type.getName() + "_reason", reason)
						.put(type.getName() + "_punisher", punisher)
						.run());
	}
}
