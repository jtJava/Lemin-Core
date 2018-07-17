package us.lemin.core.commands.impl.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.event.player.PlayerRankChangeEvent;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.core.utils.ProfileUtil;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;

public class RankCommand extends BaseCommand {
    private final CorePlugin plugin;

    public RankCommand(CorePlugin plugin) {
        super("rank", Rank.ADMIN);
        this.plugin = plugin;
        setUsage(CC.RED + "Usage: /rank <player> <rank>");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(usageMessage);
            return;
        }

        Rank rank = Rank.getByName(args[1]);

        if (rank == null) {
            sender.sendMessage(CC.RED + "Rank not found.");
            return;
        }

        if (sender instanceof Player) {
            CoreProfile playerProfile = plugin.getProfileManager().getProfile(((Player) sender).getUniqueId());

            if (!playerProfile.hasRank(rank)) {
                sender.sendMessage(CC.RED + "You can't give ranks higher than your own.");
                return;
            }
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (target == null) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[0]);

                if (profile != null && plugin.getMongoStorage().getDocument("players", profile.getId()) != null) {
                    MongoRequest.newRequest("players", profile.getId())
                            .put("rank_name", rank.getName())
                            .run();

                    sender.sendMessage(CC.GREEN + "Set " + profile.getName() + "'s rank to "
                            + rank.getColor() + rank.getName() + CC.GREEN + ".");
                } else {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                }
            });
        } else {
            CoreProfile targetProfile = plugin.getProfileManager().getProfile(target.getUniqueId());

            plugin.getServer().getPluginManager().callEvent(new PlayerRankChangeEvent(target, targetProfile, rank));
            sender.sendMessage(CC.GREEN + "Set " + target.getName() + "'s rank to "
                    + rank.getColor() + rank.getName() + CC.GREEN + ".");
        }
    }
}
