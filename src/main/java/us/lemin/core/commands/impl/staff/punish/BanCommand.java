package us.lemin.core.commands.impl.staff.punish;

import us.lemin.core.player.rank.*;

public class BanCommand extends PunishCommand {
	public BanCommand() {
		super(Rank.MOD, PunishType.BAN);
	}
}
