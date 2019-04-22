package us.lemin.core.commands.impl.staff.punish;


import org.bson.Document;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.profile.ProfileUtil;

import java.util.List;
import java.util.UUID;

import static us.lemin.core.utils.StringUtil.IP_REGEX;

// TODO: cleanup
public class UnpunishCommand extends BaseCommand {
	private final PunishType type;
	private final CorePlugin plugin;

	UnpunishCommand(Rank requiredRank, PunishType type, CorePlugin plugin) {
		super("un" + type.getName(), requiredRank);
		this.type = type;
		this.plugin = plugin;
		setUsage(CC.RED + "Usage: /" + getName() + ": <player|ip>");
	}

	@Override
	protected void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(usageMessage);
			return;
		}

		final String arg = args[0];

		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
			if (IP_REGEX.matcher(arg).matches()) {
				final Document document = plugin.getMongoStorage().getDocument("punished_addresses", arg);

				if (document == null) {
					sender.sendMessage(CC.RED + "IP not found.");
					return;
				}

				MongoRequest.newRequest("punished_addresses", arg)
						.put(type.getPastTense(), false)
						.run();

				sendUnpunishMessage(sender, arg);
			} else {
				final UUID targetId;
				final String targetName;
				final Player targetPlayer = plugin.getServer().getPlayer(args[0]);

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

					if (type == PunishType.MUTE) {
						final CoreProfile targetProfile = plugin.getProfileManager().getProfile(targetId);
						targetProfile.setMuteExpiryTime(-2);
					}
				}

				if (unpunishSucceeded(sender, targetId)) {
					sendUnpunishMessage(sender, targetName);
				}
			}
		});
	}

	private void sendUnpunishMessage(CommandSender sender, String arg) {
		final String msg = CC.GRAY + "[Silent] " + CC.GREEN + arg + " was un" + type.getPastTense() + " by " + getSenderName(sender) + ".";

		plugin.getStaffManager().messageStaff(msg);
		plugin.getLogger().info(msg);
	}

	@SuppressWarnings("unchecked")
	private boolean unpunishSucceeded(CommandSender punisher, UUID punished) {
		Document document = plugin.getMongoStorage().getDocument("punished_ids", punished);

		if (document == null || !document.getBoolean(type.getPastTense())) {
			punisher.sendMessage(CC.RED + "Player is not " + type.getPastTense() + "!");
			return false;
		}

		MongoRequest.newRequest("punished_ids", punished)
				.put(type.getPastTense(), false)
				.run();

		document = plugin.getMongoStorage().getDocument("players", punished);

		if (document != null) {
			final List<String> knownAddresses = (List<String>) document.get("known_addresses");

			if (knownAddresses != null) {
				knownAddresses.forEach(address -> plugin.getMongoStorage().getOrCreateDocument("punished_addresses", address, (doc, found) ->
						MongoRequest.newRequest("punished_addresses", address)
								.put(type.getPastTense(), false)
								.run()));
			}
		}

		return true;
	}
}
