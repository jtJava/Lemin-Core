package us.lemin.core.commands.impl.staff.punish;

import us.lemin.core.CorePlugin;
import us.lemin.core.player.rank.Rank;

public class MuteCommand extends PunishCommand {
    public MuteCommand(CorePlugin plugin) {
        super(Rank.TRIAL_MOD, PunishType.MUTE, plugin);
    }
}
