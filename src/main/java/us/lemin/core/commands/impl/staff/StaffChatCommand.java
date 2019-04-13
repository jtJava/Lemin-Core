package us.lemin.core.commands.impl.staff;

import org.bukkit.entity.Player;
import us.lemin.core.*;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.StringUtil;
import us.lemin.core.utils.message.CC;

public class StaffChatCommand extends PlayerCommand {
    private final Init init;

    public StaffChatCommand() {
        super("staffchat", Rank.TRIAL_MOD);
        init = new Init(plugin);
        setAliases("sc");
    }

    @Override
    public void execute(Player player, String[] args) {
        final CoreProfile profile = init.getProfileManager().getProfile(player.getUniqueId());

        if (args.length == 0) {
            final boolean inStaffChat = !profile.isInStaffChat();

            profile.setInStaffChat(inStaffChat);

            player.sendMessage(inStaffChat ? CC.GREEN + "You are now in staff chat." : CC.RED + "You are no longer in staff chat.");
        } else {
            final String message = StringUtil.buildString(args, 0);

            init.getStaffManager().messageStaff(profile.getChatFormat(), message);
        }
    }
}
