package us.lemin.core.commands.impl.staff;

import com.google.common.base.Objects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.NumberUtil;
import us.lemin.core.utils.message.CC;

public class SpeedCommand extends PlayerCommand {
    public SpeedCommand() {
        super("speed", Rank.ADMIN);
        setUsage(CC.RED + "Usage: /speed <speed|reset> [player]");
    }

    private static int parseSpeed(boolean flying, String arg) {
        final int defaultSpeed = flying ? 1 : 2;

        if (arg.toLowerCase().equals("reset")) {
            return defaultSpeed;
        }

        return Objects.firstNonNull(NumberUtil.getInteger(arg), defaultSpeed);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(usageMessage);
            return;
        }

        final Player target = args.length < 2 || Bukkit.getPlayer(args[1]) == null ? player : Bukkit.getPlayer(args[1]);
        final boolean flying = target.getAllowFlight();
        final int speed = parseSpeed(flying, args[0]);

        if (speed < 0 || speed > 10) {
            player.sendMessage(CC.RED + "You must enter a valid speed from 0 to 10!");
            return;
        }

        final float actualSpeed = 0.1F * speed;

        if (flying) {
            target.setFlySpeed(actualSpeed);
        } else {
            target.setWalkSpeed(actualSpeed);
        }

        target.sendMessage(CC.PRIMARY + "Your " + (flying ? "fly" : "walk") + " speed has been set to "
                + CC.SECONDARY + speed + CC.PRIMARY + ".");

        if (target != player) {
            player.sendMessage(CC.PRIMARY + "Set " + target.getDisplayName() + CC.PRIMARY + "'s "
                    + (flying ? "fly" : "walk") + " speed to " + CC.SECONDARY + speed + CC.PRIMARY + ".");
        }
    }
}
