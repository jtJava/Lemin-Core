package us.lemin.core.commands.impl.toggle;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;

public class ToggleGlobalChatCommand extends PlayerCommand {
    private final Init init;

    public ToggleGlobalChatCommand() {
        super("toggleglobalchat");
        init = new Init(plugin);
        setAliases("togglechat", "tgc");
    }

    @Override
    public void execute(Player player, String[] args) {
        final CoreProfile profile = init.getProfileManager().getProfile(player.getUniqueId());
        final boolean enabled = !profile.isGlobalChatEnabled();

        profile.setGlobalChatEnabled(enabled);
        player.sendMessage(enabled ? CC.GREEN + "Global chat enabled." : CC.RED + "Global chat disabled.");
    }
}
