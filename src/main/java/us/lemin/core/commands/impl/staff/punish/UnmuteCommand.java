package us.lemin.core.commands.impl.staff.punish;

import us.lemin.core.player.rank.*;

public class UnmuteCommand extends UnpunishCommand {
	public UnmuteCommand() {
		super(Rank.MOD, PunishType.MUTE);
	}
}
