package us.lemin.core.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface SubCommand {

	void execute(CommandSender commandSender, Player target, String[] args);

}
