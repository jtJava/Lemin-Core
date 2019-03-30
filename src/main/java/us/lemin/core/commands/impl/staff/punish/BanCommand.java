package us.lemin.core.commands.impl.staff.punish;


import us.lemin.core.CorePlugin;
import us.lemin.core.player.rank.Rank;

public class BanCommand extends PunishCommand {
	public BanCommand(CorePlugin plugin) {
		super(Rank.MOD, PunishType.BAN, plugin);
	}
}
