package us.lemin.core.commands.impl.staff.punish;

import us.lemin.core.player.rank.*;

public class UnbanCommand extends UnpunishCommand {
	public UnbanCommand() {
		super(Rank.ADMIN, PunishType.BAN);
	}
}
