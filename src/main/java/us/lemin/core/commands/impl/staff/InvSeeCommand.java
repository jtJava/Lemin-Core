package us.lemin.core.commands.impl.staff;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;

public class InvSeeCommand extends PlayerCommand {
    public InvSeeCommand() {
        super("invsee", Rank.MOD);
        setUsage(CC.RED + "Usage: /invsee <player>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }

        player.openInventory(target.getInventory());
    }
}
