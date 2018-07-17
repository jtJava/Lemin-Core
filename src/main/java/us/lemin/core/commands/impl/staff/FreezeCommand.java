package us.lemin.core.commands.impl.staff;

import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public class FreezeCommand extends PlayerCommand {
    public FreezeCommand() {
        super("freeze", Rank.ADMIN);
        setAliases("screenshare", "ss");
    }

    @Override
    public void execute(Player player, String[] args) {
        player.sendMessage(CC.PRIMARY + "Nice meme.");
    }
}
