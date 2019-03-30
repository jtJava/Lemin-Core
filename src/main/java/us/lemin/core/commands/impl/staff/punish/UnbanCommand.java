package us.lemin.core.commands.impl.staff.punish;


import us.lemin.core.CorePlugin;
import us.lemin.core.player.rank.Rank;

public class UnbanCommand extends UnpunishCommand {
	public UnbanCommand(CorePlugin plugin) {
		super(Rank.ADMIN, PunishType.BAN, plugin);
	}
}
