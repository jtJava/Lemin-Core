package us.lemin.core.commands.impl;

import org.bukkit.command.CommandSender;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.utils.player.PlayerList;

public class ListCommand extends BaseCommand {
    public ListCommand() {
        super("list");
        setAliases("online", "players", "who");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        PlayerList onlinePlayerList = PlayerList.newList().sortedByRank();

        sender.sendMessage(onlinePlayerList.asColoredNames() + " (" + onlinePlayerList.size() + ")");
        sender.sendMessage(PlayerList.ORDERED_RANKS);
    }
}
