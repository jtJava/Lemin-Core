package us.lemin.core.commands.impl;

import org.bukkit.command.CommandSender;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.player.PlayerList;

public class ListCommand extends BaseCommand {
    public ListCommand() {
        super("list");
        setAliases("online", "players", "who");
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        final PlayerList onlinePlayerList = PlayerList.newList().sortedByRank();

        sender.sendMessage(CC.PRIMARY + "Players online " + CC.SECONDARY + "(" + onlinePlayerList.size() + ")"
                + CC.PRIMARY + ": " + onlinePlayerList.asColoredNames());
        sender.sendMessage(CC.PRIMARY + "Ranks: " + PlayerList.ORDERED_RANKS);
    }
}
