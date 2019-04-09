package us.lemin.core.commands.impl.staff;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

public class HealCommand extends PlayerCommand {
    public HealCommand() {
        super("heal", Rank.ADMIN);
    }

    @Override
    public void execute(Player player, String[] args) {
        final Player target = args.length < 1 || Bukkit.getPlayer(args[0]) == null ? player : Bukkit.getPlayer(args[0]);

        if (target.isDead()) {
            player.sendMessage(CC.RED + "You can't heal a dead player.");
            return;
        }

        target.setHealth(target.getMaxHealth());
        target.sendMessage(CC.GREEN + "You have been healed.");

        if (target != player) {
            player.sendMessage(CC.GREEN + "Healed " + target.getDisplayName() + CC.GREEN + ".");
        }
    }
}
