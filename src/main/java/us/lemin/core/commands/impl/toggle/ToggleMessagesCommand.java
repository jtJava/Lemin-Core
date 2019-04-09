package us.lemin.core.commands.impl.toggle;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;

public class ToggleMessagesCommand extends PlayerCommand {
    private final CorePlugin plugin;
    private final Init init;

    public ToggleMessagesCommand(CorePlugin plugin) {
        super("togglemessages");
        this.plugin = plugin;
        init = new Init(plugin);
        setAliases("tpm");
    }

    @Override
    public void execute(Player player, String[] args) {
        final CoreProfile profile = init.getProfileManager().getProfile(player.getUniqueId());
        final boolean messaging = !profile.isMessaging();

        profile.setMessaging(messaging);
        player.sendMessage(messaging ? CC.GREEN + "Messages enabled." : CC.RED + "Messages disabled.");
    }
}
