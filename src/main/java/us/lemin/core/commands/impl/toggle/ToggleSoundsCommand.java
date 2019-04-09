package us.lemin.core.commands.impl.toggle;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;

public class ToggleSoundsCommand extends PlayerCommand {
    private final CorePlugin plugin;
    private final Init init;

    public ToggleSoundsCommand(CorePlugin plugin) {
        super("togglesounds");
        this.plugin = plugin;
        init = new Init(plugin);
        setAliases("sounds", "ts");
    }

    @Override
    public void execute(Player player, String[] args) {
        final CoreProfile profile = init.getProfileManager().getProfile(player.getUniqueId());
        final boolean playingSounds = !profile.isPlayingSounds();

        profile.setPlayingSounds(playingSounds);
        player.sendMessage(playingSounds ? CC.GREEN + "Sounds enabled." : CC.RED + "Sounds disabled.");
    }
}
