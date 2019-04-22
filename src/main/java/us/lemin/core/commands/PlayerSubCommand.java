package us.lemin.core.commands;

import org.bukkit.entity.Player;

public interface PlayerSubCommand {

	void execute(Player player, Player target, String[] args);

}
