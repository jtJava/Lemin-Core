package us.lemin.core.commands.impl.staff;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public class BackCommand extends PlayerCommand {

    public BackCommand() {
        super("back", Rank.ADMIN);
    }

    @Override
    public void execute(Player player, String[] args) {
        final CoreProfile profile = plugin.getProfileManager().getProfile(player);
        final Location last = profile.getLastLocation();

        if (last != null) {
            player.teleport(last);
            player.sendMessage(CC.GREEN + "Teleported to last location.");
        } else {
            player.sendMessage(CC.RED + "You don't have a last location stored.");
        }
    }
}
