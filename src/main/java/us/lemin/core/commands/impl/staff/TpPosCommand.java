package us.lemin.core.commands.impl.staff;


import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.NumberUtil;

public class TpPosCommand extends PlayerCommand {
    public TpPosCommand() {
        super("tppos", Rank.ADMIN);
        setUsage(CC.RED + "Usage: /tppos <x> [y] <z>");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(usageMessage);
            return;
        }

        if (args.length == 2) {
            final Double x = NumberUtil.getDouble(args[0]);
            final Double z = NumberUtil.getDouble(args[1]);

            if (x == null || z == null) {
                player.sendMessage(CC.RED + "You must provide valid coordinates.");
                return;
            }

            player.teleport(new Location(player.getWorld(), x, 100, z));
        } else if (args.length == 3) {
            final Double x = NumberUtil.getDouble(args[0]);
            final Double y = NumberUtil.getDouble(args[1]);
            final Double z = NumberUtil.getDouble(args[2]);

            if (x == null || y == null || z == null) {
                player.sendMessage(CC.RED + "You must provide valid coordinates.");
                return;
            }

            player.teleport(new Location(player.getWorld(), x, y, z));
        }
    }
}
