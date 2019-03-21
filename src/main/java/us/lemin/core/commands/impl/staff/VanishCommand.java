package us.lemin.core.commands.impl.staff;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public class VanishCommand extends BaseCommand {
    private final CorePlugin plugin;

    public VanishCommand(CorePlugin plugin) {
        super("vanish", Rank.TRIAL_MOD);
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        boolean vanished = !profile.isVanished();

        profile.setVanished(vanished);

        plugin.getStaffManager().vanishPlayer(player);

        plugin.getServer().getOnlinePlayers().forEach(online -> plugin.getStaffManager().hideVanishedStaffFromPlayer(online));

        player.sendMessage(vanished ? CC.GREEN + "Poof, you vanished." : CC.RED + "You're visible again.");
    }
}
