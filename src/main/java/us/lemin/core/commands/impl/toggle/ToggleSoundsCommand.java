package us.lemin.core.commands.impl.toggle;

import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;

public class ToggleSoundsCommand extends PlayerCommand {

    public ToggleSoundsCommand() {
        super("togglesounds");
        setAliases("sounds", "ts");
    }

    @Override
    public void execute(Player player, String[] args) {
        final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        final boolean playingSounds = !profile.isPlayingSounds();

        profile.setPlayingSounds(playingSounds);
        player.sendMessage(playingSounds ? CC.GREEN + "Sounds enabled." : CC.RED + "Sounds disabled.");
    }
}
