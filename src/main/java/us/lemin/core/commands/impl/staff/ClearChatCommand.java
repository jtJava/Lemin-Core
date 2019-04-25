package us.lemin.core.commands.impl.staff;

import org.bukkit.command.CommandSender;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

import java.util.Collections;

public class ClearChatCommand extends BaseCommand {
    private static final String BLANK_MESSAGE = String.join("", Collections.nCopies(300, "§8 §8 §1 §3 §3 §7 §8 §r\n"));

    public ClearChatCommand() {
        super("clearchat", Rank.TRIAL_MOD);
        setAliases("cc");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        plugin.getServer().getOnlinePlayers().forEach(player -> {
            final CoreProfile profile = plugin.getProfileManager().getProfile(player.getUniqueId());
            if (!profile.hasStaff()) {
                player.sendMessage(BLANK_MESSAGE);
            }
        });

        plugin.getServer().broadcastMessage(CC.GREEN + "The chat was cleared by " + getSenderName(sender) + ".");
        sender.sendMessage(CC.YELLOW + "Don't worry, staff can still see cleared messages.");
    }
}
