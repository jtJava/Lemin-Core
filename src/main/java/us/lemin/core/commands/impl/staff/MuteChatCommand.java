package us.lemin.core.commands.impl.staff;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public class MuteChatCommand extends PlayerCommand {
    private final CorePlugin plugin;

    public MuteChatCommand(CorePlugin plugin) {
        super("mutechat", Rank.TRIAL_MOD);
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        final boolean globalChatMuted = !plugin.getServerSettings().isGlobalChatMuted();

        plugin.getServerSettings().setGlobalChatMuted(globalChatMuted);
        plugin.getServer().broadcastMessage(globalChatMuted ? CC.RED + "Global chat has been muted by " + player.getName() + "."
                : CC.GREEN + "Global chat has been enabled by " + player.getName() + ".");
    }
}
