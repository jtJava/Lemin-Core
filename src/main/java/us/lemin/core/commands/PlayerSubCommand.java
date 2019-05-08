package us.lemin.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public abstract class PlayerSubCommand extends SubCommand{

	public PlayerSubCommand(String name) {
		super(name, "No description has been set for this subcommand.", Rank.MEMBER);
	}

	public PlayerSubCommand(String name, Rank requiredRank) {
		super(name, "No description has been set for this subcommand.", requiredRank);
	}

	public PlayerSubCommand(String name, String description, Rank requiredRank) {
		super(name, description, requiredRank);
	}

	public PlayerSubCommand(String name, String description) {
		super(name, description, Rank.MEMBER);
	}

	@Override
	public void execute(CommandSender commandSender, Player target, String[] args, String label) {
		if (commandSender instanceof Player) {
			execute((Player) commandSender, target, args, label);
		} else {
			commandSender.sendMessage(CC.RED + "Only players can perform this subcommand.");
		}
	}


	public abstract void execute(Player player, Player target, String[] args, String label);

}
