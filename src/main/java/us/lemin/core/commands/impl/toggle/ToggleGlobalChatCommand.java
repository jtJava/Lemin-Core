package us.lemin.core.commands.impl.toggle;

import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;

public class ToggleGlobalChatCommand extends PlayerCommand {

    public ToggleGlobalChatCommand() {
        super("toggleglobalchat");
        setAliases("togglechat", "tgc");
    }

    @Override
    public void execute(Player player, String[] args) {
        final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
        final boolean enabled = !profile.isGlobalChatEnabled();

        profile.setGlobalChatEnabled(enabled);
        player.sendMessage(enabled ? CC.GREEN + "Global chat enabled." : CC.RED + "Global chat disabled.");
    }
}
