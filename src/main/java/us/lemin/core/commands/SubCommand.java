package us.lemin.core.commands;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

@Getter
@Setter
public abstract class SubCommand {

	protected final Rank requiredRank;
	protected final String name;
	protected final String description;

	public SubCommand(String name) {
		this(name, "No description has been set for this subcommand.", Rank.MEMBER);
	}

	public SubCommand(String name, Rank requiredRank) {
		this(name, "No description has been set for this subcommand.", requiredRank);
	}

	public SubCommand(String name, String description) {
		this(name, description, Rank.MEMBER);
	}

	public SubCommand(String name, String description, Rank requiredRank) {
		this.name = name;
		this.description = description;
		this.requiredRank = requiredRank;
	}

	protected void command(CommandSender commandSender, Player target, String[] args, String label) {
		if (commandSender instanceof Player) {
			if (CorePlugin.getInstance().getProfileManager().getProfile((Player) commandSender).hasRank(requiredRank)) {
				execute(commandSender, target, args, label);
			} else {
				commandSender.sendMessage(CC.RED + "You don't have the required rank to perform this subcommand.");
			}
		}
		execute(commandSender, target, args, label);
	}

	public abstract void execute(CommandSender sender, Player target, String[] args, String label);


}
