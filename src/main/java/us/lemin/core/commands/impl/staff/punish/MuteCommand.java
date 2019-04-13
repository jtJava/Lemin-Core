package us.lemin.core.commands.impl.staff.punish;

import us.lemin.core.player.rank.*;

public class MuteCommand extends PunishCommand {
	public MuteCommand() {
		super(Rank.TRIAL_MOD, PunishType.MUTE);
	}
}
