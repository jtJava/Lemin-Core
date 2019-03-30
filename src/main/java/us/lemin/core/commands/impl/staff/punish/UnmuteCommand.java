package us.lemin.core.commands.impl.staff.punish;


import us.lemin.core.CorePlugin;
import us.lemin.core.player.rank.Rank;

public class UnmuteCommand extends UnpunishCommand {
	public UnmuteCommand(CorePlugin plugin) {
		super(Rank.MOD, PunishType.MUTE, plugin);
	}
}
